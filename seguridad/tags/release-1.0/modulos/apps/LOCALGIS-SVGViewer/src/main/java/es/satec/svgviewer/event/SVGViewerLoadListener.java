package es.satec.svgviewer.event;

import com.tinyline.svg.SVGDocument;

/**
 * Interfaz para notificar la carga de un documento SVG.
 * @author jpresa
 */
public interface SVGViewerLoadListener {
	
	public void documentLoaded(SVGDocument svgDocument);

}
