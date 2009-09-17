package xmlrefactoring.applyChanges.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;

import xmlrefactoring.plugin.xslt.FileControl;

public class XSLTransformer {

	public static void changeVersion(IFile schema, File xml, int initialVersion, int finalVersion){
		
		List<File> xslFiles = FileControl.getAllXSL(schema,initialVersion,finalVersion);
		
		//Set implementations there are going to be used
		System.setProperty("javax.xml.transform.TransformerFactory",
		"net.sf.saxon.TransformerFactoryImpl");
		// Create a transform factory instance
		TransformerFactory tfactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			for(File file:xslFiles){
			// Create a transformer for the stylesheet.
				transformer = tfactory.newTransformer(new StreamSource(file));
				transformer.transform(new StreamSource(xml),new StreamResult(xml));
			}
			
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
