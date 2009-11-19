package xmlrefactoring.plugin.xslt;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Status;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import xmlrefactoring.XMLRefactoringMessages;
import xmlrefactoring.plugin.PluginNamingConstants;
import xmlrefactoring.plugin.XMLRefactoringPlugin;
import xmlrefactoring.plugin.logic.util.CreateFileChange;
import xmlrefactoring.plugin.logic.util.CreateFolderChange;
import xmlrefactoring.plugin.logic.util.CreateXSLChange;
import xmlrefactoring.plugin.logic.util.XMLUtil;
import xmlrefactoring.plugin.refactoring.VersioningRefactoring;

public class FileControl {

	//CONSTANTS - PREFIX
	private static final String DIRECTORYPREFIX = ".REF_";
	private static final String VERSIONPREFIX = ".v_";
	private static final String FILEPREFIX = ".ref_";
	private static final String FILEEXTENSION = ".xsl";
	private static final String DESCRIPTORPREFIX = "/.desc_";
	private static final String DESCFILEEXTENSION = ".xml";

	//CONSTANTS - TAGS
	private static final String DESCRIPTORTAG = "descriptor";
	private static final String FILETAG = "lastFileNumber";
	private static final String VERSIONTAG = "lastVersion";
	private static final String VERSIONATTR = "number";
	private static final String AllVERSIONSTAG = "versions";
	private static final String VERSIONINFOTAG = "version";

	//CONSTANT - XPATH EXPRESSION
	private static final String VERSIONXPATH = "descriptor/versions/version";

	private static String INITIAL_DESCRIPTOR; 

	//Consult data from XSL and XSL control files

	/**
	 * Tests if there is already a descriptor and 
	 * @return
	 */
	public static boolean isUnderVersionControl(IFile schemaFile){
		IContainer root = schemaFile.getWorkspace().getRoot();
		return root.exists(getDescriptorFilePath(schemaFile));
	}

	private static String getInitialDescriptor() throws CoreException{
		if(INITIAL_DESCRIPTOR == null)
			INITIAL_DESCRIPTOR = createInitialDescriptor();
		return INITIAL_DESCRIPTOR;
	}
	
	private static String createInitialDescriptor() throws CoreException{
		Element descriptor = XMLUtil.createElement(DESCRIPTORTAG);
		Element allVersions = descriptor.getOwnerDocument().createElement(AllVERSIONSTAG);
		allVersions.setTextContent("");
		descriptor.appendChild(allVersions);
		Element lastFile = descriptor.getOwnerDocument().createElement(FILETAG);
		lastFile.setTextContent("2");
		descriptor.appendChild(lastFile);
		Element lastVersion = descriptor.getOwnerDocument().createElement(VERSIONTAG);
		lastVersion.setTextContent("1");
		descriptor.appendChild(lastVersion);		
		return XMLUtil.toString(descriptor);
	}

	/**
	 * Return where the next refactoring file should be created
	 * 
	 * @param schemaFile
	 * @param isInitial - flag to know if there is already an descriptor for that file or not
	 * @return
	 * @throws CoreException 
	 */
	public static IPath getNextPath(IFile schemaFile, boolean isInitial) throws CoreException{

		IPath fileName=null;
		if(isInitial)
			fileName = getFilePath(schemaFile, 1, 2);
		else{
			int[] versionAndFile = readDescriptor(schemaFile);
			fileName = getFilePath(schemaFile, versionAndFile[0], versionAndFile[1]+1);
		}
		return fileName;		
	}

	/**
	 * Return where the next reverse refactoring file should be created
	 * 
	 * @param schemaFile
	 * @param isInitial - flag to know if there is already an descriptor for that file or not
	 * @return
	 * @throws CoreException 
	 */
	public static IPath getNextReversePath(IFile schemaFile, boolean isInitial) throws CoreException {
		IPath path;
		if(isInitial){
			path = getFilePath(schemaFile, 1, -2);
		}else{
			int[] versionAndFile = readDescriptor(schemaFile);
			path = getFilePath(schemaFile, versionAndFile[0], -versionAndFile[1]-1);
		}
		return path;
	}

