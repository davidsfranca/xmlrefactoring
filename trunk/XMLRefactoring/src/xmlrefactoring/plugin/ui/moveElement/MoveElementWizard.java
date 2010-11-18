package xmlrefactoring.plugin.ui.moveElement;

import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.plugin.logic.moveElement.MoveElementProcessor;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;

public class MoveElementWizard extends BaseRefactoringWizard<MoveElementProcessor> {
	private MoveElementWizardPage page;
	private XSDNamedComponent selectedComponent;
	
	public MoveElementWizard(MoveElementProcessor processor, XSDNamedComponent selectedComponent) {
		super(processor, DIALOG_BASED_USER_INTERFACE);
		this.selectedComponent = selectedComponent;
	}

	@Override
	protected void addUserInputPages() {
		page = new MoveElementWizardPage(this, this.selectedComponent);
		addPage(page);
	}

}
