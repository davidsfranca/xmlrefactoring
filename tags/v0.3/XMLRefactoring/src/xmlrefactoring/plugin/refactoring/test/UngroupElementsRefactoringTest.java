package xmlrefactoring.plugin.refactoring.test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import xmlrefactoring.plugin.refactoring.GroupElementsRefactoring;
import xmlrefactoring.plugin.refactoring.UngroupElementsRefactoring;

public class UngroupElementsRefactoringTest extends TestCase{
	

	@Test
	public void testUngroupElementsRefactoring(){
		
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
		
		paths.add(path2);
		pathsR.add(path2R);
		
		List<QName> elementsGrouped =  new ArrayList<QName>();
		QName el1 = new QName("","el1");
		elementsGrouped.add(el1);
		QName el2 = new QName("","el2");
		elementsGrouped.add(el2);
		
		UngroupElementsRefactoring ref = new UngroupElementsRefactoring(pathsR,elementsGrouped);
		GroupElementsRefactoring rev = (GroupElementsRefactoring) ref.getReverseRefactoring();
		Assert.assertEquals(rev.getInGroup(), elementsGrouped);
		for(int i =0; i<rev.getPaths().size();i++)
			Assert.assertArrayEquals(rev.getPaths().get(i).toArray(), paths.get(i).toArray());
	}

}
