package xmlrefactoring.applyChanges.external;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.CoreException;
import org.xml.sax.SAXException;

import xmlrefactoring.applyChanges.logic.XSLTransformer;
import xmlrefactoring.plugin.xslt.FileControl;

public class XMLConverter {
	
	/**
	 * The schema of the XML`s to be converted
	 */
	private File schema;
	
	public XMLConverter(File schema){
		this.schema = schema;
	}
	
	public InputStream getXML(InputStream baseXML, int targetVersion) throws IOException, ParserConfigurationException, SAXException, CoreException{
		return getXML(baseXML, FileControl.getSchemaVersion(baseXML), targetVersion);
	}
	
	public InputStream getXML(InputStream baseXML, int version, int targetVersion) throws IOException, ParserConfigurationException, SAXException, CoreException{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		XSLTransformer.changeVersion(schema, baseXML, output, version, targetVersion);
		return new ByteArrayInputStream(output.toByteArray());
	}

	public void setSchema(File schema) {
		this.schema = schema;
	}
	
}
