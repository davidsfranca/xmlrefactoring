package xmlrefactoring.plugin.logic.attr2elem;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.plugin.logic.BaseRefactoringArguments;

public class Attr2ElemRefactoringArguments extends BaseRefactoringArguments{
	
	public Attr2ElemRefactoringArguments(List<XSDNamedComponent> components) {
		super(components);
	}

	private List<ReferenceWithCompositor> referencesWithCompositor = new ArrayList<ReferenceWithCompositor>();
	
	public List<ReferenceWithCompositor> getReferencesWithCompositor() {
		return referencesWithCompositor;
	}
	
}
