package es.satec.localgismobile.fw.remote.exceptions;

import es.satec.localgismobile.fw.common.remote.exception.RemoteException;

/**
 * Esta clase se usa para saber que la excepcion no se ha generado en el servidor,
 * sino que ha sido generado en local.
 * @version 1.0
 * @created 11-dic-2006 18:05:22
 */
public class RemoteLocalException extends RemoteException {

	/**
	 * Constructor por defecto
	 *
	 */
	public RemoteLocalException(){

	}
	
	/**
	 * Constructor al que se le indica un mensaje
	 * @param mensaje
	 */
	public RemoteLocalException(String mensaje){
		super(mensaje);
	}

	public void finalize() throws Throwable {
		super.finalize();
	}

}