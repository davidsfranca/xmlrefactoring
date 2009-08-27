package xmlrefactoring.plugin.refactoring;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.velocity.VelocityContext;

public class RenameElementRefactoring extends Refactoring{
	
	/**
	 * The new name for the element
	 */
	private String newName;
	
	private static String RENAMEELEMTEMPLATE = "/template/renameTag.vm";
	

	public RenameElementRefactoring(List<List<QName>> paths) {
		super(paths);
	}
	
	public RenameElementRefactoring(List<List<QName>> paths, String newName) {
		super(paths);
		setNewName(newName);
	}


	@Override
	//TODO Mock - alterar quando for mudada a versao do velocity
	public void fillContext(VelocityContext context) {
		context.put("newName",getNewName());
		context.put("paths", getPaths());
	}
	
	
	//Getters and Setters
	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	@Override
	public String getTemplatePath() {
		return RENAMEELEMTEMPLATE;
	}
	

}
