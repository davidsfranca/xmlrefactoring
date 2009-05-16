package xmlrefactoring.plugin.refactoring;

import java.util.List;

import org.apache.velocity.VelocityContext;

/**
 * Abstract class which represents a generic refactoring
 * All refactorings should extend this class
 * @author marcela
 *
 */
public abstract class Refactoring {
	
	/**
	 * Path where the changes will be applied
	 */
	private String path ;
	
	/**
	 * Constructor 
	 * Transform the array into a unique path
	 */
	public Refactoring(List<String> paths){
		
		StringBuffer auxiliarBuffer = new StringBuffer(paths.get(0));
		paths.remove(0);
		
		//Concatena cada pat individual em um œnico xPath
		for(String singlePath:paths){
			auxiliarBuffer.append("|");
			auxiliarBuffer.append(singlePath);
		}
		
		this.path = auxiliarBuffer.toString();
		
	}
	
	/**
	 * Fills each required variable with the correct value
	 * @param context
	 */
	public abstract void fillContext(VelocityContext context);

	
	//Getters and Setters
	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}
	
	public abstract String getTemplatePath();

	
	
	
	

}
