package xmlrefactoring.plugin.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.wst.xsd.ui.internal.editor.ISelectionMapper;
import org.eclipse.wst.xsd.ui.internal.editor.XSDSelectionMapper;
import org.eclipse.xsd.XSDNamedComponent;


public abstract class MultipleInputAction extends BaseAction {

	private List<XSDNamedComponent> selectedComponents;

	public void selectionChanged(IAction action, ISelection selection) {
		ISelectionMapper mapper = new XSDSelectionMapper();
		StructuredSelection stselection = (StructuredSelection) mapper.mapSelection(selection);
		List selectionList = stselection.toList();
		selectedComponents = new ArrayList<XSDNamedComponent>();
		action.setEnabled(false);
		for(Object element : selectionList)
			if(element instanceof XSDNamedComponent){
				selectedComponents.add((XSDNamedComponent) element);
				action.setEnabled(true);
			}
			else{
				action.setEnabled(false);
				break;
			}
	}

	protected List<XSDNamedComponent> getSelectedComponents(){
		return selectedComponents;
	}

}
