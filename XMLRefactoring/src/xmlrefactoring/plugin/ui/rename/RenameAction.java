package xmlrefactoring.plugin.ui.rename;

import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xsd.ui.internal.refactor.RefactoringComponent;
import org.eclipse.wst.xsd.ui.internal.refactor.XMLRefactoringComponent;
import org.eclipse.wst.xsd.ui.internal.refactor.rename.RenameComponentProcessor;

import xmlrefactoring.plugin.ui.SingleInputAction;

public class RenameAction extends SingleInputAction{

	@Override
	protected RefactoringWizard getWizard(){
		RefactoringComponent component = new XMLRefactoringComponent(
				getSelectedComponent(),
				(IDOMElement)getSelectedComponent().getElement(), 
				getSelectedComponent().getName(),
				getSelectedComponent().getTargetNamespace());
		RenameComponentProcessor renameProcessor = new RenameComponentProcessor(component);
		RenameRefactoring refactoring = new RenameRefactoring(renameProcessor);
		
		return new RenameWizard(refactoring);
	}
	
}
