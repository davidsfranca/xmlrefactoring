package xmlrefactoring.plugin.ui;

import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;

import xmlrefactoring.plugin.logic.BaseProcessor;

public abstract class BaseWizard<T extends BaseProcessor> extends RefactoringWizard{

	private T processor;
	
	public BaseWizard(T processor, int flags) { 
		super(new ProcessorBasedRefactoring(processor), flags);		
		this.processor = processor;
	}
	
	protected T getProcessor(){
		return processor;
	}
	

}
