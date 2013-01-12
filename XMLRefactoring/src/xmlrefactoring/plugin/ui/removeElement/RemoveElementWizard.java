package xmlrefactoring.plugin.ui.removeElement;

import xmlrefactoring.plugin.logic.removeElement.RemoveElementProcessor;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;

public class RemoveElementWizard extends BaseRefactoringWizard<RemoveElementProcessor> {

	public RemoveElementWizard(RemoveElementProcessor processor) {
		super(processor, DIALOG_BASED_USER_INTERFACE);
	}

	@Override
	protected void addUserInputPages() {
		addPage(new RemoveElementWizardPage(this));
	}

}
