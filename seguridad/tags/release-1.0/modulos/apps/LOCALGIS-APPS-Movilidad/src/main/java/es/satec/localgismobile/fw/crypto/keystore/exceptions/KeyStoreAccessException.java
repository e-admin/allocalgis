package es.satec.localgismobile.fw.crypto.keystore.exceptions;

/**
 * Excepción que indica que se ha producido un error al acceder al
 * almacen de claves
 * @author jpolo
 *
 */
public class KeyStoreAccessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor por defecto. Pone el mensaje adecuado a la excepción
	 *
	 */
	public KeyStoreAccessException(){
		super("Se ha producido un error al acceder al almacen de claves");
	}
	
	/**
	 * Construye la excepción con el mensaje indicado por parámetro
	 * @param msg Mensaje a pasar
	 */
	public KeyStoreAccessException(String msg){
		super(msg);
	}
}
