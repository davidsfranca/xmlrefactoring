package xmlrefactoring.plugin.ui.attr2elem;

import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;
import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.plugin.logic.util.SchemaElementVerifier;
import xmlrefactoring.plugin.ui.BaseWizard;

public class Attr2ElemWizard extends BaseWizard{

	private XSDNamedComponent attribute;
	
	public Attr2ElemWizard(ProcessorBasedRefactoring refactoring, XSDNamedComponent namedComponent) {
		super(refactoring, DIALOG_BASED_USER_INTERFACE | CHECK_INITIAL_CONDITIONS_ON_OPEN);
		attribute = namedComponent;
	}

	@Override
	protected void addUserInputPages() {
		if(!SchemaElementVerifier.isGlobal(attribute.getElement())){
			addPage(new ElementPositioningWizardPage());
		}		
	}

}
