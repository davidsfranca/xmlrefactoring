package xmlrefactoring.plugin.ui.ref2elem;

import xmlrefactoring.plugin.logic.ref2elem.Ref2ElemProcessor;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;

public class Ref2ElemWizard extends BaseRefactoringWizard<Ref2ElemProcessor> {
	private Ref2ElemWizardPage page;

	public Ref2ElemWizard(Ref2ElemProcessor processor) {
		super(processor, DIALOG_BASED_USER_INTERFACE);
	}

	@Override
	protected void addUserInputPages() {
		page = new Ref2ElemWizardPage(this);
		addPage(page);
	}

}
