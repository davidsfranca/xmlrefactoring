package xmlrefactoring.plugin.ui.attr2elem;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;

import xmlrefactoring.plugin.logic.attr2elem.Attr2ElemProcessor;
import xmlrefactoring.plugin.logic.util.XSDUtil;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;
import xmlrefactoring.plugin.ui.SingleInputAction;

public class Attr2ElemAction extends SingleInputAction{

	@Override
	protected BaseRefactoringWizard<Attr2ElemProcessor> getWizard() {
		Attr2ElemProcessor processor = new Attr2ElemProcessor(getSelectedComponent());		
		return new Attr2ElemWizard(processor);
	}
	
	@Override
	public void selectionChanged(IAction action, ISelection selection){
		super.selectionChanged(action, selection);
		if(getSelectedComponent() != null){
			if(!XSDUtil.isAttribute(getSelectedComponent().getElement()))
				action.setEnabled(false);
		}		
	}

}
