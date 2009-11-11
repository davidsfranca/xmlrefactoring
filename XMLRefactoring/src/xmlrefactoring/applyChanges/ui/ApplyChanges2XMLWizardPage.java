package xmlrefactoring.applyChanges.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.xml.sax.SAXException;

import xmlrefactoring.plugin.XMLRefactoringPlugin;
import xmlrefactoring.plugin.xslt.FileControl;

public class ApplyChanges2XMLWizardPage extends WizardPage{

	private static final String PAGE_NAME = "Apply changes to XML input";
	private static final String BROWSE_BUTTON_TEXT = "Browse...";
	private static final String VERSION_LABEL = "Enter with the desired version for the XML:";
	private static final String SCHEMA_LABEL = "Selected Schema:";
	private static final String PAGE_TITLE = "Apply changes to XML";
	private Combo xmlTargetVersion;
	private Combo xmlPath;
	private File selectedXMLFile;
	private IFile selectedSchema;
	private int schemaMaxVersion;
	private int xmlVersion;
	
	private final int XSD_NAME_WIDTH = 400, XSD_NAME_HEIGHT = 20, XML_PATH_WIDTH = 270, XML_PATH_HEIGHT = 20;

	public ApplyChanges2XMLWizardPage(IFile selectedSchema) {
		super(PAGE_NAME, PAGE_TITLE, null);
		this.selectedSchema = selectedSchema;
		this.schemaMaxVersion = FileControl.readDescriptor(selectedSchema)[0];
	}

	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);		
		GridLayout grid = new GridLayout();
		grid.numColumns = 2;
		composite.setLayout(grid);
		
		//Schema name label		
		new Label(composite, SWT.None).setText(SCHEMA_LABEL);
		
		//Schema name
		Text selectedSchemaText = new Text(composite, SWT.SIMPLE);
		selectedSchemaText.setEditable(false);

		if(selectedSchema != null){
			String schemaName = selectedSchema.getName();
			selectedSchemaText.setText(schemaName);
			selectedSchemaText.setSize(XSD_NAME_WIDTH, XSD_NAME_HEIGHT);
		}
		
		//XML Path
		xmlPath = new Combo(composite, SWT.DROP_DOWN);
		xmlPath.setText("Choose a XML to refactor               ");
		xmlPath.setSize(XML_PATH_WIDTH, XML_PATH_HEIGHT);
		
		//Browse button
		Button browseButton = new Button(composite, SWT.PUSH);
		browseButton.setText(BROWSE_BUTTON_TEXT);
		browseButton.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(XMLRefactoringPlugin.getShell());
				String fileString = dialog.open(); 
				xmlPath.add(fileString);
				xmlPath.select(0);
				xmlPath.setSize(XML_PATH_WIDTH, XML_PATH_HEIGHT);
				selectedXMLFile = new File(fileString);
				try {
					xmlVersion = FileControl.getSchemaVersion(new FileInputStream(selectedXMLFile));
					updateXMLTargetVersion();
					getContainer().updateButtons();
				} catch (SAXException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParserConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}			
		});
		
		//Version label
		new Label(composite, SWT.None).setText(VERSION_LABEL);
		
		//Version Combo
		xmlTargetVersion = new Combo(composite, SWT.DROP_DOWN);
		xmlTargetVersion.setText("--");
		xmlTargetVersion.addSelectionListener(new SelectionAdapter(){
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				getContainer().updateButtons();
			}
			
		});
	}
	
	private void updateXMLTargetVersion() {
		xmlTargetVersion.removeAll();
		for(Integer i = 0; i < schemaMaxVersion; i++)
				xmlTargetVersion.add(i.toString());
	}
	
	/**
	 * Returns the Selected version to apply into the XML. If no selection was made return -1.
	 */
	public int getSelectedVersion(){
		if(xmlTargetVersion.getSelectionIndex() != -1)
			return Integer.parseInt(xmlTargetVersion.getItem(xmlTargetVersion.getSelectionIndex()));
		else	
			return -1;
	}
	
	public File getSelectedXMLFile(){
		return selectedXMLFile;
	}
	
	@Override
	public boolean isPageComplete(){
		if(!super.isPageComplete() ||
			selectedSchema == null ||
			getSelectedVersion() == -1 ||
			!getSelectedXMLFile().exists()
			)
			return false;
		else			
			return true;
	}

	public int getXmlVersion() {
		return xmlVersion;
	}

}
