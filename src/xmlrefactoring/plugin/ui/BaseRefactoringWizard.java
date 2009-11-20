package xmlrefactoring.plugin.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import xmlrefactoring.plugin.XMLRefactoringPlugin;
import xmlrefactoring.plugin.logic.BaseProcessor;

public abstract class BaseRefactoringWizard<T extends BaseProcessor> extends RefactoringWizard{

	private T processor;
	
	public BaseRefactoringWizard(T processor, int flags) { 
		super(new ProcessorBasedRefactoring(processor), flags);		
		this.processor = processor;
		//TODO Trocar ’cone
		ImageDescriptor defaultImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(
				XMLRefactoringPlugin.PLUGIN_ID, "icons/applychanges.png");
		setDefaultPageImageDescriptor(defaultImageDescriptor);
	}
	
	public T getProcessor(){
		return processor;
	}
	

}
