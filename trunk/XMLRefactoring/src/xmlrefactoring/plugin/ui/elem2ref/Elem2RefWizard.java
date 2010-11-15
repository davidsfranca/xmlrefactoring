package xmlrefactoring.plugin.ui.elem2ref;

import xmlrefactoring.plugin.logic.elem2ref.Elem2RefProcessor;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;

public class Elem2RefWizard extends BaseRefactoringWizard<Elem2RefProcessor> {
	private Elem2RefWizardPage page;
	
	public Elem2RefWizard(Elem2RefProcessor processor) {
		super(processor, DIALOG_BASED_USER_INTERFACE);
	}

	@Override
	protected void addUserInputPages() {
		page = new Elem2RefWizardPage(this);
		addPage(page);
	}

}
