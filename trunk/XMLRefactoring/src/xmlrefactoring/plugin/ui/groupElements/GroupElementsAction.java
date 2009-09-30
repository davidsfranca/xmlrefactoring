package xmlrefactoring.plugin.ui.groupElements;

import org.eclipse.ltk.ui.refactoring.RefactoringWizard;

import xmlrefactoring.plugin.logic.groupElements.GroupElementsProcessor;
import xmlrefactoring.plugin.ui.BaseAction;
import xmlrefactoring.plugin.ui.MultipleInputAction;

public class GroupElementsAction extends MultipleInputAction {

	@Override
	protected RefactoringWizard getWizard() {
		GroupElementsProcessor processor = new GroupElementsProcessor(getSelectedComponents());
		return new GroupElementsWizard(processor);
	}

}
