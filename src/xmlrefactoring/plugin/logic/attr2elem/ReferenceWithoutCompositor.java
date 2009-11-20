package xmlrefactoring.plugin.logic.attr2elem;

import org.w3c.dom.Element;

public class ReferenceWithoutCompositor {

	private Element complexType;
	
	private String newCompositor;

	public Element getComplexType() {
		return complexType;
	}

	public void setComplexType(Element complexType) {
		this.complexType = complexType;
	}

	public String getNewCompositor() {
		return newCompositor;
	}

	public void setNewCompositor(String newCompositor) {
		this.newCompositor = newCompositor;
	}
	
	
}
