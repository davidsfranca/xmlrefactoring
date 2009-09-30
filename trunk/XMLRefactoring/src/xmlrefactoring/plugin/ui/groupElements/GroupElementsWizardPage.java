package xmlrefactoring.plugin.ui.groupElements;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import xmlrefactoring.plugin.ui.BaseUserInputWizardPage;

public class GroupElementsWizardPage extends BaseUserInputWizardPage {

	private Text groupName;
	private GroupElementsWizard wizard;
	
	public GroupElementsWizardPage(GroupElementsWizard wizard) {
		super("Group Element Page");
		this.wizard = wizard;
	}

	@Override
	public void createControl(Composite parent){
		super.createControl(parent);
		
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		GridLayout grid = new GridLayout();
		composite.setLayout(grid);
		grid.numColumns = 2;
		new Label(composite, SWT.NONE).setText("Enter with the group name:");
		groupName =  new Text(composite, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		groupName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		groupName.addModifyListener(new ModifyListener(){

			public void modifyText(ModifyEvent e) {
				wizard.getProcessor().setGroupName(groupName.getText());				
			}
			
		});
		
	}

}
