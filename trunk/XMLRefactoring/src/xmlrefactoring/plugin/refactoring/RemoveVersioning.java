package xmlrefactoring.plugin.refactoring;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.velocity.VelocityContext;

public class RemoveVersioning extends XMLRefactoring{
	
	private static String REMOVEVERSIONTEMPLATE = "/template/removeVersioning.vm";


	public RemoveVersioning(List<List<QName>> paths) {
		super(paths);
		setRootRef(false);
	}
	
	
	@Override
	/**
	 * This refactoring is only used to undo the addition of the attribute schemaVersion 
	 * when the file is put under version control. It doesn't have a reverserefactoring.
	 */
	public void createReverseRefactoring() {
	}	
	
	@Override
	public void fillContext(VelocityContext context) {
		
	}

	@Override
	public String getTemplatePath() {
		return REMOVEVERSIONTEMPLATE;
	}
	

}
