package xmlrefactoring.plugin.ui;

import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;

public abstract class BaseWizard extends RefactoringWizard{

	private ProcessorBasedRefactoring refactoring;
	
	public BaseWizard(ProcessorBasedRefactoring refactoring, int flags) {
		super(refactoring, flags);
		this.refactoring = refactoring;
	}
	
	public ProcessorBasedRefactoring getProcessorBasedRefactoring(){
		return refactoring;
	}

}
