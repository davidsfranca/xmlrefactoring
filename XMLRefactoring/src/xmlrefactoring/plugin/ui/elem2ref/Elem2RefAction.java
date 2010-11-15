package xmlrefactoring.plugin.ui.elem2ref;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;

import xmlrefactoring.plugin.logic.elem2ref.Elem2RefProcessor;
import xmlrefactoring.plugin.logic.util.XSDUtil;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;
import xmlrefactoring.plugin.ui.SingleInputAction;

public class Elem2RefAction extends SingleInputAction {

	@Override
	protected BaseRefactoringWizard<Elem2RefProcessor> getWizard() {
		Elem2RefProcessor processor = new Elem2RefProcessor(getSelectedComponent());
		return new Elem2RefWizard(processor);
	}
	
	@Override
	public void selectionChanged(IAction action, ISelection selection){
		super.selectionChanged(action, selection);
		if(getSelectedComponent() != null){
			if(!XSDUtil.isElement(getSelectedComponent().getElement()))
				action.setEnabled(false);
			if(XSDUtil.isReference(getSelectedComponent().getElement()))
				action.setEnabled(false);
		}		
	}
}
