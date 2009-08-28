package xmlrefactoring.plugin.refactoring;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.velocity.VelocityContext;

/**
 * Abstract class which represents a generic refactoring
 * All refactorings should extend this class
 * @author marcela
 *
 */
public abstract class XMLRefactoring {
	
	/**
	 * Paths where the changes will be applied
	 */
	private List<List<QName>> paths;

	/**
	 * Constructor 
	 * Transform the array into a unique path
	 */
	public XMLRefactoring(List<List<QName>> paths){
		this.paths = paths;
	}
	
	/**
	 * Fills each required variable with the correct value
	 * @param context
	 */
	public abstract void fillContext(VelocityContext context);

	public abstract String getTemplatePath();

	//GETTERS AND SETTERS
	public List<List<QName>> getPaths() {
		return paths;
	}

	public void setPaths(List<List<QName>> paths) {
		this.paths = paths;
	}

}
