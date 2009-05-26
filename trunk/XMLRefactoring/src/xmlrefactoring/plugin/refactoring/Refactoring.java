package xmlrefactoring.plugin.refactoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.runtime.directive.Foreach;

/**
 * Abstract class which represents a generic refactoring
 * All refactorings should extend this class
 * @author marcela
 *
 */
public abstract class Refactoring {
	
	/**
	 * Paths where the changes will be applied
	 */
	private List<QName[]> paths;

	/**
	 * Constructor 
	 * Transform the array into a unique path
	 */
	
	//TODO: Modificar para receber Lista de QName[]
	//Metodo MOCK - considera que todos os paths tem apenas um qName
	public Refactoring(List<String> paths){
		setPaths(new ArrayList<QName[]>());

		for(String path: paths){
			int i = path.lastIndexOf(":");
			QName[] qname = new QName[1];
			if(i>0){
				qname[0] = new QName(path.substring(0, i),path.substring(i+1));
			}else{
				qname[0] = new QName(path);
			}
			getPaths().add(qname);
		}
	}
	
	/**
	 * Fills each required variable with the correct value
	 * @param context
	 */
	public abstract void fillContext(VelocityContext context);

	public abstract String getTemplatePath();

	//GETTERS AND SETTERS
	public List<QName[]> getPaths() {
		return paths;
	}

	public void setPaths(List<QName[]> paths) {
		this.paths = paths;
	}
	
	//Getters and Setters
	
	
	
	
	

}
