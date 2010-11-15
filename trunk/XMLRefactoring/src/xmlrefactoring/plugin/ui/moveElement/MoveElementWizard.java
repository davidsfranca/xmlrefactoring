package xmlrefactoring.plugin.ui.moveElement;

import xmlrefactoring.plugin.logic.moveElement.MoveElementProcessor;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;

public class MoveElementWizard extends BaseRefactoringWizard<MoveElementProcessor> {
	private MoveElementWizardPage page;
	
	public MoveElementWizard(MoveElementProcessor processor) {
		super(processor, DIALOG_BASED_USER_INTERFACE);
	}

	@Override
	protected void addUserInputPages() {
		page = new MoveElementWizardPage(this);
		addPage(page);
	}

}
