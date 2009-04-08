package prototipo.plugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.Position;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.wst.common.core.search.SearchEngine;
import org.eclipse.wst.common.core.search.SearchMatch;
import org.eclipse.wst.common.core.search.pattern.QualifiedName;
import org.eclipse.wst.common.core.search.pattern.SearchPattern;
import org.eclipse.wst.common.core.search.scope.SearchScope;
import org.eclipse.wst.common.core.search.scope.SelectionSearchScope;
import org.eclipse.wst.common.core.search.scope.WorkspaceSearchScope;
import org.eclipse.wst.common.core.search.util.CollectingSearchRequestor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.search.XMLComponentDeclarationPattern;
import org.eclipse.wst.xsd.ui.internal.refactor.RefactoringComponent;
import org.eclipse.wst.xsd.ui.internal.refactor.RefactoringMessages;
import org.eclipse.wst.xsd.ui.internal.refactor.TextChangeManager;
import org.eclipse.wst.xsd.ui.internal.refactor.util.TextChangeCompatibility;

public class RenameRefactoring extends Refactoring{

	private RefactoringComponent selectedComponent;
	private TextChangeManager manager = new TextChangeManager();
	private String newName;
	
	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public RenameRefactoring(RefactoringComponent selectedComponent){
		this.selectedComponent = selectedComponent;
	}
	
	@Override
	/**
	 * MŽtodo que cuida de toda a l—gica da refatora‹o
	 * Praticamente copiado de org.eclipse.wst.xsd.ui.internal.refactor.rename.RenameComponentProcessor
	 */
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		
		String fileStr = selectedComponent.getElement().getModel().getBaseLocation();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileStr));
		
		String componentName = selectedComponent.getName();
		String componentNamespace = selectedComponent.getNamespaceURI();
		QualifiedName elementQName = new QualifiedName(componentNamespace, componentName);
		QualifiedName typeQName = selectedComponent.getTypeQName();
		
		SearchScope scope = new WorkspaceSearchScope();
		if (file != null) {
			scope = new SelectionSearchScope(new IResource[]{file});
		}
		CollectingSearchRequestor requestor = new CollectingSearchRequestor();
		SearchPattern pattern = new XMLComponentDeclarationPattern(file, elementQName, typeQName);
		SearchEngine searchEngine = new SearchEngine();
        HashMap map = new HashMap();
		searchEngine.search(pattern, requestor, scope, map, new NullProgressMonitor());
		List results = requestor.getResults();
		
		// more than one declaration found, so use offset as additional check
		Position offsetPosition = null;
		if (results.size() > 1) {
		    IDOMElement selectedElement = selectedComponent.getElement();
		    if (selectedElement != null) {
		    	int startOffset = selectedElement.getStartOffset();
		    	offsetPosition = new Position(startOffset, (selectedElement.getEndOffset() - startOffset));
		    }
		}
		
		String quotedNewName = quoteString(newName);
		
		for (Iterator iter = results.iterator(); iter.hasNext();) {
			SearchMatch match = (SearchMatch) iter.next();
			if (match != null) {
				boolean addTextChange = true;
				// additional check to verify correct declaration is changed
				if (offsetPosition != null) {
					addTextChange = offsetPosition.overlapsWith(match.getOffset(), match.getLength());  
				}
				if (addTextChange) {
					TextChange textChange = manager.get(match.getFile());
					
					ReplaceEdit replaceEdit = new ReplaceEdit(match.getOffset(), match.getLength(), quotedNewName);
					String editName = RefactoringMessages.getString("RenameComponentProcessor.Component_Refactoring_update_declatation");
					TextChangeCompatibility.addTextEdit(textChange, editName, replaceEdit);
				}
				createXSLTData(match);
			}
		}
		return new RefactoringStatus();
	}
	
	private void createXSLTData(SearchMatch match){
	
	}

	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		// TODO Auto-generated method stub
		return new RefactoringStatus();
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		try{
		pm.beginTask("Creating change...", 1);
		CompositeChange compositeChange = new CompositeChange("exemplo", manager.getAllChanges());
		return compositeChange;
		} finally{
			pm.done();
		}
	}
	
	public static String quoteString(String value) {
		value = value == null ? "" : value;

		StringBuffer sb = new StringBuffer();
		if (!value.startsWith("\"")) {
			sb.append("\"");
		}
		sb.append(value);
		if (!value.endsWith("\"")) {
			sb.append("\"");
		}
		return sb.toString();
	}
	
	@Override
	public String getName() {
		return "XSDRenameRefactoring";
	}

}
