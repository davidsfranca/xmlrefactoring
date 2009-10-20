package xmlrefactoring.plugin.logic.attr2elem;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.plugin.PluginNamingConstants;
import xmlrefactoring.plugin.logic.BaseParticipant;
import xmlrefactoring.plugin.logic.BaseProcessor;

public class Attr2ElemProcessor extends BaseProcessor{	

	private Attr2ElemRefactoringArguments arguments;
	
	public Attr2ElemProcessor(XSDNamedComponent component){		
		List<XSDNamedComponent> components = new ArrayList<XSDNamedComponent>();
		arguments = new Attr2ElemRefactoringArguments(components);
		components.add(component);
	}
	
	@Override
	protected Attr2ElemRefactoringArguments getRefactoringArguments() {
		return arguments;
	}

	@Override
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws CoreException,
			OperationCanceledException {
		// TODO Auto-generated method stub
		return new RefactoringStatus();
	}

	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		// TODO Auto-generated method stub
		return new RefactoringStatus();
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getElement() {
		return arguments.getComponents().get(0);
	}

	@Override
	public String getIdentifier() {
		return PluginNamingConstants.ATTR_2_ELEM_PROCESSOR_IDENTIFIER;
	}

	@Override
	public String getProcessorName() {
		return PluginNamingConstants.ATTR_2_ELEM_PROCESSOR_NAME;
	}

	@Override
	public boolean isApplicable() throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String getParticipantExtensionPoint() {
		return PluginNamingConstants.ATTR_2_ELEM_PARTICIPANT_EXTENSION_POINT_ID;
	}

}
