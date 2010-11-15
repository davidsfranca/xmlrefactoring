package xmlrefactoring.plugin.ui.rename;

import java.awt.Checkbox;

import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.xsd.ui.internal.refactor.INameUpdating;

import xmlrefactoring.plugin.ui.BaseUserInputWizardPage;

public class RenameWizardPage  extends BaseUserInputWizardPage{

	private Text newName;
	private RenameWizard renameWizard;
	private static final String pageName = "Rename Wizard Page";	
	
	public RenameWizardPage(RenameWizard renameWizard) {
		super(pageName);
		this.renameWizard = renameWizard;
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		GridLayout grid = new GridLayout();
		composite.setLayout(grid);
		grid.numColumns = 2;
		new Label(composite, SWT.NONE).setText("Enter with the new component name " +
				"(Leave it blank to use the same name):");
		
		newName =  new Text(composite, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		newName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		newName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				handleInputChanged();
			}
		});
	}
	
	private void handleInputChanged(){
		renameWizard.getProcessor().setNewElementName(newName.getText());
	}

}
