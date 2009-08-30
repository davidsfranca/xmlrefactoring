package xmlrefactoring.plugin.logic;

import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.xsd.XSDNamedComponent;

public class BaseRefactoringArguments extends RefactoringArguments{
	
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
