package xmlrefactoring.plugin.logic.util;

import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.XMLRefactoringPlugin;

public abstract class XMLUtil {

	public static Element createElement(String elementName) throws DOMException, CoreException{		
		return createNewDocument().createElement(elementName);
	}

	public static Element createElementNS(String nameSpaceURI, String qualifiedName) throws DOMException, CoreException{
		return createNewDocument().createElementNS(nameSpaceURI, qualifiedName);
	}

	public static Element createElementNS(String nameSpaceURI, String prefix, String elementName) throws DOMException, CoreException{
		return createElementNS(nameSpaceURI, createQName(prefix, elementName));		
	}
	
	public static QName createQName(Element element){
		String namespace = null;
		if(XSDUtil.isQualified(element))
			namespace = XSDUtil.getTargetNamespace(element);
		return new QName(namespace, XSDUtil.getName(element));
	}

	public static String createQName(String prefix, String elementName){
		StringBuilder qName = new StringBuilder();
		if(prefix != null && !prefix.equals("")){
			qName.append(prefix);
			qName.append(":");
		}
		qName.append(elementName);
		return qName.toString();
	}
	
	public static String getLocalName(String name){
		return name.substring(name.indexOf(":") + 1, name.length());
	}

	private static Document createNewDocument() throws CoreException{
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			return docBuilder.newDocument();
		} catch (ParserConfigurationException e) {
			Status status = new Status(Status.ERROR, 
					XMLRefactoringPlugin.PLUGIN_ID, 
					XMLRefactoringMessages.getString("XMLUtil.DocumentCreationError"), e);
			throw new CoreException(status);			
		}
	}

	public static String toString(Node node) throws CoreException {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer trans = transformerFactory.newTransformer();
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			DOMSource source = new DOMSource(node);
			StringWriter os = new StringWriter();
			StreamResult streamResult = new StreamResult(os);
			trans.transform(source, streamResult);
			String result = os.getBuffer().toString() + "\n";
			return result;
		} catch (TransformerException e) {
			Status status = new Status(Status.ERROR, 
					XMLRefactoringPlugin.PLUGIN_ID, 
					XMLRefactoringMessages.getString("XMLUtil.XML2StringError"), e);
			throw new CoreException(status);
		}	
	}
	
	public static String quoteString(String value){
		StringBuffer sb = new StringBuffer();
		sb.append("\"");
		if(value != null)
			sb.append(value);
		sb.append("\"");		
		return sb.toString();
	}
}
