package xmlrefactoring.plugin.ui.moveElement;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;

import xmlrefactoring.plugin.logic.moveElement.MoveElementProcessor;
import xmlrefactoring.plugin.logic.util.XSDUtil;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;
import xmlrefactoring.plugin.ui.SingleInputAction;

public class MoveElementAction extends SingleInputAction {

	@Override
	protected BaseRefactoringWizard<MoveElementProcessor> getWizard() {
		MoveElementProcessor processor = new MoveElementProcessor(getSelectedComponent());
		return new MoveElementWizard(processor);
	}
	
	@Override
	public void selectionChanged(IAction action, ISelection selection){
		super.selectionChanged(action, selection);
		if(getSelectedComponent() != null){
			if(!XSDUtil.isElement(getSelectedComponent().getElement()))
				action.setEnabled(false);
		}		
	}
}
