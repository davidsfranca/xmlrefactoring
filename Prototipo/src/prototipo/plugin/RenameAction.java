package prototipo.plugin;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.xsd.ui.internal.editor.XSDEditorPlugin;

/**
 * Classe responsavel por executar a ação de renomear
 * @author guilherme
 *
 */
public class RenameAction implements IEditorActionDelegate {
	
	public RenameAction(){
		super();
	}

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		// TODO Auto-generated method stub
		
	}

	public void run(IAction action) {			
		try {
			RefactoringWizardOpenOperation operation = new RefactoringWizardOpenOperation(new RenameWizard(new RenameRefactoring()));
			operation.run(XSDEditorPlugin.getShell(), "Refatoracao Teste Title");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		
	}

}
