package xmlrefactoring.plugin.logic.moveElement;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.w3c.dom.Element;

import xmlrefactoring.plugin.logic.BaseXSLParticipant;
import xmlrefactoring.plugin.logic.moveElement.external.MoveElementRefactoringArguments;
import xmlrefactoring.plugin.logic.rename.external.RenameRefactoringArguments;
import xmlrefactoring.plugin.logic.util.XMLUtil;
import xmlrefactoring.plugin.logic.util.XPathCreator;
import xmlrefactoring.plugin.refactoring.MoveElementRefactoring;
import xmlrefactoring.plugin.refactoring.XMLRefactoring;

public class XSLTMoveElementParticipant extends BaseXSLParticipant {

	private MoveElementRefactoringArguments arguments;
	private Element source;
	private Element receiver;
	
	@Override
	protected XMLRefactoring getXMLRefactoring() throws CoreException {
		XMLRefactoring refactoring = null;
		List<List<QName>> paths;
		List<QName> changers = new ArrayList<QName>();
		
		paths = XPathCreator.createElementPaths(source);
		
		changers.add(XMLUtil.createQName(source));
		changers.add(XMLUtil.createQName(receiver));
		
		if(!paths.isEmpty())
			refactoring = new MoveElementRefactoring(paths, 
					arguments.getElements().get(0).getLocalName(), changers);
			
		return refactoring;
	}

	@Override
	protected boolean initialize(Object element) {
		return true;
	}
	
	public void initialize(RefactoringArguments arguments)
	{
		super.initialize(arguments);
		this.arguments = (MoveElementRefactoringArguments) arguments;
		source = (Element) this.arguments.getElements().get(0).getParentNode().getParentNode().getParentNode();
		receiver = this.arguments.getReceivingElement();
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
