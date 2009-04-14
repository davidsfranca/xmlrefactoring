package prototipo.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.wst.common.core.search.SearchEngine;
import org.eclipse.wst.common.core.search.SearchMatch;
import org.eclipse.wst.common.core.search.pattern.QualifiedName;
import org.eclipse.wst.common.core.search.pattern.SearchPattern;
import org.eclipse.wst.common.core.search.scope.SearchScope;
import org.eclipse.wst.common.core.search.scope.WorkspaceSearchScope;
import org.eclipse.wst.common.core.search.util.CollectingSearchRequestor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.search.XMLComponentReferencePattern;
import org.eclipse.wst.xsd.ui.internal.refactor.RefactoringMessages;
import org.eclipse.wst.xsd.ui.internal.refactor.TextChangeManager;
import org.eclipse.wst.xsd.ui.internal.refactor.rename.ComponentRenameArguments;
import org.eclipse.wst.xsd.ui.internal.refactor.rename.RenameComponentProcessor;
import org.eclipse.wst.xsd.ui.internal.refactor.util.TextChangeCompatibility;
import org.eclipse.wst.xsd.ui.internal.search.IXSDSearchConstants;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDSchema;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import prototipo.xsltLogic.RenameRefactor;
import prototipo.xsltLogic.XSLTWriter;

public class XSLTRenameParticipant extends RenameParticipant{

	private TextChangeManager manager;
	private List<SearchMatch> matches;
	private XSDNamedComponent component;
	private List<String> paths;

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		return new RefactoringStatus();
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
	OperationCanceledException {
		if(!(component.getContainer() instanceof XSDSchema)){
			renameAnonymousElements();			
		}
		if(component.getElement().getNodeName().equals("element")){
			paths = new ArrayList<String>();
			StringBuilder sb = new StringBuilder();
			sb.append("/");
			sb.append(component.getName());
			if(component.getContainer() instanceof XSDSchema){
				//Elemento Ž global				
				paths.add(sb.toString());
			}
			else{
				//Elemento est‡ em um complex type
				
				XSDConcreteComponent container = component;
				while(!(container instanceof XSDComplexTypeDefinition)){
					container = container.getContainer();
				}
				XSDConcreteComponent complexType = container;
				searchReferences(complexType.getElement(), sb.toString());
			}
		}
		
		RenameRefactor refactor = new RenameRefactor(paths, getRenameArguments().getNewName());
		ArrayList<RenameRefactor> list = new ArrayList<RenameRefactor>();
		list.add(refactor);
		try {
			XSLTWriter.createTransformation(list);
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public void searchReferences(Element complexTypeNode, String suffix) throws CoreException{	

		//Busca as referencias
		String componentName = complexTypeNode.getAttribute("name");
		String componentNamespace = complexTypeNode.getOwnerDocument().getDocumentElement().getAttribute("targetNamespace");
		QualifiedName elementQName = new QualifiedName(componentNamespace, componentName);
		QualifiedName typeQName = new QualifiedName(complexTypeNode.getNamespaceURI(), complexTypeNode.getLocalName());

		String fileStr = ((IDOMNode) complexTypeNode).getModel().getBaseLocation();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileStr));

		SearchScope scope = new WorkspaceSearchScope();
		CollectingSearchRequestor requestor = new CollectingSearchRequestor();
		SearchPattern pattern = new XMLComponentReferencePattern(file, elementQName, typeQName);
		SearchEngine searchEngine = new SearchEngine();
		HashMap map = new HashMap();
		searchEngine.search(pattern, requestor, scope, map, new NullProgressMonitor());
		List<SearchMatch> results = requestor.getResults();

		//Cria os paths

		for(SearchMatch match : results){
			if(match.getObject() instanceof Node){
				Node node = (Node)match.getObject();
				if(node instanceof Attr){
					Attr attr = (Attr) node;
					Element element = attr.getOwnerElement();
					StringBuilder sb = new StringBuilder();
					sb.append("/");
					sb.append(element.getAttribute("name"));					
					sb.append(suffix);
					//Declaracao global de elemento
					if(element.getParentNode().getNodeName().equals("schema")){
						paths.add(sb.toString());
					}
					else{
						Element containerElement = element;
						while(!containerElement.getNodeName().equals("complexType")){
							containerElement = (Element) containerElement.getParentNode();
						}
						Element ownerComplexType = containerElement;
						if(ownerComplexType.getAttribute("name") == null){
							//Tipo an™nimo
							StringBuilder sb2 = new StringBuilder();
							sb2.append("/");
							sb2.append(((Element)ownerComplexType.getParentNode()).getAttribute("name"));
							sb2.append(sb);	
							paths.add(sb2.toString());
						}
						else
							searchReferences(ownerComplexType, sb.toString());
					}
				}
			}
		}
	}

	/**
	 * Corrige bug de renomear elementos declarados em tipos (n‹o globais)	
	 */
	public void renameAnonymousElements(){

		IDOMElement idomElement = (IDOMElement) component.getElement();
		String fileStr = idomElement.getModel().getBaseLocation();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileStr));		

		TextChange textChange = manager.get(file);
		String newName = getRenameArguments().getNewName();
		newName = RenameComponentProcessor.quoteString(newName);
		IDOMAttr attr = (IDOMAttr) idomElement.getAttributeNode("name");
		int length = attr.getEndOffset() - attr.getValueRegionStartOffset() - 1;
		ReplaceEdit replaceEdit = new ReplaceEdit(attr.getValueRegionStartOffset(), length, newName);
		String editName = RefactoringMessages.getString("RenameComponentProcessor.Component_Refactoring_update_declatation");
		TextChangeCompatibility.addTextEdit(textChange, editName, replaceEdit);
	}

	@Override
	public String getName() {
		return "XSLTRenameParticipant";
	}

	@Override
	protected boolean initialize(Object element) {
		if(element instanceof XSDNamedComponent){
			this.component = (XSDNamedComponent) element;
			if(getArguments() instanceof ComponentRenameArguments){
				// changeManger is passed in from the RenameComponentProcessor to collect all the changes
				manager = getRenameArguments().getChangeManager();
				matches = (List<SearchMatch>)((ComponentRenameArguments)getArguments()).getMatches().get(IXSDSearchConstants.XMLSCHEMA_NAMESPACE);
			}
			return true;
		}		
		return false;
	}

	private ComponentRenameArguments getRenameArguments(){
		return (ComponentRenameArguments) getArguments();
	}

}
