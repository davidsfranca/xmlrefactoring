package prototipo.plugin;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.wst.common.core.search.SearchMatch;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xsd.ui.internal.refactor.RefactoringMessages;
import org.eclipse.wst.xsd.ui.internal.refactor.TextChangeManager;
import org.eclipse.wst.xsd.ui.internal.refactor.rename.ComponentRenameArguments;
import org.eclipse.wst.xsd.ui.internal.refactor.rename.RenameComponentProcessor;
import org.eclipse.wst.xsd.ui.internal.refactor.util.TextChangeCompatibility;
import org.eclipse.wst.xsd.ui.internal.search.IXSDSearchConstants;
import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Node;

public class XSLTRenameParticipant extends RenameParticipant{

	private TextChangeManager manager;
	private List<SearchMatch> matches;
	private XSDNamedComponent element;

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		return new RefactoringStatus();
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
	OperationCanceledException {
		//Renomeia elementos an™nimos
		
		
		IDOMElement idomElement = (IDOMElement) element.getElement();
		String fileStr = idomElement.getModel().getBaseLocation();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileStr));		
		
		TextChange textChange = manager.get(file);
		String newName = getRenameArguments().getNewName();
		newName = RenameComponentProcessor.quoteString(newName);
		IDOMAttr attr = (IDOMAttr) idomElement.getAttributeNode("name");
		int length = attr.getEndOffset() - attr.getValueRegionStartOffset() - 1;
		ReplaceEdit replaceEdit = new ReplaceEdit(attr.getValueRegionStartOffset(), length, newName);
		String editName = RefactoringMessages.getString("RenameComponentProcessor.Component_Refactoring_update_declatation");
		TextChangeCompatibility.addTextEdit(textChange, editName, replaceEdit);
		
		if(matches != null)
			for(SearchMatch match : matches){
				if(match.getObject() instanceof Node){
					Node node = (Node) match;
				}
			}
		return null;
	}

	@Override
	public String getName() {
		return "XSLTRenameParticipant";
	}

	@Override
	protected boolean initialize(Object element) {
		if(element instanceof XSDNamedComponent){
			this.element = (XSDNamedComponent) element;
			if(getArguments() instanceof ComponentRenameArguments){
				// changeManger is passed in from the RenameComponentProcessor to collect all the changes
				manager = getRenameArguments().getChangeManager();
				matches = (List<SearchMatch>)((ComponentRenameArguments)getArguments()).getMatches().get(IXSDSearchConstants.XMLSCHEMA_NAMESPACE);
			}
			return true;
		}		
		return false;
	}
	
	private ComponentRenameArguments getRenameArguments(){
		return (ComponentRenameArguments) getArguments();
	}

}
