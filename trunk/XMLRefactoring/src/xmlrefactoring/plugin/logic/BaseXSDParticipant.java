package xmlrefactoring.plugin.logic;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xsd.ui.internal.refactor.TextChangeManager;
import org.eclipse.wst.xsd.ui.internal.refactor.util.TextChangeCompatibility;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDTypeDefinition;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import xmlrefactoring.plugin.PluginNamingConstants;
import xmlrefactoring.plugin.logic.util.XSDUtil;
import xmlrefactoring.plugin.logic.util.XMLUtil;
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
		
		String elementTypeName = XSDUtil.getType(rootElement);
		if(elementType.getName() == null){
			NodeList simpleTypeList = rootElement.getElementsByTagName(XSDUtil.SIMPLE_TYPE);
			//The element type is a local declared Simple Type
			if(simpleTypeList.getLength() != 0){
				Element simpleType = (Element) simpleTypeList.item(0);
				//TODO
			}
			else{
				NodeList complexTypeList = rootElement.getElementsByTagName(XSDUtil.COMPLEX_TYPE);
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
			//TODO: Talvez seria melhor n�o extender em alguns casos
			extendType(rootElement, elementType);
		}
			
	}

	private void extendType(IDOMElement rootElement, XSDTypeDefinition elementType) throws CoreException {		
		String newTypeName = elementType.getName() + "Extended";
		String newTypeQName = XMLUtil.createQName(XSDUtil.searchTargetNamespacePrefix(baseArguments.getSchema()), elementType.getName()) + "Extended";
		
		//Element update to the new type
		Element newRootElement = (Element) rootElement.cloneNode(true);
		newRootElement.setAttribute(XSDUtil.TYPE, newTypeQName);
		createReplacement(newRootElement, rootElement);
		//New type
		Element extendedType = XMLUtil.createComplexType(root, newTypeName);
		
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
		TextChangeCompatibility.addTextEdit(schemaFileChange, PluginNamingConstants.SCHEMA_VERSION_ADDITION, extendedTypeInsert);		
	}

	

	private void addAttributeComplexType(Element rootElement, IDOMElement complexType) {
		Element newComplexType = (Element) complexType.cloneNode(true);
		newComplexType.appendChild(createSchemaAttribute(rootElement.getPrefix()));
		createReplacement(newComplexType, complexType);
	}

	private void createReplacement(Node newElement, IDOMNode oldElement) {		
		String newElementText = XMLUtil.toString(newElement);
		int offset = oldElement.getStartOffset();
		int length = oldElement.getEndOffset() - offset; 
		ReplaceEdit replaceElement = new ReplaceEdit(offset, length, newElementText);
		TextChangeCompatibility.addTextEdit(schemaFileChange, PluginNamingConstants.SCHEMA_VERSION_ADDITION, replaceElement);		
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

	private void addAttributeNoType(IDOMElement rootElement) {
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
		name.setValue(PluginNamingConstants.SCHEMA_VERSION);
		attr.setAttributeNode(name);
		return attr;
	}
}
