package xmlrefactoring.plugin.logic.ungroupElements;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.wst.common.core.search.SearchMatch;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.logic.BaseXSLParticipant;
import xmlrefactoring.plugin.logic.ungroupElements.external.UngroupElementsRefactoringArguments;
import xmlrefactoring.plugin.logic.util.SearchUtil;
import xmlrefactoring.plugin.logic.util.XMLUtil;
import xmlrefactoring.plugin.logic.util.XPathCreator;
import xmlrefactoring.plugin.logic.util.XSDUtil;
import xmlrefactoring.plugin.refactoring.UngroupElementsRefactoring;
import xmlrefactoring.plugin.refactoring.XMLRefactoring;

public class XSLTUngroupElementsParticipant extends BaseXSLParticipant {
	private UngroupElementsRefactoringArguments arguments;

	
	@Override
	protected XMLRefactoring getXMLRefactoring() throws CoreException {
		Element baseElement = arguments.getElements().get(0);
		XMLRefactoring refactoring = null;
		List<List<QName>> paths;
		
		if(!XSDUtil.isComplexElement(baseElement)) {
			paths = new ArrayList<List<QName>>();
			paths.add(new ArrayList<QName>());
		}
		else
			paths = XPathCreator.createElementPaths(baseElement);
		
		QName groupName = XMLUtil.createQName(baseElement);
		List<QName> inGroup = new ArrayList<QName>();
		inGroup.add(groupName);
		
		if(!paths.isEmpty())
			refactoring = new UngroupElementsRefactoring(paths, inGroup);
		
		return refactoring;
	}

	@Override
	protected boolean initialize(Object element) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void initialize(RefactoringArguments arguments)
	{
		super.initialize(arguments);
		this.arguments = (UngroupElementsRefactoringArguments) arguments;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return XMLRefactoringMessages.getString("XSLTUngroupElementsParticipant.Name");
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		// TODO Auto-generated method stub
		return new RefactoringStatus();
	}

}
