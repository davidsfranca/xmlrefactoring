package xmlrefactoring.plugin.refactoring;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.velocity.VelocityContext;

public class MoveElementRefactoring extends XMLRefactoring {
	
	private String movingElement;
	private String from;
	private String to;
	private static String MOVEELEMENTTEMPLATE = "/template/moveElement.vm";
	
	public MoveElementRefactoring(List<List<QName>> paths, String movingElement, String from, String to)
	{
		this(paths, movingElement, from, to, true);
	}
	
	protected MoveElementRefactoring(List<List<QName>> paths, String movingElement, 
			String from, String to, boolean isRootRef) {
		super(paths);
		setMovingElement(movingElement);
		setFrom(from);
		setTo(to);
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
		context.put("moving", getMovingElement());
		context.put("from", getFrom());
		context.put("to", getTo());
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
		if(getPaths().size() > 0)			
			setReverseRefactoring(new MoveElementRefactoring(getPaths(), movingElement, getTo(), getFrom(), false));
		else
			setReverseRefactoring(new MoveElementRefactoring(null, "", null, null, false));
	}
	
	private void setFrom(String from)
	{
		this.from = from;
	}
	
	private String getFrom()
	{
		return from;
	}
	
	private void setTo(String to)
	{
		this.to = to;
	}
	
	private String getTo()
	{
		return to;
	}

}
