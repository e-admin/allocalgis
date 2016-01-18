package es.satec.localgismobile.fw.validation.exceptions;


public class FileValidationException extends ValidationException {

	 public FileValidationException(String message) {
         // Constructor.  Create a ParseError object containing
         // the given message as its error message.
      super(message);
	 }	
	 
		 
	 public FileValidationException(Exception e){
			super(e.getMessage());
		}	
	
	 public FileValidationException() {
         // Constructor.  Create a ParseError object containing
         // the given message as its error message.
      super("Se ha producido un error con los ficheros de validación");
   }		

}
