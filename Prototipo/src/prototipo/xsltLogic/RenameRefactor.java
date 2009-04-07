package prototipo.xsltLogic;

public class RenameRefactor {
	
	private String path;
	private String newName;
	
	public RenameRefactor (String path, String newName){
		this.path = path;
		this.newName = newName;
	}
	
	public String getPath() {
		return path;
	}

	public String getNewName() {
		return newName;
	}


}
