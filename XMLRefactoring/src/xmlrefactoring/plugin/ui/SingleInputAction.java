package xmlrefactoring.plugin.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.xsd.ui.internal.editor.ISelectionMapper;
import org.eclipse.wst.xsd.ui.internal.editor.XSDSelectionMapper;
import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.plugin.XMLRefactoringPlugin;

public abstract class SingleInputAction extends BaseAction implements IEditorActionDelegate {

	private XSDNamedComponent selectedComponent;
	
	public void selectionChanged(IAction action, ISelection selection) {
		ISelectionMapper mapper = new XSDSelectionMapper();
		StructuredSelection stselection = (StructuredSelection) mapper.mapSelection(selection);
		if(stselection.getFirstElement() instanceof XSDNamedComponent){
			selectedComponent = (XSDNamedComponent) stselection.getFirstElement();
			action.setEnabled(true);
		}
		else
			action.setEnabled(false);
	}
	
	protected XSDNamedComponent getSelectedComponent(){
		return selectedComponent;
	}

}
