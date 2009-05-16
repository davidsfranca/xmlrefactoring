package xmlrefactoring.plugin.logic.attr2elem;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;

public class XSLTAttr2ElemParticipant extends Attr2ElemParticipant{

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "Transform into element xslt refactoring participant";
	}

	@Override
	protected boolean initialize(Object element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void initialize(RefactoringArguments arguments) {
		// TODO Auto-generated method stub
		
	}

}
