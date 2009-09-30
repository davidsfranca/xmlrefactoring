package xmlrefactoring.plugin.logic;

import org.eclipse.xsd.XSDNamedComponent;

public class SingleInputRefactoringArguments extends BaseRefactoringArguments{
	
	/**
	 * The component that is being refactored
	 */
	private XSDNamedComponent component;
	
	public XSDNamedComponent getComponent() {
		return component;
	}

	public void setComponent(XSDNamedComponent component) {
		this.component = component;
	}

}
