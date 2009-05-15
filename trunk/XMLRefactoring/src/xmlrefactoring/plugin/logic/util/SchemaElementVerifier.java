package xmlrefactoring.plugin.logic.util;

import org.w3c.dom.Element;

public class SchemaElementVerifier {
	
	private static final  String SCHEMA = "schema";
	private static final String ELEMENT = "element";
	private static final String ATTRIBUTE = "attribute";
	
	public static boolean isElement(Element element) {
		return ELEMENT.equals(element.getLocalName());
	}

	public static boolean isElementOrAttribute(Element element){
		return ELEMENT.equals(element.getLocalName()) | ATTRIBUTE.equals(element.getLocalName());
	}
	
	public static boolean isGlobal(Element element) {
		return SCHEMA.equals(element.getParentNode().getNodeName());
	}

}
