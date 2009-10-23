package xmlrefactoring.plugin.logic;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.ParticipantExtensionPoint;
import org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.ltk.core.refactoring.participants.SharableParticipants;

import xmlrefactoring.plugin.PluginNamingConstants;

public abstract class BaseProcessor extends RefactoringProcessor{
	
	protected abstract BaseRefactoringArguments getRefactoringArguments();
	
	protected abstract String getParticipantExtensionPoint();
	
	/**
	 * The subclasses will have only one element that is target of the refactoring
	 * @return
	 */
	protected abstract Object getElement();
	
	@Override
	public RefactoringParticipant[] loadParticipants(RefactoringStatus status,
			SharableParticipants sharedParticipants) throws CoreException {
		ParticipantExtensionPoint pep = new ParticipantExtensionPoint(PluginNamingConstants.pluginID,
				getParticipantExtensionPoint(), BaseParticipant.class);
		
		return pep.getParticipants(new RefactoringStatus(), this, 
				getElement(), getRefactoringArguments(), null, getAffectedNatures(), sharedParticipants);
		
	}

	/**
	 * The processors will deal with only one element. This implementation forces the subclasses to implement
	 * a method that returns this element.
	 */
	@Override
	public Object[] getElements(){
		return new Object[]{getElement()};
	}
	
	/**
	 * The default processor doesn`t create any change
	 */
	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException{
		return null;
	}
	
	

	private String[] getAffectedNatures() {
		//TODO Verificar natureza dos projetos que ser‹o afetados
		return new String[] {"org.eclipse.jdt.core.javanature"};
	}
	
	
	
}
