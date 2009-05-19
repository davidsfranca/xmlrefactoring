package xmlrefactoring.versioning;

import org.eclipse.jface.wizard.Wizard;

public class VersionigWizard extends Wizard {

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void addPages() {
		addPage(new VersioningWizardPage());
	}

}
