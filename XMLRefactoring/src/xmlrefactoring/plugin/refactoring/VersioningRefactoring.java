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

	
	
	protected VersioningRefactoring(List<List<QName>> paths) {
		super(paths);
		setVersionNumber(0);
		createReverseRefactoring();
	}
	
	public VersioningRefactoring(int version) {
		this(version, true);
	}
	
	protected VersioningRefactoring(int version, boolean isRootRef) {
		super(null);
		setVersionNumber(version);
		if(isRootRef)
			createReverseRefactoring();
		setRootRef(isRootRef);
	}
	
	@Override
	public void createReverseRefactoring() {
		if(versionNumber>1)
			setReverseRefactoring(new VersioningRefactoring(versionNumber-1, false));
		else
			setReverseRefactoring(new RemoveVersioning());
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
