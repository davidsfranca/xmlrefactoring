package xmlrefactoring.plugin.logic.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.common.core.search.SearchMatch;
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
	public static List<List<QName>> createPaths(Element element) throws CoreException{
		List<List<QName>> complexTypeReferencePaths = new ArrayList<List<QName>>();
		return createPaths(element, new LinkedList<QName>(), complexTypeReferencePaths);
	}

	private static List<List<QName>> createPaths(Element element, List<QName> suffix, List<List<QName>> paths) throws CoreException{

		Element referenceToBeSearched = null;
		List<QName> newSuffix;

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

	private static List<QName> insertLocalElement(Element element, List<QName> suffix) {		
		QName elementQName = new QName(element.getAttribute("name"));		
		return insertElement(elementQName, suffix);		
	}

	private static List<QName> insertGlobalElement(Element element, List<QName> suffix) {
		QName elementQName = new QName(getTargetNamespace(element), element.getAttribute("name"));			
		return insertElement(elementQName, suffix);
	}
	
	private static List<QName> insertElement(QName elementQName, List<QName> suffix){
		List<QName> newPath = new LinkedList<QName>(suffix);
		newPath.add(0, elementQName);
		return newPath;
	}

	private static String getTargetNamespace(Element element) {
		Element schemaElement = (Element) element.getOwnerDocument().
			getElementsByTagNameNS(element.getNamespaceURI(), "schema").item(0);		
		return schemaElement.getAttribute("targetNamespace");
	}


	

}
