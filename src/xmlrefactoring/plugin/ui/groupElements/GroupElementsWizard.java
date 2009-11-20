package xmlrefactoring.plugin.ui.groupElements;

import xmlrefactoring.plugin.logic.groupElements.GroupElementsProcessor;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;

public class GroupElementsWizard extends BaseRefactoringWizard<GroupElementsProcessor> {

	private GroupElementsWizardPage page;
	
	public GroupElementsWizard(GroupElementsProcessor processor) {
		super(processor, DIALOG_BASED_USER_INTERFACE);
	}

	@Override
	protected void addUserInputPages() {
		page = new GroupElementsWizardPage(this);
		addPage(page);
	}
}
