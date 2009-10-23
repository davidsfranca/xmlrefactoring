package xmlrefactoring.plugin.logic.rename;

import java.util.List;

import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.plugin.logic.BaseRefactoringArguments;

public class RenameRefactoringArguments extends BaseRefactoringArguments {

	public RenameRefactoringArguments(List<XSDNamedComponent> components) {
		super(components);
	}

	private String newName;

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}
	
}
