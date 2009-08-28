package xmlrefactoring.plugin.logic.util;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.resource.DeleteResourceChange;
import org.eclipse.ltk.core.refactoring.resource.ResourceChange;

public class CreateFileChange extends ResourceChange {

	private IFile file;
	private final String CREATE_FILE_CHANGE_NAME = "Create file change";
	
	public CreateFileChange(IPath path){
		ResourcesPlugin.getWorkspace().getRoot().getFile(path);
	}
		
	@Override
	protected IResource getModifiedResource() {
		return file;
	}

	@Override
	public String getName() {
		return CREATE_FILE_CHANGE_NAME;
	}

	@Override
	public Change perform(IProgressMonitor pm) throws CoreException {
		file.create(new ByteArrayInputStream(new byte[0]), true, pm);
		return new DeleteResourceChange(file.getFullPath(), true);
	}

}
