package xmlrefactoring.plugin.logic.attr2elem;

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
		arguments = new Attr2ElemRefactoringArguments();
		arguments.setComponent(component);
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
		return arguments.getComponent();
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
	protected Class getParticipantType() {
		return BaseParticipant.class;
	}

}
