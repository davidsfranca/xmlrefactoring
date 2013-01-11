package xmlrefactoring.plugin.ui.addElement;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import xmlrefactoring.plugin.ui.BaseUserInputWizardPage;

public class AddElementWizardPage extends BaseUserInputWizardPage {

	private Text elementName;
	private Text elementValue;
	private Combo elementType;
	private Button optionalElement;
	private AddElementWizard wizard;
	private static final String pageName = "Add New Element Wizard Page";

	public AddElementWizardPage(AddElementWizard wizard) {
		super(pageName);
		this.wizard = wizard;
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		GridLayout grid = new GridLayout();
		composite.setLayout(grid);
		grid.numColumns = 2;
		ModifyListener listener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				handleInputChanged();
			}
		};
		new Label(composite, SWT.NONE).setText("Enter with the name of the new element:");
		elementName =  new Text(composite, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		elementName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		elementName.addModifyListener(listener);
		new Label(composite, SWT.NONE).setText("Enter the type of the new element:");
		elementType = new Combo(composite, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		elementType.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		String[] items = {"string", "decimal", "integer", "boolean", "date", "time"};
		elementType.setItems(items);
		elementType.setText(items[0]);
		elementType.addModifyListener(listener);
		new Label(composite, SWT.NONE).setText("Enter the default value for the element");
		elementValue =  new Text(composite, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		elementValue.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		elementValue.addModifyListener(listener);
		optionalElement = new Button(composite, SWT.CHECK);
		optionalElement.setText("Check if the element is optional");
		optionalElement.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				handleInputChanged();
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				handleInputChanged();
			}
		});
	}

	private void handleInputChanged(){
		wizard.getProcessor().setNewElementName(elementName.getText());
		wizard.getProcessor().setNewElementType(elementType.getText());
		wizard.getProcessor().setNewElementValue(elementValue.getText());
		wizard.getProcessor().setNewElemOptional(optionalElement.getSelection());
	}

}
