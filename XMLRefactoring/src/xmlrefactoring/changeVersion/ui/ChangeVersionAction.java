package xmlrefactoring.changeVersion.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;

import xmlrefactoring.applyChanges.ui.SchemaFileAction;
import xmlrefactoring.plugin.XMLRefactoringPlugin;

public class ChangeVersionAction extends SchemaFileAction {

	public void run(IAction action){
		try {
			ChangeVersionWizard wizard = new ChangeVersionWizard(getSelectedSchema());
			RefactoringWizardOpenOperation operation = new RefactoringWizardOpenOperation(wizard);
			operation.run(XMLRefactoringPlugin.getShell(), "Refatoracao Teste Title");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
