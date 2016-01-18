package es.satec.localgismobile.fw.validation.exceptions;

import es.satec.localgismobile.fw.validation.exceptions.ValidationException;

public class LoginCancelException extends ValidationException {

	 public LoginCancelException(String message) {
		 super(message);
   }	
	 
	 public LoginCancelException() {
		 super("Se ha cancelado el proceso de autenticación");
   }	
	 
	 public LoginCancelException(Exception e){
			super(e.getMessage());
		}	
}
