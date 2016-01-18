package es.satec.svgviewer.event;

import org.eclipse.swt.graphics.Point;

import com.tinyline.svg.SVGNode;

/**
 * Interfaz para notificar seleccion de nodos del SVG a la aplicacion cliente.
 * @author jpresa
 */
public interface SVGViewerLinkListener {

	public void nodeSelected(SVGNode node, Point point);

}
