package xmlrefactoring.plugin.ui.elem2ref;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import xmlrefactoring.plugin.ui.BaseUserInputWizardPage;

public class Elem2RefWizardPage extends BaseUserInputWizardPage {
	private Elem2RefWizard wizard;
	
	public Elem2RefWizardPage(Elem2RefWizard wizard) {
		super("Transform Element to Reference page");
		this.wizard = wizard;
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		GridLayout grid = new GridLayout();
		composite.setLayout(grid);
		grid.numColumns = 2;
		new Label(composite, SWT.NONE).setText("Change element into reference:");
	}

}
