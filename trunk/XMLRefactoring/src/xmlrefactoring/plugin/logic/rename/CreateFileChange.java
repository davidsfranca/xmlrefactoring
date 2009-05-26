package xmlrefactoring.plugin.logic.rename;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.resource.DeleteResourceChange;

import xmlrefactoring.plugin.xslt.FileControl;

public class CreateFileChange extends Change{

	private IFile file;
	private VelocityContext context;
	private Template template;
	

	public CreateFileChange(IPath path, VelocityContext context, Template template){
		this.file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		setTempate(template);
		setContext(context);		
	}

	@Override
	public String getName() {
		return "Create File Change";
	}

	@Override
	public Change perform(IProgressMonitor pm) throws CoreException {
		try{
			
			FileWriter writer = new FileWriter(file.getLocation().toOSString());
			template.merge(context, writer);
		    writer.flush();
		    writer.close();
		    FileControl.addToControl(file);
		    
		}catch (IOException e) {
			// TODO: handle exception
		}
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
	
	//GETTERS AND SETTERS
	public VelocityContext getContext() {
		return context;
	}

	public void setContext(VelocityContext context) {
		this.context = context;
	}

	public Template getTempate() {
		return template;
	}

	public void setTempate(Template tempate) {
		this.template = tempate;
	}

}
