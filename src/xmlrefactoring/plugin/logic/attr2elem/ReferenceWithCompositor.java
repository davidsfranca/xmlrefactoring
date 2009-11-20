package xmlrefactoring.plugin.logic.attr2elem;

import org.w3c.dom.Element;

public class ReferenceWithCompositor {
	
	public ReferenceWithCompositor(Element complexType, Element targetPosition) {
		this.complexType = complexType;
		this.targetPosition = targetPosition;
	}

	private Element complexType;
	
	private Element targetPosition;

	public Element getTargetPosition() {
		return targetPosition;
	}

	public void setTargetPosition(Element targetPosition) {
		this.targetPosition = targetPosition;
	}

	public Element getComplexType() {
		return complexType;
	}

	public void setComplexType(Element complexType) {
		this.complexType = complexType;
	}

}
