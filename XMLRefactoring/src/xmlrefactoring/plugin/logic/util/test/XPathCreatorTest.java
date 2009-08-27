package xmlrefactoring.plugin.logic.util.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.namespace.QName;

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

import xmlrefactoring.plugin.logic.util.XPathCreator;

public class XPathCreatorTest {

	private final String TEST_TYPE = "baseType";
	private final String TEST_ATTRIBUTE_GROUP = "baseGroup";
	private final String TEST_NAMESPACE = "http://www.example.org/createElementPathsTest";
	private final String SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
	private final String SCHEMA_COMPLEX_TYPE = "complexType";
	private final String SCHEMA_ATTRIBUTE_GROUP = "attributeGroup";
	private final String CREATE_ELEMENT_PATHS_TEST_SCHEMA_PATH = "/PluginTest/src/xPathCreator/createElementPathsTest.xsd";
	private final QName[][] EXPECTED_ELEMENT_PATHS = {
			{new QName(TEST_NAMESPACE, "globalElement"), new QName("internalElement")},
			{new QName(TEST_NAMESPACE, "globalElement2"), new QName("moreComplexElement"), new QName("internalElement")},
			{new QName(TEST_NAMESPACE, "globalElement3"), new QName("anonymousType"), new QName("internalElement")},
			{new QName(TEST_NAMESPACE, "globalElement4"), new QName("contentElement"), new QName("internalElement")},
			{new QName(TEST_NAMESPACE, "globalElement4"), new QName("internalElement")},
			{new QName(TEST_NAMESPACE, "globalElement6"), new QName("groupElement"), new QName("internalElement")}
	};
	private final QName[][] EXPECTED_ATTRIBUTE_PATHS = {
			{new QName(TEST_NAMESPACE, "globalElement"), new QName("internalAttribute")},
			{new QName(TEST_NAMESPACE, "globalElement2"), new QName("moreComplexElement"),new QName("internalAttribute")},
			{new QName(TEST_NAMESPACE, "globalElement3"), new QName("anonymousType"), new QName("internalAttribute")},
			{new QName(TEST_NAMESPACE, "globalElement4"), new QName("contentElement"), new QName("internalAttribute")},
			{new QName(TEST_NAMESPACE, "globalElement4"), new QName("internalAttribute")},
			{new QName(TEST_NAMESPACE, "globalElement6"), new QName("groupElement"), new QName("internalAttribute")}
	};	
	private final QName[][] EXPECTED_ATTRIBUTE_GROUP_PATHS = {
			{new QName(TEST_NAMESPACE, "globalElement7"), new QName("internalAttribute")}
	};

	@Test
	public void testCreateElementPaths() throws CoreException {

		QualifiedName elementQName = new QualifiedName(TEST_NAMESPACE, TEST_TYPE);
		QualifiedName typeQName = new QualifiedName(SCHEMA_NAMESPACE, SCHEMA_COMPLEX_TYPE);

		String fileStr = CREATE_ELEMENT_PATHS_TEST_SCHEMA_PATH;
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileStr));

		file.refreshLocal(0, null);

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
		List<List<QName>> paths = XPathCreator.createPaths(internalElement);

		Assert.assertEquals("Expected " + EXPECTED_ELEMENT_PATHS.length + " paths, actual: "
				+ paths.size(),EXPECTED_ELEMENT_PATHS.length, paths.size());
		for(QName[] path : EXPECTED_ELEMENT_PATHS){
			List pathList = new ArrayList<QName>();
			for(QName qName : path)
				pathList.add(qName);
			Assert.assertTrue("Expected path " + pathList + " wasn`t created.",paths.contains(pathList));
		}
	}

	@Test
	public void testCreateAttributePaths() throws CoreException{
		QualifiedName elementQName = new QualifiedName(TEST_NAMESPACE, TEST_TYPE);
		QualifiedName typeQName = new QualifiedName(SCHEMA_NAMESPACE, SCHEMA_COMPLEX_TYPE);

		String fileStr = CREATE_ELEMENT_PATHS_TEST_SCHEMA_PATH;
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileStr));

		file.refreshLocal(0, null);

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
		Element internalAttribute = (Element) complexType.getElementsByTagName("attribute").item(0);
		List<List<QName>> paths = XPathCreator.createPaths(internalAttribute);

		Assert.assertEquals("Expected " + EXPECTED_ATTRIBUTE_PATHS.length + " paths, actual: "
				+ paths.size(),EXPECTED_ATTRIBUTE_PATHS.length, paths.size());
		
		for(QName[] path : EXPECTED_ATTRIBUTE_PATHS){
			List pathList = new ArrayList<QName>();
			for(QName qName : path)
				pathList.add(qName);
			Assert.assertTrue("Expected path " + pathList + " wasn`t created.",paths.contains(pathList));
		}
	}


	@Test
	public void testCreateAttributeGroupPaths() throws CoreException{
		QualifiedName attributeGroupQName = new QualifiedName(TEST_NAMESPACE, TEST_ATTRIBUTE_GROUP);
		QualifiedName attributeTypeQName = new QualifiedName(SCHEMA_NAMESPACE, SCHEMA_ATTRIBUTE_GROUP);

		String fileStr = CREATE_ELEMENT_PATHS_TEST_SCHEMA_PATH;
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileStr));

		file.refreshLocal(0, null);

		SearchScope scope = new WorkspaceSearchScope();
		CollectingSearchRequestor requestor = new CollectingSearchRequestor();
		SearchPattern pattern = new XMLComponentDeclarationPattern(file, attributeGroupQName, attributeTypeQName);
		SearchEngine searchEngine = new SearchEngine();
		HashMap map = new HashMap();
		searchEngine.search(pattern, requestor, scope, map, new NullProgressMonitor());
		List<SearchMatch> matches = requestor.getResults();
		SearchMatch match = matches.get(0);

		Node node = (Node)match.getObject();
		Attr attr = (Attr) node;
		Element attributeGroup = attr.getOwnerElement();
		Element internalAttribute = (Element) attributeGroup.getElementsByTagName("attribute").item(0);
		List<List<QName>> paths = XPathCreator.createPaths(internalAttribute);

		Assert.assertEquals("Expected " + EXPECTED_ATTRIBUTE_GROUP_PATHS.length + " paths, actual: "
				+ paths.size(),EXPECTED_ATTRIBUTE_GROUP_PATHS.length, paths.size());
		
		for(QName[] path : EXPECTED_ATTRIBUTE_GROUP_PATHS){
			List pathList = new ArrayList<QName>();
			for(QName qName : path)
				pathList.add(qName);
			Assert.assertTrue("Expected path " + pathList + " wasn`t created.",paths.contains(pathList));
		}
	}
}
