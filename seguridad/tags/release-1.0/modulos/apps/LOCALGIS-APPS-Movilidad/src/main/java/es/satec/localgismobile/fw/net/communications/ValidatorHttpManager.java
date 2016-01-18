package es.satec.localgismobile.fw.net.communications;

import org.apache.log4j.Logger;

import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.net.communications.exceptions.NoConnectionException;
import es.satec.localgismobile.fw.remote.exceptions.RemoteLocalException;
import es.satec.localgismobile.fw.validation.exceptions.LoginException;
import es.satec.localgismobile.fw.validation.exceptions.RolesException;
import es.satec.localgismobile.fw.validation.exceptions.ValidationException;

/**
 * Clase que interacciona con HttpManager pero se encarga de automáticamente
 * generar la validación con el servidor.
 * 
 * @version 1.0
 * @created 11-dic-2006 18:05:30
 */
public class ValidatorHttpManager {
	
	private static Logger log = Global.getLoggerFor(ValidatorHttpManager.class);

	/**
	 * Referencia a un HttpManager que se empleará para hacer peticiones a un servidor web
	 */
	private HttpManager manager;

	/**
	 * Constructor que crea un HttpManager para ser usado por la clase
	 * @param url Url a la que se quiere conectar el HttpManager
	 */
	public ValidatorHttpManager(String url, String contentType) {

		//creamos una HttpManager sobre el que se apoya la clase
        log.debug("[ValidatorHttpManager()] Conectando con " + url);

        //OJO: Se pone como Content-Type "application/octet-stream" porque vamos a enviar
		//un flujo binario como cuerpo de la petición
		this.manager = new HttpManager(url, "POST", contentType, false, null);
	}

	public void finalize() throws Throwable {

	}

	/**
	 * Método que envía un byte[] al servidor controlado por el campo manager
	 * y que posteriormente devuelve el byte[] con la respuesta producida
	 * @param data
	 * @throws NoConnectionException 
	 * @throws ValidationException 
	 * @throws RolesException 
	 * @throws LoginException 
	 * @throws Exception 
	 * 
	 */
	public byte[] enviarYRecibir(byte[] data) throws NoConnectionException, RemoteLocalException {

		byte[] retorno = null;

		try {
			retorno = manager.enviarYRecibir(data);
		} catch (LoginException e) {
			log.error("Error de validacion", e);
			throw e;
		} catch (RolesException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		}
		
		return retorno;
	}

	/**
	 * Método que sirve para comprobar si existe conexión o no al servidor
	 * @return boolean que indica si ha habido o no conexion
	 */
	public boolean existeConexion() {
		// TODO: Tenemos que reeimplementar el método existeConexion !!!
		boolean result = false;
		
		try {
			manager.existeConexion();
		} catch (ValidationException e) {
			return true;
		} catch (Exception e) {
			return false;
		}
			
		result = true;
		
		return result;
	}

}