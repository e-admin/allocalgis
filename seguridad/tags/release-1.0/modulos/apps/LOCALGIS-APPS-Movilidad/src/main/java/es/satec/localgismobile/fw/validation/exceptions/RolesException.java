package es.satec.localgismobile.fw.validation.exceptions;

import es.satec.localgismobile.fw.validation.exceptions.ValidationException;

public class RolesException extends ValidationException {
	
	 public RolesException(String message) {
         // Constructor.  Create a ParseError object containing
         // the given message as its error message.
      super(message);
   }	
	 
	 public RolesException() {
         // Constructor.  Create a ParseError object containing
         // the given message as its error message.
      super("Se ha producido un error con los roles del usuario");
   }	
	 
	 public RolesException(Exception e){
			super(e.getMessage());
		}	

}
