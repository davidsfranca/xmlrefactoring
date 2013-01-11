package xmlrefactoring.plugin.logic.addElement;

import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.w3c.dom.Element;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.logic.BaseXSLParticipant;
import xmlrefactoring.plugin.logic.addElement.external.AddElementRefactoringArguments;
import xmlrefactoring.plugin.logic.util.XPathCreator;
import xmlrefactoring.plugin.refactoring.AddElementRefactoring;
import xmlrefactoring.plugin.refactoring.XMLRefactoring;

public class XSLTAddElementParticipant extends BaseXSLParticipant {

	private AddElementRefactoringArguments arguments;
	private Element element;

	@Override
	protected XMLRefactoring getXMLRefactoring() throws CoreException {
		List<List<QName>> paths;
		paths = XPathCreator.createElementPaths(element);
		return new AddElementRefactoring(paths, arguments.getNewElementName(), arguments.getNewElementType(), arguments.getNewElementValue(), arguments.isOptional());
	}

	@Override
	protected boolean initialize(Object element) {
		return true;
	}

	@Override
	public void initialize(RefactoringArguments arguments) {
		super.initialize(arguments);
		this.arguments = (AddElementRefactoringArguments) arguments;
		element = this.arguments.getElements().get(0);
	}

	@Override
	public String getName() {
		return XMLRefactoringMessages.getString("XSLTAddElementParticipant.Name");
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		return new RefactoringStatus();
	}

}
