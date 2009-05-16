package xmlrefactoring.plugin.logic.attr2elem;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.TextEditGroup;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Element;

import xmlrefactoring.plugin.logic.util.SchemaElementVerifier;

public class XSDAttr2ElemParticipant extends Attr2ElemParticipant {

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
		
		if(SchemaElementVerifier.isGlobal(attribute)){
			
			IDOMElement idomElement = (IDOMElement) attribute;
			String fileStr = idomElement.getModel().getBaseLocation();
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileStr));		

			TextFileChange textChange = new TextFileChange(fileStr, file);	
			
			textChange.setEdit(new MultiTextEdit());
			
			TextEditGroup group = new TextEditGroup("Attribute into element transformation");
			
			int offset = idomElement.getStartOffset() + "<".length();
			textChange.getEdit().addChild(replaceAttributeWithElement(idomElement, offset));
			
			if(idomElement.getEndStructuredDocumentRegion() != null){
				int endTagOffset = idomElement.getEndStructuredDocumentRegion().getStartOffset() + "</".length();
				textChange.getEdit().addChild(replaceAttributeWithElement(idomElement, endTagOffset));
			}
			
			return textChange;
		}
		return null;
	}

	/**
	 * Replaces the attribute string with element given an offset
	 * @param idomElement
	 * @param offset
	 * @return
	 */
	private TextEdit replaceAttributeWithElement(IDOMElement idomElement, int offset) {
		if(idomElement.getPrefix() != null)
			offset += idomElement.getPrefix().length();			
		return new ReplaceEdit(offset, SchemaElementVerifier.ATTRIBUTE.length(), SchemaElementVerifier.ELEMENT);		
	}

	@Override
	public String getName() {
		return "Transform into element schema refactoring participant";
	}

	@Override
	protected boolean initialize(Object element) {
		if(element instanceof XSDNamedComponent){
			XSDNamedComponent component = (XSDNamedComponent) element;
			if(SchemaElementVerifier.isAttribute(component.getElement())){
				attribute = component.getElement();
				return true;
			}
		}
		return false;
	}

	@Override
	protected void initialize(RefactoringArguments arguments) {
		this.arguments = (Attr2ElemRefactoringArguments) arguments;		
	}

	
	
}
