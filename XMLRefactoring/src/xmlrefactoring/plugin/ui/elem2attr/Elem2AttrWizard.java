package xmlrefactoring.plugin.ui.elem2attr;

import xmlrefactoring.plugin.logic.elem2attr.Elem2AttrProcessor;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;

public class Elem2AttrWizard extends BaseRefactoringWizard<Elem2AttrProcessor> {
	private Elem2AttrWizardPage page;
	
	public Elem2AttrWizard(Elem2AttrProcessor processor)
	{
		super(processor, DIALOG_BASED_USER_INTERFACE);
	}
	
	@Override
	protected void addUserInputPages() {
		page = new Elem2AttrWizardPage(this);
		addPage(page);
	}
}
