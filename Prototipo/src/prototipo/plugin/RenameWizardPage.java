package prototipo.plugin;

import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class RenameWizardPage extends UserInputWizardPage {

	Text newName;
	
	public RenameWizardPage(String name) {
		super(name);
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		GridLayout grid = new GridLayout();
		composite.setLayout(grid);
		grid.numColumns = 2;
		new Label(composite, SWT.NONE).setText("Enter with the new component name:");
		newName =  new Text(composite, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		newName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		newName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent event) {
				handleInputChanged();
			}
		});
	}
	
	private void handleInputChanged(){
		getRenameRefactoring().setNewName(newName.getText());
	}
	
	private RenameRefactoring getRenameRefactoring(){
		return (RenameRefactoring) getRefactoring();
	}
	
	

}
