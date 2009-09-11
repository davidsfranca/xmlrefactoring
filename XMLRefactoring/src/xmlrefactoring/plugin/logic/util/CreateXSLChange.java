package xmlrefactoring.plugin.logic.util;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.resource.DeleteResourceChange;
import org.eclipse.ltk.core.refactoring.resource.ResourceChange;

import xmlrefactoring.plugin.refactoring.XMLRefactoring;
import xmlrefactoring.plugin.xslt.FileControl;

public class CreateXSLChange extends ResourceChange{

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
			
			xslFile.create(new ByteArrayInputStream("".getBytes()), true, pm);
			FileWriter writer = new FileWriter(xslFile.getLocation().toFile());
			template.merge(context, writer);
		    writer.flush();
		    writer.close();
		    
		}catch (IOException e) {
			e.printStackTrace();
			e.printStackTrace();
		}
		return new DeleteResourceChange(xslFile.getFullPath(),true);
	}

	@Override
	protected IResource getModifiedResource() {
		//Workaround: The validation demands that the resource`s change exists
		IResource resource = xslFile;
		while(!resource.exists())
			resource = resource.getParent();
		return resource;
	}

}
