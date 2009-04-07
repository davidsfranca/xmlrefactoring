package prototipo.xsltLogic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class XSLTWriter {
	
	public static String RENAMETEMPLATE = "templates/renameTag.vm";
	public static String MAINTEMPLATE = "templates/main.vm";
	
	private static String createRenameFile(RenameRefactor rename) throws ResourceNotFoundException, ParseErrorException, Exception{	

		VelocityContext context = new VelocityContext();
		context.put("rename", rename);
		
		Template template = Velocity.getTemplate(RENAMETEMPLATE);
		
		//Cria um nome para o arquivo
		//Precisa de uma logica mais robusta de renomeacao
		String fileName = "rename"+ rename.getPath().replace("/", "_")+"to"+rename.getNewName()+".xsl";
		
		BufferedWriter writer =
		      new BufferedWriter(new FileWriter("saida/"+fileName));
		
		template.merge(context, writer);
	    writer.flush();
	    writer.close();
		
		return fileName;
		
	}
	
	public static void createTransformation(ArrayList<RenameRefactor> refactor) throws ResourceNotFoundException, ParseErrorException, Exception{
		
		ArrayList refactorFiles = new ArrayList<String>();
		
		for (int i = 0; i < refactor.size(); i++) {
			String file = createRenameFile(refactor.get(i));
			refactorFiles.add(file);
		}
		
		VelocityContext context = new VelocityContext();
		context.put("refactors", refactorFiles);
		Template template = Velocity.getTemplate(MAINTEMPLATE);

		    BufferedWriter writer =
		      new BufferedWriter(new FileWriter("saida/mainRefactor.xsl"));

		    template.merge(context, writer);
		    writer.flush();
		    writer.close();
	}
	
}
