package prototipo.plugin;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xsd.ui.internal.editor.ISelectionMapper;
import org.eclipse.wst.xsd.ui.internal.editor.XSDEditorPlugin;
import org.eclipse.wst.xsd.ui.internal.editor.XSDSelectionMapper;
import org.eclipse.wst.xsd.ui.internal.refactor.RefactoringComponent;
import org.eclipse.wst.xsd.ui.internal.refactor.XMLRefactoringComponent;
import org.eclipse.xsd.XSDNamedComponent;

/**
 * Classe responsavel por executar a ação de renomear
 * @author guilherme
 *
 */
public class RenameAction implements IEditorActionDelegate {
	
	private XSDNamedComponent selectedComponent;	
	
	public RenameAction(){
		super();
	}

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		// TODO Auto-generated method stub
		
	}

	public void run(IAction action) {			
		try {
			RefactoringComponent component = new XMLRefactoringComponent(
					selectedComponent,
					(IDOMElement)selectedComponent.getElement(), 
					selectedComponent.getName(),
					selectedComponent.getTargetNamespace());
			RenameRefactoring refactoring = new RenameRefactoring(component);
			RefactoringWizardOpenOperation operation = new RefactoringWizardOpenOperation(new RenameWizard(refactoring));
			operation.run(XSDEditorPlugin.getShell(), "Refatoracao Teste Title");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	public void selectionChanged(IAction action, ISelection selection) {
		ISelectionMapper mapper = new XSDSelectionMapper();
		StructuredSelection stselection = (StructuredSelection) mapper.mapSelection(selection);
		selectedComponent = (XSDNamedComponent) stselection.getFirstElement();
	}

}
