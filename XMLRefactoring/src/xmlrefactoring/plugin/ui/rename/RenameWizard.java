package xmlrefactoring.plugin.ui.rename;

import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.wst.xsd.ui.internal.refactor.INameUpdating;

public class RenameWizard extends RefactoringWizard{

	public RenameWizard(RenameRefactoring refactoring){
		super(refactoring, DIALOG_BASED_USER_INTERFACE);
	}

	@Override
	protected void addUserInputPages() {
		addPage(new RenameWizardPage(this));		
	}
	
	protected INameUpdating getProcessor() {		
		return (INameUpdating)getRefactoring().getAdapter(INameUpdating.class);	
	}

}
