package xmlrefactoring.applyChanges.ui;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.applyChanges.logic.XSLTransformer;
import xmlrefactoring.plugin.XMLRefactoringPlugin;

public class ApplyChanges2XMLWizard extends Wizard {
	
	private static final String APPLY_CHANGES_WIZARD_TITLE = "Apply changes to XML";
	private ApplyChanges2XMLWizardPage page;
	private IFile schema;

	public ApplyChanges2XMLWizard(IFile selectedSchema) {
		page = new ApplyChanges2XMLWizardPage(selectedSchema);
		schema = selectedSchema;
		setWindowTitle(APPLY_CHANGES_WIZARD_TITLE);
		ImageDescriptor defaultImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(
				XMLRefactoringPlugin.PLUGIN_ID, "icons/applychanges.png");
		setDefaultPageImageDescriptor(defaultImageDescriptor);
	}

	@Override
	public boolean performFinish() {
		
			try {
				XSLTransformer.changeVersion(schema,page.getSelectedXMLFile(),page.getXmlVersion(), page.getSelectedVersion());
			} catch (IOException e) {
				e.printStackTrace();
				MessageDialog.openError(XMLRefactoringPlugin.getShell(), 
						XMLRefactoringMessages.getString("ApplyChanges2XMLWizard.XMLFileProblemTitle"), 
						XMLRefactoringMessages.getString("ApplyChanges2XMLWizard.XMLFileProblemMessage"));
				return false;
			} catch (CoreException e) {
				e.printStackTrace();
				MessageDialog.openError(XMLRefactoringPlugin.getShell(), 
						XMLRefactoringMessages.getString("ApplyChanges2XMLWizard.XMLFileProblemTitle"), 
						e.getMessage());
				return false;
			}
		
		return true;
	}
	
	@Override
	public void addPages() {
		addPage(page);
	}

}
