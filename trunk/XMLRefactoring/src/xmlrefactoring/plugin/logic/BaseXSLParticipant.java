package xmlrefactoring.plugin.logic;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.xsd.XSDNamedComponent;

import xmlrefactoring.plugin.logic.util.CreateXSLChange;
import xmlrefactoring.plugin.refactoring.XMLRefactoring;
import xmlrefactoring.plugin.xslt.FileControl;


public abstract class BaseXSLParticipant extends RefactoringParticipant {

	private static final String XSLT_CHANGE_TEXT = "XSLT change";
	private XSDNamedComponent component;
	private IFile schemaFile;

	@Override
	public void initialize(RefactoringArguments arguments){
		BaseRefactoringArguments baseArguments = (BaseRefactoringArguments) arguments;
		component = baseArguments.getComponent();
		String schemaPath = ((IDOMElement) component.getElement()).getModel().getBaseLocation();
		schemaFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(schemaPath));
	}


	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
	OperationCanceledException {
		
		CompositeChange compositeChange = new CompositeChange(XSLT_CHANGE_TEXT);
		IPath xslPath;

		if(FileControl.isUnderVersionControl(schemaFile)){
			xslPath = FileControl.getNextPath(schemaFile, false);
			Change incrementDescriptorLastFile = FileControl.incrementLastFile(schemaFile);
			compositeChange.add(incrementDescriptorLastFile);
		}
		else{
			compositeChange.add(FileControl.addToVersionControl(schemaFile));
			xslPath = FileControl.getNextPath(schemaFile, true);
		}
		Change xslChange = new CreateXSLChange(getXMLRefactoring(), xslPath);
		compositeChange.add(xslChange);
		
		

		return compositeChange;		
	}

	protected abstract XMLRefactoring getXMLRefactoring();	
}
