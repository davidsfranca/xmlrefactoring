package xmlrefactoring.versioning;

import org.eclipse.jface.wizard.Wizard;

public class VersionigWizard extends Wizard {
	
	private VersioningWizardPage page = new VersioningWizardPage();

	@Override
	public boolean performFinish() {
		System.out.println(page.getSelectedVersion());
		return true;
	}
	
	@Override
	public void addPages() {
		addPage(page);
	}

}
