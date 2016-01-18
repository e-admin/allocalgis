package es.satec.localgismobile.fw.validation.exceptions;

import es.satec.localgismobile.fw.validation.exceptions.ValidationException;

public class LoginException extends ValidationException {
	
	 public LoginException(String message) {
         // Constructor.  Create a ParseError object containing
         // the given message as its error message.
      super(message);
   }	
	 
	 public LoginException() {
         // Constructor.  Create a ParseError object containing
         // the given message as its error message.
      super("Se ha producido un error al logearse el usuario");
   }	
	 
	 public LoginException(Exception e){
			super(e.getMessage());
		}	

}
