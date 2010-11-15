package xmlrefactoring.plugin.ui.elem2attr;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import xmlrefactoring.plugin.ui.BaseUserInputWizardPage;

public class Elem2AttrWizardPage extends BaseUserInputWizardPage {
	private Elem2AttrWizard wizard;
	private Text attrName;
	
	public Elem2AttrWizardPage(Elem2AttrWizard wizard) {
		super("Transform Element to Attribute Page");
		this.wizard = wizard;
	}
	
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		GridLayout grid = new GridLayout();
		composite.setLayout(grid);
		grid.numColumns = 2;
		new Label(composite, SWT.NONE).setText("Enter with the new attribute name:");
		attrName = new Text(composite, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		attrName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		attrName.addModifyListener(new ModifyListener() {
			
			public void modifyText(ModifyEvent e) {
				handleInputChanged();
			}
		});
	}
	
	public void handleInputChanged()
	{
		String text = attrName.getText();
		if(attrName.getText().isEmpty()) text = "";
		
		wizard.getProcessor().setAttrName(text);
	}
}
