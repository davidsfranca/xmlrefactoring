package xmlrefactoring.plugin.xslt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import xmlrefactoring.plugin.logic.util.CreateFolderChange;
import xmlrefactoring.plugin.logic.util.CreateXSLChange;
import xmlrefactoring.plugin.refactoring.VersioningRefactoring;


public class FileControl {
	
	//CONSTANTS - PREFIX
	private static final String DIRECTORYPREFIX = ".REF_";
	private static final String VERSIONPREFIX = ".v_";
	private static final String FILEPREFIX = ".ref_";
	private static final String FILEEXTENSION = ".xsl";
	private static final String DESCRIPTORPREFIX = "/.desc_";
	private static final String DESCFILEEXTENSION = ".xml";
	
	//CONSTANTS - TAGS
	private static final String DESCRIPTORTAG = "descriptor";
	private static final String FILETAG = "lastFileNumber";
	private static final String VERSIONTAG = "lastVersion";
	private static final String VERSIONATTR = "number";
	
	//CONSTANT - XPATH EXPRESSION
	private static final String VERSIONXPATH = "descriptor/versions/version";
	
	//CONSTANT - OTHERS
	private static final String COMPOSITENAME = "Add to version control";
	
	/**
	 * Tests if there is already an descriptor and 
	 * @return
	 */
	public static boolean isUnderVersionControl(IFile schemaFile){
		IContainer container = schemaFile.getParent();
		return container.exists(getDescriptorFilePath(schemaFile));
	}
	
	/**
	 * Return where the next refactoring file should be created
	 * 
	 * @param schemaFile
	 * @param isInitial - flag to know if there is already an descriptr for that file or not
	 * @return
	 */
	public static IPath getNextPath(IFile schemaFile, boolean isInitial) throws ParserConfigurationException, SAXException, IOException{
		
		int[] versionAndFile = readDescriptor(schemaFile);
		
		IContainer refactoringFolder = schemaFile.getParent();
		String fileName=null;
		if(isInitial)
			getFilePath(schemaFile, 0, 1);
		else
			getFilePath(schemaFile, versionAndFile[0], versionAndFile[1]+1);
		
		return refactoringFolder.getFullPath().append(fileName);		
	}
	
	/**
	 * Creates all the required structure to versioning the refactorings in a XSD file
	 * @param schemaFile
	 * @return
	 */
	public static CompositeChange addToVersionControl(IFile schemaFile){
		CompositeChange allChanges = new CompositeChange(COMPOSITENAME);
		
		//Create descriptor file
		IContainer container = schemaFile.getParent();
		IPath descPath = container.getFullPath().append(getDescriptorFilePath(schemaFile));
		//Change change = new CreateFileChange(descPath);
		
		//Create refactoring directory
		IPath refDirPath = container.getFullPath().append(getRefactoringDirPath(schemaFile));
		Change refDirCreation = new CreateFolderChange(refDirPath);
		allChanges.add(refDirCreation);
		
		//Create version directory
		allChanges.add(createVersioningDir(schemaFile, 0));

		
		//Create XSL that adds version
		Change xslChange = new CreateXSLChange(new VersioningRefactoring(null,0),getFilePath(schemaFile, 0, 0));
		allChanges.add(xslChange);
		
		return allChanges;
	}
	
	//TODO: change to receive a IPath and return a TextFileChange
	/**
	 * Changes the file Descriptor to include the newest file
	 * @param file
	 */
	public static void addToControl(IFile file) {
		try{
		//get fileNumber
		String number = (file.getName().replace(FILEEXTENSION,"")).replace(FILEPREFIX, "");
		String version = file.getParent().getName().replace(VERSIONPREFIX,"");
		String fileName = file.getParent().getParent().getName().replace(DIRECTORYPREFIX, "");
	
		String descriptionFile = file.getParent().getParent().getParent().getLocation()+DESCRIPTORPREFIX+fileName+DESCFILEEXTENSION;
		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		
		Element descriptor = doc.createElement(DESCRIPTORTAG);
		Element lastVersion = doc.createElement(VERSIONTAG);
		lastVersion.setTextContent(version);
		descriptor.appendChild(lastVersion);
		Element lastFileNumer = doc.createElement(FILETAG);
		lastFileNumer.setTextContent(number);
		descriptor.appendChild(lastFileNumer);
		doc.appendChild(descriptor);
		
		//TODO: Verificar como, usando XML substituir no arquivo, usar outra implementa��o do DOM
		saveXMLdoc(descriptionFile,doc);
		
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}


	/**
	 * Reads the descriptor file for that Schema 
	 * Gets the last version and file Number
	 * Only called when it is known that the descriptor file is available
	 * @param schemaFile
	 * @return [1]: version number, [2]: file number
	 */	
	private static int[] readDescriptor (IFile schemaFile) {
		
		int[] versionAndFile = new int[2];
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse (getDescriptorFilePath(schemaFile).toString());
			
			NodeList versionNode = doc.getElementsByTagName(VERSIONTAG);
			
			if(versionNode.getLength()!=1){
				//TODO:error
				System.out.println("Arquivo descritor Invalido");
			}else{
				versionAndFile[0] = (new Integer(versionNode.item(0).getTextContent())).intValue();
			}
			
			NodeList fileNode = doc.getElementsByTagName(FILETAG);
			
			if(fileNode.getLength()!=1){
				//TODO:error - trocar por excecao
				System.out.println("Arquivo descritor Invalido");
			}else{
				versionAndFile[1] = (new Integer(fileNode.item(0).getTextContent())).intValue();
			}
		}catch(Exception e){
			//TODO: treat Eception
		}
		return versionAndFile;	
	}
	
