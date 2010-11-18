package xmlrefactoring.plugin.logic.moveElement.external;

import java.util.List;

import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Element;

import xmlrefactoring.plugin.logic.BaseRefactoringArguments;

public class MoveElementRefactoringArguments extends BaseRefactoringArguments {
	private Element receivingElement;
	
	public MoveElementRefactoringArguments(List<XSDNamedComponent> components) {
		super(components);
		Element movingElement = getElements().get(0);	
	}
	
	public void setReceivingElement(Element receivingElement)
	{
		this.receivingElement = receivingElement;
	}
	
	public Element getReceivingElement()
	{
		return receivingElement;
	}

}
