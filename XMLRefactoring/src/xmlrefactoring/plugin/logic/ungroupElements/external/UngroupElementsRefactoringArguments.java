package xmlrefactoring.plugin.logic.ungroupElements.external;

import java.util.List;

import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Element;

import xmlrefactoring.plugin.logic.BaseRefactoringArguments;
import xmlrefactoring.plugin.logic.util.XSDUtil;

public class UngroupElementsRefactoringArguments extends BaseRefactoringArguments {
	private boolean element;
	private boolean attribute;
	
	public UngroupElementsRefactoringArguments(
			List<XSDNamedComponent> components) {
		super(components);
		Element groupToUngroup = getElements().get(0);
		boolean element = XSDUtil.isElement(groupToUngroup);
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
