package xmlrefactoring.plugin.refactoring;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.velocity.VelocityContext;

public class Elem2AttrRefactoring extends XMLRefactoring {

	private static String ELEM2ATTRTEMPLATE = "/template/elem2Attr.vm";	
	private QName attribute;	
	
	public Elem2AttrRefactoring(List<List<QName>> paths, QName attribute) {
		this(paths, attribute, true);
	}
	
	public Elem2AttrRefactoring(List<List<QName>> paths, QName attribute, boolean isRootRef) {
		super(paths);
		this.setAttribute(attribute);
		if(isRootRef){
			createReverseRefactoring();
		}
			
	}

	@Override
	public void createReverseRefactoring() {
		setReverseRefactoring(new Attr2ElemRefactoring(getPaths(), attribute, false));
	}
	
	
	@Override
	public void fillContext(VelocityContext context) {
		context.put("paths", getPaths());
		context.put("attribute", getAttribute());

	}

	@Override
	public String getTemplatePath() {
		return ELEM2ATTRTEMPLATE;
	}

	public void setAttribute(QName attribute) {
		this.attribute = attribute;
	}

	public QName getAttribute() {
		return attribute;
	}

}