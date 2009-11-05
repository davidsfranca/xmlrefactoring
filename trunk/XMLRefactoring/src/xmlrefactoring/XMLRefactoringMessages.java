package xmlrefactoring;

import java.util.ResourceBundle;

public class XMLRefactoringMessages {
	
	private static final String BUNDLE = "xmlrefactoring.messages";
	
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE);
	
	public static String getString(String key){
		return resourceBundle.getString(key);
	}

}
