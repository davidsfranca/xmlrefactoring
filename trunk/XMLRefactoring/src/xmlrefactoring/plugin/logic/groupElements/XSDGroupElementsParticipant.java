package xmlrefactoring.plugin.logic.groupElements;

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
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MoveSourceEdit;
import org.eclipse.text.edits.MoveTargetEdit;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xsd.ui.internal.refactor.TextChangeManager;
import org.eclipse.wst.xsd.ui.internal.refactor.util.TextChangeCompatibility;
import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.plugin.PluginNamingConstants;
import xmlrefactoring.plugin.logic.util.SchemaElementVerifier;

public class XSDGroupElementsParticipant extends GroupElementsParticipant {

	private GroupElementsRefactoringArguments arguments;
	private String prefix;
	
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
		TextChangeManager manager = new TextChangeManager();		
		IDOMElement idomElement = (IDOMElement) arguments.getComponents().get(0).getElement();
		String fileStr = idomElement.getModel().getBaseLocation();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileStr));		
		TextChange change = manager.get(file);
		
		//Insert a complexType in the end of the document 
		IDOMElement root = (IDOMElement) idomElement.getOwnerDocument().getDocumentElement();		
		int includeOffset = root.getEndStructuredDocumentRegion().getStartOffset();
		InsertEdit newType = new InsertEdit(includeOffset, createNewType(idomElement));		
		TextChangeCompatibility.addTextEdit(change, PluginNamingConstants.GROUP_ELEMENT_TYPE_CREATION, newType);
		
		//Move the elements to the new complexType
		for(XSDNamedComponent component : arguments.getComponents()){
			IDOMElement movingElement = (IDOMElement) component.getElement();
			MoveSourceEdit source = new MoveSourceEdit(movingElement.getStartOffset(), movingElement.getEndOffset() - movingElement.getStartOffset());
			MoveTargetEdit target = new MoveTargetEdit(includeOffset);
			source.setTargetEdit(target);
			target.setSourceEdit(source);		
			TextChangeCompatibility.addTextEdit(change, "Element move", source);
			TextChangeCompatibility.addTextEdit(change, "Element move", target);
			InsertEdit newLine = new InsertEdit(includeOffset, "\n");		
			TextChangeCompatibility.addTextEdit(change, "", newLine);			
		}		
		
		//Insert the closing tag
		InsertEdit closeTag = new InsertEdit(includeOffset, createCloseTag(idomElement));		
		TextChangeCompatibility.addTextEdit(change, "Closing Tag", closeTag);
		
		//Insert the new element of the created type
		int newElementOffset = idomElement.getStartOffset();
		InsertEdit newElement = new InsertEdit(newElementOffset, createElement());
		TextChangeCompatibility.addTextEdit(change, "Grouping element", newElement);
		
		return new CompositeChange(PluginNamingConstants.GROUP_ELEMENT_TRANSF_NAME,manager.getAllChanges());
	}

	private String createElement() {
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append(prefix);
		sb.append(SchemaElementVerifier.ELEMENT);
		sb.append(" name=\"");
		sb.append(arguments.getGroupName());
		sb.append("\"");
		sb.append(" type=\"");
		sb.append(arguments.getTypeName());
		sb.append("\"");
		sb.append("/>\n");
		return sb.toString();
	}

	private String createCloseTag(IDOMElement idomElement) {
		StringBuilder sb = new StringBuilder();
		sb.append("</");
		sb.append(prefix);
		sb.append(SchemaElementVerifier.ALL);
		sb.append(">\n");
		sb.append("</");
		sb.append(prefix);
		sb.append(SchemaElementVerifier.COMPLEX_TYPE);
		sb.append(">\n");
		return sb.toString();
	}

	private String createNewType(IDOMElement idomElement) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n<");
		sb.append(prefix);
		sb.append(SchemaElementVerifier.COMPLEX_TYPE);
		sb.append(" name=\"");
		sb.append(arguments.getTypeName());
		sb.append("\">\n");
		sb.append("<");
		sb.append(SchemaElementVerifier.ALL);
		sb.append(">\n");
		
		return sb.toString() ;
	}
	
	private String createPrefix(IDOMElement idomElement){
		String prefix = idomElement.getPrefix();
		if(prefix != null)
			prefix = prefix + ":";
		else
			prefix = "";
		return prefix;
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
	protected void initialize(RefactoringArguments arguments) {
		this.arguments = (GroupElementsRefactoringArguments) arguments;
		IDOMElement baseElement = (IDOMElement) this.arguments.getComponents().get(0).getElement();
		prefix = createPrefix(baseElement);
	}

}
