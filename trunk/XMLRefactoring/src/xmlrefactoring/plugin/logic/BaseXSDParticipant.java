package xmlrefactoring.plugin.logic;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xsd.ui.internal.refactor.TextChangeManager;
import org.eclipse.wst.xsd.ui.internal.refactor.util.TextChangeCompatibility;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDTypeDefinition;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.PluginNamingConstants;
import xmlrefactoring.plugin.logic.util.XMLUtil;
import xmlrefactoring.plugin.logic.util.XSDUtil;
import xmlrefactoring.plugin.xslt.FileControl;

public abstract class BaseXSDParticipant extends BaseParticipant{

	private static final String ROOT_ELEMENT_XPATH = "/element()[(namespace-uri(.)='http://www.w3.org/2001/XMLSchema')and(local-name(.)='schema')]/element()[(namespace-uri(.)='http://www.w3.org/2001/XMLSchema')and(local-name(.)='element')]";
	protected Document schemaDocument;
	protected TextChangeManager manager;
	private TextChange schemaFileChange;
	private IDOMElement root;

	@Override
	public void initialize(RefactoringArguments arguments){
		super.initialize(arguments);
		schemaDocument = baseArguments.getSchemaDocument();
		manager = new TextChangeManager();
		schemaFileChange = manager.get(baseArguments.getSchemaFile());
	}
	
	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		return new RefactoringStatus();
	}

	/**
	 * Base method that adds the schemaVersion attribute to the root elements
	 * of the XML Schema
	 * The changes returned are NOT supposed to be composed, because they will be
	 * returned by the TextChangeManager
	 */
	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
	OperationCanceledException {

		if(!FileControl.isUnderVersionControl(baseArguments.getSchemaFile())){			

			root = (IDOMElement) baseArguments.getSchemaDocument().getDocumentElement();
			List<XSDElementDeclaration> rootElements = baseArguments.getSchema().getElementDeclarations();
			for(XSDElementDeclaration element : rootElements){
				addSchemaVersion(element);
			}

		}

		return null;
	}

	private void addSchemaVersion(XSDElementDeclaration element) throws CoreException {

		IDOMElement rootElement = (IDOMElement) element.getElement();
		XSDTypeDefinition elementType = element.getType();
		String schemaNamespace = rootElement.getNamespaceURI();

		String elementTypeName = XSDUtil.getType(rootElement);
		if(elementType.getName() == null){
			
			NodeList simpleTypeList = rootElement.getElementsByTagNameNS(schemaNamespace, XSDUtil.SIMPLE_TYPE);
			//The element type is a local declared Simple Type
			if(simpleTypeList.getLength() != 0){
				Element simpleType = (Element) simpleTypeList.item(0);
				//TODO
			}
			else{
				NodeList complexTypeList = rootElement.getElementsByTagNameNS(schemaNamespace, XSDUtil.COMPLEX_TYPE);
				//The element type is a local declared Complex Type
				if(complexTypeList.getLength() != 0){
					IDOMElement complexType = (IDOMElement) complexTypeList.item(0);
					Element derivedContent = getDerivedContent(complexType); 
					if(derivedContent == null){
						addAttributeComplexType(rootElement, complexType);
					}
				}
				//The element type is not declared
				else{
					addAttributeNoType(rootElement);					
				}					
			}				
		}
		else{
			//TODO: Talvez seria melhor n‹o extender em alguns casos
			extendType(rootElement, elementType);
		}

	}

	private void extendType(IDOMElement rootElement, XSDTypeDefinition elementType) throws CoreException {		
		String newTypeName = elementType.getName() + "Extended";
		String newTypeQName = XMLUtil.createQName(XSDUtil.searchTargetNamespacePrefix(baseArguments.getSchema()), elementType.getName()) + "Extended";

		//Element update to the new type		
		IDOMAttr attr = (IDOMAttr) rootElement.getAttributeNode(XSDUtil.TYPE);
		int offset;
		TextEdit edit;
		String newTypeQuoted = XMLUtil.quoteString(newTypeQName);
		if(attr != null){ //Type explicitly declared
			offset = attr.getValueRegionStartOffset();
			int length = attr.getEndOffset() - attr.getValueRegionStartOffset();
			edit = new ReplaceEdit(offset, length, newTypeQuoted);
		}
		else{
			//Add the type right after the name
			IDOMAttr nameAttr = (IDOMAttr) rootElement.getAttributeNode(XSDUtil.NAME);
			offset = nameAttr.getEndOffset();
			StringBuffer typeInclusion = new StringBuffer();
			typeInclusion.append(" ");
			typeInclusion.append(XSDUtil.TYPE);
			typeInclusion.append("=");
			typeInclusion.append(newTypeQuoted);			
			edit = new InsertEdit(offset, typeInclusion.toString());
		}
		
		TextChangeCompatibility.addTextEdit(schemaFileChange, XMLRefactoringMessages.getString("BaseXSDParticipant.SchemaVersionAddition"), edit);
		
		//New type
		Element extendedType = XSDUtil.createComplexType(root, newTypeName);

		String contentQName;
		if(elementType.getSimpleType() == null)
			contentQName = XMLUtil.createQName(extendedType.getPrefix(), XSDUtil.COMPLEX_CONTENT);
		else
			contentQName = XMLUtil.createQName(extendedType.getPrefix(), XSDUtil.SIMPLE_CONTENT);
		Element contentElement = schemaDocument.createElement(contentQName);
		extendedType.appendChild(contentElement);

		String extensionQName = XMLUtil.createQName(extendedType.getPrefix(), XSDUtil.EXTENSION);		
		Element extension = schemaDocument.createElement(extensionQName);
		extension.setAttribute(XSDUtil.BASE, elementType.getQName(baseArguments.getSchema()));
		contentElement.appendChild(extension);
		extension.appendChild(createSchemaAttribute(root.getPrefix()));
		InsertEdit extendedTypeInsert = new InsertEdit(root.getEndStartOffset(), XMLUtil.toString(extendedType));
		TextChangeCompatibility.addTextEdit(schemaFileChange, XMLRefactoringMessages.getString("BaseXSDParticipant.SchemaVersionAddition"), extendedTypeInsert);		
	}
	
	private void addAttributeComplexType(Element rootElement, IDOMElement complexType) throws CoreException {
		int offset = complexType.getEndStartOffset();
		Element schemaAttribute = createSchemaAttribute(rootElement.getPrefix());
		InsertEdit insert = new InsertEdit(offset, XMLUtil.toString(schemaAttribute));
		TextChangeCompatibility.addTextEdit(schemaFileChange, XMLRefactoringMessages.getString("BaseXSDParticipant.SchemaVersionAddition"), insert);		
	}

	private void createReplacement(Node newElement, IDOMNode oldElement) throws CoreException {		
		String newElementText = XMLUtil.toString(newElement);
		int offset = oldElement.getStartOffset();
		int length = oldElement.getEndOffset() - offset; 
		ReplaceEdit replaceElement = new ReplaceEdit(offset, length, newElementText);
		TextChangeCompatibility.addTextEdit(schemaFileChange, XMLRefactoringMessages.getString("BaseXSDParticipant.SchemaVersionAddition"), replaceElement);		
	}

	private Element getDerivedContent(Element complexType) {
		Element content = null;
		NodeList contentList = complexType.getElementsByTagName(XSDUtil.SIMPLE_CONTENT);
		if(contentList.getLength() == 0){
			contentList = complexType.getElementsByTagName(XSDUtil.COMPLEX_CONTENT);
		}
		content = (Element) contentList.item(0);			
		return content;
	}

	private void addAttributeNoType(IDOMElement rootElement) throws CoreException {
		Element complexType = createLocalComplexType(rootElement);
		Node newRootElement = rootElement.cloneNode(false);
		newRootElement.appendChild(complexType);
		createReplacement(newRootElement, rootElement);
	}

	private Element createLocalComplexType(IDOMElement rootElement) {		
		String qName = XMLUtil.createQName(rootElement.getPrefix(), XSDUtil.COMPLEX_TYPE);
		Element complexType = schemaDocument.createElement(qName);		
		complexType.appendChild(createSchemaAttribute(rootElement.getPrefix()));
		return complexType;
	}

	private Element createSchemaAttribute(String prefix){
		String attrQName = XMLUtil.createQName(prefix, XSDUtil.ATTRIBUTE);
		Element attr = schemaDocument.createElement(attrQName);
		Attr name = schemaDocument.createAttribute(XSDUtil.NAME);
		name.setValue(XMLRefactoringMessages.getString("schemaVersion"));
		attr.setAttributeNode(name);
		return attr;
	}
}
