package es.satec.localgismobile.fw.image.exceptions;

/**
 * Excepción que indica que la imagen que se está procesando
 * no tiene el formato esperado
 * @author jpolo
 *
 */
public class IncorrectImageFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor por defecto de la excepción. Pone el mensaje
	 * adecuado
	 *
	 */
	public IncorrectImageFormatException(){
		super("Formato de imagen incorrecto");
	}
	
	/**
	 * Construye la excepción con el mensaje que se le indique
	 * @param msg Mensaje descriptivo de la excepción
	 */
	public IncorrectImageFormatException(String msg){
		super(msg);
	}
}
