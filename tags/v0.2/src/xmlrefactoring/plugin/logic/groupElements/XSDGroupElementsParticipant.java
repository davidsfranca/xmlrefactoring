package xmlrefactoring.plugin.logic.groupElements;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.EList;
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
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDTypeDefinition;
import org.w3c.dom.Element;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.PluginNamingConstants;
import xmlrefactoring.plugin.logic.BaseXSDParticipant;
import xmlrefactoring.plugin.logic.util.XMLUtil;
import xmlrefactoring.plugin.logic.util.XSDUtil;
import xmlrefactoring.plugin.refactoring.XMLRefactoring;

public class XSDGroupElementsParticipant extends BaseXSDParticipant {

	private GroupElementsRefactoringArguments arguments;
	private static final String TNS_PREFIX = "tns";
	private static final String ALTERNATIVE_PREFIX = "tn";

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		RefactoringStatus status = super.checkConditions(pm, context);
		EList<XSDTypeDefinition> declaredTypes = arguments.getSchema().getTypeDefinitions();
		for(int i = 0; i < declaredTypes.size(); i++){
			if(arguments.getTypeName().equals(declaredTypes.get(i).getName()))
				status.addFatalError(XMLRefactoringMessages.getString("XSDGroupElementsParticipant.TypeNameAlreadyUsed"));
		}		
		return status;
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
	OperationCanceledException {
		
		//Gets the TextChange for the file		
		TextChange change = manager.get(arguments.getSchemaFile());

		//Create the new grouping type
		IDOMElement root = (IDOMElement) arguments.getSchemaDocument().getDocumentElement();
		Element complexType = XMLUtil.createComplexType(root, arguments.getTypeName());
		
		Element compositor = createCompositorNode(complexType); 			
		complexType.appendChild(compositor);
		
		for(IDOMElement movingElement : arguments.getElements()){
			compositor.appendChild(movingElement.cloneNode(true));
			DeleteEdit deleteElement = new DeleteEdit(movingElement.getStartOffset(), movingElement.getEndOffset() - movingElement.getStartOffset());
			TextChangeCompatibility.addTextEdit(change, XMLRefactoringMessages.getString("XSDGroupElementsParticipant.GroupElementDelete"), deleteElement);
		}
		int includeOffset = root.getEndStructuredDocumentRegion().getStartOffset();
		InsertEdit newType = new InsertEdit(includeOffset, XMLUtil.toString(complexType));
		TextChangeCompatibility.addTextEdit(change, XMLRefactoringMessages.getString("XSDGroupElementsParticipant.GroupElementTypeCreation"), newType);

		//Insert the new element of the created type
		IDOMElement firstElement = arguments.getElements().get(0);
		int newElementOffset = firstElement.getStartOffset();
		InsertEdit newElement = new InsertEdit(newElementOffset, XMLUtil.toString(createNewElement(firstElement)));
		TextChangeCompatibility.addTextEdit(change, XMLRefactoringMessages.getString("XSDGroupElementsParticipant.GroupingElement"), newElement);
		
		super.createChange(pm);
		return new CompositeChange(XMLRefactoringMessages.getString("XSDGroupElementsParticipant.Transf"),manager.getAllChanges());
	}

	private Element createNewElement(Element firstElement) {
		String elementQName = XMLUtil.createQName(firstElement.getPrefix(),	XSDUtil.ELEMENT);
		Element newElement = arguments.getSchemaDocument().createElement(elementQName);
		newElement.setAttribute(XSDUtil.NAME, arguments.getGroupName());
		String typeQName;
		
		if(arguments.getSchema().getTargetNamespace() == null)
			typeQName = arguments.getTypeName();
		else{
			String prefix = XSDUtil.searchTargetNamespacePrefix(arguments.getSchema());
			if(prefix == null){
				//There is a declared targetNamespace but no prefix was made to that namespace
				//Uses the "tns" prefix, except if it`s being used by http://www.w3.org/2001/XMLSchema.
				//In this case, uses "tn".
				Map<String, String> QNameMap = arguments.getSchema().getQNamePrefixToNamespaceMap();
				String tnsPrefixNamespace = QNameMap.get(TNS_PREFIX);
				if(tnsPrefixNamespace == null || !tnsPrefixNamespace.equals(XSDUtil.SCHEMA_NAMESPACE))
					prefix = TNS_PREFIX;
				else
					prefix = ALTERNATIVE_PREFIX;					
				String namespacePrefixDeclaration = XMLUtil.createQName(XSDUtil.XMLNS, prefix);
				newElement.setAttribute(namespacePrefixDeclaration, arguments.getSchema().getTargetNamespace());				
			}
			typeQName = XMLUtil.createQName(prefix, arguments.getTypeName());
		}			
		newElement.setAttribute(XSDUtil.TYPE, typeQName);		
		return newElement;
	}

	private Element createCompositorNode(Element complexType) {
		IDOMElement firstElement = arguments.getElements().get(0); 		
		String compositor;
		//If the element being grouped is global uses the SEQUENCE compositor, else uses its parent compositor.
		if(XSDUtil.isGlobal(firstElement))
			compositor = XSDUtil.SEQUENCE;
		else
			compositor = firstElement.getParentNode().getLocalName();		
		return complexType.getOwnerDocument().createElement(XMLUtil.createQName(complexType.getPrefix(), compositor));
	}

	@Override
	public String getName() {
		return XMLRefactoringMessages.getString("XSDGroupElementsParticipant.Name");
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
