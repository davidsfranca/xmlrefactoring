package xmlrefactoring.plugin.ui.moveElement;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.plugin.ui.BaseUserInputWizardPage;

public class MoveElementWizardPage extends BaseUserInputWizardPage {
	private MoveElementWizard wizard;
	private XSDNamedComponent selectedComponent;
	
	public MoveElementWizardPage(MoveElementWizard wizard, XSDNamedComponent selectedComponent) {
		super("Move Element Page");
		this.wizard = wizard;
		this.selectedComponent = selectedComponent;
	}
	
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		setControl(composite);
		GridLayout grid = new GridLayout();
		composite.setLayout(grid);
		grid.numColumns = 2;
		new Label(composite, SWT.NONE).setText("Select where to move Element:");
		
		List<XSDElementDeclaration> elementList = selectedComponent.getSchema().getElementDeclarations();
		
		final ListViewer lv = new ListViewer(composite, SWT.NONE);
		lv.setContentProvider(new ElementContentProvider());
		lv.setLabelProvider(new ElementLabelProvider());
		lv.setInput(new ElementList(elementList));
		
		Button select = new Button(composite, SWT.NONE);
		select.setText("Select");
		select.addMouseListener(new MouseListener() {
			
			public void mouseUp(MouseEvent e) {
				wizard.getProcessor().setReceivingElement((XSDElementDeclaration) ((StructuredSelection) lv.getSelection()).getFirstElement());
			}
			
			public void mouseDown(MouseEvent e) {}
			
			public void mouseDoubleClick(MouseEvent e) {}
		});		
	}
}

class ElementList
{
	private List<XSDElementDeclaration> elements;
	
	public ElementList(List<XSDElementDeclaration> list)
	{
		elements = new ArrayList<XSDElementDeclaration>();
		for(XSDElementDeclaration ed : list)
			elements.add(ed);
	}
	
	public List<XSDElementDeclaration> getElements()
	{
		return Collections.unmodifiableList(elements);
	}
}

class ElementContentProvider implements IStructuredContentProvider
{

	public void dispose() {}
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

	public Object[] getElements(Object arg0) {
		return ((ElementList) arg0).getElements().toArray();
	}	
}

class ElementLabelProvider implements ILabelProvider
{

	public void addListener(ILabelProviderListener listener) {}
	public void dispose() {}
	public boolean isLabelProperty(Object element, String property) {return false;}
	public void removeListener(ILabelProviderListener listener) {}
	public Image getImage(Object element) {return null;}
	public String getText(Object element) {
		return ((XSDElementDeclaration) element).getName();
	}
	
}