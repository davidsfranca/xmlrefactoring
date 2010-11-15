package xmlrefactoring.plugin.ui.ref2elem;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;

import xmlrefactoring.plugin.logic.ref2elem.Ref2ElemProcessor;
import xmlrefactoring.plugin.logic.util.XSDUtil;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;
import xmlrefactoring.plugin.ui.SingleInputAction;

public class Ref2ElemAction extends SingleInputAction {

	@Override
	protected BaseRefactoringWizard<Ref2ElemProcessor> getWizard() {
		Ref2ElemProcessor processor = new Ref2ElemProcessor(getSelectedComponent());
		return new Ref2ElemWizard(processor);
	}
	
	@Override
	public void selectionChanged(IAction action, ISelection selection){
		super.selectionChanged(action, selection);
		if(getSelectedComponent() != null){
			if(!XSDUtil.isReference(getSelectedComponent().getElement()))
				action.setEnabled(false);
		}		
	}
}
