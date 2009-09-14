package xmlrefactoring.changeVersion.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import xmlrefactoring.plugin.XMLRefactoringPlugin;

public class ChangeVersionWizard extends RefactoringWizard {

	private static final String CHANGE_VERSION_WIZARD_TITLE = "Change XSD version";
	private ChangeVersionWizardPage page;
	
	public ChangeVersionWizard(IFile selectedSchema){
		super(new ChangeVersionRefactoring(selectedSchema), DIALOG_BASED_USER_INTERFACE | CHECK_INITIAL_CONDITIONS_ON_OPEN);
		page = new ChangeVersionWizardPage(selectedSchema);
		setWindowTitle(CHANGE_VERSION_WIZARD_TITLE);
		ImageDescriptor defaultImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(
				XMLRefactoringPlugin.PLUGIN_ID, "icons/applychanges.png");
		setDefaultPageImageDescriptor(defaultImageDescriptor);
	}

	@Override
	protected void addUserInputPages() {
		addPage(page);
	}
	
	public void setNewVersion(int newVersion){
		ChangeVersionRefactoring refactoring = (ChangeVersionRefactoring) getRefactoring();
		refactoring.setNewVersion(newVersion);
	}

}
