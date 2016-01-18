package es.satec.localgismobile.fw.net.communications.exceptions;

import org.apache.log4j.Logger;

import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.net.communications.HttpManager;

public class NoConnectionException extends Exception {
	
	private static Logger log = Global.getLoggerFor(NoConnectionException.class);
	
	private int httpCode;
	
	public NoConnectionException(){		
		super("ERROR de CONEXIÓN.");
		log.error("ERROR de CONEXIÓN.");
		//se pone -1 por indicar algún número, pero no tiene 
		//ningún valor semántico sobre el error de conexión
		this.httpCode = -1;
	}
	
	public NoConnectionException(int code){
		super("ERROR de CONEXIÓN. HTTP Code == " + code);
		log.error("ERROR de CONEXIÓN. HTTP Code == " + code);
		this.httpCode = code;
	}
	
	public NoConnectionException(String mensaje){
		super("ERROR de CONEXIÓN: "+mensaje);
		log.error("ERROR de CONEXIÓN: "+mensaje);
		//se pone -1 por indicar algún número, pero no tiene 
		//ningún valor semántico sobre el error de conexión
		this.httpCode = -1;
	}

	public int getHttpCode() {
		return httpCode;
	}

}
