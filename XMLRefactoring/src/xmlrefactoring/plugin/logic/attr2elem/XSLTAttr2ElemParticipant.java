package xmlrefactoring.plugin.logic.attr2elem;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.logic.BaseXSLParticipant;
import xmlrefactoring.plugin.refactoring.XMLRefactoring;

public class XSLTAttr2ElemParticipant extends BaseXSLParticipant{

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		// TODO Auto-generated method stub
		return new RefactoringStatus();
	}

	@Override
	public String getName() {
		return XMLRefactoringMessages.getString("XSLTAttr2ElemParticipant.Name");
	}

	@Override
	protected boolean initialize(Object element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected XMLRefactoring getXMLRefactoring() {
		return null;
	}

}
