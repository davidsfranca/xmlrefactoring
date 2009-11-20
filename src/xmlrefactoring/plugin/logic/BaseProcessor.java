package xmlrefactoring.plugin.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.ParticipantExtensionPoint;
import org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.ltk.core.refactoring.participants.SharableParticipants;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDParser;

import xmlrefactoring.XMLRefactoringMessages;

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
		ParticipantExtensionPoint pep = new ParticipantExtensionPoint(XMLRefactoringMessages.getString("pluginID"),
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
	
	/**
	 * Checks the workspace consistency
	 */
	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm)
		throws CoreException, OperationCanceledException {
		RefactoringStatus status = new RefactoringStatus();
		IFile schemaFile = getRefactoringArguments().getSchemaFile();
		if(!schemaFile.isSynchronized(IResource.DEPTH_ZERO))
			status.addFatalError(XMLRefactoringMessages.getString("BaseProcessor.SchemaFileOutSync"));
		return status;
	}
	
	@Override
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm, CheckConditionsContext context)
		throws CoreException, OperationCanceledException {
		RefactoringStatus status = new RefactoringStatus();	
		return status;
	}
	
}