	/**
	 * Reads the descriptor file for that Schema 
	 * Gets the last version and file Number
	 * Only called when it is known that the descriptor file is available
	 * @param schemaFile
	 * @return [0]: version number, [1]: file number
	 * @throws CoreException 
	 */	
	public static int[] readDescriptor (IFile schemaFile) throws CoreException {

		int[] versionAndFile = new int[2];
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse (getDescriptorFileAbsolutePath(schemaFile).toFile());

			NodeList versionNode = doc.getElementsByTagName(VERSIONTAG);
			NodeList fileNode = doc.getElementsByTagName(FILETAG);

			if(versionNode.getLength()!=1 || fileNode.getLength()!=1){
				Status status = new Status(Status.ERROR, 
						XMLRefactoringPlugin.PLUGIN_ID, 
						XMLRefactoringMessages.getString("FileControl.InvalidDescriptorFile"));
				throw new CoreException(status);
			}
			
			versionAndFile[0] = (new Integer(versionNode.item(0).getTextContent())).intValue();
			versionAndFile[1] = (new Integer(fileNode.item(0).getTextContent())).intValue();

		}catch (SAXException e) {
			Status status = new Status(Status.ERROR, 
					XMLRefactoringPlugin.PLUGIN_ID, 
					XMLRefactoringMessages.getString("FileControl.DescriptorFileProblem"), e);
			throw new CoreException(status);
		} catch (IOException e) {
			Status status = new Status(Status.ERROR, 
					XMLRefactoringPlugin.PLUGIN_ID, 
					XMLRefactoringMessages.getString("FileControl.DescriptorFileInaccessible"), e);
			throw new CoreException(status);
		} catch (ParserConfigurationException e) {
			Status status = new Status(Status.ERROR, 
					XMLRefactoringPlugin.PLUGIN_ID, 
					XMLRefactoringMessages.getString("FileControl.DescriptorFileProblem"), e);
			throw new CoreException(status);
		} catch(Exception e){
			Status status = new Status(Status.ERROR, 
					XMLRefactoringPlugin.PLUGIN_ID, 
					XMLRefactoringMessages.getString("FileControl.DescriptorFileProblem"), e);
			throw new CoreException(status);
		}
		return versionAndFile;	
	}

	/**
	 * Reads the descriptor file for that Schema 
	 * Gets the last file Number for each version
	 * @param schemafile
	 * @return
	 * @throws CoreException 
	 */
	public static List<File> getAllXSL(File schemaFile, int startVersion, int finalVersion) throws CoreException{

		List<File> files = new ArrayList<File>();
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

		try{
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();		
			Document doc = docBuilder.parse (getDescriptorFile(schemaFile));

			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr;

			if(startVersion<=finalVersion){
				for(int i = startVersion+1; i <= finalVersion; i++){
					expr = xpath.compile(VERSIONXPATH+"[@"+VERSIONATTR+"="+i+"]");
					Node file = ((NodeList)expr.evaluate(doc, XPathConstants.NODESET)).item(0);
					int maxFile = new Integer(file.getFirstChild().getNodeValue());
					for(int j = 1; j<=maxFile; j++){
						files.add(new File(getFilePath(schemaFile, i, j)));
					}
				}
			}else if(startVersion!=finalVersion){
				for(int i = startVersion; i > finalVersion; i--){
					expr = xpath.compile(VERSIONXPATH+"[@"+VERSIONATTR+"="+i+"]");
					Node file = ((NodeList)expr.evaluate(doc, XPathConstants.NODESET)).item(0);
					int maxFile = new Integer(file.getFirstChild().getNodeValue());
					for(int j = -maxFile; j<= -1; j++){
						files.add(new File(getFilePath(schemaFile, i, j)));
					}
				}

			}

		} catch (Exception e) {
			Status status = new Status(Status.ERROR, 
					XMLRefactoringPlugin.PLUGIN_ID, 
					XMLRefactoringMessages.getString("FileControl.XSLRecover"), e);
			throw new CoreException(status);			
		}
		return files;
	}

	public static IPath getDescriptorFilePath(IFile schemaFile){
		IContainer container = schemaFile.getParent();
		IPath descPath = container.getFullPath().append(buildDescriptorFilePath(schemaFile));		
		return descPath;
	}

	private static IPath getDescriptorFileAbsolutePath(IFile schemaFile){
		IContainer container = schemaFile.getParent();
		return container.getLocation().append(buildDescriptorFilePath(schemaFile));
	}

	public static IFile getDescriptorFile(IFile schemaFile){
		IPath path = getDescriptorFilePath(schemaFile);
		return ResourcesPlugin.getWorkspace().getRoot().getFile(path);
	}

	private static String buildDescriptorFilePath(IFile schemaFile){
		//Build the descriptorFilePath
		StringBuilder filePath = new StringBuilder(DESCRIPTORPREFIX);
		filePath.append(schemaFile.getName().substring(0,schemaFile.getName().length()-4));
		filePath.append(DESCFILEEXTENSION);

		return filePath.toString();
	}

	private static IPath getRefactoringDirPath(IFile schemaFile){
		IContainer container = schemaFile.getParent();

		//Build the descriptorFilePath
		StringBuilder dirPath = new StringBuilder("/");
		dirPath.append(DIRECTORYPREFIX);
		dirPath.append(schemaFile.getName().substring(0,schemaFile.getName().length()-4));
		dirPath.append("/");

		return container.getFullPath().append(dirPath.toString());
	}

	private static IPath getVersionDirPath(IFile schemaFile , int version){
		IPath dir =  getRefactoringDirPath(schemaFile);
		StringBuilder dirPath = new StringBuilder("/");
		dirPath.append(VERSIONPREFIX);
		dirPath.append(version);

		return dir.append(dirPath.toString());

	}

	private static IPath getFilePath(IFile schemaFile, int version, Integer fileNumber){
		StringBuffer fileName = new StringBuffer("/");
		fileName.append(FILEPREFIX);
		fileName.append(fileNumber.toString());
		fileName.append(FILEEXTENSION);

		IPath dir = getVersionDirPath(schemaFile, version);
		return dir.append(fileName.toString());
	}

	private static String getDescriptorFileAbsolutePath(File schemaFile){
		String container = schemaFile.getParent();
		return container.concat(buildDescriptorFilePath(schemaFile));
	}

	private static File getDescriptorFile(File schemaFile){
		String descriptorPath = getDescriptorFileAbsolutePath(schemaFile);
		return new File(descriptorPath);
	}

	private static String buildDescriptorFilePath(File schemaFile){
		//Build the descriptorFilePath
		StringBuilder filePath = new StringBuilder(DESCRIPTORPREFIX);
		filePath.append(schemaFile.getName().substring(0,schemaFile.getName().length()-4));
		filePath.append(DESCFILEEXTENSION);

		return filePath.toString();
	}

	/**
	 * Returns the absolute path of the refactoring directory
	 * @param schemaFile
	 * @return
	 */
	private static String getRefactoringDirPath(File schemaFile){
		StringBuilder refactoringDirPath = new StringBuilder();
		refactoringDirPath.append(schemaFile.getParent());

		refactoringDirPath.append("/");
		refactoringDirPath.append(DIRECTORYPREFIX);
		refactoringDirPath.append(schemaFile.getName().substring(0,schemaFile.getName().length()-4));
		refactoringDirPath.append("/");

		return refactoringDirPath.toString();
	}

	/**
	 * Returns the absolute path of this XSL directory
	 * @param schemaFile
	 * @param version
	 * @return
	 */
	private static String getVersionDirPath(File schemaFile , int version){
		StringBuilder versionDirPath = new StringBuilder();
		versionDirPath.append(getRefactoringDirPath(schemaFile));
		versionDirPath.append("/");
		versionDirPath.append(VERSIONPREFIX);
		versionDirPath.append(version);

		return versionDirPath.toString();
	}

	/**
	 * Returns the absolute path of this XSL file
	 * @param schemaFile
	 * @param version
	 * @param fileNumber
	 * @return
	 */
	private static String getFilePath(File schemaFile, int version, Integer fileNumber){
		StringBuilder filePath = new StringBuilder();
		filePath.append(getVersionDirPath(schemaFile, version));		
		filePath.append("/");
		filePath.append(FILEPREFIX);
		filePath.append(fileNumber.toString());
		filePath.append(FILEEXTENSION);

		return filePath.toString();
	}

	//Create changes into the XSL and XSL control files

	/**
	 * Creates all the required structure to versioning the refactorings in a XSD file
	 * @param schemaFile
	 * @return
	 * @throws CoreException 
	 */
	public static CompositeChange addToVersionControl(IFile schemaFile) throws CoreException{
		CompositeChange allChanges = new CompositeChange(XMLRefactoringMessages.getString("FileControl.CompositeName"));

		//Create descriptor file
		Change descriptorCreation = new CreateFileChange(getDescriptorFilePath(schemaFile), new ByteArrayInputStream(getInitialDescriptor().getBytes()));		
		allChanges.add(descriptorCreation);

		//Create refactoring directory
		Change refDirCreation = new CreateFolderChange(getRefactoringDirPath(schemaFile));
		allChanges.add(refDirCreation);

		//Create version directory
		allChanges.add(createVersioningDir(schemaFile, 1));


		//Create XSL that adds version
		Change xslChange = createVersioningRefactoring(schemaFile, 1);
		allChanges.add(xslChange);

		return allChanges;
	}

	// It is called from the versioning participant
	public static Change createVersioningDir(IFile schemaFile, int version){

		return new CreateFolderChange(getVersionDirPath(schemaFile, version));

	}

	// It is called from the versioning participant
	public static CompositeChange createVersioningRefactoring(IFile schemaFile,
			int newVersion) {
		VersioningRefactoring ref = new VersioningRefactoring(newVersion);
		Change directChange = new CreateXSLChange(ref,getFilePath(schemaFile, newVersion, 1));
		Change reverseChange = new CreateXSLChange(ref.getReverseRefactoring(),getFilePath(schemaFile, newVersion, -1));

		CompositeChange versionChange = new CompositeChange("Version Change");
		versionChange.add(directChange);
		versionChange.add(reverseChange);

		return versionChange;
	}


	public static Change incrementLastFile(IFile schemaFile) throws CoreException{

		IFile descriptorFile = ResourcesPlugin.getWorkspace().getRoot().getFile(getDescriptorFilePath(schemaFile));
		IDOMModel model =  getDescriptorFileModel(descriptorFile);

		IDOMElement lastFileTag = (IDOMElement) model.getDocument().getElementsByTagName(FILETAG).item(0);

		TextChange change = new TextFileChange("Last File Increase", descriptorFile);
		int textContentOffset = lastFileTag.getStartEndOffset();
		Integer lastFileNewValue = Integer.parseInt(lastFileTag.getFirstChild().getNodeValue()) + 1;
		int length = lastFileTag.getEndStartOffset() - lastFileTag.getStartEndOffset();
		TextEdit edit = new ReplaceEdit(textContentOffset, length, lastFileNewValue.toString());
		change.setEdit(edit);
		return change;	

	}

	/**
	 * Method used to update the descriptor file when the version changes
	 * It's responsible for save the last version information, increment the last version number,
	 * change the lastFile number to 0
	 */
	public static Change updateDescriptor(IFile schemaFile) throws CoreException {

		IFile descriptorFile = ResourcesPlugin.getWorkspace().getRoot().getFile(getDescriptorFilePath(schemaFile));
		IDOMModel model = getDescriptorFileModel(descriptorFile);

		IDOMElement lastVersionTag = (IDOMElement) model.getDocument().getElementsByTagName(VERSIONTAG).item(0);
		IDOMElement lastFileTag = (IDOMElement) model.getDocument().getElementsByTagName(FILETAG).item(0);
		IDOMElement versions = (IDOMElement) model.getDocument().getElementsByTagName(AllVERSIONSTAG).item(0);

		TextChange change = new TextFileChange("Version Increase", descriptorFile);
		int textContentOffset = lastVersionTag.getStartEndOffset();
		Integer lastVersionValue = Integer.parseInt(lastVersionTag.getFirstChild().getNodeValue());
		Integer lastFileValue = Integer.parseInt(lastFileTag.getFirstChild().getNodeValue());

		int length = lastVersionTag.getEndStartOffset() - lastVersionTag.getStartEndOffset();
		TextEdit edit = new ReplaceEdit(textContentOffset, length, (new Integer(lastVersionValue+1)).toString());
		change.setEdit(edit);

		TextChange fileChange = new TextFileChange("Restart the file number counting", descriptorFile);
		int fileTextContentOffset = lastFileTag.getStartEndOffset();
		int fileLength = lastFileTag.getEndStartOffset() - lastFileTag.getStartEndOffset();
		TextEdit fileEdit = new ReplaceEdit(fileTextContentOffset, fileLength, "1");
		fileChange.setEdit(fileEdit);

		//Version information to be saved		
		Element versionInfo = XMLUtil.createElement(VERSIONINFOTAG);
		versionInfo.setAttribute(VERSIONATTR, lastVersionValue.toString());
		versionInfo.setTextContent(lastFileValue.toString());
		
		InsertEdit insertLast = new InsertEdit(versions.getEndStartOffset(), XMLUtil.toString(versionInfo));
		TextChange change2 = new TextFileChange("Save last version Info", descriptorFile);
		change2.setEdit(insertLast);

		CompositeChange changes = new CompositeChange(XMLRefactoringMessages.getString("FileControl.CompositeName"));
		changes.add(change);
		changes.add(fileChange);
		changes.add(change2);

		return changes;	

	}

	private static IDOMModel getDescriptorFileModel(IFile descriptorFile) throws CoreException{
		try{			
			return (IDOMModel) StructuredModelManager.getModelManager().getModelForEdit(descriptorFile);
		}
		catch(IOException e){
			Status status = new Status(Status.ERROR, 
					XMLRefactoringPlugin.PLUGIN_ID, 
					XMLRefactoringMessages.getString("FileControl.DescriptorModel"), e);
			throw new CoreException(status);
		}
	}

	//TODO: ƒ responsabilidade do FileControl?
	public static int getSchemaVersion(InputStream selectedXMLFile) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();				
		Document doc = docBuilder.parse (selectedXMLFile);
		String versionString = doc.getDocumentElement().getAttribute(XMLRefactoringMessages.getString("schemaVersion"));
		if(versionString.equals(""))
			return 0;
		else
			return Integer.parseInt(versionString);
	}
}
