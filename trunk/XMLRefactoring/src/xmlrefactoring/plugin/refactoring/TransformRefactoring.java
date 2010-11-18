package xmlrefactoring.plugin.refactoring;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.velocity.VelocityContext;

public class TransformRefactoring extends XMLRefactoring {

	private static String ATTR2ELEMTEMPLATE = "/template/attr2Elem.vm";
	
	private QName element;
	
	
	public TransformRefactoring(List<List<QName>> paths, QName element) {
		this(paths, element, true);
	}
	
	public TransformRefactoring(List<List<QName>> paths, QName element, boolean isRootRef) {
		super(paths);
		this.setElement(element);
		if(isRootRef){
			createReverseRefactoring();
		}
			
	}

	@Override
	public void createReverseRefactoring() {
		setReverseRefactoring(new TransformRefactoring(getPaths(), element, false));
	}
	
	
	@Override
	public void fillContext(VelocityContext context) {
		context.put("paths", getPaths());
		context.put("element", getElement());

	}

	@Override
	public String getTemplatePath() {
		return ATTR2ELEMTEMPLATE;
	}

	public void setElement(QName element) {
		this.element = element;
	}

	public QName getElement() {
		return element;
	}

}