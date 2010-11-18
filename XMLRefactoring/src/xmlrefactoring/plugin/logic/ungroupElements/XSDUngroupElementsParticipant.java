package xmlrefactoring.plugin.logic.ungroupElements;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.wst.common.core.search.SearchMatch;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xsd.ui.internal.refactor.TextChangeManager;
import org.eclipse.wst.xsd.ui.internal.refactor.util.TextChangeCompatibility;
import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.PluginNamingConstants;
import xmlrefactoring.plugin.logic.BaseXSDParticipant;
import xmlrefactoring.plugin.logic.ungroupElements.external.UngroupElementsRefactoringArguments;
import xmlrefactoring.plugin.logic.util.SearchUtil;
import xmlrefactoring.plugin.logic.util.XMLUtil;
import xmlrefactoring.plugin.logic.util.XSDUtil;

@SuppressWarnings("restriction")
public class XSDUngroupElementsParticipant extends BaseXSDParticipant {
	private UngroupElementsRefactoringArguments arguments;
	private XSDNamedComponent component;
	private Element ungroupingElement;
	private Element typedElement;
	private List<Node> nodeList;
	private List<Element> elementList;
	
	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		RefactoringStatus status = super.checkConditions(pm, context);
		
		if(!arguments.isElement())
		{
			status.addFatalError(XMLRefactoringMessages.getString("XSDUngroupElementsParticipant.ElementIsExpected"));
		}
		else if(XSDUtil.isComplexElement(ungroupingElement))
		{
			IDOMElement element = (IDOMElement) ungroupingElement;
			
			nodeList = new ArrayList<Node>();
			Node node = element.getFirstChild();
			
			while(node.getNodeName().equals("#text"))
				node = node.getNextSibling();
			
			node = node.getFirstChild();
			
			while(node != null)
			{
				if(!node.getNodeName().equals("#text"))
				{
					if(node.getLocalName().equals(XSDUtil.ALL))
						status.addFatalError(XMLRefactoringMessages.getString("XSDUngroupElementsParticipant.AllFoundSequenceExpected"));
					else if(node.getLocalName().equals(XSDUtil.CHOICE))
						status.addFatalError(XMLRefactoringMessages.getString("XSDUngroupElementsParticipant.ChoiceFoundSequenceExpected"));
					else if(node.getLocalName().equals(XSDUtil.ATTRIBUTE))
					{
						if(!node.getAttributes().getNamedItem("name").getNodeValue().equals("schemaVersion"))
							status.addFatalError(XMLRefactoringMessages.getString("XSDUngroupElementsParticipant.ChoiceFoundSequenceExpected"));
					}
					else
						nodeList.add(node);						
				}
				
				node = node.getNextSibling();
			}
		}
		
