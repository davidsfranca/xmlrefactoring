package xmlrefactoring.plugin.logic.ungroupElements;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMEntity;
import org.eclipse.xsd.XSDTypeDefinition;
import org.w3c.dom.Element;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.logic.BaseXSDParticipant;
import xmlrefactoring.plugin.logic.ungroupElements.external.UngroupElementsRefactoringArguments;
import xmlrefactoring.plugin.logic.util.XMLUtil;

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
		Element complexType = (Element) arguments.getElements().get(0).getChildNodes().item(0);		
		
		System.out.println(complexType.toString());
		
		return null;
	}
	
	@Override
	protected boolean initialize(Object element) {
		// TODO Auto-generated method stub
		return false;
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
