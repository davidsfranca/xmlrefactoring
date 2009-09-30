package xmlrefactoring.plugin.logic.groupElements;

import xmlrefactoring.plugin.logic.MultipleInputRefactoringArguments;
import xmlrefactoring.plugin.logic.SingleInputRefactoringArguments;

public class GroupElementsRefactoringArguments extends MultipleInputRefactoringArguments {

	private String groupName;
	private String typeName;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
		this.typeName = groupName.concat("Type");
	}
	
	public String getTypeName(){
		return typeName;
	}
	
}
