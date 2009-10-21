package xmlrefactoring.plugin.logic.util;

import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class XMLUtil {

	public static Element createElement(String elementName){		
		return createNewDocument().createElement(elementName);
	}

	public static Element createElementNS(String nameSpaceURI, String qualifiedName){
		return createNewDocument().createElementNS(nameSpaceURI, qualifiedName);
	}

	public static Element createElementNS(String nameSpaceURI, String prefix, String elementName){
		return createElementNS(nameSpaceURI, createQName(prefix, elementName));		
	}
	
	public static QName createQName(Element element){
		String namespace = null;
		if(XSDUtil.isGlobal(element))
			namespace = XSDUtil.getTargetNamespace(element);
		return new QName(namespace, XSDUtil.getName(element));
	}

	public static String createQName(String prefix, String elementName){
		StringBuilder qName = new StringBuilder();
		if(prefix != null){
			qName.append(prefix);
			qName.append(":");
		}
		qName.append(elementName);
		return qName.toString();
	}
	
	public static String getLocalName(String name){
		return name.substring(name.indexOf(":") + 1, name.length());
	}

	private static Document createNewDocument(){
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			return docBuilder.newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String toString(Node node) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			//TODO: Pq parou de funcionar?
			//transformerFactory.setAttribute("indent-number", 4);
			Transformer trans = transformerFactory.newTransformer();
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			DOMSource source = new DOMSource(node);
			StringWriter os = new StringWriter();
			StreamResult streamResult = new StreamResult(os);
			trans.transform(source, streamResult);
			String result = os.getBuffer().toString();
			return result;
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;		
	}
	
	public static Element createComplexType(Element root, String name) {
		String qName = XMLUtil.createQName(root.getPrefix(), XSDUtil.COMPLEX_TYPE);
		Element complexType = root.getOwnerDocument().createElement(qName);
		Attr nameAttr = complexType.getOwnerDocument().createAttribute("name");
		nameAttr.setValue(name);
		complexType.setAttributeNode(nameAttr);
		return complexType;
	}

}
