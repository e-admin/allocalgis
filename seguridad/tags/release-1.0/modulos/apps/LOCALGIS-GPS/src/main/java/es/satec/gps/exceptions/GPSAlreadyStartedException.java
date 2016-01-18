package es.satec.gps.exceptions;

/**
 * Indica que el GPS ya ha sido iniciado.
 * User: Hugo
 * Date: 02-ago-2007
 * Time: 12:26:34
 */
public class GPSAlreadyStartedException extends GPSException {
	private static final long serialVersionUID = -6262879498509449036L;

	public GPSAlreadyStartedException(String string) {
        super(string);
    }
}
