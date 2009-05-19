package xmlrefactoring.plugin.logic.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.common.core.search.SearchMatch;
import org.eclipse.wst.xsd.ui.internal.adt.design.editparts.ComplexTypeEditPart;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XPathCreator {
	
	/**
	 * Search references to the specified element and create the XPaths corresponding to
	 * the references.
	 * @param element - the complexType which the references will be searched
	 * @param suffix - the suffix of the XPath that takes to the element
	 * @throws CoreException 
	 */
	public static List<String> createPaths(Element element) throws CoreException{
		List<String> complexTypeReferencePaths = new ArrayList<String>();
		return createPaths(element, "", complexTypeReferencePaths);
	}

	private static List<String> createPaths(Element element, String suffix, List<String> paths) throws CoreException{

		Element referenceToBeSearched = null;
		String newSuffix;

		if(SchemaElementVerifier.isGlobal(element)){
			newSuffix = insertGlobalElement(element,suffix);
			paths.add(newSuffix);
		}
		else{
			//If it`s not a global element, then the reference is inside a complexType or a group

			//Search through the namedContainers of the element (complexType or elements)
			//Adds the elements names to the path, stops when the container is a global element
			Element namedContainer;
			Element anonymousContainer = element;
			newSuffix = suffix;
			do{						
				while(SchemaElementVerifier.isAnonymous(anonymousContainer))
					anonymousContainer = (Element)anonymousContainer.getParentNode();
				namedContainer = anonymousContainer;
				if(SchemaElementVerifier.isElementOrAttribute(namedContainer)){
					//The container is an element
					if(SchemaElementVerifier.isGlobal(namedContainer)){
						//If the global container is an element,  adds the path
						newSuffix = insertGlobalElement(namedContainer, newSuffix);
						paths.add(newSuffix);
					}else{
						newSuffix = insertLocalElement(namedContainer, newSuffix);							
					}							
				}	
				anonymousContainer = (Element) namedContainer.getParentNode();
			}while(!SchemaElementVerifier.isGlobal(namedContainer));
			//If the global container isn`t an element, should search for references
			if(!SchemaElementVerifier.isElementOrAttribute(namedContainer))
				referenceToBeSearched = namedContainer;

		}		

		if(referenceToBeSearched != null){
			List<SearchMatch> results = SearchUtil.searchReferences(referenceToBeSearched);

			for(SearchMatch match : results){
				if(match.getObject() instanceof Node){
					Node node = (Node)match.getObject();
					if(node instanceof Attr){
						Attr attr = (Attr) node;
						createPaths(attr.getOwnerElement(), newSuffix, paths);
					}
				}
			}
		}
		return paths;
	}

	private static String insertLocalElement(Element element, String suffix) {
		StringBuilder namedElement = new StringBuilder();
		namedElement.append("/");
		namedElement.append(element.getAttribute("name"));					
		namedElement.append(suffix);
		return namedElement.toString();		
	}

	private static String insertGlobalElement(Element element, String suffix) {
		StringBuilder namedElement = new StringBuilder();
		namedElement.append("/");
		namedElement.append(getTargetNamespace(element));
		namedElement.append(":");
		namedElement.append(element.getAttribute("name"));					
		namedElement.append(suffix);
		return namedElement.toString();
	}

	private static String getTargetNamespace(Element element) {
		Element schemaElement = (Element) element.getOwnerDocument().
			getElementsByTagNameNS(element.getNamespaceURI(), "schema").item(0);		
		return schemaElement.getAttribute("targetNamespace");
	}


	

}
