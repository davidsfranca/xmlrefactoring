package xmlrefactoring.plugin.logic.util;

import java.io.InputStream;

import junit.framework.Assert;

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
	private InputStream content;
	private final String CREATE_FILE_CHANGE_NAME = "Create file change";
	
	public CreateFileChange(IPath path, InputStream content){
		Assert.assertNotNull(content);
		this.file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);		
		this.content = content;
	}
		
	@Override
	protected IResource getModifiedResource() {
		//Workaround: The validation demands that the resource`s change exists
		IResource resource = file;
		while(!resource.exists())
			resource = resource.getParent();
		return resource;
	}

	@Override
	public String getName() {
		return CREATE_FILE_CHANGE_NAME;
	}

	@Override
	public Change perform(IProgressMonitor pm) throws CoreException {
		file.create(content, true, pm);
		return new DeleteResourceChange(file.getFullPath(), true);
	}

}
