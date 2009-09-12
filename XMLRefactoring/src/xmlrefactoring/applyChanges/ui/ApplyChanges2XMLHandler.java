package xmlrefactoring.applyChanges.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;

import xmlrefactoring.plugin.XMLRefactoringPlugin;

/**
 * Primeira idéia de chamada do ApplyChanges. 
 * Foi trocado pela seleção a partir do XSD, através de ApplyChanges2XMLAction.
 * @author guilherme
 *
 */
@Deprecated
public class ApplyChanges2XMLHandler extends AbstractHandler{

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ApplyChanges2XMLWizard wizard = new ApplyChanges2XMLWizard(null);
		WizardDialog dialog = new WizardDialog
		(XMLRefactoringPlugin.getShell(),wizard);
		dialog.open();
		return null;
	}

}
