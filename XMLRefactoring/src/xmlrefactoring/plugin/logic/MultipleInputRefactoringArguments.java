package xmlrefactoring.plugin.logic;

import java.util.List;

import org.eclipse.xsd.XSDNamedComponent;


public class MultipleInputRefactoringArguments extends BaseRefactoringArguments {

	/**
	 * The component that are being refactored
	 */
	private List<XSDNamedComponent> components;
	
	public List<XSDNamedComponent> getComponents() {
		return components;
	}

	public void setComponents(List<XSDNamedComponent> component) {
		this.components = component;
	}
	
}
