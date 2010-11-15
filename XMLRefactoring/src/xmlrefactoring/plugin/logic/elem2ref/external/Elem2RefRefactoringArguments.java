package xmlrefactoring.plugin.logic.elem2ref.external;

import java.util.List;

import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Element;

import xmlrefactoring.plugin.logic.BaseRefactoringArguments;
import xmlrefactoring.plugin.logic.util.XSDUtil;

public class Elem2RefRefactoringArguments extends BaseRefactoringArguments {
	private boolean element;
	private boolean reference;
	private String refName;
	
	public Elem2RefRefactoringArguments(List<XSDNamedComponent> components) {
		super(components);
		Element transformingElement = getElements().get(0);
		
		this.element = XSDUtil.isElement(transformingElement);
		this.reference = XSDUtil.isReference(transformingElement);
		
		this.refName = transformingElement.getAttribute("name");
	}
	
	public boolean isElement() {
		return this.element;
	}
	
	public boolean isReference() {
		return this.reference;
	}
	
	public void setRefName(String elemName) {
		if(!elemName.equals("")) this.refName = elemName;
	}
	
	public String getRefName() {
		return this.refName;
	}
}
