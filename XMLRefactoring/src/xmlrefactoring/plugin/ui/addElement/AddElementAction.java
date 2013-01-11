package xmlrefactoring.plugin.ui.addElement;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.xsd.XSDElementDeclaration;

import xmlrefactoring.plugin.logic.addElement.AddElementProcessor;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;
import xmlrefactoring.plugin.ui.SingleInputAction;

public class AddElementAction extends SingleInputAction {

	@Override
	protected BaseRefactoringWizard<AddElementProcessor> getWizard() {
		return new AddElementWizard(new AddElementProcessor(getSelectedComponent()));
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		super.selectionChanged(action, selection);
		if(!(getSelectedComponent() instanceof XSDElementDeclaration))
			action.setEnabled(false);
	}

}
