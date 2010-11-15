package xmlrefactoring.plugin.logic.elem2ref;

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
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xsd.ui.internal.refactor.TextChangeManager;
import org.eclipse.wst.xsd.ui.internal.refactor.util.TextChangeCompatibility;
import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Element;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.logic.BaseXSDParticipant;
import xmlrefactoring.plugin.logic.elem2ref.external.Elem2RefRefactoringArguments;
import xmlrefactoring.plugin.logic.util.XMLUtil;
import xmlrefactoring.plugin.logic.util.XSDUtil;

public class XSDElem2RefParticipant extends BaseXSDParticipant {
	Elem2RefRefactoringArguments arguments;
	private XSDNamedComponent component;
	private Element transformingElement;
	
	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, 
			OperationCanceledException {
		
		TextChangeManager manager = new TextChangeManager();
		transformElement(manager);
		
		super.createChange(pm);
		return new CompositeChange(XMLRefactoringMessages.getString("XSDElem2RefParticipant.TransformElement"), 
				manager.getAllChanges());
	}
	
	private void transformElement(TextChangeManager manager) throws CoreException {
		IDOMElement idomElement = (IDOMElement) transformingElement;		
				
		String fileStr = idomElement.getModel().getBaseLocation();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileStr));		
		TextChange textChange = manager.get(file);
		
		TextChangeCompatibility.addTextEdit(textChange, 
				XMLRefactoringMessages.getString("XSDRef2ElemParticipant.DecName"), 
				createReferenciableElement(manager));
		
		int offset = idomElement.getStartOffset();
		int length = idomElement.getEndOffset() - idomElement.getStartOffset();
		
		TextChangeCompatibility.addTextEdit(textChange, 
				XMLRefactoringMessages.getString("XSDRef2ElemParticipant.DecName"), 
				createReference(manager, offset, length));
	}
	
	private TextEdit [] createReferenciableElement(TextChangeManager manager) throws CoreException {
		List<TextEdit> ref2ElemTransformation = new ArrayList<TextEdit>();
		IDOMElement root = (IDOMElement) arguments.getSchemaDocument().getDocumentElement();
		Element e = XSDUtil.copyElement(root, transformingElement);
		
		InsertEdit insertElement = new InsertEdit(root.getStartStructuredDocumentRegion().getEnd() + 1,
				XMLUtil.toString(e));
		
		ref2ElemTransformation.add(insertElement);
		
		return ref2ElemTransformation.toArray(new TextEdit[0]);
	}
	
	private TextEdit [] createReference(TextChangeManager manager, int offset, 
			int length) throws CoreException {
		List<TextEdit> ref2ElemTransformation = new ArrayList<TextEdit>();
		IDOMElement root = (IDOMElement) arguments.getSchemaDocument().getDocumentElement();
		Element e = XSDUtil.createRefElement(root, transformingElement, arguments.getSchema());
		
		DeleteEdit deleteElement = new DeleteEdit(offset, length);
		InsertEdit insertElement = new InsertEdit(offset, XMLUtil.toString(e));
		
		ref2ElemTransformation.add(deleteElement);
		ref2ElemTransformation.add(insertElement);
		
		return ref2ElemTransformation.toArray(new TextEdit[0]);
	}
	
	@Override
	protected boolean initialize(Object element) {
		if(element instanceof XSDNamedComponent)
		{
			this.component = (XSDNamedComponent) element;
			
			if(!XSDUtil.isReference(this.component.getElement()))
			{
				this.transformingElement = component.getElement();
				return true;
			}
		}
		return false;
	}

	@Override
	public void initialize(RefactoringArguments arguments)
	{
		super.initialize(arguments);
		this.arguments = (Elem2RefRefactoringArguments) arguments;
	}
	
	@Override
	public String getName() {
		return XMLRefactoringMessages.getString("XSDElem2RefParticipant.Name");
	}
}
