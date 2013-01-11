package xmlrefactoring.plugin.logic.addElement.external;

import java.util.List;

import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.plugin.logic.BaseRefactoringArguments;

public class AddElementRefactoringArguments extends BaseRefactoringArguments {

	public AddElementRefactoringArguments(List<XSDNamedComponent> components) {
		super(components);
	}

	private String newElementName;
	private String newElementType;
	private String newElementValue;
	private boolean optional;

	public String getNewElementName() {
		return newElementName;
	}

	public void setNewElementName(String name) {
		this.newElementName = name;
	}

	public String getNewElementType() {
		return newElementType;
	}

	public void setNewElementType(String newElementType) {
		this.newElementType = newElementType;
	}

	public String getNewElementValue() {
		return newElementValue;
	}

	public void setNewElementValue(String newElementValue) {
		this.newElementValue = newElementValue;
	}

	public boolean isOptional() {
		return optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

}
