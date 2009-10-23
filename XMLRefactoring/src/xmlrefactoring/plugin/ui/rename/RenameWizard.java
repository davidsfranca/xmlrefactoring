package xmlrefactoring.plugin.ui.rename;

import xmlrefactoring.plugin.logic.rename.RenameProcessor;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;

public class RenameWizard extends BaseRefactoringWizard<RenameProcessor>{

	public RenameWizard(RenameProcessor processor) {
		super(processor, DIALOG_BASED_USER_INTERFACE);
	}

	@Override
	protected void addUserInputPages() {
		addPage(new RenameWizardPage(this));
	}

	

}
