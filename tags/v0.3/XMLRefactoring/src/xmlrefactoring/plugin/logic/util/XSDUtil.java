package xmlrefactoring.plugin.logic.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.xsd.XSDSchema;
import org.w3c.dom.Element;

public class XSDUtil {

	public static final  String SCHEMA = "schema";
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
		return COMPLEX_TYPE.equals(element.getLocalName());
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

}
