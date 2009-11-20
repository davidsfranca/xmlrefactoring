package xmlrefactoring.plugin.refactoring.test;

import java.util.List;

import javax.xml.namespace.QName;

import junit.framework.Assert;

import org.junit.Test;

import xmlrefactoring.plugin.refactoring.RemoveVersioning;
import xmlrefactoring.plugin.refactoring.VersioningRefactoring;

public class VersioningRefactoringTest {
	
	@Test
	public void VersioningRefactoringTest() {
		VersioningRefactoring ver = new VersioningRefactoring(2);
		Assert.assertEquals(((VersioningRefactoring) ver.getReverseRefactoring()).getVersionNumber(), 1);
	}
	@Test
	public void VersioningFirstRefactoringTest() {
		VersioningRefactoring ver = new VersioningRefactoring(1);
		Assert.assertNotNull((RemoveVersioning) ver.getReverseRefactoring());
	}

}
