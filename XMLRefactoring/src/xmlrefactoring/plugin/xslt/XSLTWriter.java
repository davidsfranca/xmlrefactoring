package xmlrefactoring.plugin.xslt;

import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;

import xmlrefactoring.plugin.logic.rename.CreateFileChange;
import xmlrefactoring.plugin.refactoring.Refactoring;

/**
 * Class responsible for create the xsl file
 * @author marcela
 *
 */
public class XSLTWriter {
	
	/**
	 * Creates the chane for creating the files
	 * @param refactoring
	 * @param schema
	 */
	public static CreateFileChange createXSL(Refactoring refactoring, IFile schema){
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
		
		IPath newFilePath = FileControl.getNextPath(schema);
		
		CreateFileChange change = new CreateFileChange(newFilePath, context, template);
		
		return change;
		
	}

	
}
