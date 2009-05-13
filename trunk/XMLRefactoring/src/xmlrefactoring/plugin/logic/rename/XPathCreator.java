package xmlrefactoring.plugin.logic.rename;

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

		StringBuilder namedElementPrefix = new StringBuilder();
		namedElementPrefix.append("/");
		namedElementPrefix.append(element.getAttribute("name"));					
		namedElementPrefix.append(suffix);

		Element referenceToBeSearched = null;
		String newSuffix = null;

		if(isGlobal(element)){
			paths.add(namedElementPrefix.toString());
			referenceToBeSearched = element;
			newSuffix = namedElementPrefix.toString();
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
					Element namedContainer;
					StringBuilder sb2 = new StringBuilder();
					do{
						Element anonymousContainer = ownerComplexType;
						while(isAnonymous(anonymousContainer))
							anonymousContainer = (Element)anonymousContainer.getParentNode();
						namedContainer = anonymousContainer;
						if(!isComplexType(namedContainer)){								
							sb2.insert(0,namedContainer.getAttribute("name"));
							sb2.insert(0,"/");
						}						
					}while(!isGlobal(namedContainer));					
					if(isAnonymous(element)){
						sb2.append(suffix);
					}else{
						sb2.append(namedElementPrefix);
					}
					paths.add(sb2.toString());
					referenceToBeSearched = namedContainer;
					newSuffix = sb2.toString();
				}
				else{
					referenceToBeSearched = ownerComplexType;
					if(isAnonymous(element)){
						newSuffix = suffix;
					}else{
						newSuffix = namedElementPrefix.toString();
					}
				}
			}
		}		

		//TODO Deve ser retirado quando método estiver completo
		if(referenceToBeSearched != null){

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
		}
		return paths;
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
