package xmlrefactoring.plugin.refactoring;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.velocity.VelocityContext;

public class AddElementRefactoring extends XMLRefactoring {

	private String name;
	private String value;
	private String type;
	private boolean optional;
	private static String ADDELEMTEMPLATE = "/template/addTag.vm";

	public AddElementRefactoring(List<List<QName>> paths, String name, String type, String value, boolean isOptional) {
		this(paths, name, true);
		setType(type);
		setValue(value);
		setOptional(isOptional);
	}

	protected AddElementRefactoring(List<List<QName>> paths, String name, boolean isRootRef) {
		super(paths);
		setName(name);
		if(isRootRef)
			createReverseRefactoring();
		setRootRef(isRootRef);
	}

	@Override
	public void fillContext(VelocityContext context) {
		context.put("name", getName());
		context.put("type", getType());
		context.put("paths", getPaths());
		context.put("defaultValue", getValue());
		if(isOptional())
			context.put("minOccurs", "0");
		else
			context.put("minOccurs", "1");
	}

	@Override
	public void createReverseRefactoring() {
		setReverseRefactoring(new RemoveElementRefactoring(getPaths(), getName(), false));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getTemplatePath() {
		return ADDELEMTEMPLATE;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isOptional() {
		return optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

}
