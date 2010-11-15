package xmlrefactoring.plugin.logic.ref2elem.external;

import java.util.List;

import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Element;

import xmlrefactoring.plugin.logic.BaseRefactoringArguments;
import xmlrefactoring.plugin.logic.util.XSDUtil;

public class Ref2ElemRefactoringArguments extends BaseRefactoringArguments {
	private boolean reference;
	private boolean element;
	private String elemName;
	
	public Ref2ElemRefactoringArguments(List<XSDNamedComponent> components) {
		super(components);
		Element transformingElement = getElements().get(0);
		
		this.reference = XSDUtil.isReference(transformingElement);
		this.element = XSDUtil.isElement(transformingElement);
		
		this.elemName = transformingElement.getAttribute("ref") + "Elem";
	}

	public boolean isReference()
	{
		return this.reference;
	}
	
	public boolean isElement()
	{
		return this.element;
	}

	public void setElementName(String name) {
		if(!name.equals("")) this.elemName = name;		
	}
	
	public String getElementName()
	{
		return this.elemName;
	}
}