		return status;
	}
	
	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		
		if(XSDUtil.isComplexElement(ungroupingElement))
		{
			transformDeclaration(manager);
			if(XSDUtil.isGlobal(ungroupingElement))
			{
				transformReferences(manager);
			}
		}
		
		super.createChange(pm);
		return new CompositeChange(XMLRefactoringMessages.getString("XSDAttr2ElemParticipant.Name"), 
				manager.getAllChanges());
	}
	

	private void transformDeclaration(TextChangeManager manager) throws CoreException
	{
		IDOMElement idomElement = (IDOMElement) ungroupingElement;
		String fileStr = idomElement.getModel().getBaseLocation();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileStr));
		
		TextChange textChange = manager.get(file); 
		
		elementList = new ArrayList<Element>();
		for(Node n : nodeList)
		{
			if(n.getLocalName().equals(XSDUtil.SEQUENCE))
			{
				Node child = n.getFirstChild();
				while(child != null)
				{
					if(!child.getNodeName().equals("#text"))
						elementList.add(createElement(child));
					
					child = child.getNextSibling();
				}
			 }
			 else
				 elementList.add(createElement(n));
		}
		
		TextChangeCompatibility.addTextEdit(textChange, 
				XMLRefactoringMessages.getString("XSDUngroupElementsParticipant.Transf"), 
				ungroupingTransformation(ungroupingElement, elementList));
	}
	
	private TextEdit [] ungroupingTransformation(Element element, 
			List<Element> list) throws CoreException
	{		
		List<TextEdit> ungroupingTransformation = new ArrayList<TextEdit>();
		
		IDOMElement idomElement;
		int offset;
		
		if(typedElement == null)
			idomElement = (IDOMElement) element;
		else
			idomElement = (IDOMElement) typedElement;
		
		offset = idomElement.getStartOffset();
		int length = idomElement.getEndOffset() - idomElement.getStartOffset();

		DeleteEdit deletion = new DeleteEdit(offset, length);	
		ungroupingTransformation.add(deletion);
		
		for(Element e : list)
		{
			idomElement = (IDOMElement) e;
			InsertEdit insertion = new InsertEdit(offset, XMLUtil.toString(e) + "\n");
			ungroupingTransformation.add(insertion);
			offset += idomElement.getStartOffset();
		}
		
		return ungroupingTransformation.toArray(new TextEdit[0]);
	}
	
	private Element createElement(Node n)
	{
		IDOMElement root = (IDOMElement) arguments.getSchemaDocument().getDocumentElement();
		
		return XSDUtil.createElement(root, n.getAttributes(), n.getChildNodes(),
				XSDUtil.isGlobal(ungroupingElement));
	}
	
	private void transformReferences(TextChangeManager manager) throws CoreException
	{
		List<SearchMatch> matches = SearchUtil.searchReferences((IDOMElement) ungroupingElement);
		List<TextEdit> ungroupingTransform = new ArrayList<TextEdit>();
		
		for(SearchMatch match : matches){
			TextChange textChange = manager.get(match.getFile());
			
			IDOMAttr attr = (IDOMAttr)match.getObject();
			IDOMElement element = (IDOMElement)attr.getOwnerElement() ;
			
			int offset = element.getStartOffset();
			int length = element.getEndOffset() - element.getStartOffset();
			
			DeleteEdit deletion = new DeleteEdit(offset, length);
			ungroupingTransform.add(deletion);
			
			IDOMElement root = (IDOMElement) arguments.getSchemaDocument().getDocumentElement();
			
			for(Element e : elementList)
			{
				Element elem = XSDUtil.createRefElement(root, e, arguments.getSchema());
				
				InsertEdit insertion = new InsertEdit(offset, XMLUtil.toString(elem) + "\n");
				ungroupingTransform.add(insertion);
			}
			
			TextChangeCompatibility.addTextEdit(textChange, 
					PluginNamingConstants.RENAME_PARTICIPANT_RENAME_REFERENCE, 
					ungroupingTransform.toArray(new TextEdit[0]));
		}
	}
	
	@Override
	protected boolean initialize(Object element) {
		if(element instanceof XSDNamedComponent){
			this.component = (XSDNamedComponent) element;
			this.ungroupingElement = component.getElement();
			this.typedElement = null;
			
			if(!XSDUtil.isComplexElement(ungroupingElement))
			{
				this.typedElement = this.ungroupingElement;
				this.ungroupingElement = getTypedElement(this.typedElement);
			}
			
			return true;
		}
		return false;
	}
	
	private Element getTypedElement(Element element)
	{		
		List<SearchMatch> matches = new ArrayList<SearchMatch>();
		
		try {
			matches = SearchUtil.searchComplexTypeDeclaration(element);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(SearchMatch match : matches)
		{
			if(match.getObject() instanceof Node)
			{
				Node n = (Node) match.getObject();
				
				if(n instanceof Attr)
				{
					Element type = ((Attr) n).getOwnerElement();
					
					IDOMElement root = (IDOMElement) arguments.getSchemaDocument().getDocumentElement();
					String name = element.getAttribute("name");
					
					return XSDUtil.createElementBasedUponType(root, type, name);
				}
			}
		}
		
		return null;
	}
		
	@Override
	public void initialize(RefactoringArguments arguments) {
		super.initialize(arguments);
		this.arguments = (UngroupElementsRefactoringArguments) arguments;
	}
	
	@Override
	public String getName() {
		return XMLRefactoringMessages.getString("XSDUngroupElementsParticipant.Name");
	}

}
