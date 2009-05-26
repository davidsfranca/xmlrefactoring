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


public class FileControl {
	
	public static IPath getNextPath(IFile schemaFile) throws ParserConfigurationException, SAXException, IOException{
		
		String SchemaFileName = schemaFile.getName().substring(0,schemaFile.getName().length()-4);
		int[] versionAndFile = readDescriptor(schemaFile);
		
		
		IContainer refactoringFolder = schemaFile.getParent();
		
		StringBuffer fileName = new StringBuffer("/REF_");
		//Takes the name without the extension
		fileName.append(schemaFile.getName().substring(0,schemaFile.getName().length()-4));
		fileName.append("/v_");
		fileName.append(versionAndFile[0]);
		fileName.append("/ref_");
		fileName.append(versionAndFile[1]+1);
		fileName.append(".xsl");
		
		return refactoringFolder.getFullPath().append(fileName.toString());	
		
	}
	
	
	/**
	 * Changes the file Descriptor to include the newest file
	 * @param file
	 */
	public static void addToControl(IFile file) {
		try{
		//get fileNumber
		String number = (file.getName().replace(".xsl","")).replace("ref_", "");
		String version = file.getParent().getName().replace("v_","");
		String fileName = file.getParent().getParent().getName().replace("REF_", "");
	
		String descriptionFile = file.getParent().getParent().getParent().getLocation()+"/desc_"+fileName+".xml";
		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		
		Element descriptor = doc.createElement("descriptor");
		Element lastVersion = doc.createElement("lastVersion");
		lastVersion.setTextContent(version);
		descriptor.appendChild(lastVersion);
		Element lastFileNumer = doc.createElement("lastFileNumber");
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
		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse (getDescriptorFilePath(schemaFile));
		
		NodeList versionNode = doc.getElementsByTagName("lastVersion");
		
		int[] versionAndFile = new int[2];
		
		if(versionNode.getLength()!=1){
			//TODO:error
			System.out.println("Arquivo descritor Invalido");
		}else{
			versionAndFile[0] = (new Integer(versionNode.item(0).getTextContent())).intValue();
		}
		
		NodeList fileNode = doc.getElementsByTagName("lastFileNumber");
		
		if(fileNode.getLength()!=1){
			//TODO:error - trocar por excecao
			System.out.println("Arquivo descritor Invalido");
		}else{
			versionAndFile[1] = (new Integer(fileNode.item(0).getTextContent())).intValue();
		}
		
		return versionAndFile;
		
	}
	
	private static String getDescriptorFilePath(IFile schemaFile){
		//Build the descriptorFilePath
		StringBuilder filePath = new StringBuilder(schemaFile.getParent().getLocation().toString());
		filePath.append("/desc_");
		filePath.append(schemaFile.getName().substring(0,schemaFile.getName().length()-4));
		filePath.append(".xml");
		
		return filePath.toString();
	}



}
