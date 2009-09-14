package xmlrefactoring.applyChanges.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import xmlrefactoring.plugin.XMLRefactoringPlugin;
import xmlrefactoring.plugin.xslt.FileControl;

/**
 * Base class to actions which the selection is a schema file under version control
 * @author guilherme
 *
 */
public abstract class SchemaFileAction implements IObjectActionDelegate {

	private IFile selectedSchema;

	public void selectionChanged(IAction action, ISelection selection) {
		action.setEnabled(false); 
		if(selection instanceof TreeSelection){
			TreeSelection tree = (TreeSelection) selection;
			if(tree.getFirstElement() instanceof IFile){
				IFile selectedFile = (IFile) tree.getFirstElement();
				if(FileControl.isUnderVersionControl(selectedFile)){
					action.setEnabled(true);
					selectedSchema = selectedFile;
				}
			}
		}		
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
		
	}
	
	public IFile getSelectedSchema(){
		return selectedSchema;
	}

}
