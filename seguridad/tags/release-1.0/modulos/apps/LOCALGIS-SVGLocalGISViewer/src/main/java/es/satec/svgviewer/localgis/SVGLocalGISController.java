package es.satec.svgviewer.localgis;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Point;

import com.tinyline.svg.SVGNode;
import com.tinyline.tiny2d.TinyColor;

import es.satec.svgviewer.event.SVGViewerLinkListener;
import es.satec.svgviewer.event.SVGViewerRasterTransformListener;

public class SVGLocalGISController {

	private static Logger logger = (Logger) Logger.getInstance(SVGLocalGISController.class);
	
	private SVGLocalGISViewer viewer;
	
	private SVGNode currentSelectedNode;
//	private SVGNode lastSelectedNode;
//	private TinyColor lastSelectedNodeStroke;
//	private TinyColor lastSelectedNodeFill;

	private int selectedNodeStroke;
	private int selectedNodeFill;
	private float brightness;
	private float contrast;
	
	public SVGLocalGISController(SVGLocalGISViewer v) {
		this.viewer = v;
		selectedNodeStroke = 0xFF000000; // negro
		selectedNodeFill = 0xFFB4B4B4; // gris claro
		brightness = 1.0f;
		contrast = 1.0f;
        
		viewer.addSVGViewerLinkListener(new SVGViewerLinkListener() {
			public void nodeSelected(SVGNode node, Point point) {
				logger.debug("Nodo seleccionado: " + node);
				if (node != null && (node.nameAtts == null || node.nameAtts.isEmpty())) {
					// Cambiar el nodo seleccionado por su grupo
					logger.debug("Nodo de grupo");
					currentSelectedNode = node.parent;
				}
				else {
					currentSelectedNode = node;
				}
				viewer.drawSVG();
			}
		});
		
		/* Este listener debe ejecutarse siempre despues del que aplica los estilos */
		viewer.addSVGViewerRasterTransformListener(new SVGViewerRasterTransformListener() {
			public void rasterTransformed() {
				if (currentSelectedNode != null) {
					logger.debug("Aplicando color al nodo seleccionado");
					
					// Se guardan los valores originales del nodo seleccionado, para
					// restaurarlos antes de cada pintado
					viewer.saveSelectedNodeState(currentSelectedNode);

// Eliminado por incompatibilidades con la aplicacion de estilos
//					// Restaurar los colores del nodo seleccionado anteriormente
//					if (lastSelectedNode != null && lastSelectedNode != currentSelectedNode) {
//						lastSelectedNode.stroke = lastSelectedNodeStroke;
//						lastSelectedNode.fill = lastSelectedNodeFill;
//					}
//					// Guardar el nodo seleccionado ahora
//					lastSelectedNode = currentSelectedNode;
//					lastSelectedNodeStroke = currentSelectedNode.stroke;
//					lastSelectedNodeFill = currentSelectedNode.fill;
					
					
					// Cambio de color del nodo seleccionado
					if (brightness == 1.f && contrast == 1.f) {
						// Cambio a un color fijo
						currentSelectedNode.stroke = new TinyColor(selectedNodeStroke);
						currentSelectedNode.fill = new TinyColor(selectedNodeFill);
					}
					else {
						// Cambio modificando el brillo y el contraste
						// Stroke
						TinyColor color = currentSelectedNode.stroke;
						// Si no tiene color definido, se obtiene del nodo ancestro del que lo hereda
						SVGNode parent = currentSelectedNode.parent;
						while (color == TinyColor.INHERIT && parent != null) {
							color = parent.stroke;
							parent = parent.parent;
						}
						
						if (color == TinyColor.NONE || color == TinyColor.INHERIT) { // No tiene ningun color definido
							currentSelectedNode.stroke = new TinyColor(selectedNodeStroke);
						}
						else {
							int value = color.value;
							int a = (value & 0xFF000000) >> 24;
							int r = (value & 0xFF0000) >> 16;
							int g = (value & 0xFF00) >> 8;
							int b = value & 0xFF;
							// Transformacion del color
							r = clamp((int) (255*transfer(r/255.0f)));
							g = clamp((int) (255*transfer(g/255.0f)));
							b = clamp((int) (255*transfer(b/255.0f)));
							currentSelectedNode.stroke = new TinyColor((a<<24) | (r<<16) | (g<<8) | b);
						}

						// Relleno
						color = currentSelectedNode.fill;
						// Si no tiene color definido, se obtiene del nodo ancestro del que lo hereda
						parent = currentSelectedNode.parent;
						while (color == TinyColor.INHERIT && parent != null) {
							color = parent.fill;
							parent = parent.parent;
						}
						
						if (color == TinyColor.NONE || color == TinyColor.INHERIT) { // No tiene ningun color definido
							currentSelectedNode.fill = new TinyColor(selectedNodeFill);
						}
						else {
							int value = color.value;
							int a = (value & 0xFF000000) >> 24;
							int r = (value & 0xFF0000) >> 16;
							int g = (value & 0xFF00) >> 8;
							int b = value & 0xFF;
							// Transformacion del color
							r = clamp((int) (255*transfer(r/255.0f)));
							g = clamp((int) (255*transfer(g/255.0f)));
							b = clamp((int) (255*transfer(b/255.0f)));
							currentSelectedNode.fill = new TinyColor((a<<24) | (r<<16) | (g<<8) | b);
						}
					}
					viewer.update(); // para esperar a que se repinte
					viewer.redraw();
				}
			}
        });
	}
	
	/**
	 * Establece el color del borde del nodo seleccionado
	 * @param color
	 */
	public void setSelectedNodeStroke(int color) {
		selectedNodeStroke = color;
	}
	
	/**
	 * Establece el color del relleno del nodo seleccionado
	 * @param color
	 */
	public void setSelectedNodeFill(int color) {
		selectedNodeFill = color;
	}

	/**
	 * Establece el contraste a aplicar al nodo seleccionado
	 * @param contrast
	 */
	public void setContrast(float contrast) {
		if (contrast >= 0.f && contrast <=1.f)
			this.contrast = contrast;
	}
	
	/**
	 * Establece el brillo a aplicar al nodo seleccionado
	 * @param brightness
	 */
	public void setBrightness(float brightness) {
		if (brightness >=0.f && brightness <= 1.f)
			this.brightness = brightness;
	}
	
	/**
	 * Transforma el color en función del brillo y el contraste
	 */
	private float transfer(float f) {
		f = f*brightness;
		f = (f-0.5f)*contrast+0.5f;
		return f;
	}

	/**
	 * Restringe un valor al rango 0..255
	 */
	private int clamp(int c) {
		if (c < 0)
			return 0;
		if (c > 255)
			return 255;
		return c;
	}

}
