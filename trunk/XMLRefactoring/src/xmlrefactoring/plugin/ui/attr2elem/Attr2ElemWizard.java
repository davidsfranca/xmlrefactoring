package xmlrefactoring.plugin.ui.attr2elem;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.common.core.search.SearchMatch;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import xmlrefactoring.plugin.logic.attr2elem.Attr2ElemProcessor;
import xmlrefactoring.plugin.logic.util.SearchUtil;
import xmlrefactoring.plugin.logic.util.XSDUtil;
import xmlrefactoring.plugin.ui.BaseRefactoringWizard;

public class Attr2ElemWizard extends BaseRefactoringWizard<Attr2ElemProcessor>{

	private XSDNamedComponent attribute;
	
	public Attr2ElemWizard(Attr2ElemProcessor processor, XSDNamedComponent namedComponent) {
		super(processor, DIALOG_BASED_USER_INTERFACE | CHECK_INITIAL_CONDITIONS_ON_OPEN);
		attribute = namedComponent;
	}

	@Override
	protected void addUserInputPages() {
		try {
			for(SearchMatch match : SearchUtil.searchReferences((IDOMElement) attribute.getElement())){
				if(match.getObject() instanceof Node){
					Node node = (Node)match.getObject();
					if(node instanceof Attr){
						Attr attr = (Attr) node;
						Element reference = attr.getOwnerElement();
						Element container = reference;
						while(!XSDUtil.isComplexType(container))
							container = (Element) container.getParentNode();
						addPage(new ElementPositioningWizardPage(attribute, container));
					}
				}			
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
