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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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

	public static IPath getNextPath(IFile schemaFile) throws ParserConfigurationException, SAXException, IOException{
		
		int[] versionAndFile = readDescriptor(schemaFile);
		
		IContainer refactoringFolder = schemaFile.getParent();
		String fileName = getFilePath(schemaFile, versionAndFile[0], versionAndFile[1]+1);
		
		return refactoringFolder.getFullPath().append(fileName);	
		
	}
	
	
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
		
		//TODO: Verificar como, usando XML substituir no arquivo, usar outra implementação do DOM
		saveXMLdoc(descriptionFile,doc);
		
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
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

	/**
	 * Reads the descriptor file for that Schema 
	 * Gets the last version and file Number
	 * @param schemaFile
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */	
	private static int[] readDescriptor (IFile schemaFile) throws ParserConfigurationException, SAXException, IOException{
		
		int[] versionAndFile = new int[2];
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		try{
			Document doc = docBuilder.parse (getDescriptorFilePath(schemaFile));
			
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
		}catch(FileNotFoundException e){
			//In this case, this is the first change in this XSD
			//Create the Refactoring Dir
			createVersioningDir(schemaFile,0);
			versionAndFile[0] = 0;
			versionAndFile[1] = 0;
		}
		return versionAndFile;	
	}
	
	private static String getDescriptorFilePath(IFile schemaFile){
		//Build the descriptorFilePath
		StringBuilder filePath = new StringBuilder(schemaFile.getParent().getLocation().toString());
		filePath.append(DESCRIPTORPREFIX);
		filePath.append(schemaFile.getName().substring(0,schemaFile.getName().length()-4));
		filePath.append(DESCFILEEXTENSION);
		
		return filePath.toString();
	}

	private static String getRefactoringDirPath(IFile schemaFile, int version){
		//Build the descriptorFilePath
		StringBuilder dirPath = new StringBuilder(schemaFile.getParent().getLocation().toString());
		dirPath.append("/");
		dirPath.append(DIRECTORYPREFIX);
		dirPath.append(schemaFile.getName().substring(0,schemaFile.getName().length()-4));
		dirPath.append("/");
		dirPath.append(VERSIONPREFIX);
		dirPath.append(version);
		
		return dirPath.toString();
	}
	
	private static String getFilePath(IFile schemaFile, int version, int fileNumber){
		StringBuffer fileName = new StringBuffer("/");
		fileName.append(DIRECTORYPREFIX);
		//Takes the name without the extension
		fileName.append(schemaFile.getName().substring(0,schemaFile.getName().length()-4));
		fileName.append("/");
		fileName.append(VERSIONPREFIX);
		fileName.append(version);
		fileName.append("/");
		fileName.append(FILEPREFIX);
		fileName.append(fileNumber);
		fileName.append(FILEEXTENSION);
		
		return fileName.toString();
	}

	private static void createVersioningDir(IFile schemaFile, int version) throws ParserConfigurationException, SAXException, IOException{
		String path = getRefactoringDirPath(schemaFile, version);
		try{
			(new File(path)).mkdirs();
			
			//First file in the directory
			IPath filePath= schemaFile.getParent().getFullPath().append(getFilePath(schemaFile, version, 0));
			
			XSLTWriter.createXSL(new VersioningRefactoring(null,0), schemaFile,filePath );
		
		}catch (Exception e){//Catch exception if any
		        System.err.println("Error: " + e.getMessage());
		}
		
	}

}
