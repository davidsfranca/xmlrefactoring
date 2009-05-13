package xmlrefactoring.plugin.logic.rename;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.resource.DeleteResourceChange;

public class CreateFileChange extends Change{

	private IFile file;
	
	public CreateFileChange(IPath path){
		this.file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
	}

	@Override
	public String getName() {
		return "Create File Change";
	}

	@Override
	public Change perform(IProgressMonitor pm) throws CoreException {		
		file.create(new ByteArrayInputStream(new byte[0]), true, null);
		return new DeleteResourceChange(file.getFullPath(),true);
	}

	@Override
	public Object getModifiedElement() {
		return file;
	}

	@Override
	public void initializeValidationData(IProgressMonitor pm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RefactoringStatus isValid(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		return new RefactoringStatus();
	}

}
