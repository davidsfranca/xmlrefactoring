package xmlrefactoring.plugin.ui.ungroupElements;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDTypeDefinition;

import xmlrefactoring.plugin.logic.ungroupElements.UngroupElementsProcessor;
import xmlrefactoring.plugin.logic.util.XSDUtil;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;
import xmlrefactoring.plugin.ui.SingleInputAction;

public class UngroupElementsAction extends SingleInputAction {
	@Override
	protected BaseRefactoringWizard<UngroupElementsProcessor> getWizard(){
		UngroupElementsProcessor processor = new UngroupElementsProcessor(getSelectedComponent());
		return new UngroupElementsWizard(processor);
	}
	
	/* 
	 * Verifies if the selected component is Element Declaration
	 * Doesn't care if it's simple or complex
	 */
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		super.selectionChanged(action, selection);
		if(!(getSelectedComponent() instanceof XSDElementDeclaration))
			action.setEnabled(false);
	}
}
