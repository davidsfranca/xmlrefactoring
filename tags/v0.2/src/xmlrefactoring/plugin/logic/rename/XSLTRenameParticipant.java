package xmlrefactoring.plugin.logic.rename;

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
import xmlrefactoring.plugin.logic.util.XMLUtil;
import xmlrefactoring.plugin.logic.util.XPathCreator;
import xmlrefactoring.plugin.logic.util.XSDUtil;
import xmlrefactoring.plugin.refactoring.RenameAttributeRefactoring;
import xmlrefactoring.plugin.refactoring.RenameElementRefactoring;
import xmlrefactoring.plugin.refactoring.XMLRefactoring;

//Este participant é uma exceção na arquitetura, pois se integra à estrutura do editor,
//sem extender as classes base da aplicação
/**
 * Class responsible for creating the XSLT files related to the rename refactoring
 */
public class XSLTRenameParticipant extends BaseXSLParticipant{

	private RenameRefactoringArguments arguments;
	private Element element;
	
	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		return new RefactoringStatus();
	}

	@Override
	public String getName() {
		return XMLRefactoringMessages.getString("XSLTRenameParticipant.Name");
	}

	@Override
	protected XMLRefactoring getXMLRefactoring() throws CoreException {
		
		XMLRefactoring refactoring = null;
		List<List<QName>> paths;
		
		if(XSDUtil.isElement(element)){
			paths = XPathCreator.createElementPaths(element);
			refactoring = new RenameElementRefactoring(paths, arguments.getNewName());
		}
		else{
			paths = XPathCreator.createAttributePaths(element);	
			QName attr = XMLUtil.createQName(element);
			refactoring = new RenameAttributeRefactoring(paths, arguments.getNewName(), attr);
		}
		return refactoring;
	}

	@Override
	protected boolean initialize(Object element) {
		return true;
	}
	
	@Override
	public void initialize(RefactoringArguments arguments) {
		super.initialize(arguments);
		this.arguments = (RenameRefactoringArguments) arguments;
		element = this.arguments.getElements().get(0);
	}

}
