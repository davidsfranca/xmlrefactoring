package xmlrefactoring.plugin.logic.elem2attr;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
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
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xsd.ui.internal.refactor.TextChangeManager;
import org.eclipse.wst.xsd.ui.internal.refactor.util.TextChangeCompatibility;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.logic.BaseXSDParticipant;
import xmlrefactoring.plugin.logic.elem2attr.external.Elem2AttrRefactoringArguments;
import xmlrefactoring.plugin.logic.util.SearchUtil;
import xmlrefactoring.plugin.logic.util.XMLUtil;
import xmlrefactoring.plugin.logic.util.XSDUtil;

@SuppressWarnings("restriction")
public class XSDElem2AttrParticipant extends BaseXSDParticipant {
	private Elem2AttrRefactoringArguments arguments;
	private XSDNamedComponent component;
	private Element transformingElement;
	
	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		RefactoringStatus status = super.checkConditions(pm, context);
		
		if(arguments.isElement()){
			EList<XSDAttributeDeclaration> componentDeclarations = arguments.getSchema().getAttributeDeclarations();
			for(int i = 0; i < componentDeclarations.size(); i++){
				if(transformingElement.getAttribute("name").equals(componentDeclarations.get(i).getName()))
					status.addFatalError(XMLRefactoringMessages.getString("XSDElem2AttrParticipant.ElementNameAlreadyUsed"));
			}
			
			if(transformingElement.getAttributeNode("maxOccurs") != null && 
					!transformingElement.getAttribute("maxOccurs").equals("1"))
				status.addFatalError(XMLRefactoringMessages.getString("XSDElem2AttrParticipant.CardinalityNotAllowed"));
		}
		
		if(arguments.isAttribute())
			status.addError(XMLRefactoringMessages.getString("XSDElem2AttrParticipant.ElementIsExpected"));
		
		return status;
	}
	
	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		
		TextChangeManager manager = new TextChangeManager();
		transformDeclaration(manager);		
		
		if(XSDUtil.isGlobal(transformingElement)){
			transformReferences(manager);
		}			
		
		super.createChange(pm);
		return new CompositeChange(XMLRefactoringMessages.getString("XSDElem2AttrParticipant.TransformReference"),manager.getAllChanges());
	}
	
	public void transformDeclaration(TextChangeManager manager) throws CoreException
	{
		IDOMElement idomElement = arguments.getElements().get(0);
		
		String fileStr = idomElement.getModel().getBaseLocation();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileStr));		
		TextChange textChange = manager.get(file);
		
		TextChangeCompatibility.addTextEdit(textChange, 
				XMLRefactoringMessages.getString("XSDAttr2ElemParticipant.DecName"), 
				elem2attr(transformingElement, false));
	}
	
	private void transformReferences(TextChangeManager manager)  throws CoreException
	{
		for(SearchMatch match : SearchUtil.searchReferences((IDOMElement) transformingElement))
		{
			TextChange change = manager.get(match.getFile());
			if(match.getObject() instanceof Node)
			{				
				Node node = (Node) match.getObject();
				
				if(node instanceof Attr)
				{
					Attr attr = (Attr) node;
					
					TextEdit [] transformation = elem2attr(attr.getOwnerElement(), true);
					
					TextChangeCompatibility.addTextEdit(change, 
								XMLRefactoringMessages.getString("XSDAttr2ElemParticipant.DecName"), 
								transformation);
				}

			}
		}
	}
	
	private TextEdit[] elem2attr(Element element, boolean isRef) throws CoreException
	{
		List<TextEdit> elem2attrTransformation = new ArrayList<TextEdit>();
		IDOMElement idomElement = (IDOMElement) element;
		IDOMElement root = (IDOMElement) arguments.getSchemaDocument().getDocumentElement();
		
		int offset = idomElement.getStartOffset();
		int length = idomElement.getEndOffset() - idomElement.getStartOffset();
		
		DeleteEdit deleteElement = new DeleteEdit(offset, length);
		elem2attrTransformation.add(deleteElement);
		
		Element attr = null;
		
		if(isRef)
		{
			attr = XSDUtil.createRefAttribute(root, idomElement.getAttribute("ref"), arguments.getSchema());
		}
		else
		{		
			String occurence = "";
			
			if(transformingElement.hasAttribute("minOccurs"))
			{
				if(transformingElement.getAttribute("minOccurs").equals("0"))
					occurence = "optional";
				else if(transformingElement.getAttribute("minOccurs").equals("1"))
					occurence = "required";
			}			
			
			attr = XSDUtil.createAttribute(root, idomElement.getAttribute("name"),
					idomElement.getAttribute("type"), occurence);
		}
		
		Node parentNode = idomElement.getParentNode();
		Element parent = (Element) parentNode;
		
		idomElement = (IDOMElement) parent;
		
		offset = idomElement.getEndOffset() + 1;
		
		InsertEdit insertElement = new InsertEdit(offset, XMLUtil.toString(attr));		
		elem2attrTransformation.add(insertElement);
		
		return elem2attrTransformation.toArray(new TextEdit[0]);
	}
	
	@Override
	protected boolean initialize(Object element) {
		if(element instanceof XSDNamedComponent)
		{
			this.component = (XSDNamedComponent) element;
			
			if(XSDUtil.isElement(this.component.getElement()))
			{
				this.transformingElement = component.getElement();				
				if(!XSDUtil.isComplexType(transformingElement))
						return true;
			}
			
		}
		return false;
	}

	@Override
	public String getName() {
		return XMLRefactoringMessages.getString("XSDElem2AttrParticipant.Name");
	}
	
	@Override
	public void initialize(RefactoringArguments arguments){
		super.initialize(arguments);
		this.arguments = (Elem2AttrRefactoringArguments) arguments;
	}
}
