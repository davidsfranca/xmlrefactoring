package xmlrefactoring.plugin.refactoring;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.velocity.VelocityContext;

public class MoveElementRefactoring extends XMLRefactoring {
	
	private String movingElement;
	private List<QName> changers;
	private static String MOVEELEMENTTEMPLATE = "/template/moveElement.vm";
	
	public MoveElementRefactoring(List<List<QName>> paths, String movingElement, List<QName> changers)
	{
		this(paths, movingElement, changers, true);
	}
	
	protected MoveElementRefactoring(List<List<QName>> paths, String movingElement, 
			List<QName> changers, boolean isRootRef) {
		super(paths);
		setMovingElement(movingElement);
		setChangers(changers);
		if(isRootRef)
			createReverseRefactoring();
		setRootRef(isRootRef);
	}

	private void setMovingElement(String movingElement) {
		this.movingElement = movingElement;
	}
	
	@Override
	public void fillContext(VelocityContext context) {
		context.put("paths", getPaths());
		context.put("movingElement", getMovingElement());
		context.put("changers", getChangers());
	}
	
	private String getMovingElement()
	{
		return movingElement;
	}
	
	@Override
	public String getTemplatePath() {
		return MOVEELEMENTTEMPLATE;
	}

	@Override
	public void createReverseRefactoring() {
		List<QName> newChangers = new ArrayList<QName>();
		if(getChangers().size() > 0)
		{
			newChangers.add(getChangers().get(1));
			newChangers.add(getChangers().get(0));
			
			setReverseRefactoring(new MoveElementRefactoring(getPaths(), movingElement, newChangers, false));
		}
		else
			setReverseRefactoring(new MoveElementRefactoring(null, "", null, false));
	}
	
	private void setChangers(List<QName> changers)
	{
		this.changers = changers;
	}
	
	private List<QName> getChangers()
	{
		return changers;
	}

}
