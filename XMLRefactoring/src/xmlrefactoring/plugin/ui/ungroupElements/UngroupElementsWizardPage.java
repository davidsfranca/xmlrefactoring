package xmlrefactoring.plugin.ui.ungroupElements;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import xmlrefactoring.plugin.ui.BaseUserInputWizardPage;

public class UngroupElementsWizardPage extends BaseUserInputWizardPage {
	private UngroupElementsWizard wizard;
	private static final String pageName = "Ungroup Elements Wizard Page";
	
	public UngroupElementsWizardPage(UngroupElementsWizard wizard) {
		super(pageName);
		this.wizard = wizard;
	}

	public void createControl(Composite parent){		
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		GridLayout grid = new GridLayout();
		composite.setLayout(grid);
		grid.numColumns = 2;
		new Label(composite, SWT.NONE).setText("Are you sure you want to ungroup the selected item?");
	}
}
