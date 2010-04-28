package xmlrefactoring.plugin.ui.rename;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDElementDeclaration;

import xmlrefactoring.plugin.logic.rename.RenameProcessor;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;
import xmlrefactoring.plugin.ui.SingleInputAction;

public class RenameAction extends SingleInputAction{

	@Override
	protected BaseRefactoringWizard<RenameProcessor> getWizard(){
		return new RenameWizard(new RenameProcessor(getSelectedComponent()));
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		super.selectionChanged(action, selection);
		if(!(getSelectedComponent() instanceof XSDElementDeclaration ) &&
				!(getSelectedComponent() instanceof XSDAttributeDeclaration))
			action.setEnabled(false);
	}
}
