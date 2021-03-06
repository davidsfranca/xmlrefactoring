package xmlrefactoring.plugin.refactoring;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.velocity.VelocityContext;

public class RenameAttributeRefactoring extends XMLRefactoring{
	
	/**
	 * The actual attribute name
	 */
	private QName attrName;
	
	/**
	 * The new name for the attribute
	 */
	private String newName;
	
	private static String RENAMEATTRTEMPLATE = "/template/renameAttr.vm";
	

	protected RenameAttributeRefactoring(List<List<QName>> paths) {
		super(paths);
	}
	public  RenameAttributeRefactoring(List<List<QName>> paths, String newName, QName attr) {
		this(paths,newName,attr, true);
	}
	protected RenameAttributeRefactoring(List<List<QName>> paths, String newName, QName attr, boolean isRootRef) {
		super(paths);
		setNewName(newName);
		setAttrName(attr);
		if(isRootRef)
			createReverseRefactoring();
		setRootRef(isRootRef);
	}

	@Override
	public void createReverseRefactoring() {
		String newName = getAttrName().getLocalPart();
		QName attrName = new QName(getAttrName().getNamespaceURI(),getNewName());
		setReverseRefactoring(new RenameAttributeRefactoring(getPaths(),newName,attrName,false));
	}

	@Override
	public void fillContext(VelocityContext context) {
		context.put("newName",getNewName());
		context.put("attrName",getAttrName());
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
		return RENAMEATTRTEMPLATE;
	}

	public QName getAttrName() {
		return attrName;
	}

	public void setAttrName(QName attrName) {
		this.attrName = attrName;
	}
	
}

