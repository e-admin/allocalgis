package es.satec.gps.exceptions;

/**
 * Excepción que se lanza cuando se produce un error en el sistema de control
 * del GPS
 * User: Hugo
 * Date: 01-ago-2007
 * Time: 13:44:38
 */
public class GPSException extends Exception {

	private static final long serialVersionUID = -8374030793835263682L;

	public GPSException(String string) {
        super(string);
    }
}
