package xmlrefactoring.plugin.ui.ungroupElements;

import xmlrefactoring.plugin.logic.ungroupElements.UngroupElementsProcessor;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;

public class UngroupElementsWizard extends BaseRefactoringWizard<UngroupElementsProcessor> {
	private UngroupElementsWizardPage page;
	
	public UngroupElementsWizard(UngroupElementsProcessor processor) {
		super(processor, DIALOG_BASED_USER_INTERFACE);
	}

	@Override
	protected void addUserInputPages() {
		page = new UngroupElementsWizardPage(this);
		addPage(page);
	}
}
