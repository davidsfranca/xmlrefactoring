package xmlrefactoring.changeVersion.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.applyChanges.ui.SchemaFileAction;
import xmlrefactoring.plugin.XMLRefactoringPlugin;

public class ChangeVersionAction extends SchemaFileAction {

	public void run(IAction action){
		try {
			ChangeVersionWizard wizard = new ChangeVersionWizard(getSelectedSchema());
			RefactoringWizardOpenOperation operation = new RefactoringWizardOpenOperation(wizard);
			operation.run(XMLRefactoringPlugin.getShell(), "Refatoracao Teste Title");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch(CoreException e){
			MessageDialog.openError(XMLRefactoringPlugin.getShell(), 
					XMLRefactoringMessages.getString("ChangeVersionWizardPage.InvalidSchemaFileTitle"), 
					e.getMessage());
			e.printStackTrace();
		}
	}

}
