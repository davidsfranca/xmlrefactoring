package xmlrefactoring.plugin.refactoring.test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.eclipse.core.runtime.CoreException;
import org.junit.Assert;
import org.junit.Test;

import xmlrefactoring.plugin.refactoring.RenameElementRefactoring;

public class RenameElementRefactoringTest extends TestCase{
	
	@Test
	public void testRenameElementRefactoring() throws CoreException {
		String newEl = "newEl";
		String oldEl = "oldEl";
		
		List<List<QName>> paths =  new ArrayList();
		List<List<QName>> pathsR =  new ArrayList();
		
		List<QName> path1 = new ArrayList<QName>();
		List<QName> path1R = new ArrayList<QName>();
		
		QName element1 = new QName("http://uri1","elem1");
		path1.add(element1);
		path1R.add(element1);
		QName element2 = new QName("http://uri2",oldEl);
		QName element2R = new QName("http://uri2",newEl);
		path1.add(element2);
		path1R.add(element2R);
		
		paths.add(path1);
		pathsR.add(path1R);
		
		List<QName> path2 = new ArrayList<QName>();
		List<QName> path2R = new ArrayList<QName>();
		
		QName element3 = new QName("http://uri3",oldEl);
		QName element3R = new QName("http://uri3",newEl);
		path2.add(element3);
		path2R.add(element3R);
		
		paths.add(path2);
		pathsR.add(path2R);
		
		RenameElementRefactoring test = new RenameElementRefactoring(paths,newEl);
		
		RenameElementRefactoring reverse = (RenameElementRefactoring) test.getReverseRefactoring();
		Assert.assertNotNull(reverse);
		Assert.assertEquals(reverse.getNewName(), oldEl);
		for(int i= 0; i<reverse.getPaths().size();i++){
				Assert.assertArrayEquals(pathsR.get(i).toArray(), reverse.getPaths().get(i).toArray());
		}
	}
	

}
