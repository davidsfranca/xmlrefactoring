package xmlrefactoring.plugin.logic.removeElement;

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
import xmlrefactoring.plugin.logic.removeElement.external.RemoveElementRefactoringArguments;

public class RemoveElementProcessor extends BaseProcessor {

	private RemoveElementRefactoringArguments arguments;

	public RemoveElementProcessor(XSDNamedComponent component) {
		List<XSDNamedComponent> list = new ArrayList<XSDNamedComponent>();
		list.add(component);
		arguments = new RemoveElementRefactoringArguments(list);
	}

	@Override
	protected BaseRefactoringArguments getRefactoringArguments() {
		return arguments;
	}

	@Override
	protected String getParticipantExtensionPoint() {
		return XMLRefactoringMessages.getString("RemoveElementParticipant.ExtensionPointID");
	}

	@Override
	protected Object getElement() {
		return arguments.getComponents().get(0);
	}

	@Override
	public String getIdentifier() {
		return XMLRefactoringMessages.getString("RemoveElementProcessor.Identifier");
	}

	@Override
	public String getProcessorName() {
		return XMLRefactoringMessages.getString("RemoveElementProcessor.Name");
	}

	@Override
	public boolean isApplicable() throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {		
		RefactoringStatus status = super.checkInitialConditions(pm);
		if(getElement() == null)
			status.addFatalError(XMLRefactoringMessages.getString("RemoveElementProcessor.ReferenceRemoveAttempt"));
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
