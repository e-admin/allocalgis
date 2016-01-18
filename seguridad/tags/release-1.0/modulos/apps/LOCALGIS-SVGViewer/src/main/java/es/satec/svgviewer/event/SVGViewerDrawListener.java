package es.satec.svgviewer.event;

/**
 * Interfaz para notificar eventos de dibujado del SVG
 * @author jpresa
 */
public interface SVGViewerDrawListener {

	public void beginDraw();
	
	public void endDraw();
	
}
