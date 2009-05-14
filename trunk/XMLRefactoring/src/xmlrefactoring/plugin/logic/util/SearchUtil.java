package xmlrefactoring.plugin.logic.util;

import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.common.core.search.SearchEngine;
import org.eclipse.wst.common.core.search.SearchMatch;
import org.eclipse.wst.common.core.search.pattern.QualifiedName;
import org.eclipse.wst.common.core.search.pattern.SearchPattern;
import org.eclipse.wst.common.core.search.scope.SearchScope;
import org.eclipse.wst.common.core.search.scope.WorkspaceSearchScope;
import org.eclipse.wst.common.core.search.util.CollectingSearchRequestor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.search.XMLComponentReferencePattern;
import org.w3c.dom.Element;

public class SearchUtil {
	
	public static List<SearchMatch> searchReferences(Element element) throws CoreException {
		String componentName = element.getAttribute("name");
		String componentNamespace = element.getOwnerDocument().getDocumentElement().getAttribute("targetNamespace");
		QualifiedName elementQName = new QualifiedName(componentNamespace, componentName);
		QualifiedName typeQName = new QualifiedName(element.getNamespaceURI(), element.getLocalName());

		String fileStr = ((IDOMNode) element).getModel().getBaseLocation();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileStr));

		SearchScope scope = new WorkspaceSearchScope();
		CollectingSearchRequestor requestor = new CollectingSearchRequestor();
		SearchPattern pattern = new XMLComponentReferencePattern(file, elementQName, typeQName);
		SearchEngine searchEngine = new SearchEngine();
		HashMap map = new HashMap();
		searchEngine.search(pattern, requestor, scope, map, new NullProgressMonitor());
		return requestor.getResults();
	}

}
