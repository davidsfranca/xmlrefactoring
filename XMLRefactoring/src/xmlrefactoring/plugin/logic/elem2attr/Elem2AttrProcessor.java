package xmlrefactoring.plugin.logic.elem2attr;

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
import xmlrefactoring.plugin.logic.elem2attr.external.Elem2AttrRefactoringArguments;

public class Elem2AttrProcessor extends BaseProcessor {
	
	private Elem2AttrRefactoringArguments arguments;
	
	public Elem2AttrProcessor(XSDNamedComponent component) {
		List<XSDNamedComponent> list = new ArrayList<XSDNamedComponent>();
		list.add(component);
		arguments = new Elem2AttrRefactoringArguments(list);
	}
	
	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		// TODO Auto-generated method stub
		return super.checkInitialConditions(pm);
	}
	
	@Override
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws CoreException,
			OperationCanceledException {
		RefactoringStatus status = super.checkFinalConditions(pm, context);
		return status;
	}

	@Override
	protected BaseRefactoringArguments getRefactoringArguments() {
		return arguments;
	}

	@Override
	protected String getParticipantExtensionPoint() {
		return XMLRefactoringMessages.getString("Elem2AttrParticipant.ExtensionPointID");
	}

	@Override
	protected Object getElement() {
		return arguments.getComponents().get(0);
	}

	@Override
	public String getIdentifier() {
		return XMLRefactoringMessages.getString("Elem2AttrProcessor.Identifier");
	}

	@Override
	public String getProcessorName() {
		return XMLRefactoringMessages.getString("Elem2AttrProcessor.Name");
	}

	@Override
	public boolean isApplicable() throws CoreException {
		return false;
	}
}
