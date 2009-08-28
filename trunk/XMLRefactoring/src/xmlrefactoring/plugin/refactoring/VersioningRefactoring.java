package xmlrefactoring.plugin.refactoring;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.velocity.VelocityContext;

public class VersioningRefactoring extends XMLRefactoring{

	/**
	 * The new schema version for the document
	 */
	private int versionNumber;

	private static String VERSIONTEMPLATE = "/template/versioning.vm";

	
	
	public VersioningRefactoring(List<List<QName>> paths) {
		super(paths);
		setVersionNumber(0);
	}
	
	public VersioningRefactoring(List<List<QName>> paths, int version) {
		super(paths);
		setVersionNumber(version);
	}
	
	@Override
	public void fillContext(VelocityContext context) {
		context.put("version",getVersionNumber());
		
	}

	@Override
	public String getTemplatePath() {
		return VERSIONTEMPLATE;
	}
	
	//GETTERS AND SETTERS
	public int getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}	

}
