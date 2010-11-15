package xmlrefactoring.plugin.ui.elem2attr;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;

import xmlrefactoring.plugin.logic.elem2attr.Elem2AttrProcessor;
import xmlrefactoring.plugin.logic.util.XSDUtil;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;
import xmlrefactoring.plugin.ui.SingleInputAction;

public class Elem2AttrAction extends SingleInputAction {

	@Override
	protected BaseRefactoringWizard<Elem2AttrProcessor> getWizard() {
		Elem2AttrProcessor processor = new Elem2AttrProcessor(getSelectedComponent());
		return new Elem2AttrWizard(processor);
	}
	
	@Override
	public void selectionChanged(IAction action, ISelection selection){
		super.selectionChanged(action, selection);
		if(getSelectedComponent() != null){
			if(!XSDUtil.isElement(getSelectedComponent().getElement()))
				action.setEnabled(false);
			else if(XSDUtil.isComplexType(getSelectedComponent().getElement()))
				action.setEnabled(false);
		}		
	}
}