	/**
	 * Reads the descriptor file for that Schema 
	 * Gets the last file Number for each version
	 * @param schemafile
	 * @return
	 */
	public static int[] getVersionsFromDescriptor(IFile schemaFile, int initialVersion, int lastVersion){

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		int fileNumbers[] = new int[lastVersion-initialVersion];
		try{
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();		
			Document doc = docBuilder.parse (getDescriptorFilePath(schemaFile).toString());

			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr;
			
			for(int i = initialVersion; i < lastVersion; i++){
				expr = xpath.compile(VERSIONXPATH+"[@"+VERSIONATTR+"="+i+"]");
				Node file = ((NodeList)expr.evaluate(doc, XPathConstants.NODESET)).item(0);
				fileNumbers[i]=new Integer(file.getNodeValue());
			}
			
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileNumbers;
	}
	// It is called from the RefactoringParticipant and from the versioning participant
	public static Change createVersioningDir(IFile schemaFile, int version){
		
		IContainer container = schemaFile.getParent();
		IPath versionDirPath = container.getFullPath().append(getVersionDirPath(schemaFile, version));
		Change versionDirCreation = new CreateFolderChange(versionDirPath);
		
		return versionDirCreation;
	}

	
	//TODO: Probably this is not required anymore
	private static void saveXMLdoc(String fileName, Document doc){
		
		File xmlOutputFile = new File(fileName);
		FileOutputStream fos = null;
		Transformer transformer = null;
		
		try {
			fos = new FileOutputStream(xmlOutputFile);
		}
		catch (FileNotFoundException e) {
			System.out.println("Error occured: " + e.getMessage());
		}
		
		// Use a Transformer for output
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			transformer = transformerFactory.newTransformer();
		}
		catch (TransformerConfigurationException e) {
			System.out.println("Transformer configuration error: " + e.getMessage());
		}
		
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(fos);
		// transform source into result will do save
		try {
			transformer.transform(source, result);
		}
		catch (TransformerException e) {
		      System.out.println("Error transform: " + e.getMessage());
		}
		
	}
	
	public static IPath getDescriptorFilePath(IFile schemaFile){
		IContainer container = schemaFile.getParent();
		
		//Build the descriptorFilePath
		StringBuilder filePath = new StringBuilder(DESCRIPTORPREFIX);
		filePath.append(schemaFile.getName().substring(0,schemaFile.getName().length()-4));
		filePath.append(DESCFILEEXTENSION);
		
		IPath descPath = container.getFullPath().append(filePath.toString());
		
		return descPath;
	}

	private static IPath getRefactoringDirPath(IFile schemaFile){
		IContainer container = schemaFile.getParent();
		
		//Build the descriptorFilePath
		StringBuilder dirPath = new StringBuilder("/");
		dirPath.append(DIRECTORYPREFIX);
		dirPath.append(schemaFile.getName().substring(0,schemaFile.getName().length()-4));
		dirPath.append("/");
		
		return container.getLocation().append(dirPath.toString());
	}
	
	private static IPath getVersionDirPath(IFile schemaFile , int version){
		IPath dir =  getRefactoringDirPath(schemaFile);
		StringBuilder dirPath = new StringBuilder("/");
		dirPath.append(VERSIONPREFIX);
		dirPath.append(version);
		
		return dir.append(dirPath.toString());
		
	}
		
	private static IPath getFilePath(IFile schemaFile, int version, int fileNumber){
		StringBuffer fileName = new StringBuffer("/");
		fileName.append(FILEPREFIX);
		fileName.append(fileNumber);
		fileName.append(FILEEXTENSION);
		
		return getVersionDirPath(schemaFile, version).append(fileName.toString());
	}


}
