package xmlrefactoring.plugin.logic.ungroupElements;

import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.w3c.dom.Element;

import xmlrefactoring.plugin.logic.BaseXSLParticipant;
import xmlrefactoring.plugin.logic.ungroupElements.external.UngroupElementsRefactoringArguments;
import xmlrefactoring.plugin.logic.util.XSDUtil;
import xmlrefactoring.plugin.refactoring.XMLRefactoring;

public class XSLTUngroupElementsParticipant extends BaseXSLParticipant {
	private UngroupElementsRefactoringArguments arguments;
	private Element element;
	
	@Override
	protected XMLRefactoring getXMLRefactoring() throws CoreException {
		XMLRefactoring refactoring = null;
		List<List<QName>> paths;
		
		if(XSDUtil.isElement(element)) {
			
		}
		
		return null;
	}

	@Override
	protected boolean initialize(Object element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		// TODO Auto-generated method stub
		return null;
	}

}
