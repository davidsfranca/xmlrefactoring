package xmlrefactoring.plugin.refactoring;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.velocity.VelocityContext;

public class GroupElementsRefactoring extends XMLRefactoring{
	
	/**
	 * All elements that will be grouped
	 */
	private List<QName> inGroup;
	
	/**
	 * The tag name that will involve 
	 */
	private QName groupName;
	
	private static String GROUPELEMENTSTEMPLATE = "/template/groupElement.vm";
	

	public GroupElementsRefactoring(List<List<QName>> paths) {
		super(paths);
	}
	
	public GroupElementsRefactoring(List<List<QName>> paths, QName groupName, List<QName> elementsGroup, boolean isRootRef) {
		super(paths);
		setGroupName(groupName);
		setInGroup(inGroup);
		if(isRootRef)
			createReverseRefactoring();
		setRootRef(isRootRef);
	}

	@Override
	public void createReverseRefactoring() {
		List<List<QName>> newPaths =  new ArrayList();
		for(int i= 0; i<getPaths().size();i++){
			List<QName> path = new ArrayList<QName>();
			List<QName> oldPath = getPaths().get(i);
			int lastIndex = oldPath.size()-1;
			for(int j = 0; j<lastIndex;j++)
				path.add(new QName(oldPath.get(j).getNamespaceURI(),oldPath.get(j).getLocalPart()));
			path.add(new QName(getGroupName().getNamespaceURI(),getGroupName().getLocalPart()));
			newPaths.add(path);
		}
		setReverseRefactoring(new UngroupElementsRefactoring(newPaths,getInGroup(),false));
	}

	@Override
	public void fillContext(VelocityContext context) {
		context.put("groupName",getGroupName());
		context.put("inGroup",getInGroup());
		context.put("paths", getPaths());
	}

	//Getters and Setters
	@Override
	public String getTemplatePath() {
		return GROUPELEMENTSTEMPLATE;
	}

	public List<QName> getInGroup() {
		return inGroup;
	}

	public void setInGroup(List<QName> inGroup) {
		this.inGroup = inGroup;
	}

	public QName getGroupName() {
		return groupName;
	}

	public void setGroupName(QName groupName) {
		this.groupName = groupName;
	}
		
}
