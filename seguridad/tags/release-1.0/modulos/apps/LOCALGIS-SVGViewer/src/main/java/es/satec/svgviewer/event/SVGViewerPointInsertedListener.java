package es.satec.svgviewer.event;

import com.tinyline.svg.SVGNode;

/**
 * Interfaz para notificar la inserción de un punto en el documento SVG
 * a la aplicación cliente.
 * @author jpresa
 */
public interface SVGViewerPointInsertedListener {

	public void pointInserted(SVGNode point);
	
}
