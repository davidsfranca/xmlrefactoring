package xmlrefactoring.plugin.logic;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.ParticipantExtensionPoint;
import org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.ltk.core.refactoring.participants.SharableParticipants;

import xmlrefactoring.plugin.Activator;
import xmlrefactoring.plugin.PluginNamingConstants;

public abstract class BaseProcessor extends RefactoringProcessor{

	protected abstract Class getParticipantType();
	
	protected abstract BaseRefactoringArguments getRefactoringArguments();
	
	@Override
	public RefactoringParticipant[] loadParticipants(RefactoringStatus status,
			SharableParticipants sharedParticipants) throws CoreException {
		ParticipantExtensionPoint pep = new ParticipantExtensionPoint(PluginNamingConstants.pluginID,
				PluginNamingConstants.participantExtensionPointID, getParticipantType());
		//TODO Verificar argumentos
		String[] affectedNatures = null;
		SharableParticipants shared = null;
		return pep.getParticipants(new RefactoringStatus(), this, 
				getElements(), getRefactoringArguments(), null, affectedNatures, shared);
	}
	
}
