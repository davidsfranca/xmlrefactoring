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
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import xmlrefactoring.plugin.PluginNamingConstants;
import xmlrefactoring.plugin.logic.util.CreateFileChange;
import xmlrefactoring.plugin.logic.util.CreateFolderChange;
import xmlrefactoring.plugin.logic.util.CreateXSLChange;
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

	//CONSTANT - OTHERS
	private static final String COMPOSITENAME = "Add to version control";
	//TODO: mudar para arquivo
	private static final String INITIAL_DESCRIPTOR = "<" + DESCRIPTORTAG + ">\n<" + AllVERSIONSTAG + ">\n</" + AllVERSIONSTAG + ">\n<" +
	FILETAG + ">2</" + FILETAG + ">\n<" + VERSIONTAG + ">0</" + VERSIONTAG + ">\n</" +
	DESCRIPTORTAG + ">";

	/**
	 * Tests if there is already a descriptor and 
	 * @return
	 */
	public static boolean isUnderVersionControl(IFile schemaFile){
		IContainer root = schemaFile.getWorkspace().getRoot();
		return root.exists(getDescriptorFilePath(schemaFile));
	}


	/**
	 * Return where the next refactoring file should be created
	 * 
	 * @param schemaFile
	 * @param isInitial - flag to know if there is already an descriptor for that file or not
	 * @return
	 */
	public static IPath getNextPath(IFile schemaFile, boolean isInitial){

		IPath fileName=null;
		if(isInitial)
			fileName = getFilePath(schemaFile, 0, 2);
		else{
			int[] versionAndFile = readDescriptor(schemaFile);
			fileName = getFilePath(schemaFile, versionAndFile[0], versionAndFile[1]+1);
		}
		return fileName;		
	}

	public static IPath getNextReversePath(IFile schemaFile, boolean isInitial) {
		IPath path;
		if(isInitial){
			path = getFilePath(schemaFile, 0, -2);
		}else{
			int[] versionAndFile = readDescriptor(schemaFile);
			path = getFilePath(schemaFile, versionAndFile[0], -versionAndFile[1]-1);
		}
		return path;
	}

	/**
	 * Creates all the required structure to versioning the refactorings in a XSD file
	 * @param schemaFile
	 * @return
	 */
	public static CompositeChange addToVersionControl(IFile schemaFile){
		CompositeChange allChanges = new CompositeChange(COMPOSITENAME);

		//Create descriptor file
		IContainer container = schemaFile.getParent();
		Change descriptorCreation = new CreateFileChange(getDescriptorFilePath(schemaFile), new ByteArrayInputStream(INITIAL_DESCRIPTOR.getBytes()));		
		allChanges.add(descriptorCreation);

		//Create refactoring directory
		Change refDirCreation = new CreateFolderChange(getRefactoringDirPath(schemaFile));
		allChanges.add(refDirCreation);

		//Create version directory
		allChanges.add(createVersioningDir(schemaFile, 0));


		//Create XSL that adds version
		Change xslChange = createVersioningRefactoring(schemaFile, 0);
		allChanges.add(xslChange);

		return allChanges;
	}


	/**
	 * Reads the descriptor file for that Schema 
	 * Gets the last version and file Number
	 * Only called when it is known that the descriptor file is available
	 * @param schemaFile
	 * @return [0]: version number, [1]: file number
	 */	

	public static int[] readDescriptor (IFile schemaFile) {

		int[] versionAndFile = new int[2];
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse (getDescriptorFileAbsolutePath(schemaFile).toFile());

			NodeList versionNode = doc.getElementsByTagName(VERSIONTAG);

			if(versionNode.getLength()!=1){
				//TODO:error
				System.out.println("Arquivo descritor Invalido");
			}else{
				versionAndFile[0] = (new Integer(versionNode.item(0).getTextContent())).intValue();
			}

			NodeList fileNode = doc.getElementsByTagName(FILETAG);

			if(fileNode.getLength()!=1){
				//TODO:error - trocar por excecao
				System.out.println("Arquivo descritor Invalido");
			}else{
				versionAndFile[1] = (new Integer(fileNode.item(0).getTextContent())).intValue();
			}
		}catch(Exception e){
			//TODO: treat Exception
			e.printStackTrace();
		}
		return versionAndFile;	
	}


	/**
	 * Reads the descriptor file for that Schema 
	 * Gets the last file Number for each version
	 * @param schemafile
	 * @return
	 */
	public static List<File> getAllXSL(File schemaFile, int initialVersion, int lastVersion){

		List<File> files = new ArrayList<File>();
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

		try{
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();		
			Document doc = docBuilder.parse (getDescriptorFile(schemaFile));

			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr;

			for(int i = initialVersion; i <= lastVersion; i++){
				expr = xpath.compile(VERSIONXPATH+"[@"+VERSIONATTR+"="+i+"]");
				Node file = ((NodeList)expr.evaluate(doc, XPathConstants.NODESET)).item(0);
				int maxFile = new Integer(file.getFirstChild().getNodeValue());
				for(int j = 1; j<=maxFile; j++){
					//TODO:Manipulação de arquivo bem feia - mudar
					files.add(new File(getFilePath(schemaFile, i, j)));
				}
			}

		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return files;
	}
	// It is called from the RefactoringParticipant and from the versioning participant
	public static Change createVersioningDir(IFile schemaFile, int version){

		return new CreateFolderChange(getVersionDirPath(schemaFile, version));

	}

	public static Change createVersioningRefactoring(IFile schemaFile,
			int newVersion) {
		VersioningRefactoring ref = new VersioningRefactoring(null,newVersion, true);
		Change directChange = new CreateXSLChange(ref,getFilePath(schemaFile, newVersion, 1));
		Change reverseChange = new CreateXSLChange(ref.getReverseRefactoring(),getFilePath(schemaFile, newVersion, -1));

		CompositeChange versionChange = new CompositeChange("Version Change");
		versionChange.add(directChange);
		versionChange.add(reverseChange);

		return versionChange;
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

	public static Change incrementLastFile(IFile schemaFile) throws CoreException{
		try{
			IFile descriptorFile = ResourcesPlugin.getWorkspace().getRoot().getFile(getDescriptorFilePath(schemaFile));
			IDOMModel model =  (IDOMModel) StructuredModelManager.getModelManager().getModelForEdit(descriptorFile);
			IDOMElement lastFileTag = (IDOMElement) model.getDocument().getElementsByTagName(FILETAG).item(0);

			TextChange change = new TextFileChange("Last File Increase", descriptorFile);
			int textContentOffset = lastFileTag.getStartEndOffset();
			Integer lastFileNewValue = Integer.parseInt(lastFileTag.getFirstChild().getNodeValue()) + 1;
			int length = lastFileTag.getEndStartOffset() - lastFileTag.getStartEndOffset();
			TextEdit edit = new ReplaceEdit(textContentOffset, length, lastFileNewValue.toString());
			change.setEdit(edit);
			return change;	
		}
		catch(IOException e){
			e.printStackTrace();
			//TODO Exceção
		}
		return null;
	}

	//TODO: Quebrar em dois métodos ou preservar um?
	/**
	 * Method used to update the descriptor file when the version changes
	 * It's responsible for save the last version information, increment the last version number,
	 * change the lastFile number to 0
	 */
	public static Change updateDescriptor(IFile schemaFile) throws CoreException {
		try{
			IFile descriptorFile = ResourcesPlugin.getWorkspace().getRoot().getFile(getDescriptorFilePath(schemaFile));
			IDOMModel model =  (IDOMModel) StructuredModelManager.getModelManager().getModelForEdit(descriptorFile);

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
			//TODO: FEIO!!!
			String versionInfo = "<"+VERSIONINFOTAG+" " + VERSIONATTR+"=\""+lastVersionValue+"\" >"+ lastFileValue +
			"</"+ VERSIONINFOTAG + " >\n";
			InsertEdit insertLast = new InsertEdit(versions.getEndStartOffset(),versionInfo);
			TextChange change2 = new TextFileChange("Save last version Info", descriptorFile);
			change2.setEdit(insertLast);

			CompositeChange changes = new CompositeChange(COMPOSITENAME);
			changes.add(change);
			changes.add(fileChange);
			changes.add(change2);

			return changes;	
		}
		catch(IOException e){
			e.printStackTrace();
			//TODO Exceção
		}
		return null;
	}

	//TODO: É responsabilidade do FileControl?
	public static int getSchemaVersion(InputStream selectedXMLFile) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();				
		Document doc = docBuilder.parse (selectedXMLFile);
		String versionString = doc.getDocumentElement().getAttribute(PluginNamingConstants.SCHEMA_VERSION);
		if(versionString.equals(""))
			return 0;
		else
			return Integer.parseInt(versionString);
	}


}
