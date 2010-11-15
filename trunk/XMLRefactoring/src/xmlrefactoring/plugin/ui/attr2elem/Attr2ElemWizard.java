package xmlrefactoring.plugin.ui.attr2elem;

import xmlrefactoring.plugin.logic.attr2elem.Attr2ElemProcessor;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;

public class Attr2ElemWizard extends BaseRefactoringWizard<Attr2ElemProcessor>{
	private Attr2ElemWizardPage page;
	
	public Attr2ElemWizard(Attr2ElemProcessor processor)
	{
		super(processor, DIALOG_BASED_USER_INTERFACE);
	}
	
	@Override
	protected void addUserInputPages() {
		page = new Attr2ElemWizardPage(this);
		addPage(page);
	}
}
