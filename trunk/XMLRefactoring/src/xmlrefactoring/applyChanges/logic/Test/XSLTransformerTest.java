package xmlrefactoring.applyChanges.logic.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.resources.ResourcesPlugin;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import xmlrefactoring.applyChanges.logic.XSLTransformer;


public class XSLTransformerTest {
	
	private DocumentBuilder docBuilder;
	private XPath xpath;
	
	private static final String ABSOLUTE_ROOT_PATH = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	private static final String BASE_PATH = "/PluginTest/src/transformerTest/";
	private static final String SCHEMA_NAME = "test.xsd";
	private static final String XML_INITIAL = "test.xml";
	private static final String XML_V1 = "test1.xml";
	private static final String XML_V2 = "test2.xml";
	private static final String XML_V3 = "test3.xml";

	
	public XSLTransformerTest() throws ParserConfigurationException{
		 xpath = XPathFactory.newInstance().newXPath();
		 docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	}
	
	@Test
	public void changeVersion() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException{
		String dirPath = ABSOLUTE_ROOT_PATH+BASE_PATH;
		File schema = new File(dirPath+SCHEMA_NAME);
		InputStream xml = new FileInputStream(new File(dirPath+XML_INITIAL));
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		XSLTransformer.changeVersion(schema, xml,output, 0, 3);
		Assert.assertNotNull(output);
		Assert.assertNotSame(output.size(), 0);
		
		InputStream xml3 = new FileInputStream(new File(dirPath+XML_V3));
		XSLTransformer.changeVersion(schema, xml3,output, 3, 0);
		Assert.assertNotNull(output);
		Assert.assertNotSame(output.size(), 0);
		
	}

}
