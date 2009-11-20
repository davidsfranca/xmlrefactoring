package xmlrefactoring.plugin.logic.util;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.resource.DeleteResourceChange;
import org.eclipse.ltk.core.refactoring.resource.ResourceChange;

public class CreateFolderChange extends ResourceChange {

	private IFolder folder;
	private final String CREATE_FOLDER_CHANGE_NAME = "Create folder change";
	
	public CreateFolderChange(IPath path){
		folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(path);
	}
	
	@Override
	protected IResource getModifiedResource() {
		//Workaround: The validation demands that the resource`s change exists
		IResource resource = folder;
		while(!resource.exists())
			resource = resource.getParent();
		return resource;
	}

	@Override
	public String getName() {
		return CREATE_FOLDER_CHANGE_NAME;
	}

	@Override
	public Change perform(IProgressMonitor pm) throws CoreException {
		folder.create(true, false, pm);
		return new DeleteResourceChange(folder.getFullPath(), true);
	}

}
