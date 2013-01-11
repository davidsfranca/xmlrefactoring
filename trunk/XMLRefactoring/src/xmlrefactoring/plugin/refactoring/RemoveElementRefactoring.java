package xmlrefactoring.plugin.refactoring;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.velocity.VelocityContext;

public class RemoveElementRefactoring extends XMLRefactoring {

	private static String REMOVEELEMTEMPLATE = "/template/removeElement.vm";
	private String elementName;

	public RemoveElementRefactoring(List<List<QName>> paths, String elementName) {
		this(paths, elementName, true);
	}

	protected RemoveElementRefactoring(List<List<QName>> paths, String elementName, boolean isRootRef) {
		super(paths);
		setElementName(elementName);
		if(isRootRef)
			createReverseRefactoring();
		setRootRef(isRootRef);
	}

	@Override
	public void fillContext(VelocityContext context) {
		context.put("paths", getPaths());
		context.put("elementName", getElementName());
	}

	@Override
	public String getTemplatePath() {
		return REMOVEELEMTEMPLATE;
	}

	@Override
	public void createReverseRefactoring() {
		setReverseRefactoring(new AddElementRefactoring(getPaths(), getElementName(), false));
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public String getElementName() {
		return elementName;
	}

}
