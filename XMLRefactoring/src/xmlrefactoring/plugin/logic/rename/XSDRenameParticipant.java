package xmlrefactoring.plugin.logic.rename;

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
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.wst.common.core.search.SearchMatch;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xsd.ui.internal.refactor.RefactoringMessages;
import org.eclipse.wst.xsd.ui.internal.refactor.rename.ComponentRenameArguments;
import org.eclipse.wst.xsd.ui.internal.refactor.rename.RenameComponentProcessor;
import org.eclipse.wst.xsd.ui.internal.refactor.util.TextChangeCompatibility;
import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Node;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.PluginNamingConstants;
import xmlrefactoring.plugin.logic.BaseXSDParticipant;
import xmlrefactoring.plugin.logic.util.SearchUtil;
import xmlrefactoring.plugin.logic.util.XMLUtil;
import xmlrefactoring.plugin.logic.util.XSDUtil;
import xmlrefactoring.plugin.refactoring.XMLRefactoring;

public class XSDRenameParticipant extends BaseXSDParticipant{

	private RenameRefactoringArguments arguments;
	private XSDNamedComponent component;
	private IDOMElement element;

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		// TODO Auto-generated method stub
		return new RefactoringStatus();
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
	OperationCanceledException {

		renameDeclaration();		
		if(XSDUtil.isGlobal(element)){
			renameReferences();
		}			
		super.createChange(pm);
		return new CompositeChange(XMLRefactoringMessages.getString("XSDRenameParticipant.RenameReference"),manager.getAllChanges());
	}
	
	private void renameDeclaration(){

		IDOMElement idomElement = arguments.getElements().get(0);
		String fileStr = idomElement.getModel().getBaseLocation();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileStr));		

		TextChange textChange = manager.get(file);
		String newName = arguments.getNewName();
		IDOMAttr attr = (IDOMAttr) idomElement.getAttributeNode("name");
		int offset = attr.getValueRegionStartOffset() + 1;
		int length = attr.getValue().length();
		ReplaceEdit replaceEdit = new ReplaceEdit(offset, length, newName);
		String editName = RefactoringMessages.getString("RenameComponentProcessor.Component_Refactoring_update_declatation");
		TextChangeCompatibility.addTextEdit(textChange, editName, replaceEdit);
	}

	private void renameReferences() throws CoreException {
		List<SearchMatch> matches = SearchUtil.searchReferences(element);
		for(SearchMatch match : matches){
			TextChange textChange = manager.get(match.getFile());
			String newName = arguments.getNewName();
			String qualifier = "";

			if(match.getObject() instanceof IDOMAttr){
				IDOMAttr attr = (IDOMAttr)match.getObject();
				IDOMElement element = (IDOMElement)attr.getOwnerElement() ;
				newName = XMLUtil.createQName(XSDUtil.searchTargetNamespacePrefix(arguments.getSchema()), newName);
			}
			newName = XMLUtil.quoteString(newName);

			ReplaceEdit replaceEdit = new ReplaceEdit(match.getOffset(), match.getLength(), newName );
			TextChangeCompatibility.addTextEdit(textChange, PluginNamingConstants.RENAME_PARTICIPANT_RENAME_REFERENCE, replaceEdit);
		}
		
	}

	@Override
	public String getName() {
		//TODO Padronizar msg
		return "XSD Rename Participant";
	}

	@Override
	public boolean initialize(Object element) {
		//TODO Verificar se Ž element/atributo
		if(element instanceof XSDNamedComponent){
			this.component = (XSDNamedComponent) element;
			this.element = (IDOMElement) component.getElement();
			return true;
		}
		return false;
	}

	@Override
	public void initialize(RefactoringArguments arguments){
		super.initialize(arguments);
		this.arguments = (RenameRefactoringArguments) arguments;
	}

	

}
