package xmlrefactoring.plugin.ui.attr2elem;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import xmlrefactoring.plugin.logic.attr2elem.ReferenceWithCompositor;
import xmlrefactoring.plugin.logic.util.XSDUtil;
import xmlrefactoring.plugin.ui.BaseUserInputWizardPage;

public class ElementPositioningWizardPage extends BaseUserInputWizardPage {

	private XSDNamedComponent attribute;
	private Element referenceComplexType;
	private String attributeName;
	private static final String[] compositors = {XSDUtil.ALL, XSDUtil.SEQUENCE, XSDUtil.CHOICE};
	
	public ElementPositioningWizardPage(XSDNamedComponent attribute, Element referenceComplexType) {
		super("Element Positioning");
		this.attribute = attribute;
		this.referenceComplexType = referenceComplexType;
		attributeName = XSDUtil.getName(attribute.getElement());
		if(attributeName == null){
			attributeName = XSDUtil.getRef(attribute.getElement());
		}
	}	

	@Override
	public void createControl(Composite parent){
		super.createControl(parent);

		getShell().setSize(600,400);

		Composite composite = new Composite(parent, SWT.NONE);
		FillLayout fill = new FillLayout(SWT.VERTICAL);	
		composite.setLayout(fill);
		setControl(composite);	



		Element rootCompositor = (Element) referenceComplexType.getElementsByTagNameNS( referenceComplexType.getNamespaceURI(),
				XSDUtil.SEQUENCE).item(0);
		if(rootCompositor == null)
			rootCompositor = (Element) referenceComplexType.getElementsByTagNameNS( referenceComplexType.getNamespaceURI(),
					XSDUtil.ALL).item(0);
		if(rootCompositor == null)
			rootCompositor = (Element) referenceComplexType.getElementsByTagNameNS( referenceComplexType.getNamespaceURI(),
					XSDUtil.CHOICE).item(0);

		if(rootCompositor != null){

			createSelectionTopMessage(composite, referenceComplexType);			

			final Tree selectionTree = new Tree(composite, SWT.BORDER);

			selectionTree.setSize(300,500);
			createTreeBranch(selectionTree, null, rootCompositor);
			selectionTree.addSelectionListener(new SelectionListener(){

				TreeItem newNode;

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);					
				}

				public void widgetSelected(SelectionEvent e) {
					e.getSource();
					TreeItem selectedItem = (TreeItem) e.item;
					if(selectedItem != newNode)
						if(newNode == null){
							newNode = createNewNode(selectedItem);
						}
						else{
							newNode.dispose();
							newNode = createNewNode(selectedItem);
						}
				}

				private TreeItem createNewNode(TreeItem selectedItem){
					TreeItem newNode;
					if(!XSDUtil.isElement((Element) selectedItem.getData())){
						newNode = new TreeItem(selectedItem,0,0);
					}
					else{
						TreeItem parentNode = selectedItem.getParentItem();
						int newNodeIndex = parentNode.indexOf(selectedItem) + 1;
						newNode = new TreeItem(parentNode, 0, newNodeIndex);
					}
					newNode.setText("NEW ELEMENT: " + attributeName);
					ReferenceWithCompositor reference = new ReferenceWithCompositor(referenceComplexType,(Element)selectedItem.getData());
					return newNode;
				}
			});
			
			
		}
		else{
			createCompositorCreationTopMessage(composite, referenceComplexType);
			final Button[] selectionButtons = new Button[3];
			for(int i = 0; i < compositors.length; i++){
				selectionButtons[i] = new Button(composite, SWT.RADIO);
				selectionButtons[i].setText(compositors[i]);
			}
			for(int i = 0; i < selectionButtons.length; i++){
				selectionButtons[i].addSelectionListener(new SelectionListener(){
					
					public void widgetDefaultSelected(SelectionEvent e) {
						widgetSelected(e);						
					}

					public void widgetSelected(SelectionEvent e) {
						for(int j = 0; j < selectionButtons.length; j++){
							Button thisButton = (Button)e.getSource();
							if(thisButton.getSelection() && !selectionButtons[j].equals(thisButton)){
								selectionButtons[j].setSelection(false);
							}
						}
					}
					
				});
			}
		}
	}

	private void createCompositorCreationTopMessage(Composite composite,
			Element referenceComplexType2) {
		StringBuilder topMessage = new StringBuilder();
		topMessage.append("The type ");
		if(XSDUtil.isAnonymous(referenceComplexType)){
			topMessage.append("of the element \"");			
			topMessage.append(XSDUtil.getName((Element) referenceComplexType.getParentNode()));
			topMessage.append("\"");
		}
		else
			topMessage.append(XSDUtil.getName(referenceComplexType));
		topMessage.append(" don`t have a root compositor, chose one:");
		Label topLabel = new Label(composite, SWT.NONE);
		topLabel.setText(topMessage.toString());
		topLabel.setSize(600,50);
		
	}

	private void createSelectionTopMessage(Composite composite,
			Element referenceComplexType2) {
		StringBuilder topMessage = new StringBuilder();
		topMessage.append("Select the position of the new element on the type ");
		if(XSDUtil.isAnonymous(referenceComplexType)){
			topMessage.append("of the element \"");			
			topMessage.append(XSDUtil.getName((Element) referenceComplexType.getParentNode()));
			topMessage.append("\"");
		}
		else
			topMessage.append(XSDUtil.getName(referenceComplexType));
		Label topLabel = new Label(composite, SWT.NONE);
		topLabel.setText(topMessage.toString());
		topLabel.setSize(600,50);	
	}

	private void createTreeBranch(Tree tree, TreeItem parentNode, Element element) {
		TreeItem childNode;
		if(parentNode == null){
			childNode = new TreeItem(tree, 0);
			tree.select(childNode);
		}
		else		
			childNode = new TreeItem(parentNode, 0);
		childNode.setText(element.getTagName());
		childNode.setData(element);
		NodeList list = element.getChildNodes();
		for(int i = 0; i < list.getLength(); i++){
			if(list.item(i) instanceof Element){
				Element child = (Element) list.item(i);
				createTreeBranch(tree, childNode, child);
			}
		}
	}




}
