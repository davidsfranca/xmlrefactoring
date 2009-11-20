package xmlrefactoring.plugin.refactoring.test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.core.runtime.CoreException;
import org.junit.Assert;
import org.junit.Test;

import xmlrefactoring.plugin.refactoring.RenameAttributeRefactoring;
import xmlrefactoring.plugin.refactoring.RenameElementRefactoring;

public class RenameAttributeRefactoringTest {
	@Test
	public void testRenameElementRefactoring() throws CoreException {
		String newAt = "newAt";
		QName attr=new QName("http://uriAttr","oldAt");
		
		List<List<QName>> paths =  new ArrayList();
		
		List<QName> path1 = new ArrayList<QName>();
		QName element1 = new QName("http://uri1","elem1");
		path1.add(element1);
		QName element2 = new QName("http://uri2","elem2");
		path1.add(element2);
		paths.add(path1);
		
		List<QName> path2 = new ArrayList<QName>();
			
		QName element3 = new QName("http://uri3","elem3");
		path2.add(element3);
		paths.add(path2);
		
		RenameAttributeRefactoring test = new RenameAttributeRefactoring(paths,newAt,attr);

		RenameAttributeRefactoring reverse = (RenameAttributeRefactoring) test.getReverseRefactoring();
		Assert.assertNotNull(reverse);
		Assert.assertEquals(reverse.getNewName(), attr.getLocalPart());
		Assert.assertEquals(reverse.getAttrName(), new QName(attr.getNamespaceURI(),newAt));

	}
}
