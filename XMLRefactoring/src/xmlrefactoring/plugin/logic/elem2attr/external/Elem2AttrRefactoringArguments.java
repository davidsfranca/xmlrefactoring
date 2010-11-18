package xmlrefactoring.plugin.logic.elem2attr.external;

import java.util.List;

import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Element;

import xmlrefactoring.plugin.logic.BaseRefactoringArguments;
import xmlrefactoring.plugin.logic.util.XSDUtil;

public class Elem2AttrRefactoringArguments extends BaseRefactoringArguments {
	private boolean element;
	private boolean attribute;
	
	public  Elem2AttrRefactoringArguments(List<XSDNamedComponent> components) {
		super(components);
		Element transformingElement = getElements().get(0);
		
		boolean element = XSDUtil.isElement(transformingElement);
		this.element = element;
		this.attribute = !element;
	}
	
	public boolean isElement(){
		return element;
	}

	public boolean isAttribute(){
		return attribute;
	}
}
