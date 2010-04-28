package xmlrefactoring.plugin.refactoring.test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import xmlrefactoring.plugin.refactoring.GroupElementsRefactoring;
import xmlrefactoring.plugin.refactoring.UngroupElementsRefactoring;

public class GroupElementsRefactoringTest extends TestCase{

	@Test
	public void testGroupElementsRefactoring(){
		
		QName group = new QName("http://uriGroup", "group");
		
		List<List<QName>> paths =  new ArrayList();
		List<List<QName>> pathsR =  new ArrayList();
		
		List<QName> path1 = new ArrayList<QName>();
		List<QName> path1R = new ArrayList<QName>();
		
		QName element1 = new QName("http://uri1","elem1");
		path1.add(element1);
		path1R.add(element1);
		QName element2 = new QName("http://uri2","elem2");
		path1.add(element2);
		path1R.add(element2);
		path1R.add(group);
		
		paths.add(path1);
		pathsR.add(path1R);
		
		List<QName> path2 = new ArrayList<QName>();
		List<QName> path2R = new ArrayList<QName>();
		
		QName element3 = new QName("http://uri3","elem3");
		path2.add(element3);
		path2R.add(element3);
		path2R.add(group);
		
		List<QName> elementsGrouped =  new ArrayList<QName>();
		QName el1 = new QName("","el1");
		elementsGrouped.add(el1);
		QName el2 = new QName("","el2");
		elementsGrouped.add(el2);
		
		GroupElementsRefactoring ref = new GroupElementsRefactoring(paths,group,elementsGrouped);
		UngroupElementsRefactoring rev = (UngroupElementsRefactoring) ref.getReverseRefactoring();
		Assert.assertEquals(rev.getInGroup(), elementsGrouped);
		for(int i =0; i<rev.getPaths().size();i++)
			Assert.assertArrayEquals(rev.getPaths().get(i).toArray(), pathsR.get(i).toArray());
	}
}
