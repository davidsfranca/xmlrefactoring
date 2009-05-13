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
	public static List<String> createElementPaths(Element element, String suffix) throws CoreException{
		List<String> complexTypeReferencePaths = new ArrayList<String>();
		return createElementPaths(element, suffix, complexTypeReferencePaths);
	}

	private static List<String> createElementPaths(Element elementNode, String suffix, List<String> paths) throws CoreException{

		List<SearchMatch> results = SearchUtil.searchReferences(elementNode);

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

					if(isGlobalElement(element)){
						paths.add(sb.toString());
					}
					else{
						Element containerElement = element;
						while(!isComplexType(containerElement)){
							containerElement = (Element) containerElement.getParentNode();
						}
						Element ownerComplexType = containerElement;
						if(isExtension(element)){
							createElementPaths(ownerComplexType, suffix, paths);
						}else{							
							if(ownerComplexType.getAttribute("name") == null){
								//Anonymous type								
								sb.insert(0,((Element)ownerComplexType.getParentNode()).getAttribute("name"));
								sb.insert(0,("/"));
								paths.add(sb.toString());
							}
							else
								createElementPaths(ownerComplexType, sb.toString(), paths);
						}
					}
				}
			}
		}
		return paths;
	}
	
	private static boolean isExtension(Element element) {
		return element.getNodeName().equals("extension");
	}

	private static boolean isComplexType(Element element) {
		return element.getNodeName().equals("complexType");
	}

	private static boolean isGlobalElement(Element element) {
		return element.getParentNode().getNodeName().equals("schema");
	}
}
