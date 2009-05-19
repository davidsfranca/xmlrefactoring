package xmlrefactoring.plugin.ui.attr2elem;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;

import xmlrefactoring.plugin.logic.attr2elem.Attr2ElemProcessor;
import xmlrefactoring.plugin.logic.util.SchemaElementVerifier;
import xmlrefactoring.plugin.ui.BaseAction;

public class Attr2ElemAction extends BaseAction{

	@Override
	protected RefactoringWizard getWizard() {
		Attr2ElemProcessor processor = new Attr2ElemProcessor(getSelectedComponent());
		
		return new Attr2ElemWizard(processor, getSelectedComponent());
	}
	
	@Override
	public void selectionChanged(IAction action, ISelection selection){
		super.selectionChanged(action, selection);
		if(getSelectedComponent() != null){
			if(!SchemaElementVerifier.isAttribute(getSelectedComponent().getElement()))
				action.setEnabled(false);
		}
	}
	
	

}
