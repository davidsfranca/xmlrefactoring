package xmlrefactoring.plugin.refactoring;

import java.util.ArrayList;
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
	 * XMLRefactoring that undo this refactoring
	 */
	private XMLRefactoring reverseRefactoring;
	
	/**
	 * Defines if the refactoring has a reverse or not
	 */
	private boolean isRootRef;

	/**
	 * Constructor 
	 * Transform the array into a unique path
	 */
	protected XMLRefactoring(List<List<QName>> paths){
		this(paths, true);
	}
	
	/**
	 * Constructor 
	 * Transform the array into a unique path
	 */
	protected XMLRefactoring(List<List<QName>> paths, boolean isRoot){
		if(paths==null)
			paths = new ArrayList();
		this.paths = paths;
		setRootRef(isRootRef);
	}
	
	/**
	 * Fills each required variable with the correct value
	 * @param context
	 */
	public abstract void fillContext(VelocityContext context);

	public abstract String getTemplatePath();
	
	public abstract void createReverseRefactoring();

	//GETTERS AND SETTERS
	public List<List<QName>> getPaths() {
		return paths;
	}

	public void setPaths(List<List<QName>> paths) {
		this.paths = paths;
	}
	
	public XMLRefactoring getReverseRefactoring(){
		return reverseRefactoring;
	}

	public void setReverseRefactoring(XMLRefactoring reverseRefactoring) {
		this.reverseRefactoring = reverseRefactoring;
	}

	public boolean isRootRef() {
		return isRootRef;
	}

	public void setRootRef(boolean isRootRef) {
		this.isRootRef = isRootRef;
	}

}
