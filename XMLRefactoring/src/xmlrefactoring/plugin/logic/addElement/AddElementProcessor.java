package xmlrefactoring.plugin.logic.addElement;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.logic.BaseProcessor;
import xmlrefactoring.plugin.logic.BaseRefactoringArguments;
import xmlrefactoring.plugin.logic.addElement.external.AddElementRefactoringArguments;

public class AddElementProcessor extends BaseProcessor {

	private AddElementRefactoringArguments arguments;

	public AddElementProcessor(XSDNamedComponent component) {
		List<XSDNamedComponent> list = new ArrayList<XSDNamedComponent>();
		list.add(component);
		arguments = new AddElementRefactoringArguments(list);
	}

	@Override
	protected BaseRefactoringArguments getRefactoringArguments() {
		return arguments;
	}

	@Override
	protected String getParticipantExtensionPoint() {
		return XMLRefactoringMessages.getString("AddElementParticipant.ExtensionPointID");
	}

	@Override
	protected XSDNamedComponent getElement() {
		return arguments.getComponents().get(0);
	}

	@Override
	public String getIdentifier() {
		return XMLRefactoringMessages.getString("AddElementProcessor.Identifier");
	}

	@Override
	public String getProcessorName() {
		return XMLRefactoringMessages.getString("AddElementProcessor.Name");
	}

	@Override
	public boolean isApplicable() throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setNewElementName(String name) {
		arguments.setNewElementName(name);
	}

	public void setNewElementType(String type) {
		arguments.setNewElementType(type);
	}

	public void setNewElementValue(String value) {
		arguments.setNewElementValue(value);
	}

	public void setNewElemOptional(boolean isOptional) {
		arguments.setOptional(isOptional);
	}

	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {		
		RefactoringStatus status = super.checkInitialConditions(pm);
		if(getElement() == null)
			status.addFatalError(XMLRefactoringMessages.getString("AddElementProcessor.ReferenceAddAttempt"));
		return status;
	}

	@Override
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws CoreException,
			OperationCanceledException {
		RefactoringStatus status = super.checkFinalConditions(pm, context);
		return status;
	}

}
