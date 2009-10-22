package xmlrefactoring.plugin.refactoring;



import java.util.*;

import javax.xml.namespace.QName;

import org.apache.velocity.VelocityContext;

public class RenameElementRefactoring extends XMLRefactoring{
	
	/**
	 * The new name for the element
	 */
	private String newName;
	private static String RENAMEELEMTEMPLATE = "/template/renameTag.vm";

	public RenameElementRefactoring(List<List<QName>> paths, String newName) {
		this(paths,newName,true);
	}
	protected RenameElementRefactoring(List<List<QName>> paths, String newName, boolean isRootRef) {
		super(paths);
		setNewName(newName);
		if(isRootRef)
			createReverseRefactoring();
		setRootRef(isRootRef);
	}

	@Override
	public void createReverseRefactoring() {
		List<List<QName>> newPaths =  new ArrayList();
		String newName = getPaths().get(0).get(getPaths().get(0).size()-1).getLocalPart();
		
		for(int i= 0; i<getPaths().size();i++){
			
			List<QName> path = new ArrayList<QName>();
			
			List<QName> oldPath = getPaths().get(i);
			int lastIndex = oldPath.size()-1;
			for(int j = 0; j<lastIndex;j++)
				path.add(new QName(oldPath.get(j).getNamespaceURI(),oldPath.get(j).getLocalPart()));
			
			//QName elementName = newPaths.get(i).get(lastIndex);
			path.add(new QName(oldPath.get(lastIndex).getNamespaceURI(),getNewName()));
			newPaths.add(path);
		}
		setReverseRefactoring(new RenameElementRefactoring(newPaths,newName,false));
	}

	@Override
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
