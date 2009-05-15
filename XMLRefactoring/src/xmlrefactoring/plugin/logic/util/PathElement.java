package xmlrefactoring.plugin.logic.util;

import org.w3c.dom.Element;

public class PathElement {
	
	/**
	 * If the element is global, the qualifier is it`s namespace, otherwise it`s the name of
	 * the complex type which it belongs to.
	 */
	private String qualifier;
	private String localName;
	private boolean global;
	
	public PathElement(Element element){
		if(isGlobal(element)){
			qualifier = getTargetNamespace(element);
			localName = element.getAttribute("name");
			global = true;
		}else{
			
			
			
			global = false;
		}
	}
	
	@Override
	public String toString(){
		if(global){
			return qualifier + localName;
		}
		else
			return localName;
	}
	
	private boolean isGlobal(Element element) {
		return element.getParentNode().getNodeName().equals("schema");
	}
	
	private String getTargetNamespace(Element element) {
		Element schemaElement = (Element) element.getOwnerDocument().getElementsByTagName("schema").item(0);		
		return schemaElement.getAttribute("targetNamespace");
	}

}
