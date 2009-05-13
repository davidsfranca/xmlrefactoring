package xmlrefactoring.plugin.logic.rename.test;

import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

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

	private final String TEST_TYPE = "baseType";
	private final String TEST_NAMESPACE = "http://www.example.org/createElementPathsTest";
	private final String SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
	private final String SCHEMA_COMPLEX_TYPE = "complexType";
	private final String CREATE_ELEMENT_PATHS_TEST_SCHEMA_PATH = "/PluginTest/src/xPathCreator/createElementPathsTest.xsd";
	private final String[] EXPECTED_PATHS = {
			"/globalElement/internalElement",
			"/globalElement2/moreComplexElement/internalElement",
			"/globalElement3/anonymousType/internalElement",
			"/globalElement4/contentElement/internalElement",
			"/globalElement4/internalElement"};/* ,
			"/globalElement5/globalElement/internalElement"}; */
	
	
	@Test
	public void testCreateElementPaths() throws CoreException {
		QualifiedName elementQName = new QualifiedName(TEST_NAMESPACE, TEST_TYPE);
		QualifiedName typeQName = new QualifiedName(SCHEMA_NAMESPACE, SCHEMA_COMPLEX_TYPE);

		String fileStr = CREATE_ELEMENT_PATHS_TEST_SCHEMA_PATH;
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
		Element complexType = attr.getOwnerElement();
		Element internalElement = (Element) complexType.getElementsByTagName("element").item(0);
		List<String> paths = XPathCreator.createElementPaths(internalElement);

		Assert.assertEquals("Expected " + EXPECTED_PATHS.length + " paths, actual: "
				+ paths.size(),EXPECTED_PATHS.length, paths.size());
		for(String path : EXPECTED_PATHS)
			Assert.assertTrue("Expected path " + path + " wasn`t created.",paths.contains(path));	
	}

}
