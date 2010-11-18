package xmlrefactoring.plugin.logic.attr2elem;

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
import xmlrefactoring.plugin.logic.attr2elem.external.Attr2ElemRefactoringArguments;
import xmlrefactoring.plugin.logic.util.XMLUtil;
import xmlrefactoring.plugin.logic.util.XPathCreator;
import xmlrefactoring.plugin.logic.util.XSDUtil;
import xmlrefactoring.plugin.refactoring.Attr2ElemRefactoring;
import xmlrefactoring.plugin.refactoring.XMLRefactoring;

public class XSLTAttr2ElemParticipant extends BaseXSLParticipant{

	private Attr2ElemRefactoringArguments arguments;
	private Element element;
	
	@Override
	protected XMLRefactoring getXMLRefactoring() throws CoreException {
		
		List<List<QName>> paths = null;
		if(!XSDUtil.isElement(element)){
			paths = XPathCreator.createAttributePaths(element);
		}
		QName elementQName = XMLUtil.createQName(element);
		XMLRefactoring refactoring = new Attr2ElemRefactoring(paths, elementQName);
		return refactoring;
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		
		return new RefactoringStatus();
	}

	@Override
	public String getName() {
		return XMLRefactoringMessages.getString("XSLTAttr2ElemParticipant.Name");
	}

	@Override
	protected boolean initialize(Object arg0) {
		return true;
	}
	
	@Override
	public void initialize(RefactoringArguments arguments) {
		super.initialize(arguments);
		this.arguments = (Attr2ElemRefactoringArguments) arguments;
		element = this.arguments.getElements().get(0);
		
	}

}

