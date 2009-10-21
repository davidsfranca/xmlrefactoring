package xmlrefactoring.plugin.refactoring;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.velocity.VelocityContext;

public class UngroupElementsRefactoring extends XMLRefactoring{
	
	/**
	 * All elements that will be grouped, this is required to create the reverse change
	 */
	private List<QName> inGroup;
	
	private static String UNGROUPELEMENTSTEMPLATE = "/template/ungroupElement.vm";
	

	protected UngroupElementsRefactoring (List<List<QName>> paths) {
		super(paths);
		setRootRef(false);
	}
	
	public UngroupElementsRefactoring(List<List<QName>> paths, List<QName> inGroup, boolean isRootRef) {
		super(paths);
		setInGroup(inGroup);
		if(isRootRef)
			createReverseRefactoring();
		setRootRef(isRootRef);
	}

	@Override
	public void createReverseRefactoring() {
		QName groupName = getPaths().get(0).get(getPaths().get(0).size());
		List<List<QName>> newPaths =  new ArrayList();
		for(int i= 0; i<getPaths().size();i++){
			List<QName> path = new ArrayList<QName>();
			List<QName> oldPath = getPaths().get(i);
			int lastIndex = oldPath.size()-1;
			for(int j = 0; j<lastIndex;j++)
				path.add(new QName(oldPath.get(j).getNamespaceURI(),oldPath.get(j).getLocalPart()));
			newPaths.add(path);
		}
		setReverseRefactoring(new UngroupElementsRefactoring(newPaths,getInGroup(),false));
	}

	@Override
	public void fillContext(VelocityContext context) {
		context.put("paths",getPaths());
	}

	//Getters and Setters
	@Override
	public String getTemplatePath() {
		return UNGROUPELEMENTSTEMPLATE;
	}

	public List<QName> getInGroup() {
		return inGroup;
	}

	public void setInGroup(List<QName> inGroup) {
		this.inGroup = inGroup;
	}

		
}