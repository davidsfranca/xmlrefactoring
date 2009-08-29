package xmlrefactoring.applyChanges.ui;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import xmlrefactoring.plugin.XMLRefactoringPlugin;

public class VersioningWizardPage extends WizardPage{

	private static final String pageName = "XML Versioning";
	private static final String BROWSE_BUTTON_TEXT = "Browse...";
	private static final String VERSION_LABEL = "Enter with the desired version for the XML:";
	private static final String SCHEMA_LABEL = "Selected Schema:";
	private Combo xmlTargetVersion;
	private Combo xmlPath;
	private File selectedXMLFile;
	private IFile selectedSchema;
	private int schemaMaxVersion;
	private int xmlVersion;
	
	private final int XML_PATH_WIDTH = 30, XML_PATH_HEIGHT = 50;

	public VersioningWizardPage(IFile selectedSchema) {
		super(pageName);
		this.selectedSchema = selectedSchema;
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);		
		GridLayout grid = new GridLayout();
		composite.setLayout(grid);
		grid.numColumns = 2;
				
		new Label(composite, SWT.None).setText(SCHEMA_LABEL);
		Text selectedSchemaText = new Text(composite, SWT.SIMPLE);
		selectedSchemaText.setEditable(false);
		//TODO Verificar como ficar‡ l—gica, colocado para manter compatibilidade com Handler
		if(selectedSchemaText != null)
			selectedSchemaText.setText(selectedSchema.getName());
		
		xmlPath = new Combo(composite, SWT.DROP_DOWN);		
		Button browseButton = new Button(composite, SWT.PUSH);
		browseButton.setText(BROWSE_BUTTON_TEXT);
		browseButton.addSelectionListener(new SelectionAdapter(){

			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(XMLRefactoringPlugin.getShell());
				String fileString = dialog.open(); 
				xmlPath.add(fileString);
				xmlPath.select(0);
				selectedXMLFile = new File(fileString);
				try {
					DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();				
					Document doc = docBuilder.parse (selectedXMLFile);
					String versionString = doc.getDocumentElement().getAttribute("schemaVersion");
					xmlVersion = Integer.parseInt(versionString);
					updateXMLTargetVersion();
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
		
		new Label(composite, SWT.None).setText(VERSION_LABEL);
		
		xmlTargetVersion = new Combo(composite, SWT.DROP_DOWN);				
	}
	
	private void updateXMLTargetVersion() {
		xmlTargetVersion.removeAll();
		for(Integer i = xmlVersion + 1; i <= schemaMaxVersion; i++)
				xmlTargetVersion.add(i.toString());
	}
	
	public String getSelectedVersion(){
		return xmlTargetVersion.getItem(xmlTargetVersion.getSelectionIndex());
	}
	
	public String getSelectedFile(){
		return xmlPath.getItem(xmlPath.getSelectionIndex());
	}

}
