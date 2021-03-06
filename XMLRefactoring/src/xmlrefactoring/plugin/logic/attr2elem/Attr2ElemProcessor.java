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

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.logic.BaseProcessor;
import xmlrefactoring.plugin.logic.attr2elem.external.Attr2ElemRefactoringArguments;

public class Attr2ElemProcessor extends BaseProcessor{	

	private Attr2ElemRefactoringArguments arguments;
	
	public Attr2ElemProcessor(XSDNamedComponent component){		
		List<XSDNamedComponent> components = new ArrayList<XSDNamedComponent>();
		components.add(component);
		arguments = new Attr2ElemRefactoringArguments(components);
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
		return super.checkInitialConditions(pm);
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
		return XMLRefactoringMessages.getString("Attr2ElemProcessor.Identifier");
	}

	@Override
	public String getProcessorName() {
		return XMLRefactoringMessages.getString("Attr2ElemProcessor.Name");
	}

	@Override
	public boolean isApplicable() throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String getParticipantExtensionPoint() {
		return XMLRefactoringMessages.getString("Attr2ElemParticipant.ExtensionPointID");
	}

}
