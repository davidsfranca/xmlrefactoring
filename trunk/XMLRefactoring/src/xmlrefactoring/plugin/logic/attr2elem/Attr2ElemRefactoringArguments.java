package xmlrefactoring.plugin.logic.attr2elem;

import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.plugin.logic.BaseRefactoringArguments;

public class Attr2ElemRefactoringArguments extends BaseRefactoringArguments{

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
