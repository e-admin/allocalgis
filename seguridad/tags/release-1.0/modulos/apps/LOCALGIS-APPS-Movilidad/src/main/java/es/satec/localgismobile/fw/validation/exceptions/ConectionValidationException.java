package es.satec.localgismobile.fw.validation.exceptions;

public class ConectionValidationException extends ValidationException{
	
	 public ConectionValidationException(String message) {
         // Constructor.  Create a ParseError object containing
         // the given message as its error message.
      super(message);
	 }	
	 
		 
	 public ConectionValidationException(Exception e){
			super(e.getMessage());
		}	
	
	 public ConectionValidationException() {
         // Constructor.  Create a ParseError object containing
         // the given message as its error message.
      super("Se ha producido un error de conexion");
   }	

}
