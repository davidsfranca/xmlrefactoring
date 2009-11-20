package xmlrefactoring.plugin.logic.rename;

import java.util.List;

import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Element;

import xmlrefactoring.plugin.logic.BaseRefactoringArguments;
import xmlrefactoring.plugin.logic.util.XSDUtil;

public class RenameRefactoringArguments extends BaseRefactoringArguments {

	public RenameRefactoringArguments(List<XSDNamedComponent> components) {
		super(components);
		Element renamingComponent = getElements().get(0);
		boolean element = XSDUtil.isElement(renamingComponent);
		this.element = element;
		this.attribute = !element;
	}

	private String newName;
	private boolean element;
	private boolean attribute;
	
	public boolean isElement(){
		return element;
	}

	public boolean isAttribute(){
		return attribute;
	}
	
	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}
	
}
