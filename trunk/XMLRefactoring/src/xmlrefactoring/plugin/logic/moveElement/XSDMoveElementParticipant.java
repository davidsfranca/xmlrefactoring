package xmlrefactoring.plugin.logic.moveElement;

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
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xsd.ui.internal.refactor.TextChangeManager;
import org.eclipse.wst.xsd.ui.internal.refactor.util.TextChangeCompatibility;
import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.logic.BaseXSDParticipant;
import xmlrefactoring.plugin.logic.moveElement.external.MoveElementRefactoringArguments;
import xmlrefactoring.plugin.logic.ref2elem.external.Ref2ElemRefactoringArguments;
import xmlrefactoring.plugin.logic.util.XMLUtil;
import xmlrefactoring.plugin.logic.util.XSDUtil;

public class XSDMoveElementParticipant extends BaseXSDParticipant {
	private MoveElementRefactoringArguments arguments;
	private XSDNamedComponent component;
	private Element movingElement;
	
	public RefactoringStatus checkConditions(IProgressMonitor pm, 
			CheckConditionsContext context) throws OperationCanceledException {
		RefactoringStatus status = super.checkConditions(pm, context);
		
		if(!XSDUtil.isComplexElement(arguments.getReceivingElement()))
			status.addFatalError(XMLRefactoringMessages.getString("XSDMoveElementParticipant.NotComplexElement"));
		
		return status;
	}
	
	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, 
			OperationCanceledException {
		
		TextChangeManager manager = new TextChangeManager();
		createTransform(manager);
		
		super.createChange(pm);
		return new CompositeChange(XMLRefactoringMessages.getString("XSDRef2ElemParticipant.TransformReference"), 
				manager.getAllChanges());
	}
	
	private void createTransform(TextChangeManager manager) throws CoreException
	{
		IDOMElement idomElement = (IDOMElement) movingElement;
		
		String fileStr = idomElement.getModel().getBaseLocation();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileStr));		
		TextChange textChange = manager.get(file);
		
		TextChangeCompatibility.addTextEdit(textChange, 
				XMLRefactoringMessages.getString("XSDMoveElementParticipant.MoveElement"), 
				moveElement(movingElement, arguments.getReceivingElement()));
	}
	
	private TextEdit[] moveElement(Element element, Element receivingElement) throws CoreException
	{
		List<TextEdit> movingTransformation = new ArrayList<TextEdit>();
		
		IDOMElement root = (IDOMElement) arguments.getSchemaDocument().getDocumentElement();
		Element moved = XSDUtil.createSimpleElement(root, element.getAttribute("name"), 
				element.getAttribute("type"));
		
		IDOMElement idomElement = (IDOMElement) element;
		int offset = idomElement.getStartOffset();
		int length = idomElement.getEndOffset() - idomElement.getStartOffset();
		
		DeleteEdit deletion = new DeleteEdit(offset, length);
		movingTransformation.add(deletion);
		
		Node receiving = receivingElement.getFirstChild();
		while(receiving.getNodeName().equals("#text"))
			receiving = receiving.getNextSibling();
		
		receiving = receiving.getFirstChild();
		while(true)
		{
			if(receiving.getNodeName().equals("#text"))
				receiving = receiving.getNextSibling();
			else if(receiving.getLocalName().equals(XSDUtil.ATTRIBUTE))
				receiving = receiving.getNextSibling();
			else
				break;
		}
		
		offset = ((IDOMElement)((Element) receiving)).getEndStartOffset() - 1;
		
		InsertEdit insertion = new InsertEdit(offset, XMLUtil.toString(moved));
		movingTransformation.add(insertion);
		
		return movingTransformation.toArray(new TextEdit[0]);
	}
	
	@Override
	protected boolean initialize(Object element) {
		if(element instanceof XSDNamedComponent)
		{
			this.component = (XSDNamedComponent) element;
			
			if(XSDUtil.isElement(this.component.getElement()))
			{
				this.movingElement = component.getElement();
				return true;
			}
		}
		return false;
	}

	@Override
	public void initialize(RefactoringArguments arguments)
	{
		super.initialize(arguments);
		this.arguments = (MoveElementRefactoringArguments) arguments;
	}
	
	@Override
	public String getName() {
		return XMLRefactoringMessages.getString("XSDMoveElementParticipant.Name");
	}

}
