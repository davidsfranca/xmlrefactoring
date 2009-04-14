package prototipo.xsltLogic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;

public class XSLTWriter {
	
	public static String RENAMETEMPLATE = "/templates/renameTag.vm";
	public static String MAINTEMPLATE = "/templates/main.vm";
	
	private static String createRenameFile(RenameRefactor rename, IContainer folder) throws ResourceNotFoundException, ParseErrorException, Exception{	

		VelocityContext context = new VelocityContext();
		context.put("rename", rename);
		
		Properties p = new Properties();
		p.setProperty( "resource.loader", "class" );
		p.setProperty( "class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
		Velocity.init(p);
		Template template = Velocity.getTemplate(RENAMETEMPLATE);
		
		//Cria um nome para o arquivo
		//Precisa de uma logica mais robusta de renomeacao
		String fileName = "rename"+ rename.getPath().replace("/", "")+"to"+rename.getNewName()+".xsl";
		IFile file = folder.getFile(new Path("/"+fileName));
		
		FileWriter writer = new FileWriter(file.getLocation().toFile());
//		BufferedWriter writer =
//		      new BufferedWriter(new FileWriter("/saida/"+fileName));
		template.merge(context, writer);
	    writer.flush();
	    writer.close();
		
		return fileName;
		
	}
	
	public static void createTransformation(ArrayList<RenameRefactor> refactor, IContainer folder) throws ResourceNotFoundException, ParseErrorException, Exception{
		
		ArrayList refactorFiles = new ArrayList<String>();
		
		for (int i = 0; i < refactor.size(); i++) {
			String file = createRenameFile(refactor.get(i), folder);
			refactorFiles.add(file);
		}
		
		VelocityContext context = new VelocityContext();
		context.put("refactors", refactorFiles);
		Template template = Velocity.getTemplate(MAINTEMPLATE);
		IFile file = folder.getFile(new Path("/mainRefactor.xsl"));
		 BufferedWriter writer =
		      new BufferedWriter(new FileWriter(file.getLocation().toFile()));

		    template.merge(context, writer);
		    writer.flush();
		    writer.close();
	}
	
	
}
