package xmlrefactoring.plugin.logic.removeElement;

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
import org.eclipse.wst.common.core.search.SearchMatch;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xsd.ui.internal.refactor.RefactoringMessages;
import org.eclipse.wst.xsd.ui.internal.refactor.util.TextChangeCompatibility;
import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.PluginNamingConstants;
import xmlrefactoring.plugin.logic.BaseXSDParticipant;
import xmlrefactoring.plugin.logic.removeElement.external.RemoveElementRefactoringArguments;
import xmlrefactoring.plugin.logic.util.SearchUtil;
import xmlrefactoring.plugin.logic.util.XSDUtil;

public class XSDRemoveElementParticipant extends BaseXSDParticipant {

	private RemoveElementRefactoringArguments arguments;
	private XSDNamedComponent component;
	private IDOMElement element;

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm, 
			CheckConditionsContext context) throws OperationCanceledException {
		RefactoringStatus status = super.checkConditions(pm, context);
		
		if(element == null)
			status.addFatalError(XMLRefactoringMessages.getString("XSDRemoveElementParticipant.SelectedElementIsNeeded"));
		
		return status;
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		removeDeclaration();
		if(XSDUtil.isGlobal(element)) {
			removeReferences();
		}
		
		super.createChange(pm);
		return new CompositeChange(XMLRefactoringMessages.getString("XSDRemoveElementParticipant.RemoveReference"),
				manager.getAllChanges());
	}

	private void removeDeclaration() {
		IDOMElement idomElement = arguments.getElements().get(0);
		String fileStr = idomElement.getModel().getBaseLocation();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileStr));
		
		TextChange textChange = manager.get(file);
		int offset = idomElement.getStartOffset();
		int length = idomElement.getEndOffset() - idomElement.getStartOffset();
		DeleteEdit deleteEdit = new DeleteEdit(offset, length);
		String editName = RefactoringMessages.getString("RenameComponentProcessor.Component_Refactoring_update_declatation");
		TextChangeCompatibility.addTextEdit(textChange, editName, deleteEdit);
	}

	private void removeReferences() throws CoreException{
		List<SearchMatch> matches = SearchUtil.searchReferences(element);
		for(SearchMatch match : matches) {
			TextChange textChange = manager.get(match.getFile());
			
			if(match.getObject() instanceof IDOMAttr) {
				IDOMAttr attr = (IDOMAttr) match.getObject();
				IDOMElement elem = (IDOMElement) attr.getOwnerElement();
				int offset = elem.getStartOffset();
				int length = elem.getEndOffset() - elem.getStartOffset();
				DeleteEdit deleteEdit = new DeleteEdit(offset, length);
				TextChangeCompatibility.addTextEdit(textChange, PluginNamingConstants.REMOVE_ELEMENT_PARTICIPANT_REMOVE_REFERENCE, deleteEdit);
			}
		}
	}

	@Override
	protected boolean initialize(Object element) {
		if(element instanceof XSDNamedComponent) {
			this.component = (XSDNamedComponent) element;
			this.element = (IDOMElement) component.getElement();
			return true;
		}
		return false;
	}

	@Override
	public void initialize(RefactoringArguments arguments) {
		super.initialize(arguments);
		this.arguments = (RemoveElementRefactoringArguments) arguments;
	}

	@Override
	public String getName() {
		return XMLRefactoringMessages.getString("XSDRemoveElementParticipant.Name");
	}

}
