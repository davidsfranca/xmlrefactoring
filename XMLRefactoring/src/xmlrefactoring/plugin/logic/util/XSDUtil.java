package xmlrefactoring.plugin.logic.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.saxon.instruct.UseAttributeSets;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.xsd.XSDSchema;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class XSDUtil {

	public static final String SCHEMA = "schema";
	public static final String ELEMENT = "element";
	public static final String ATTRIBUTE = "attribute";
	public static final String COMPLEX_TYPE = "complexType";
	public static final String SEQUENCE = "sequence";
	public static final String ALL = "all";
	public static final String CHOICE = "choice";
	public static final String SIMPLE_TYPE = "simpleType";
	public static final String NAME = "name";
	public static final String SIMPLE_CONTENT = "simpleContent";
	public static final String COMPLEX_CONTENT = "complexContent";
	public static final String TYPE = "type";
	public static final String EXTENSION = "extension";
	public static final String BASE = "base";
	public static final String SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
	public static final String XMLNS = "xmlns";
	public static final String ELEMENT_FORM_DEFAULT = "elementFormDefault";
	public static final String QUALIFIED = "qualified";

	public static boolean isElement(Element element) {
		return ELEMENT.equals(element.getLocalName());
	}

	public static boolean isElementOrAttribute(Element element){
		return ELEMENT.equals(element.getLocalName()) | ATTRIBUTE.equals(element.getLocalName());
	}

	public static boolean isAttribute(Element element){
		return ATTRIBUTE.equals(element.getLocalName());
	}

	public static boolean isComplexType(Element element){		
		boolean result = COMPLEX_TYPE.equals(element.getLocalName());
		
		if(element.getFirstChild() != null)
			if(element.getFirstChild().getNextSibling() != null)
				result = result || COMPLEX_TYPE.equals(element.getFirstChild().getNextSibling().getLocalName());
		
		return result;
	}
	
	public static boolean isReference(Element element) {
		return element.hasAttribute("ref");
	}

	public static boolean isGlobal(Element element) {
		return SCHEMA.equals(element.getParentNode().getLocalName());
	}

	public static boolean isAnonymous(Element element) {
		return element.getAttribute("name") == null;
	}

	public static String getName(Element element){
		return element.getAttribute("name");
	}

	public static String getRef(Element element){
		return element.getAttribute("ref");
	}

	public static String getType(Element element){
		return element.getAttribute("type");
	}

	public static String getTargetNamespace(Element element) {
		Element schemaElement = getSchemaElement(element);
		return schemaElement.getAttribute("targetNamespace");
	}
	
	private static Element getSchemaElement(Element element){
		return (Element) element.getOwnerDocument().
		getElementsByTagNameNS(element.getNamespaceURI(), "schema").item(0);
	}

	public static boolean isSimpleContent(Element derivedContent) {
		return derivedContent.getLocalName().equals(SIMPLE_CONTENT);
	}

	/**
	 * Returns the prefix setted for the targetNamespace. If no namespace was setted, returns null.
	 * If the it`s the default namespace returns an empty string ("")
	 * @param schema
	 * @return
	 */
	public static String searchTargetNamespacePrefix(XSDSchema schema) {
		Map<String, String> map = schema.getQNamePrefixToNamespaceMap();
		String targetNamespace = schema.getTargetNamespace();
		if(targetNamespace == null)
			return null;
		Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, String> entry = iterator.next();
			if(entry.getValue().equals(targetNamespace)){
				if(entry.getKey() == null)
					return "";
				else
					return entry.getKey();
			}
		}
		return null;
	}

	public static boolean isQualified(Element element) {
		if(isElement(element)){
			if(isGlobal(element))
				return true;
			else{
				Element schemaElement = getSchemaElement(element);
				if(QUALIFIED.equals(schemaElement.getAttribute(ELEMENT_FORM_DEFAULT)))
					return true;
			}
		}
		return false;
	}

	public static Element createComplexType(Element root, String name) {
		String qName = XMLUtil.createQName(root.getPrefix(), COMPLEX_TYPE);
		Element complexType = root.getOwnerDocument().createElement(qName);
		Attr nameAttr = complexType.getOwnerDocument().createAttribute("name");
		nameAttr.setValue(name);
		complexType.setAttributeNode(nameAttr);
		return complexType;
	}

	public static Element createSimpleType(Element root, String name, String type) {
		String qName = XMLUtil.createQName(root.getPrefix(), SIMPLE_TYPE);
		Element simpleType = root.getOwnerDocument().createElement(qName);
		
		Attr nameAttr = simpleType.getOwnerDocument().createAttribute("name");
		nameAttr.setValue(name);
		
		Attr typeAttr = simpleType.getOwnerDocument().createAttribute("type");
		typeAttr.setValue(type);
		
		simpleType.setAttributeNode(nameAttr);
		simpleType.setAttributeNode(typeAttr);
		
		return simpleType;
	}
	
	public static Element createAttribute(Element root, String name, String type, String occurence) {
		String qName = XMLUtil.createQName(root.getPrefix(), ATTRIBUTE);
		Element attrType = root.getOwnerDocument().createElement(qName);
		
		Attr nameAttr = attrType.getOwnerDocument().createAttribute("name");
		nameAttr.setValue(name);
		
		Attr typeAttr = attrType.getOwnerDocument().createAttribute("type");
		typeAttr.setValue(type);
		
		attrType.setAttributeNode(nameAttr);
		attrType.setAttributeNode(typeAttr);
		
		if(!occurence.equals(""))
		{
			Attr useAttr = attrType.getOwnerDocument().createAttribute("use");
			useAttr.setValue(occurence);
			attrType.setAttributeNode(useAttr);
		}
		
		return attrType;
	}

	public static Element createRefAttribute(IDOMElement root, String ref) {
		String qName = XMLUtil.createQName(root.getPrefix(), ATTRIBUTE);
		Element refAttrType = root.getOwnerDocument().createElement(qName);
		
		Attr refAttr = refAttrType.getOwnerDocument().createAttribute("ref");
		refAttr.setValue(ref);
		
		refAttrType.setAttributeNode(refAttr);
		
		return refAttrType;
	}
	
	public static Element createRefElement(IDOMElement root, Element transformingElement, XSDSchema schema) {
		String qName = XMLUtil.createQName(root.getPrefix(), ELEMENT);
		Element e = root.getOwnerDocument().createElement(qName);
		
		String targetNamespace = searchTargetNamespacePrefix(schema);
		
		Attr refAttr = e.getOwnerDocument().createAttribute("ref");
		String qRef = XMLUtil.createQName(targetNamespace, transformingElement.getAttribute("name"));
		refAttr.setValue(qRef);
		
		e.setAttributeNode(refAttr);
		
		return e;
	}

	public static Element createElementBasedUpon(IDOMElement root, Element element, String name) {
		String qName = XMLUtil.createQName(root.getPrefix(), ELEMENT);
		Element e = root.getOwnerDocument().createElement(qName);
		
		String [] nameMap = name.split(":");
		
		NamedNodeMap nodeMap = element.getAttributes();
		
		int i;
		for(i = 0; i < nodeMap.getLength(); i++)
		{
			Attr attr = e.getOwnerDocument().createAttribute(nodeMap.item(i).toString());
			if(!nodeMap.item(i).toString().equals("name"))
				attr.setValue(element.getAttribute(nodeMap.item(i).toString()));
			else
				attr.setValue(nameMap[nameMap.length - 1]);
			
			e.setAttributeNode(attr);
		}
		
		NodeList nodeList = element.getChildNodes();
		
		for(i = 0; i < nodeList.getLength(); i++)
			e.appendChild(nodeList.item(i).cloneNode(true));
		
		return e;
	}

	public static Element copyElement(IDOMElement root, Element transformingElement) {
		String qName = XMLUtil.createQName(root.getPrefix(), ELEMENT);
		Element e = root.getOwnerDocument().createElement(qName);
		
		NamedNodeMap nodeMap = transformingElement.getAttributes();
		
		int i;
		for(i = 0; i < nodeMap.getLength(); i++)
		{
			Attr attr = e.getOwnerDocument().createAttribute(nodeMap.item(i).toString());
				attr.setValue(transformingElement.getAttribute(nodeMap.item(i).toString()));
			
			e.setAttributeNode(attr);
		}
		
		NodeList nodeList = transformingElement.getChildNodes();
		
		for(i = 0; i < nodeList.getLength(); i++)
			e.appendChild(nodeList.item(i).cloneNode(true));
		
		return e;
	}
}
