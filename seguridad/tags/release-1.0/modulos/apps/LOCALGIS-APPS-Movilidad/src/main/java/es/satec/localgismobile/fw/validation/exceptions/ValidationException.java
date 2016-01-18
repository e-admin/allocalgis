package es.satec.localgismobile.fw.validation.exceptions;

import es.satec.localgismobile.fw.remote.exceptions.RemoteLocalException;

public class ValidationException extends RemoteLocalException {
	
	 public ValidationException(String message) {
         // Constructor.  Create a ParseError object containing
         // the given message as its error message.
		 super(message);
	 }		 
		 
	 public ValidationException(Exception e){
			super(e.getMessage());
		}	
	
	 public ValidationException() {
         // Constructor.  Create a ParseError object containing
         // the given message as its error message.
      super("Se ha producido un error de validación");
   }	


}
