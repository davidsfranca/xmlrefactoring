package xmlrefactoring.plugin.logic;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.xsd.XSDNamedComponent;
import org.w3c.dom.Document;


public class BaseRefactoringArguments extends RefactoringArguments {

	/**
	 * The components that are being refactored
	 */
	private List<XSDNamedComponent> components;
	
	
	/**
	 * The elements correspondent to the components
	 */
	private List<IDOMElement> elements;	
	
	/**
	 * The XML schema file of this refactoring
	 */
	private IFile schemaFile;
	
	/**
	 * The XML schema Document of this refactoring
	 */
	private Document schemaDocument;
	
	public BaseRefactoringArguments(List<XSDNamedComponent> components) {
		super();
		this.components = components;
		XSDNamedComponent component = components.get(0);
		IDOMElement element = (IDOMElement) component.getElement();
		String schemaPath = element.getModel().getBaseLocation();
		schemaFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(schemaPath));
		schemaDocument = element.getOwnerDocument();
	}	
	
	public List<XSDNamedComponent> getComponents() {
		return components;
	}

	public void setComponents(List<XSDNamedComponent> component) {
		this.components = component;
	}

	public List<IDOMElement> getElements() {
		return elements;
	}

	public void setElements(List<IDOMElement> elements) {
		this.elements = elements;
	}

	public IFile getSchemaFile() {
		return schemaFile;
	}

	public Document getSchemaDocument() {
		return schemaDocument;
	}
	
}
