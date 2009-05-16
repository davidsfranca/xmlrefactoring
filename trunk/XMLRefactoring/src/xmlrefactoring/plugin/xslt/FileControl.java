package xmlrefactoring.plugin.xslt;

import java.util.Random;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.sse.internal.contentproperties.ISubject;

public class FileControl {
	
	public static IPath getNextPath(IFile schemaFile){

		Random r = new Random();
		
		//Workaround
		//IContainer refactoringFolder = schemaFile.getParent().getFolder(new Path("/refactoring"+schemaFile.getName().substring(0,schemaFile.getName().length()-4)));
		IContainer refactoringFolder = schemaFile.getParent();
		String fileName = "/refactoring"+r.nextInt()+".xsl";
		return refactoringFolder.getFullPath().append(fileName);
		
	}

	public void addToControl(IFile xslFile){
		
	}
	
	private static boolean isUnderControl(IFile schemaFile){
		
		IContainer folder = schemaFile.getParent();
		
		//Path to the folder where should be the xsl files
		
		//Considera que se para estar sobre controle basta j‡ ter este 
		//TODO: adequar ao verdadeiro controle de versao
		return folder.exists(new Path("/refactoring"+schemaFile.getName()));
	}
}
