package xmlrefactoring.plugin.logic;

import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant;

public abstract class BaseParticipant extends RefactoringParticipant{

	protected BaseRefactoringArguments baseArguments;

	@Override
	public void initialize(RefactoringArguments arguments){
		baseArguments = (BaseRefactoringArguments) arguments;		
	}
	
}
