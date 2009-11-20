package xmlrefactoring.changeVersion.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.XMLRefactoringPlugin;
import xmlrefactoring.plugin.xslt.FileControl;

public class ChangeVersionWizardPage extends UserInputWizardPage {

	private IFile selectedSchema;
	private int newVersion;
	
	public ChangeVersionWizardPage(IFile selectedSchema, int newVesion) {
		super(XMLRefactoringMessages.getString("ChangeVersionWizardPage.Name"));
		this.selectedSchema = selectedSchema;
		this.newVersion = newVesion;
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		//TODO Trocar pelo layout simples
		composite.setLayout(new GridLayout());
		new Label(composite, SWT.NONE).setText(XMLRefactoringMessages.getString("ChangeVersionWizardPage.Confirmation") + newVersion + "?");
	}

}
