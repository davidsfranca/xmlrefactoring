package xmlrefactoring.plugin.logic.util;

import java.util.ArrayList;
import java.util.List;

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
	public static List<String> createElementPaths(Element element) throws CoreException{
		List<String> complexTypeReferencePaths = new ArrayList<String>();
		return createElementPaths(element, "", complexTypeReferencePaths);
	}

	private static List<String> createElementPaths(Element element, String suffix, List<String> paths) throws CoreException{

		Element referenceToBeSearched;
		String newSuffix;

		if(isGlobal(element)){			
			referenceToBeSearched = element;
			newSuffix = insertGlobalElement(element,suffix);
			paths.add(newSuffix);
		}
		else{
			//If it`s not a global element, then the reference is inside a complexType
			Element containerElement = element;
			while(!isComplexType(containerElement)){
				containerElement = (Element) containerElement.getParentNode();
			}
			Element ownerComplexType = containerElement;
			if(isExtension(element)){
				referenceToBeSearched = ownerComplexType;
				newSuffix = suffix;
			}else{
				//If it`s not an extension, then the reference is an element inside a complexType
				if(isAnonymous(ownerComplexType)){
					//Reference is inside an anonymous complexType					
					Element namedContainer;
					Element anonymousContainer = element;
					newSuffix = suffix;
					do{						
						while(isAnonymous(anonymousContainer))
							anonymousContainer = (Element)anonymousContainer.getParentNode();
						namedContainer = anonymousContainer;
						if(!isComplexType(namedContainer)){
							if(isGlobal(namedContainer)){
								newSuffix = insertGlobalElement(namedContainer, newSuffix);
							}else{
								newSuffix = insertLocalElement(namedContainer, newSuffix);							
							}							
						}	
						anonymousContainer = (Element) namedContainer.getParentNode();
					}while(!isGlobal(namedContainer));
					paths.add(newSuffix);
					referenceToBeSearched = namedContainer;
				}
				else{
					//Reference is inside a named complexType
					referenceToBeSearched = ownerComplexType;
					newSuffix = insertLocalElement(element, suffix);
				}
			}
		}		

		List<SearchMatch> results = SearchUtil.searchReferences(referenceToBeSearched);

		for(SearchMatch match : results){
			if(match.getObject() instanceof Node){
				Node node = (Node)match.getObject();
				if(node instanceof Attr){
					Attr attr = (Attr) node;
					createElementPaths(attr.getOwnerElement(), newSuffix, paths);
				}
			}
		}
		return paths;
	}

	private static String insertLocalElement(Element element, String suffix) {
		if(isAnonymous(element))
			return suffix;
		StringBuilder namedElement = new StringBuilder();
		namedElement.append("/");
		namedElement.append(element.getAttribute("name"));					
		namedElement.append(suffix);
		return namedElement.toString();		
	}

	private static String insertGlobalElement(Element element, String suffix) {
		if(isAnonymous(element))
			return suffix;
		StringBuilder namedElement = new StringBuilder();
		namedElement.append("/");
		namedElement.append(getTargetNamespace(element));
		namedElement.append(":");
		namedElement.append(element.getAttribute("name"));					
		namedElement.append(suffix);
		return namedElement.toString();
	}

	private static String getTargetNamespace(Element element) {
		Element schemaElement = (Element) element.getOwnerDocument().getElementsByTagName("schema").item(0);		
		return schemaElement.getAttribute("targetNamespace");
	}

	private static boolean isAnonymous(Element element) {
		return element.getAttribute("name") == null;
	}

	private static boolean isExtension(Element element) {
		return element.getNodeName().equals("extension");
	}

	private static boolean isComplexType(Element element) {
		return element.getNodeName().equals("complexType");
	}

	private static boolean isGlobal(Element element) {
		return element.getParentNode().getNodeName().equals("schema");
	}
}
