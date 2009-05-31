package xmlrefactoring.plugin.logic.attr2elem;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.plugin.logic.BaseRefactoringArguments;

public class Attr2ElemRefactoringArguments extends BaseRefactoringArguments{

	/**
	 * The component that is being refactored
	 */
	private XSDNamedComponent component;
	
	private List<ReferenceWithCompositor> referencesWithCompositor = new ArrayList<ReferenceWithCompositor>();
	
	public List<ReferenceWithCompositor> getReferencesWithCompositor() {
		return referencesWithCompositor;
	}

	public XSDNamedComponent getComponent() {
		return component;
	}

	public void setComponent(XSDNamedComponent component) {
		this.component = component;
	}
	
	
	
}
