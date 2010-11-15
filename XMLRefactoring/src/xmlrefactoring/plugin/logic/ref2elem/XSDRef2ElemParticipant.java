package xmlrefactoring.plugin.logic.ref2elem;

import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.wst.common.core.search.SearchMatch;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xsd.ui.internal.refactor.TextChangeManager;
import org.eclipse.wst.xsd.ui.internal.refactor.util.TextChangeCompatibility;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.logic.BaseXSDParticipant;
import xmlrefactoring.plugin.logic.ref2elem.external.Ref2ElemRefactoringArguments;
import xmlrefactoring.plugin.logic.util.SearchUtil;
import xmlrefactoring.plugin.logic.util.XMLUtil;
import xmlrefactoring.plugin.logic.util.XSDUtil;

public class XSDRef2ElemParticipant extends BaseXSDParticipant {
	Ref2ElemRefactoringArguments arguments;
	private XSDNamedComponent component;
	private Element transformingElement;
	
	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm, 
			CheckConditionsContext context) throws OperationCanceledException {
		RefactoringStatus status = super.checkConditions(pm, context);
		
		if(arguments.isReference())
		{
			
		}
		else
			status.addFatalError(XMLRefactoringMessages.getString("XSDRef2ElemParticipant.ReferenceIsNeeded"));
		
		return status;
	}
	
	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, 
			OperationCanceledException {
		
		TextChangeManager manager = new TextChangeManager();
		transformReference(manager);
		
		super.createChange(pm);
		return new CompositeChange(XMLRefactoringMessages.getString("XSDRef2ElemParticipant.TransformReference"), 
				manager.getAllChanges());
	}
	
	private void transformReference(TextChangeManager manager) throws CoreException
	{
		EList<XSDElementDeclaration> componentDeclaration = arguments.getSchema().getElementDeclarations();
		IDOMElement idomElement = (IDOMElement) transformingElement;
		Element element = null;
		
		for(XSDElementDeclaration ed : componentDeclaration)
		{
			element = ed.getElement();
			if(transformingElement.getAttribute("ref").contains(element.getAttribute("name"))) break;
		}
		
		int offset = idomElement.getStartOffset();
		int length = idomElement.getEndOffset() - idomElement.getStartOffset();
		
		String fileStr = idomElement.getModel().getBaseLocation();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileStr));		
		TextChange textChange = manager.get(file);
		
		TextChangeCompatibility.addTextEdit(textChange, 
				XMLRefactoringMessages.getString("XSDRef2ElemParticipant.DecName"), 
				copyElement(element, offset, length));
	}
	
	private TextEdit [] copyElement(Element element, int offset, int length) throws CoreException
	{
		List<TextEdit> ref2ElemTransformation = new ArrayList<TextEdit>();
		IDOMElement root = (IDOMElement) arguments.getSchemaDocument().getDocumentElement();
		Element e = XSDUtil.createElementBasedUpon(root, element, arguments.getElementName());
		
		DeleteEdit deleteElement = new DeleteEdit(offset, length);
		InsertEdit insertElement = new InsertEdit(offset, XMLUtil.toString(e));
		
		ref2ElemTransformation.add(deleteElement);
		ref2ElemTransformation.add(insertElement);
		
		return ref2ElemTransformation.toArray(new TextEdit[0]);
	}
	
	@Override
	protected boolean initialize(Object element) {
		if(element instanceof XSDNamedComponent)
		{
			this.component = (XSDNamedComponent) element;
			
			if(XSDUtil.isReference(this.component.getElement()))
			{
				this.transformingElement = component.getElement();
				return true;
			}
		}
		return false;
	}

	@Override
	public void initialize(RefactoringArguments arguments)
	{
		super.initialize(arguments);
		this.arguments = (Ref2ElemRefactoringArguments) arguments;
	}
	
	@Override
	public String getName() {
		return XMLRefactoringMessages.getString("XSDRef2ElemParticipant.Name");
	}
}
