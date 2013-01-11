package xmlrefactoring.plugin.logic.addElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xsd.ui.internal.refactor.util.TextChangeCompatibility;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDParticle;
import org.w3c.dom.Element;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.logic.BaseXSDParticipant;
import xmlrefactoring.plugin.logic.addElement.external.AddElementRefactoringArguments;
import xmlrefactoring.plugin.logic.util.XMLUtil;
import xmlrefactoring.plugin.logic.util.XSDUtil;

public class XSDAddElementParticipant extends BaseXSDParticipant {

	private AddElementRefactoringArguments arguments;
	private XSDNamedComponent component;
	private Element element;

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm, 
			CheckConditionsContext context) throws OperationCanceledException {
		RefactoringStatus status = super.checkConditions(pm, context);
		
		EList<XSDElementDeclaration> componentDeclarations = arguments.getSchema().getElementDeclarations();
		for(int i = 0; i < componentDeclarations.size(); i++) {
			if(arguments.getNewElementName().equals(componentDeclarations.get(i).getName()))
				status.addFatalError(XMLRefactoringMessages.getString("XSDAddElementParticipant.ElementNameAlreadyUsed"));
		}
		
		if(element == null)
			status.addFatalError(XMLRefactoringMessages.getString("XSDAddElementParticipant.SelectedElementIsNeeded"));
		
		return status;
	}

	@Override
	protected boolean initialize(Object element) {
		if(element instanceof XSDNamedComponent) {
			this.component = (XSDNamedComponent) element;
			this.element = component.getElement();
			return true;
		}
		return false;
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, 
			OperationCanceledException {
		
		addDeclaration();
		
		super.createChange(pm);
		return new CompositeChange(XMLRefactoringMessages.getString("XSDAddElementParticipant.TransformReference"), 
				manager.getAllChanges());
	}

	private void addDeclaration() throws CoreException
	{
		EList<XSDElementDeclaration> componentDeclaration = arguments.getSchema().getElementDeclarations();
		IDOMElement idomElement = (IDOMElement) arguments.getElements().get(0);
		Element element = null;
		
		for(XSDElementDeclaration ed : componentDeclaration)
		{
			element = ed.getElement();
			if(element.getAttribute("name").contains(this.element.getAttribute("name"))) {
				break;
			}
		}
		
		IDOMNode node = (IDOMNode) idomElement.getFirstChild();
		while(true) {
			if(node.getLocalName() != null && node.getLocalName().equals("complexType"))
				node = (IDOMNode) node.getFirstChild();
			else if(node.getLocalName() != null && node.getLocalName().equals("sequence"))
				node = (IDOMNode) node.getFirstChild();
			else if(node.getNextSibling() != null)
				node = (IDOMNode) node.getNextSibling();
			else
				break;
		}
		node = (IDOMNode) node.getPreviousSibling();
		String lastElem = node.getAttributes().getNamedItem("name").getNodeValue();
		IDOMElement idomElem = null;
		
		for(XSDElementDeclaration ed : componentDeclaration)
		{
			EList<EObject> decl = ed.getTypeDefinition().getComplexType().getContent().eContents();
			for(EObject cont : decl) {
				XSDParticle content = (XSDParticle) cont;
				idomElem = (IDOMElement) content.getElement();
				if(idomElement.getAttribute("name").contains(lastElem))
					break;
			}
		}
		
		int offset = idomElement.getEndOffset() + 1;
		offset = idomElem.getEndOffset() + 1;
		
		String fileStr = idomElement.getModel().getBaseLocation();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileStr));		
		TextChange textChange = manager.get(file);
		
		IDOMElement root = (IDOMElement) arguments.getSchemaDocument().getDocumentElement();
		Element e = XSDUtil.createElementBasedUponNameAndType(root, arguments.getNewElementName(), 
				arguments.getNewElementType(), arguments.isOptional());
		InsertEdit insertElement = new InsertEdit(offset, XMLUtil.toString(e));
		
		TextChangeCompatibility.addTextEdit(textChange, 
				XMLRefactoringMessages.getString("XSDAddElementParticipant.DecName"), 
				insertElement);
	}

	@Override
	public void initialize(RefactoringArguments arguments){
		super.initialize(arguments);
		this.arguments = (AddElementRefactoringArguments) arguments;
	}

	@Override
	public String getName() {
		return XMLRefactoringMessages.getString("XSDAddElementParticipant.Name");
	}

}
