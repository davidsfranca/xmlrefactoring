package xmlrefactoring.applyChanges.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import xmlrefactoring.plugin.XMLRefactoringPlugin;
import xmlrefactoring.plugin.xslt.FileControl;

public class ApplyChanges2XMLAction implements IObjectActionDelegate{

	private IFile selectedSchema;

	public void run(IAction action) {
		VersionigWizard wizard = new VersionigWizard(selectedSchema);
		WizardDialog dialog = new WizardDialog
		(XMLRefactoringPlugin.getShell(),wizard);
		dialog.open();		
	}

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


}
