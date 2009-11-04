package xmlrefactoring.plugin.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import xmlrefactoring.plugin.XMLRefactoringPlugin;

public abstract class BaseAction implements IEditorActionDelegate {

	protected abstract RefactoringWizard getWizard();
	
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		// TODO Auto-generated method stub
		action.setEnabled(false);
	}
	
	public void run(IAction action) {
		try{
		RefactoringWizardOpenOperation operation = new RefactoringWizardOpenOperation(getWizard());
		operation.run(XMLRefactoringPlugin.getShell(), "Refatoracao Teste Title");
		}
		catch(InterruptedException e){
			//TODO
			e.printStackTrace();
		}
	}

}
