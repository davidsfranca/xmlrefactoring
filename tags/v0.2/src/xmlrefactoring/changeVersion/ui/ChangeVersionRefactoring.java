package xmlrefactoring.changeVersion.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.refactoring.VersioningRefactoring;
import xmlrefactoring.plugin.xslt.FileControl;

public class ChangeVersionRefactoring extends Refactoring {

	private int newVersion;
	private IFile schemaFile;
	
	public ChangeVersionRefactoring(IFile schemaFile){
		super();
		this.schemaFile = schemaFile;
	}

	@Override
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		// TODO Auto-generated method stub
		return new RefactoringStatus();
	}

	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		RefactoringStatus status = new RefactoringStatus();		
		if(!schemaFile.isSynchronized(IResource.DEPTH_ZERO))
			status.addFatalError(XMLRefactoringMessages.getString("ChangeVersionRefactoring.SchemaFileOutSync"));
		if(!FileControl.getDescriptorFile(schemaFile).isSynchronized(IResource.DEPTH_ZERO))
			status.addFatalError(XMLRefactoringMessages.getString("ChangeVersionRefactoring.DescriptorFileOutSync"));
		return status;
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		CompositeChange composite = new CompositeChange(XMLRefactoringMessages.getString("ChangeVersionRefactoring.ChangeName"));
		Change versionDirCreation = FileControl.createVersioningDir(schemaFile, newVersion);
		Change versionRefactoring = FileControl.createVersioningRefactoring(schemaFile, newVersion);
		Change descriptorUpdate = FileControl.updateDescriptor(schemaFile);
		composite.add(versionDirCreation);
		composite.add(descriptorUpdate);
		composite.add(versionRefactoring);
		return composite;
	}

	@Override
	public String getName() {
		return XMLRefactoringMessages.getString("ChangeVersionRefactoring.RefactoringName");
	}

	public void setNewVersion(int newVersion) {
		this.newVersion = newVersion;
	}

}
