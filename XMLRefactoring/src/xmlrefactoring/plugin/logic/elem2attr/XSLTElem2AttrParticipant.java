package xmlrefactoring.plugin.logic.elem2attr;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;

import xmlrefactoring.plugin.logic.BaseXSLParticipant;
import xmlrefactoring.plugin.refactoring.XMLRefactoring;

public class XSLTElem2AttrParticipant extends BaseXSLParticipant {

	@Override
	protected XMLRefactoring getXMLRefactoring() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean initialize(Object element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		// TODO Auto-generated method stub
		return new RefactoringStatus();
	}

}
