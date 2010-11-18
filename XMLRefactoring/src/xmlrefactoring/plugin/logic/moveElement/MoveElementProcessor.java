package xmlrefactoring.plugin.logic.moveElement;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Element;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.logic.BaseProcessor;
import xmlrefactoring.plugin.logic.BaseRefactoringArguments;
import xmlrefactoring.plugin.logic.moveElement.external.MoveElementRefactoringArguments;

public class MoveElementProcessor extends BaseProcessor {
	private MoveElementRefactoringArguments arguments;
	
	public MoveElementProcessor(XSDNamedComponent component)
	{
		List<XSDNamedComponent> components = new ArrayList<XSDNamedComponent>();
		components.add(component);
		arguments = new MoveElementRefactoringArguments(components);
	}
	
	@Override
	protected BaseRefactoringArguments getRefactoringArguments() {
		return arguments;
	}

	@Override
	protected String getParticipantExtensionPoint() {
		return XMLRefactoringMessages.getString("MoveElementParticipant.ExtensionPointID");
	}

	@Override
	protected Object getElement() {
		return arguments.getComponents().get(0);
	}

	@Override
	public String getIdentifier() {
		return XMLRefactoringMessages.getString("MoveElementProcessor.Identifier");
	}

	@Override
	public String getProcessorName() {
		return XMLRefactoringMessages.getString("MoveElementProcessor.Name");
	}

	@Override
	public boolean isApplicable() throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setReceivingElement(XSDElementDeclaration ed) {
		arguments.setReceivingElement(ed.getElement());
	}
}
