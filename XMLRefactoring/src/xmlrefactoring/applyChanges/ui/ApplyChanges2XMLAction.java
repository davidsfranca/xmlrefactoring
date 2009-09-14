package xmlrefactoring.applyChanges.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;

import xmlrefactoring.plugin.XMLRefactoringPlugin;

public class ApplyChanges2XMLAction extends SchemaFileAction{

	public void run(IAction action) {
		ApplyChanges2XMLWizard wizard = new ApplyChanges2XMLWizard(getSelectedSchema());
		WizardDialog dialog = new WizardDialog
		(XMLRefactoringPlugin.getShell(), wizard);
		dialog.open();		
	}

}
