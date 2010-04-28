package xmlrefactoring.plugin.refactoring.test;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import xmlrefactoring.plugin.refactoring.RemoveVersioning;
import xmlrefactoring.plugin.refactoring.VersioningRefactoring;

public class VersioningRefactoringTest extends TestCase{
	
	@Test
	public void testVersioningRefactoring() {
		VersioningRefactoring ver = new VersioningRefactoring(2);
		Assert.assertEquals(((VersioningRefactoring) ver.getReverseRefactoring()).getVersionNumber(), 1);
	}
	@Test
	public void testVersioningFirstRefactoring() {
		VersioningRefactoring ver = new VersioningRefactoring(1);
		Assert.assertNotNull((RemoveVersioning) ver.getReverseRefactoring());
	}

}
