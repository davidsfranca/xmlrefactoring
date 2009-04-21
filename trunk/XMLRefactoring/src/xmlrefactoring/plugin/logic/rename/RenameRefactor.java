package xmlrefactoring.plugin.logic.rename;

import java.util.List;

import org.eclipse.core.resources.IWorkspace;

public class RenameRefactor {
	
	private String path;
	private String newName;
	
	public RenameRefactor (String path, String newName){
		this.path = path;
		this.newName = newName;
	}
	
	public RenameRefactor (List<String> paths, String newName){
		
		StringBuffer auxiliarBuffer = new StringBuffer(paths.get(0));
		paths.remove(0);
		
		//Concatena cada pat individual em um œnico xPath
		for(String singlePath:paths){
			auxiliarBuffer.append("|");
			auxiliarBuffer.append(path);
		}
		
		this.path = auxiliarBuffer.toString();
		this.newName = newName;
	}
	
	public String getPath() {
		return path;
	}

	public String getNewName() {
		return newName;
	}


}
