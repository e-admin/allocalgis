package es.satec.localgismobile.fw.common.remote.exception;

/**
 * Clase de la que van ha heredar todas las excepciones que se generen en la
 * invocación remota de un método
 * @version 1.0
 * @created 11-dic-2006 18:05:21
 */
public class RemoteException extends Exception {

	/**
	 * Campo donde se guarda el mensaje que se desee asignar en la exception
	 */
	private String message;

	/**
	 * Constructor por defecto
	 *
	 */
	public RemoteException(){		
		super("Generada una exception durante la gestion de llamadas remotas");
		//por defecto generamos este mensaje ante una RemoteException
		this.message = "Generada una exception durante la gestion de llamadas remotas";
	}
	
	/**
	 * Constructor al que se le pasa el mensaje que queremos que indique la Exception
	 * @param message
	 */
	public RemoteException(String message){
		super(message);
		this.message = message;
	}

	public void finalize() throws Throwable {

	}

	/**
	 * Método getter para el campo message
	 */
	public String getMessage(){
		return message;
	}
	
	/**
	 * Método setter para el campo message
	 * @param message Mensaje para almacenar en la RemoteException
	 */
	public void setMessage(String message){
		this.message = message;
	}
	
	
	public String toString(){
		return "RemoteException:"+this.message;	
	}

}