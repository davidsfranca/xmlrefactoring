package xmlrefactoring.changeVersion.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.XMLRefactoringPlugin;
import xmlrefactoring.plugin.xslt.FileControl;

public class ChangeVersionWizard extends RefactoringWizard {

	private IFile selectedSchema;
	private int newVersion;

	public ChangeVersionWizard(IFile selectedSchema) throws CoreException{
		super(new ChangeVersionRefactoring(selectedSchema), DIALOG_BASED_USER_INTERFACE | CHECK_INITIAL_CONDITIONS_ON_OPEN);
		this.selectedSchema = selectedSchema;
		this.newVersion = FileControl.readDescriptor(selectedSchema)[0] + 1;
		((ChangeVersionRefactoring)getRefactoring()).setNewVersion(newVersion);
		setWindowTitle(XMLRefactoringMessages.getString("ChangeVersionWizard.Title"));
		ImageDescriptor defaultImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(
				XMLRefactoringPlugin.PLUGIN_ID, "icons/applychanges.png");
		setDefaultPageImageDescriptor(defaultImageDescriptor);
	}

	@Override
	protected void addUserInputPages() {			 
		ChangeVersionWizardPage page = new ChangeVersionWizardPage(selectedSchema, newVersion);
		addPage(page);	
	}

}
