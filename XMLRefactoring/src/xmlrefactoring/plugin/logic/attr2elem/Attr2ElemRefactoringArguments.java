package xmlrefactoring.plugin.logic.attr2elem;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.plugin.logic.SingleInputRefactoringArguments;

public class Attr2ElemRefactoringArguments extends SingleInputRefactoringArguments{


	
	private List<ReferenceWithCompositor> referencesWithCompositor = new ArrayList<ReferenceWithCompositor>();
	
	public List<ReferenceWithCompositor> getReferencesWithCompositor() {
		return referencesWithCompositor;
	}


	
	
	
}
