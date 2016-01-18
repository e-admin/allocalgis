package es.enxenio.util.exceptions;

/**
 * La excepcion raiz de todas las excepciones en el modelo.
 */
public abstract class ModelException extends Exception {

	protected ModelException() {}

	protected ModelException(String message) {
		super(message);
	}

}
