package xmlrefactoring.plugin.logic.rename;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.PluginNamingConstants;
import xmlrefactoring.plugin.logic.BaseProcessor;
import xmlrefactoring.plugin.logic.BaseRefactoringArguments;
import xmlrefactoring.plugin.logic.rename.external.RenameRefactoringArguments;

public class RenameProcessor extends BaseProcessor{

	private RenameRefactoringArguments arguments;
	
	public RenameProcessor(XSDNamedComponent component){
		List<XSDNamedComponent> list = new ArrayList<XSDNamedComponent>();
		list.add(component);
		arguments = new RenameRefactoringArguments(list);
	}
	
	@Override
	protected XSDNamedComponent getElement() {
		return arguments.getComponents().get(0);
	}

	@Override
	protected String getParticipantExtensionPoint() {
		return XMLRefactoringMessages.getString("RenameParticipant.ExtensionPointID");
	}

	@Override
	protected BaseRefactoringArguments getRefactoringArguments() {
		return arguments;
	}

	@Override
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws CoreException,
			OperationCanceledException {
		RefactoringStatus status = super.checkFinalConditions(pm, context);
		return status;
	}

	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {		
		RefactoringStatus status = super.checkInitialConditions(pm);
		if(getElement().getName() == null)
			status.addFatalError(XMLRefactoringMessages.getString("RenameProcessor.ReferenceRenameAttempt"));
		return status;
	}

	@Override
	public String getIdentifier() {
		return XMLRefactoringMessages.getString("RenameProcessor.Identifier");
	}

	@Override
	public String getProcessorName() {
		return XMLRefactoringMessages.getString("RenameProcessor.Name");
	}

	@Override
	public boolean isApplicable() throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setNewElementName(String newName) {
		arguments.setNewName(newName);
	}

}
