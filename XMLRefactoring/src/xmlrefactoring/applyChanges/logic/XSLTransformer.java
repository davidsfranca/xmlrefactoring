package xmlrefactoring.applyChanges.logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.core.resources.IFile;
import org.xml.sax.SAXException;

import xmlrefactoring.plugin.xslt.FileControl;

public class XSLTransformer {

	public static void changeVersion(IFile schema, File xml, int initialVersion, int finalVersion){

		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			changeVersion(schema.getLocation().toFile(), new FileInputStream(xml), output, initialVersion, finalVersion);			
			
			if(output.size()!=0){
				FileOutputStream newXML = new FileOutputStream(xml);
				newXML.write(output.toByteArray());
				newXML.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	public static void changeVersion(File schema, InputStream xml, OutputStream destination, int initialVersion, int finalVersion) throws IOException{
		
		List<File> xslFiles = FileControl.getAllXSL(schema,initialVersion,finalVersion);
		if(xslFiles.size()!=0){
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] inputBytes = new byte[xml.available()];
			xml.read(inputBytes);
			ByteArrayInputStream input = new ByteArrayInputStream(inputBytes); 
	
			//Set implementations there are going to be used
			if(!"net.sf.saxon.TransformerFactoryImpl".equals(System.getProperty("javax.xml.transform.TransformerFactory")))
				System.setProperty("javax.xml.transform.TransformerFactory",
			"net.sf.saxon.TransformerFactoryImpl");
			// Create a transform factory instance
			TransformerFactory tfactory = TransformerFactory.newInstance();
			Transformer transformer;
			try {
				for(File file:xslFiles){
					// Create a transformer for the stylesheet.
					output = new ByteArrayOutputStream();
					transformer = tfactory.newTransformer(new StreamSource(file));
					transformer.transform(new StreamSource(input),new StreamResult(output));
					input = new ByteArrayInputStream(output.toByteArray());				
				}
				destination.write(output.toByteArray());
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		
	}

}
