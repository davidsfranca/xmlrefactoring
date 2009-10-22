package xmlrefactoring.plugin.logic.groupElements;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xsd.ui.internal.refactor.util.TextChangeCompatibility;
import org.w3c.dom.Element;

import xmlrefactoring.plugin.PluginNamingConstants;
import xmlrefactoring.plugin.logic.BaseXSDParticipant;
import xmlrefactoring.plugin.logic.util.XSDUtil;
import xmlrefactoring.plugin.logic.util.XMLUtil;

public class XSDGroupElementsParticipant extends BaseXSDParticipant {

	private GroupElementsRefactoringArguments arguments;

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		// TODO Auto-generated method stub
		return new RefactoringStatus();
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
	OperationCanceledException {
		
		//Gets the TextChange for the file		
		TextChange change = manager.get(arguments.getSchemaFile());

		//Create the new grouping type
		IDOMElement root = (IDOMElement) arguments.getSchemaDocument().getDocumentElement();
		Element complexType = XMLUtil.createComplexType(root, arguments.getTypeName());
		Element all = createAllNode(complexType); 			
		complexType.appendChild(all);
		for(IDOMElement movingElement : arguments.getElements()){
			all.appendChild(movingElement.cloneNode(true));
			DeleteEdit deleteElement = new DeleteEdit(movingElement.getStartOffset(), movingElement.getEndOffset() - movingElement.getStartOffset());
			TextChangeCompatibility.addTextEdit(change, PluginNamingConstants.GROUP_ELEMENT_DELETE, deleteElement);
		}
		int includeOffset = root.getEndStructuredDocumentRegion().getStartOffset();
		InsertEdit newType = new InsertEdit(includeOffset, XMLUtil.toString(complexType));
		TextChangeCompatibility.addTextEdit(change, PluginNamingConstants.GROUP_ELEMENT_TYPE_CREATION, newType);

		//Insert the new element of the created type
		IDOMElement firstElement = arguments.getElements().get(0);
		int newElementOffset = firstElement.getStartOffset();
		InsertEdit newElement = new InsertEdit(newElementOffset, XMLUtil.toString(createNewElement(firstElement)));
		TextChangeCompatibility.addTextEdit(change, PluginNamingConstants.GROUP_ELEMENT_GROUPING_ELEMENT, newElement);
		
		super.createChange(pm);
		return new CompositeChange(PluginNamingConstants.GROUP_ELEMENT_TRANSF_NAME,manager.getAllChanges());
	}

	private Element createNewElement(Element firstElement) {
		String elementQName = XMLUtil.createQName(firstElement.getPrefix(),	XSDUtil.ELEMENT);
		Element newElement = arguments.getSchemaDocument().createElement(elementQName);
		newElement.setAttribute(XSDUtil.NAME, arguments.getGroupName());
		String typeQName = XMLUtil.createQName(XSDUtil.searchTargetNamespacePrefix(arguments.getSchema()), arguments.getTypeName());
		newElement.setAttribute(XSDUtil.TYPE, typeQName);		
		return newElement;
	}

	private Element createAllNode(Element complexType) {
		return complexType.getOwnerDocument().createElement(XMLUtil.createQName(complexType.getPrefix(), XSDUtil.ALL));
	}

	@Override
	public String getName() {
		//TODO Padronizar msg
		return "Group elements XSD participant";
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
