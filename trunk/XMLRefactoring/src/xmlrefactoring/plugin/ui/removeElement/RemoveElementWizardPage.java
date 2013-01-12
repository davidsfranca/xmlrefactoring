package xmlrefactoring.plugin.ui.removeElement;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import xmlrefactoring.plugin.ui.BaseUserInputWizardPage;

public class RemoveElementWizardPage extends BaseUserInputWizardPage {

	private RemoveElementWizard wizard;
	private static final String pageName = "Remove Element Wizard Page";

	public RemoveElementWizardPage(RemoveElementWizard wizard) {
		super(pageName);
		this.wizard = wizard;
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		GridLayout grid = new GridLayout();
		composite.setLayout(grid);
		grid.numColumns = 2;
		new Label(composite, SWT.NONE).setText("Remove element");
	}

}
