package xmlrefactoring.plugin.logic.util.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.stream.FileImageInputStream;
import javax.swing.ProgressMonitor;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import junit.framework.Assert;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.junit.After;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import xmlrefactoring.plugin.xslt.FileControl;

public class FileControlTest {
	
	private static final String ABSOLUTE_ROOT_PATH = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	private static final String BASE_PATH = "/PluginTest/src/fileControl";
	private static final String UNDER_VERSION_CONTROL_FILE = "underVersionControlTest.xsd";
	private static final String NOT_UNDER_VERSION_CONTROL_FILE = "notUnderVersionControlTest.xsd";
	private static final String BASE_REF_PATH = BASE_PATH + "/.REF_underVersionControlTest";	
	private static final String UNDER_VERSION_CONTROL_PATH = BASE_PATH + "/" + UNDER_VERSION_CONTROL_FILE;
	private static final String NOT_UNDER_VERSION_CONTROL_PATH = BASE_PATH + "/" + NOT_UNDER_VERSION_CONTROL_FILE;
	private static final String INITIAL_XSL_PATH = BASE_PATH + "/.REF_underVersionControlTest/.v_1/.ref_2.xsl";
	private static final String NEXT_XSL_PATH = BASE_PATH + "/.REF_underVersionControlTest/.v_5/.ref_2.xsl";
	private static final String INITIAL_REVERSE_XSL_PATH = BASE_PATH + "/.REF_underVersionControlTest/.v_1/.ref_-2.xsl";
	private static final String NEXT_REVERSE_XSL_PATH = BASE_PATH + "/.REF_underVersionControlTest/.v_5/.ref_-2.xsl";
	private static final String NEW_VERSION_DIR = BASE_REF_PATH + "/.v_6";
	private static final String NEW_VERSION_REFACTORING = NEW_VERSION_DIR + "/.ref_1.xsl";
	private static final String NEW_VERSION_REVERSE_REF = NEW_VERSION_DIR + "/.ref_-1.xsl";
	private static final String DESC_PATH = BASE_PATH + "/.desc_underVersionControlTest.xml";
	private static final String FILETAGXPATH = "//lastFileNumber";
	private static final int[] expectedDescriptor = {5,1};	
	
	private IFile underVersionControlFile;
	private IWorkspaceRoot workspaceRoot;
	
	private IFolder newVersionDir;
	private IFile newVersionRef;
	private IFile newVersionRevRef;
	
	public FileControlTest() throws CoreException{
		workspaceRoot = ResourcesPlugin.getWorkspace().getRoot(); 
		workspaceRoot.refreshLocal(IResource.DEPTH_INFINITE, null);
		Path path = new Path(UNDER_VERSION_CONTROL_PATH);
		underVersionControlFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		newVersionDir = workspaceRoot.getFolder(new Path(NEW_VERSION_DIR));
		newVersionRef = workspaceRoot.getFile(new Path(NEW_VERSION_REFACTORING));
		newVersionRevRef = workspaceRoot.getFile(new Path(NEW_VERSION_REVERSE_REF));
	}
	
	@Test
	public void isUnderVersionControlTest(){		
		Assert.assertTrue("The file expected to be under version control is not", FileControl.isUnderVersionControl(underVersionControlFile));
		Path path = new Path(NOT_UNDER_VERSION_CONTROL_PATH);
		IFile notUnderVersionControlFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		Assert.assertFalse("The file not expected to be under version control is", FileControl.isUnderVersionControl(notUnderVersionControlFile));
	}
	
	@Test
	public void getNextPathTest(){
		//isInitial = true
		String actualPath = FileControl.getNextPath(underVersionControlFile, true).toString();
		Assert.assertEquals(INITIAL_XSL_PATH, actualPath);
		
		//isInitial = false
		actualPath = FileControl.getNextPath(underVersionControlFile, false).toString();
		Assert.assertEquals(NEXT_XSL_PATH, actualPath);
		
	}
	
