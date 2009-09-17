package xmlrefactoring.applyChanges.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.plugin.AbstractUIPlugin;

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
		XSLTransformer.changeVersion(schema,page.getSelectedXMLFile(),page.getXmlVersion()+1, page.getSelectedVersion());
		System.out.println(page.getSelectedVersion());
		System.out.println(page.getSelectedXMLFile().getName());
		return true;
	}
	
	@Override
	public void addPages() {
		addPage(page);
	}

}
