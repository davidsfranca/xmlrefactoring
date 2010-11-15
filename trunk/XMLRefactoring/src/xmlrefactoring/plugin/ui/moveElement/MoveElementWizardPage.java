package xmlrefactoring.plugin.ui.moveElement;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import xmlrefactoring.plugin.logic.util.XMLUtil;
import xmlrefactoring.plugin.logic.util.XSDUtil;
import xmlrefactoring.plugin.ui.BaseUserInputWizardPage;

public class MoveElementWizardPage extends BaseUserInputWizardPage {
	private MoveElementWizard wizard;
	
	public MoveElementWizardPage(MoveElementWizard wizard) {
		super("Move Element Page");
		this.wizard = wizard;
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		GridLayout grid = new GridLayout();
		composite.setLayout(grid);
		grid.numColumns = 2;
		new Label(composite, SWT.NONE).setText("Select where to move Element:");
		
		final Tree selectionTree = new Tree(composite, SWT.BORDER);

		selectionTree.setSize(300,500);
		createTreeBranch(selectionTree, null,));	
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