	@Test
	public void getNextReversePathTest(){
		//isInitial = true
		String actualPath = FileControl.getNextReversePath(underVersionControlFile, true).toString();
		Assert.assertEquals(INITIAL_REVERSE_XSL_PATH, actualPath);
		
		//isInitial = false
		actualPath = FileControl.getNextReversePath(underVersionControlFile, false).toString();
		Assert.assertEquals(NEXT_REVERSE_XSL_PATH, actualPath);		
	}
	
	@Test
	public void readDescriptorTest(){
		int[] actualDescriptor =  FileControl.readDescriptor(underVersionControlFile);
		Assert.assertEquals(expectedDescriptor[0], actualDescriptor[0]);
		Assert.assertEquals(expectedDescriptor[1], actualDescriptor[1]);
	}
	
	@Test
	public void getAllXSTest(){
		
		int oldVersion = 1;
		int newVersion = 4;
		int[] numberOfRefs = {0, 2, 1, 4, 0, 1};
		
		//Tests for a regular incremental initial and final version
		List<File> expectedIncrementalFiles = new ArrayList<File>();
		for(int i = oldVersion + 1; i <= newVersion; i++){
			for(int j = 1; j <= numberOfRefs[i]; j++){
				String filePath = createFilePath(i,j);
				expectedIncrementalFiles.add(new File(filePath));
			}
		}		
		List<File> actualFiles = FileControl.getAllXSL(underVersionControlFile.getLocation().toFile(), oldVersion, newVersion);
		Assert.assertEquals(expectedIncrementalFiles, actualFiles);
		
		//Tests for a regular decremental initial and final version
		List<File> expectedDecrementalFiles = new ArrayList<File>();
		for(int i = newVersion; i > oldVersion; i--){
			for(int j = -numberOfRefs[i]; j <= -1; j++){
				String filePath = createFilePath(i,j);
				expectedDecrementalFiles.add(new File(filePath));
			}
		}		
		actualFiles = FileControl.getAllXSL(underVersionControlFile.getLocation().toFile(), newVersion, oldVersion);
		Assert.assertEquals(expectedDecrementalFiles, actualFiles);		
	}

	private String createFilePath(int i, int j) {
		StringBuilder sb = new StringBuilder();
		sb.append(ABSOLUTE_ROOT_PATH);
		sb.append(BASE_REF_PATH);
		sb.append("/.v_");
		sb.append(i);
		sb.append("/.ref_");
		sb.append(j);
		sb.append(".xsl");
		return sb.toString();
	}
	
	//Tests createVersioningDir too
	@Test
	public void createVersioningRefactoringTest() throws CoreException{
		
		Assert.assertFalse(newVersionDir.exists());
		Assert.assertFalse(newVersionRef.exists());
		Assert.assertFalse(newVersionRevRef.exists());
		
		Change versionDirCreation = FileControl.createVersioningDir(underVersionControlFile, 6);
		versionDirCreation.perform(null);
		CompositeChange versioningRefactoring = FileControl.createVersioningRefactoring(underVersionControlFile, 6);
		for(Change change : versioningRefactoring.getChildren())
			change.perform(null);
		ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);		
		
		Assert.assertTrue(newVersionDir.exists());
		Assert.assertTrue(newVersionRef.exists());
		Assert.assertTrue(newVersionRevRef.exists());
	}
	
	@Test
	public void incrementLastFileTest() throws CoreException, SAXException, IOException, ParserConfigurationException, XPathExpressionException{
		Path path = new Path(UNDER_VERSION_CONTROL_PATH);
		IFile underVersionControlFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		
		Change  changeLastFile = FileControl.incrementLastFile(underVersionControlFile);	
		//TODO: investigar como fazer esse perform
		//changeLastFile.perform();
		FileInputStream descriptor = new FileInputStream(new File(ABSOLUTE_ROOT_PATH+DESC_PATH));

		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse (descriptor);

		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile(FILETAGXPATH);
		Node file = ((NodeList)expr.evaluate(doc, XPathConstants.NODESET)).item(0);
		//Assert.assertEquals("2",file.getTextContent());
		
		changeLastFile.dispose();
		
	}
	@After
	public void cleanUp() throws CoreException{
		newVersionRef.delete(true, null);
		newVersionRevRef.delete(true, null);
		newVersionDir.delete(true, null);
	}
	
}
