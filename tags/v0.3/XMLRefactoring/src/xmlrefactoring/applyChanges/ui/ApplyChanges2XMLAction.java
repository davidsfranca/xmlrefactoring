package xmlrefactoring.applyChanges.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.XMLRefactoringPlugin;

public class ApplyChanges2XMLAction extends SchemaFileAction{

	public void run(IAction action) {
		try{
			ApplyChanges2XMLWizard wizard = new ApplyChanges2XMLWizard(getSelectedSchema());
			WizardDialog dialog = new WizardDialog
			(XMLRefactoringPlugin.getShell(), wizard);
			dialog.open();
		}catch(CoreException e){
			MessageDialog.openError(XMLRefactoringPlugin.getShell(), 
					XMLRefactoringMessages.getString("ApplyChanges2XMLWizardPage.ReadDescriptorError"), 
					e.getMessage());
			e.printStackTrace();			
		}
	}

}
