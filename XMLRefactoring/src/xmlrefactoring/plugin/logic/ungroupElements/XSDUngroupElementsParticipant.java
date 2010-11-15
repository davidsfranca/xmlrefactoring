package xmlrefactoring.plugin.logic.ungroupElements;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMEntity;
import org.eclipse.wst.xsd.ui.internal.refactor.util.TextChangeCompatibility;
import org.eclipse.xsd.XSDTypeDefinition;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.logic.BaseXSDParticipant;
import xmlrefactoring.plugin.logic.ungroupElements.external.UngroupElementsRefactoringArguments;
import xmlrefactoring.plugin.logic.util.XMLUtil;
import xmlrefactoring.plugin.logic.util.XSDUtil;

public class XSDUngroupElementsParticipant extends BaseXSDParticipant {
	private UngroupElementsRefactoringArguments arguments;
	
	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		RefactoringStatus status = super.checkConditions(pm, context);	
		return status;
	}
	
	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		//Gets the TextChange for the file		
		TextChange change = manager.get(arguments.getSchemaFile());
		
		IDOMElement root = (IDOMElement) arguments.getSchemaDocument().getDocumentElement();
		IDOMElement idomElement = arguments.getElements().get(0);
		
		int moveOffset = idomElement.getEndOffset() + 1;		
		Node moveElement = idomElement.getFirstChild().getNextSibling();
		
		if(moveElement.getNodeName().equals("sequence"))
		{
			moveElement = moveElement.getFirstChild();
			
			while(moveElement != null)
			{
				System.out.println(moveElement.getNodeName());
				
				if(moveElement.getNodeName().equals("element"))
				{
					Element simpleType = XSDUtil.createSimpleType(root, moveElement.getAttributes().getNamedItem("name").getNodeValue(), 
							moveElement.getAttributes().getNamedItem("type").getNodeValue());
					InsertEdit moveType = new InsertEdit(moveOffset, XMLUtil.toString(simpleType));
					TextChangeCompatibility.addTextEdit(change, XMLRefactoringMessages.getString("XSDUngroupElementsParticipant.UngroupingElement"), moveType);
				}
					
				moveElement = moveElement.getNextSibling();
			}
			
			DeleteEdit deleteElement = new DeleteEdit(idomElement.getStartOffset(), idomElement.getEndOffset() - idomElement.getStartOffset());
			TextChangeCompatibility.addTextEdit(change, XMLRefactoringMessages.getString("XSDUngroupElementsParticipant.GroupElementDelete"), deleteElement);
			
			super.createChange(pm);
		}
			
		return new CompositeChange(XMLRefactoringMessages.getString("XSDUngroupElementsParticipant.Transf"),manager.getAllChanges());
	}
	
	@Override
	protected boolean initialize(Object element) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void initialize(RefactoringArguments arguments) {
		super.initialize(arguments);
		this.arguments = (UngroupElementsRefactoringArguments) arguments;
	}
	
	@Override
	public String getName() {
		return XMLRefactoringMessages.getString("XSDUngroupElementsParticipant.Name");
	}

}
