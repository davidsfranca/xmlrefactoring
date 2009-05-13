package xmlrefactoring.plugin.logic.rename.test;

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
import org.eclipse.wst.xml.core.internal.search.XMLComponentDeclarationPattern;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import xmlrefactoring.plugin.logic.rename.XPathCreator;

public class XPathCreatorTest {

	@Test
	public void testCreateComplexTypeReferencePaths() throws CoreException {
		String componentName = "zetype";
		String componentNamespace = "http://www.example.org/NewXMLSchema";
		QualifiedName elementQName = new QualifiedName(componentNamespace, componentName);
		QualifiedName typeQName = new QualifiedName("http://www.w3.org/2001/XMLSchema", "complexType");

		String fileStr = "/PluginTest/src/NewXMLSchema.xsd";
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileStr));

		SearchScope scope = new WorkspaceSearchScope();
		CollectingSearchRequestor requestor = new CollectingSearchRequestor();
		SearchPattern pattern = new XMLComponentDeclarationPattern(file, elementQName, typeQName);
		SearchEngine searchEngine = new SearchEngine();
		HashMap map = new HashMap();
		searchEngine.search(pattern, requestor, scope, map, new NullProgressMonitor());
		List<SearchMatch> matches = requestor.getResults();
		SearchMatch match = matches.get(0);
		
		Node node = (Node)match.getObject();
		Attr attr = (Attr) node;
		Element element = attr.getOwnerElement();
		List<String> paths = XPathCreator.createElementPaths(element, "/internalzeelement");
		
		
	}

}
