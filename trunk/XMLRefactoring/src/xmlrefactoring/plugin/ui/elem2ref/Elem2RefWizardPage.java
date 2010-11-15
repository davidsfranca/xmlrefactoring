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
	private Text refName;
	
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
		new Label(composite, SWT.NONE).setText("Enter with the new reference name:");
		refName = new Text(composite, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		refName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		refName.addModifyListener(new ModifyListener() {
			
			public void modifyText(ModifyEvent e) {
				handleInputChanged();
			}
		});	
	}
	
	public void handleInputChanged()
	{
		String text = refName.getText();
		if(refName.getText().isEmpty()) text = "";
		
		wizard.getProcessor().setRefName(text);
	}
}
