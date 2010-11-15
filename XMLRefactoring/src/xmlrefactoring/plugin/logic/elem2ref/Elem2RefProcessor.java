package xmlrefactoring.plugin.logic.elem2ref;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.logic.BaseProcessor;
import xmlrefactoring.plugin.logic.BaseRefactoringArguments;
import xmlrefactoring.plugin.logic.elem2ref.external.Elem2RefRefactoringArguments;

public class Elem2RefProcessor extends BaseProcessor {
	
	private Elem2RefRefactoringArguments arguments;
	
	public Elem2RefProcessor(XSDNamedComponent component) {
		List<XSDNamedComponent> components = new ArrayList<XSDNamedComponent>();
		components.add(component);
		arguments = new Elem2RefRefactoringArguments(components);
	}
	
	@Override
	protected BaseRefactoringArguments getRefactoringArguments() {
		return arguments;
	}

	@Override
	protected String getParticipantExtensionPoint() {
		return XMLRefactoringMessages.getString("Elem2RefParticipant.ExtensionPointID");
	}

	@Override
	protected Object getElement() {
		return arguments.getComponents().get(0);
	}

	@Override
	public String getIdentifier() {
		return XMLRefactoringMessages.getString("Elem2RefProcessor.Identifier");
	}

	@Override
	public String getProcessorName() {
		return XMLRefactoringMessages.getString("Elem2RefProcessor.Name");
	}

	@Override
	public boolean isApplicable() throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setRefName(String name) {		
		arguments.setRefName(name);	
	}
}
