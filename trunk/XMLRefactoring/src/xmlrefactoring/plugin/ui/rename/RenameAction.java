package xmlrefactoring.plugin.ui.rename;

import org.eclipse.ltk.ui.refactoring.RefactoringWizard;

import xmlrefactoring.plugin.logic.rename.RenameProcessor;
import xmlrefactoring.plugin.ui.SingleInputAction;

public class RenameAction extends SingleInputAction{

	@Override
	protected RefactoringWizard getWizard(){
		return new RenameWizard(new RenameProcessor(getSelectedComponent()));
	}
	
}
