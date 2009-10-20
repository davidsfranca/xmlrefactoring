package xmlrefactoring.plugin.logic.util;

import org.w3c.dom.Element;

public class SchemaElementVerifier {
	
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
		Element schemaElement = (Element) element.getOwnerDocument().
			getElementsByTagNameNS(element.getNamespaceURI(), "schema").item(0);		
		return schemaElement.getAttribute("targetNamespace");
	}

}
