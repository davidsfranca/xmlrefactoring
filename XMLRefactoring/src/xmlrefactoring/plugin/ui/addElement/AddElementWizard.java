package xmlrefactoring.plugin.ui.addElement;

import xmlrefactoring.plugin.logic.addElement.AddElementProcessor;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;

public class AddElementWizard extends BaseRefactoringWizard<AddElementProcessor> {

	public AddElementWizard(AddElementProcessor processor) {
		super(processor, DIALOG_BASED_USER_INTERFACE);
	}

	@Override
	protected void addUserInputPages() {
		addPage(new AddElementWizardPage(this));
	}

}
