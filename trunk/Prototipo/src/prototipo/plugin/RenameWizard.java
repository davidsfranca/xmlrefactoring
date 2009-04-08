package prototipo.plugin;

import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;

public class RenameWizard extends RefactoringWizard{


	public RenameWizard(Refactoring refactoring) {
		super(refactoring, DIALOG_BASED_USER_INTERFACE | PREVIEW_EXPAND_FIRST_NODE);
	}

	@Override
	protected void addUserInputPages() {
		addPage(new RenameWizardPage("Rename"));		
	}
	
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return super.performFinish();
	}

}
