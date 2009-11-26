package xmlrefactoring.plugin.logic.groupElements.external;

import java.util.List;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.plugin.logic.BaseRefactoringArguments;

public class GroupElementsRefactoringArguments extends BaseRefactoringArguments {

	public GroupElementsRefactoringArguments(List<XSDNamedComponent> components) {
		super(components);
	}

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
