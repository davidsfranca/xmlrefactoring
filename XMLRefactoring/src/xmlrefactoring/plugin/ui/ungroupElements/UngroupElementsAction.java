package xmlrefactoring.plugin.ui.ungroupElements;

import xmlrefactoring.plugin.logic.ungroupElements.UngroupElementsProcessor;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;
import xmlrefactoring.plugin.ui.SingleInputAction;

public class UngroupElementsAction extends SingleInputAction {
	@Override
	protected BaseRefactoringWizard<UngroupElementsProcessor> getWizard(){
		UngroupElementsProcessor processor = new UngroupElementsProcessor(getSelectedComponent());
		return new UngroupElementsWizard(processor);
	}
}
