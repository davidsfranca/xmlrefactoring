package xmlrefactoring.versioning;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;

import xmlrefactoring.plugin.XMLRefactoringPlugin;

public class VersioningHandler extends AbstractHandler{

	public Object execute(ExecutionEvent event) throws ExecutionException {
		VersionigWizard wizard = new VersionigWizard();
		WizardDialog dialog = new WizardDialog
		(XMLRefactoringPlugin.getShell(),wizard);
		dialog.open();
		return null;
	}

}
