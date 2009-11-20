package xmlrefactoring.plugin.logic.groupElements;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Element;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.logic.BaseXSLParticipant;
import xmlrefactoring.plugin.logic.util.XSDUtil;
import xmlrefactoring.plugin.logic.util.XMLUtil;
import xmlrefactoring.plugin.logic.util.XPathCreator;
import xmlrefactoring.plugin.refactoring.GroupElementsRefactoring;
import xmlrefactoring.plugin.refactoring.XMLRefactoring;

public class XSLTGroupElementsParticipant extends BaseXSLParticipant {	
	
	private GroupElementsRefactoringArguments arguments;

	@Override
	protected XMLRefactoring getXMLRefactoring() throws CoreException {
		Element baseElement =  arguments.getComponents().get(0).getElement();	
		List<List<QName>> paths;
		if(XSDUtil.isGlobal(baseElement)){
			paths = new ArrayList<List<QName>>();
			paths.add(new ArrayList<QName>());
		}
		else
			paths = XPathCreator.createElementPaths((Element) baseElement.getParentNode());
		
		List<QName> elementsGroup = new ArrayList<QName>();
		for(XSDNamedComponent component : arguments.getComponents()){
			Element element = component.getElement();
			QName elementQName = XMLUtil.createQName(element);
			elementsGroup.add(elementQName);
		}
		
		//The new element is local
		QName groupName = new QName(null, arguments.getGroupName());
		
		XMLRefactoring refactoring = null;		
		if(!paths.isEmpty())
			refactoring = new GroupElementsRefactoring(paths, groupName, elementsGroup);
		
		return refactoring;
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		// TODO Auto-generated method stub
		return new RefactoringStatus();
	}

	@Override
	public String getName() {

		return XMLRefactoringMessages.getString("XSLTGroupElementsParticipant.Name");
	}

	@Override
	protected boolean initialize(Object element) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void initialize(RefactoringArguments arguments) {
		super.initialize(arguments);
		this.arguments = (GroupElementsRefactoringArguments) arguments;
	}
	
	

}
