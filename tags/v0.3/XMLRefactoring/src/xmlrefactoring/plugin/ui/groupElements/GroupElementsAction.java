package xmlrefactoring.plugin.ui.groupElements;


import xmlrefactoring.plugin.logic.groupElements.GroupElementsProcessor;
import xmlrefactoring.plugin.ui.BaseAction;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;
import xmlrefactoring.plugin.ui.MultipleInputAction;

public class GroupElementsAction extends MultipleInputAction {

	@Override
	protected BaseRefactoringWizard<GroupElementsProcessor> getWizard() {
		GroupElementsProcessor processor = new GroupElementsProcessor(getSelectedComponents());
		return new GroupElementsWizard(processor);
	}

}
