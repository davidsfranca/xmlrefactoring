package xmlrefactoring.plugin.logic.attr2elem.external;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Element;

import xmlrefactoring.plugin.logic.BaseRefactoringArguments;
import xmlrefactoring.plugin.logic.util.ReferenceWithCompositor;
import xmlrefactoring.plugin.logic.util.XSDUtil;

public class Attr2ElemRefactoringArguments extends BaseRefactoringArguments{
	
	private boolean element;
	private boolean attribute;
	private String elementName;
	
	public Attr2ElemRefactoringArguments(List<XSDNamedComponent> components) {
		super(components);
		Element transformingElement = getElements().get(0);
		boolean element = XSDUtil.isElement(transformingElement);
		this.element = element;
		this.attribute = !element;
		
		this.elementName = transformingElement.getAttribute("name");
	}
	
	public boolean isElement()
	{
		return this.element;
	}
	
	public boolean isAttribute()
	{
		return this.attribute;
	}

	private List<ReferenceWithCompositor> referencesWithCompositor = new ArrayList<ReferenceWithCompositor>();
	
	public List<ReferenceWithCompositor> getReferencesWithCompositor() {
		return referencesWithCompositor;
	}
}
