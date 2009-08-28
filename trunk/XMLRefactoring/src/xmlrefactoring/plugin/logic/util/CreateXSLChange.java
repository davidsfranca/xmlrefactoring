package xmlrefactoring.plugin.logic.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.resource.DeleteResourceChange;

import xmlrefactoring.plugin.refactoring.XMLRefactoring;
import xmlrefactoring.plugin.xslt.FileControl;

public class CreateXSLChange extends Change{

	private XMLRefactoring refactoring;
	private IFile xslFile;

	public CreateXSLChange(XMLRefactoring refactoring,	IPath nextFilePath) {
		this.refactoring = refactoring;
		xslFile = ResourcesPlugin.getWorkspace().getRoot().getFile(nextFilePath);
	}

	@Override
	public String getName() {
		return "Create File Change";
	}

	@Override
	public Change perform(IProgressMonitor pm) throws CoreException {
		try{
			//Configures the Velocity
			Properties p = new Properties();
			p.setProperty( "resource.loader", "class" );
			p.setProperty( "class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
			try {
				Velocity.init(p);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Velocity problems");
			}
			
			VelocityContext context = new VelocityContext();
			refactoring.fillContext(context);
			
			Template template = null;
			try {
				template = Velocity.getTemplate(refactoring.getTemplatePath());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FileWriter writer = new FileWriter(xslFile.getLocation().toOSString());
			template.merge(context, writer);
		    writer.flush();
		    writer.close();
		    FileControl.addToControl(xslFile);
		    
		}catch (IOException e) {
			// TODO: handle exception
		}
		return new DeleteResourceChange(xslFile.getFullPath(),true);
	}

	@Override
	public Object getModifiedElement() {
		return xslFile;
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
