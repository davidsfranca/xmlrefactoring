package xmlrefactoring.plugin.logic.groupElements;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.plugin.PluginNamingConstants;
import xmlrefactoring.plugin.logic.BaseProcessor;
import xmlrefactoring.plugin.logic.SingleInputRefactoringArguments;

public class GroupElementsProcessor extends BaseProcessor {

	private GroupElementsRefactoringArguments arguments;
	
	/**
	 * Initialize the processor with the elements to be grouped
	 * @param components
	 */
	public GroupElementsProcessor(List<XSDNamedComponent> components){
		arguments = new GroupElementsRefactoringArguments();
		arguments.setComponents(components);
	}
	
	@Override
	protected Object getElement() {
		return arguments.getComponents();
	}

	@Override
	protected Class getParticipantType() {
		return GroupElementsParticipant.class;
	}

	@Override
	protected GroupElementsRefactoringArguments getRefactoringArguments() {
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
	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		return null;
	}

	@Override
	public String getIdentifier() {
		return PluginNamingConstants.GROUP_ELEMENTS_PROCESSOR_IDENTIFIER;
	}

	@Override
	public String getProcessorName() {
		return PluginNamingConstants.GROUP_ELEMENTS_PROCESSOR_NAME;
	}

	@Override
	public boolean isApplicable() throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setGroupName(String groupName){
		arguments.setGroupName(groupName);
	}

	@Override
	protected String getParticipantExtensionPoint() {
		return PluginNamingConstants.GROUP_ELEMENT_PARTICIPANT_EXTENSION_POINT_ID;
	}

}
