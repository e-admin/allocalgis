package es.satec.svgviewer.event;

/**
 * Evento para notificar un error en el visor.
 * @author jpresa
 */
public class SVGViewerErrorEvent {
	
	private String errorMessage;
	private Throwable throwable;

	public SVGViewerErrorEvent(String errorMessage, Throwable throwable) {
		this.errorMessage = errorMessage;
		this.throwable = throwable;
	}
	
	public SVGViewerErrorEvent(String errorMessage) {
		this(errorMessage, null);
	}
	
	public SVGViewerErrorEvent(Throwable throwable) {
		this(throwable.getMessage(), throwable);
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public Throwable getThrowable() {
		return throwable;
	}
	
	public String toString() {
		return errorMessage;
	}
}
