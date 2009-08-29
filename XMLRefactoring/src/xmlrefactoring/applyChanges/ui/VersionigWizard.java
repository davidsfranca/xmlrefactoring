package xmlrefactoring.applyChanges.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.wizard.Wizard;

public class VersionigWizard extends Wizard {
	
	private VersioningWizardPage page;

	public VersionigWizard(IFile selectedSchema) {
		page = new VersioningWizardPage(selectedSchema);
	}

	@Override
	public boolean performFinish() {
		System.out.println(page.getSelectedVersion());
		System.out.println(page.getSelectedFile());
		return true;
	}
	
	@Override
	public void addPages() {
		addPage(page);
	}

}
