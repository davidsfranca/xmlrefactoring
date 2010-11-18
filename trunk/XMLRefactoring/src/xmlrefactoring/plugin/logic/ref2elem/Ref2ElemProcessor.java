package xmlrefactoring.plugin.logic.ref2elem;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.logic.BaseProcessor;
import xmlrefactoring.plugin.logic.BaseRefactoringArguments;
import xmlrefactoring.plugin.logic.ref2elem.external.Ref2ElemRefactoringArguments;

public class Ref2ElemProcessor extends BaseProcessor {
	Ref2ElemRefactoringArguments arguments;
	
	public Ref2ElemProcessor(XSDNamedComponent component) {
		List<XSDNamedComponent> list = new ArrayList<XSDNamedComponent>();
		list.add(component);
		arguments = new Ref2ElemRefactoringArguments(list);
	}

	@Override
	protected BaseRefactoringArguments getRefactoringArguments() {
		return arguments;
	}

	@Override
	protected String getParticipantExtensionPoint() {
		return XMLRefactoringMessages.getString("Ref2ElemParticipant.ExtensionPointID");
	}

	@Override
	protected Object getElement() {
		return arguments.getComponents().get(0);
	}

	@Override
	public String getIdentifier() {
		return XMLRefactoringMessages.getString("Ref2ElemProcessor.Identifier");
	}

	@Override
	public String getProcessorName() {
		return XMLRefactoringMessages.getString("Ref2ElemProcessor.Name");
	}

	@Override
	public boolean isApplicable() throws CoreException {
		return false;
	}

}
