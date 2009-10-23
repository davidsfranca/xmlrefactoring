package xmlrefactoring.plugin.logic.rename;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.plugin.PluginNamingConstants;
import xmlrefactoring.plugin.logic.BaseProcessor;
import xmlrefactoring.plugin.logic.BaseRefactoringArguments;

public class RenameProcessor extends BaseProcessor{

	private RenameRefactoringArguments arguments;
	
	public RenameProcessor(XSDNamedComponent component){
		List<XSDNamedComponent> list = new ArrayList<XSDNamedComponent>();
		list.add(component);
		arguments = new RenameRefactoringArguments(list);
	}
	
	@Override
	protected Object getElement() {
		return arguments.getComponents().get(0);
	}

	@Override
	protected String getParticipantExtensionPoint() {
		return PluginNamingConstants.RENAME_PARTICIPANT_EXTENSION_POINT_ID;
	}

	@Override
	protected BaseRefactoringArguments getRefactoringArguments() {
		return arguments;
	}

	@Override
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws CoreException,
			OperationCanceledException {
		return new RefactoringStatus();
	}

	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		return new RefactoringStatus();
	}

	@Override
	public String getIdentifier() {
		return PluginNamingConstants.RENAME_PROCESSOR_IDENTIFIER;
	}

	@Override
	public String getProcessorName() {
		return PluginNamingConstants.RENAME_PROCESSOR_NAME;
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
