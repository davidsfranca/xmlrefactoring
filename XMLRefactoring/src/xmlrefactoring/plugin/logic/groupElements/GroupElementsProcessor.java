package xmlrefactoring.plugin.logic.groupElements;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.PluginNamingConstants;
import xmlrefactoring.plugin.logic.BaseProcessor;

public class GroupElementsProcessor extends BaseProcessor {

	private GroupElementsRefactoringArguments arguments;
	
	/**
	 * Initialize the processor with the elements to be grouped
	 * @param components
	 */
	public GroupElementsProcessor(List<XSDNamedComponent> components){
		Collections.sort(components, new Comparator<XSDNamedComponent>(){

			public int compare(XSDNamedComponent arg0, XSDNamedComponent arg1) {
				IDOMElement element0 = (IDOMElement) arg0.getElement();
				IDOMElement element1 = (IDOMElement) arg1.getElement();
				return element0.getStartOffset() - element1.getStartOffset();
			}
			
		});
		arguments = new GroupElementsRefactoringArguments(components);
	}

	@Override
	protected Object getElement() {
		return arguments.getComponents();
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
		return XMLRefactoringMessages.getString("GroupElementsProcessor.Identifier");
	}

	@Override
	public String getProcessorName() {
		return XMLRefactoringMessages.getString("GroupElementsProcessor.Name");
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
		return XMLRefactoringMessages.getString("GroupElementsPartcipant.ExtensionPointID");
	}

}
