package xmlrefactoring.plugin.logic.attr2elem;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.wst.common.core.search.SearchMatch;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xsd.ui.internal.refactor.TextChangeManager;
import org.eclipse.wst.xsd.ui.internal.refactor.util.TextChangeCompatibility;
import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.PluginNamingConstants;
import xmlrefactoring.plugin.logic.BaseXSDParticipant;
import xmlrefactoring.plugin.logic.util.SearchUtil;
import xmlrefactoring.plugin.logic.util.XSDUtil;

public class XSDAttr2ElemParticipant extends BaseXSDParticipant {

	private Element attribute;
	private Attr2ElemRefactoringArguments arguments;

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		// TODO Auto-generated method stub
		return new RefactoringStatus();
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		
		if(XSDUtil.isGlobal(attribute)){
			
			TextChangeManager manager = new TextChangeManager();
			
			transformDeclaration(manager);
			transformReferences(manager);			
			
			return new CompositeChange(XMLRefactoringMessages.getString("XSDAttr2ElemParticipant.Name"), 
					manager.getAllChanges());
		}
		return null;
	}
	
	private void transformDeclaration(TextChangeManager manager){
		IDOMElement idomElement = (IDOMElement) attribute;
		String fileStr = idomElement.getModel().getBaseLocation();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileStr));
		
		TextChange textChange = manager.get(file); 
		
		TextEdit[] attr2elemTransformation = attr2elem(attribute);
		
		TextChangeCompatibility.addTextEdit(textChange, 
				XMLRefactoringMessages.getString("XSDAttr2ElemParticipant.DecName"), attr2elemTransformation);
	}

	private void transformReferences(TextChangeManager manager) throws CoreException {
		for(SearchMatch match : SearchUtil.searchReferences((IDOMElement) attribute)){
			TextChange change = manager.get(match.getFile());
			if(match.getObject() instanceof Node){
				Node node = (Node)match.getObject();
				if(node instanceof Attr){
					Attr attr = (Attr) node;
					TextEdit[] transformation = attr2elem(attr.getOwnerElement());
					TextChangeCompatibility.addTextEdit(change, 
							XMLRefactoringMessages.getString("XSDAttr2ElemParticipant.RefName"), transformation);
				}	
			}
		}		
	}

	private TextEdit[] attr2elem(Element attribute) {
		List<TextEdit> attr2elemTransformation = new ArrayList<TextEdit>();
		IDOMElement idomElement = (IDOMElement) attribute;
		
		int offset = idomElement.getStartOffset() + "<".length();
		attr2elemTransformation.add(replaceAttributeWithElement(idomElement, offset));
		
		if(idomElement.getEndStructuredDocumentRegion() != null){
			int endTagOffset = idomElement.getEndStructuredDocumentRegion().getStartOffset() + "</".length();
			attr2elemTransformation.add(replaceAttributeWithElement(idomElement, endTagOffset));
		}
		return attr2elemTransformation.toArray(new TextEdit[0]);
	}

	/**
	 * Replaces the attribute string with element given an offset
	 * @param idomElement
	 * @param offset
	 * @return
	 */
	private TextEdit replaceAttributeWithElement(IDOMElement idomElement, int offset) {
		if(idomElement.getPrefix() != null){
			offset += idomElement.getPrefix().length();
			offset += ":".length();
		}
		return new ReplaceEdit(offset, XSDUtil.ATTRIBUTE.length(), XSDUtil.ELEMENT);		
	}

	@Override
	public String getName() {
		return "Transform into element schema refactoring participant";
	}

	@Override
	protected boolean initialize(Object element) {
		if(element instanceof XSDNamedComponent){
			XSDNamedComponent component = (XSDNamedComponent) element;
			if(XSDUtil.isAttribute(component.getElement())){
				attribute = component.getElement();
				return true;
			}
		}
		return false;
	}

	@Override
	public void initialize(RefactoringArguments arguments) {
		this.arguments = (Attr2ElemRefactoringArguments) arguments;		
	}

	
	
}
