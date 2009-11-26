package xmlrefactoring.plugin.logic.rename.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.apache.velocity.runtime.resource.Resource;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import xmlrefactoring.plugin.logic.rename.XSDRenameParticipant;
import xmlrefactoring.plugin.logic.rename.external.RenameRefactoringArguments;

public class XSDRenameTest {
	
	private static final String newElementName = "elementNewName";
	private static final String TARGET_FILE_PATH = "/PluginTest/src/renameTest/target.xsd";
	private File target;
	
	@Before
	public void beforeTest() throws CoreException{
		ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
	}
	
	@After
	public void afterTest(){
		target.delete();
	}
	
	//TODO
	
	public void renameElementTest() throws ParserConfigurationException, SAXException, IOException, CoreException{
		//Creates the original file copying the base file
		IFile base = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path("/PluginTest/src/renameTest/base.xsd"));
		base.copy(new Path(TARGET_FILE_PATH), true, null);
		
		target = new Path(ResourcesPlugin.getWorkspace().getRoot().getLocation() + TARGET_FILE_PATH).toFile();		
		
		XSDRenameParticipant participant = new XSDRenameParticipant();
		XSDNamedComponent component = getXSDElement(target); 
		List<XSDNamedComponent> components = new ArrayList<XSDNamedComponent>();
		components.add(component);
		RenameRefactoringArguments arguments = new RenameRefactoringArguments(components);
		arguments.setNewName(newElementName);
		participant.initialize(component);
		participant.initialize(arguments);
		Change change = participant.createChange(null);
		performChanges(change);	
		
		XSDNamedComponent newComponent = getXSDElement(target);
		Assert.assertEquals(newElementName, newComponent.getName());
	}
	
	private void performChanges(Change change) throws CoreException {
		if(change instanceof CompositeChange){
			CompositeChange composite = (CompositeChange) change;
			for(Change child : composite.getChildren())
				performChanges(child);
		}
		else
			change.perform(null);
	}

	private XSDElementDeclaration getXSDElement(File file) throws FileNotFoundException{		
		return getXSDSchema(file).getElementDeclarations().get(0);
	}
	
	private XSDSchema getXSDSchema(File file) throws FileNotFoundException{
		XSDParser parser = new XSDParser(null);
		parser.parse(new FileInputStream(file));
		return parser.getSchema();
	}
	
}
