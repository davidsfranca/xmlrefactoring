package prototipo.xsltLogic;

import java.util.ArrayList;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class ClasseExecutavel {

	/**
	 * @param args
	 * @throws Exception 
	 * @throws ParseErrorException 
	 * @throws ResourceNotFoundException 
	 */
	public static void main(String[] args) throws ResourceNotFoundException, ParseErrorException, Exception {
		
		RenameRefactor refactor = new RenameRefactor("/books/book","bookref");
		RenameRefactor refactor2 = new RenameRefactor("author","autor");
		ArrayList listaRefat = new ArrayList<RenameRefactor>();
		listaRefat.add(refactor);
		listaRefat.add(refactor2);
		//XSLTWriter.createTransformation(listaRefat);

	}

}
