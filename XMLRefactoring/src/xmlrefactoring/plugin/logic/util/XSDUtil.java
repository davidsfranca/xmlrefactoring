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

	public static boolean isSimpleContent(Element derivedContent) {
		return derivedContent.getLocalName().equals(SIMPLE_CONTENT);
	}
	
	public static String searchTargetNamespacePrefix(XSDSchema schema) {
		Map<String, String> map = schema.getQNamePrefixToNamespaceMap();
		String targetNamespace = schema.getTargetNamespace();
		Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, String> entry = iterator.next();
			if(entry.getValue().equals(targetNamespace))
				return entry.getKey();
		}
		return null;
	}

}
