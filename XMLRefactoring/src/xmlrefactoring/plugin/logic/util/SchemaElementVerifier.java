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

}
