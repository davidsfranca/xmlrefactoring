package xmlrefactoring.plugin.logic.removeElement.external;

import java.util.List;

import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.plugin.logic.BaseRefactoringArguments;

public class RemoveElementRefactoringArguments extends BaseRefactoringArguments {

	private String elementName;

	public RemoveElementRefactoringArguments(List<XSDNamedComponent> components) {
		super(components);
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

}
