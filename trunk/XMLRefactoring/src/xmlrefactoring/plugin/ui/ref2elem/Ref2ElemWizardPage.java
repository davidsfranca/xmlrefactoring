package xmlrefactoring.plugin.ui.ref2elem;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import xmlrefactoring.plugin.ui.BaseUserInputWizardPage;

public class Ref2ElemWizardPage extends BaseUserInputWizardPage {
	private Ref2ElemWizard wizard;
	private Text elemName;

	public Ref2ElemWizardPage(Ref2ElemWizard wizard) {
		super("Transform Reference to Element Page");
		this.wizard = wizard;
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		GridLayout grid = new GridLayout();
		composite.setLayout(grid);
		grid.numColumns = 2;
		new Label(composite, SWT.NONE).setText("Change reference into element:");
	}
}
