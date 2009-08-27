package xmlrefactoring.versioning;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class VersioningWizardPage extends WizardPage{

	private static final String pageName = "XML Versioning";
	private Combo xmlTargetVersion;
	
	protected VersioningWizardPage() {
		super(pageName);
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);		
		GridLayout grid = new GridLayout();
		composite.setLayout(grid);
		grid.numColumns = 2;		
		
		new Label(composite, SWT.None).setText("Enter with the desired version for the XML:");
		
		xmlTargetVersion = new Combo(composite, SWT.DROP_DOWN);
		for(int i = 1; i <= 3; i++){
			for(int j = 0; j < 5; j++){
				xmlTargetVersion.add(i + "." + j);
			}
		}
		
	}
	
	public String getSelectedVersion(){
		return xmlTargetVersion.getItem(xmlTargetVersion.getSelectionIndex());
	}

}
