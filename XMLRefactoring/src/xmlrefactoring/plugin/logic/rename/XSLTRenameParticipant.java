package xmlrefactoring.plugin.logic.rename;

import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xsd.ui.internal.refactor.RefactoringMessages;
import org.eclipse.wst.xsd.ui.internal.refactor.TextChangeManager;
import org.eclipse.wst.xsd.ui.internal.refactor.rename.ComponentRenameArguments;
import org.eclipse.wst.xsd.ui.internal.refactor.rename.RenameComponentProcessor;
import org.eclipse.wst.xsd.ui.internal.refactor.util.TextChangeCompatibility;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDSchema;

import xmlrefactoring.plugin.logic.util.CreateXSLChange;
import xmlrefactoring.plugin.logic.util.SchemaElementVerifier;
import xmlrefactoring.plugin.logic.util.XMLUtil;
import xmlrefactoring.plugin.logic.util.XPathCreator;
import xmlrefactoring.plugin.refactoring.RenameAttributeRefactoring;
import xmlrefactoring.plugin.refactoring.RenameElementRefactoring;
import xmlrefactoring.plugin.refactoring.XMLRefactoring;
import xmlrefactoring.plugin.xslt.FileControl;

//Este participant é uma exceção na arquitetura, pois se integra à estrutura do editor,
//sem extender as classes base da aplicação
/**
 * Class responsible for creating the XSLT files related to the rename refactoring
 */
public class XSLTRenameParticipant extends RenameParticipant{

	private TextChangeManager manager;
	private XSDNamedComponent component;

	private static final String XSLT_CHANGE_TEXT = "XSLT change";
	
	/**
	 * The list of Xpaths to the element being renamed
	 */
	private List<List<QName>> paths;

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		return new RefactoringStatus();
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
	OperationCanceledException {

		if(!isGlobalElement(component))
			renameLocalElements();

		XMLRefactoring refactoring = null;
		
		if(SchemaElementVerifier.isElement(component.getElement())){
			paths = XPathCreator.createElementPaths(component.getElement());
			refactoring = new RenameElementRefactoring(paths, getRenameArguments().getNewName(),true);
		}
		else{
			paths = XPathCreator.createAttributePaths(component.getElement());	
			QName attr = XMLUtil.createQName(component.getElement());
			refactoring = new RenameAttributeRefactoring(paths,getRenameArguments().getNewName(), attr, true);
		}
			
		String schemaPath = ((IDOMElement) component.getElement()).getModel().getBaseLocation();
		IFile schemaFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(schemaPath));
		
		CompositeChange compositeChange = new CompositeChange(XSLT_CHANGE_TEXT);
		IPath xslPath;
		IPath xslPathRev;

		if(FileControl.isUnderVersionControl(schemaFile)){
			xslPath = FileControl.getNextPath(schemaFile, false);	
			Change incrementDescriptorLastFile = FileControl.incrementLastFile(schemaFile);
			compositeChange.add(incrementDescriptorLastFile);
			xslPathRev = FileControl.getNextReversePath(schemaFile,false);
		}
		else{
			compositeChange.add(FileControl.addToVersionControl(schemaFile));
			xslPath = FileControl.getNextPath(schemaFile, true);
			xslPathRev = FileControl.getNextReversePath(schemaFile,true);
		}
		Change xslChange = new CreateXSLChange(refactoring, xslPath);
		compositeChange.add(xslChange);	
		
		Change xslReverseChange = new CreateXSLChange(refactoring.getReverseRefactoring(), xslPathRev);
		compositeChange.add(xslReverseChange);

		return compositeChange;
	}

	/**
	 * Return true if the component is a global element
	 * @param component
	 * @return
	 */
	private boolean isGlobalElement(XSDNamedComponent component) {
		if(component.getContainer() instanceof XSDSchema)
			return true;
		else
			return false;
	}	

	/**
	 * Corrige bug de renomear elementos declarados em tipos (não globais)	
	 */
	private void renameLocalElements(){

		IDOMElement idomElement = (IDOMElement) component.getElement();
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
	}

	@Override
	public String getName() {
		return "XSLTRenameParticipant";
	}

	@Override
	protected boolean initialize(Object element) {
		if(element instanceof XSDNamedComponent){
			this.component = (XSDNamedComponent) element;
			if(getArguments() instanceof ComponentRenameArguments){
				// changeManger is passed in from the RenameComponentProcessor to collect all the changes
				manager = getRenameArguments().getChangeManager();
			}
			return true;
		}		
		return false;
	}

	private ComponentRenameArguments getRenameArguments(){
		return (ComponentRenameArguments) getArguments();
	}

}
