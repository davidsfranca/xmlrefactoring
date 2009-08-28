package xmlrefactoring.plugin.xslt;

import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.xml.sax.SAXException;

import xmlrefactoring.plugin.logic.rename.CreateFileChange;
import xmlrefactoring.plugin.refactoring.XMLRefactoring;

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
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public static CreateFileChange createXSL(XMLRefactoring refactoring, IFile schema, IPath nextFilePath ) throws ParserConfigurationException, SAXException, IOException{
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
			
		CreateFileChange change = new CreateFileChange(nextFilePath, context, template);
		
		return change;
		
	}
	
	public static CreateFileChange createXSL(XMLRefactoring refactoring, IFile schema) throws ParserConfigurationException, SAXException, IOException{

		IPath newFilePath = FileControl.getNextPath(schema);
		return createXSL(refactoring,schema,newFilePath);
	}

	
}
