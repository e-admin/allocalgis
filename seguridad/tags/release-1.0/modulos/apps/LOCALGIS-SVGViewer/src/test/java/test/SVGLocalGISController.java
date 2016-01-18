package test;

import org.eclipse.swt.graphics.Point;

import com.tinyline.svg.SVGNode;
import com.tinyline.tiny2d.TinyColor;

import es.satec.svgviewer.SVGViewer;
import es.satec.svgviewer.event.SVGViewerLinkListener;

public class SVGLocalGISController {

	private SVGViewer viewer;
	
	private SVGNode lastSelectedNode;
	private TinyColor lastSelectedNodeStroke;
	private TinyColor lastSelectedNodeFill;

	private int selectedNodeStroke;
	private int selectedNodeFill;
	private float brightness;
	private float contrast;
	
	public SVGLocalGISController(SVGViewer v) {
		this.viewer = v;
		selectedNodeStroke = 0xFF000000; // negro
		selectedNodeFill = 0; // ninguno
		brightness = 1.0f;
		contrast = 1.0f;
        
		viewer.addSVGViewerLinkListener(new SVGViewerLinkListener() {
			public void nodeSelected(SVGNode node, Point point) {
				System.out.println("Nodo seleccionado: " + node);
				if (node != null) {
					// Restaurar los colores del nodo seleccionado anteriormente
					if (lastSelectedNode != null) {
						lastSelectedNode.stroke = lastSelectedNodeStroke;
						lastSelectedNode.fill = lastSelectedNodeFill;
					}
					// Guardar el nodo seleccionado ahora
					lastSelectedNode = node;
					lastSelectedNodeStroke = node.stroke;
					lastSelectedNodeFill = node.fill;
					
					// Cambio de color del nodo seleccionado
					if (brightness == 1.f && contrast == 1.f) {
						// Cambio a un color fijo
						node.stroke = new TinyColor(selectedNodeStroke);
						node.fill = new TinyColor(selectedNodeFill);
					}
					else {
						// Cambio modificando el brillo y el contraste
						// Stroke
						int value = node.stroke.value;
						// Si no tiene color definido, se obtiene del nodo ancestro del que lo hereda
						SVGNode parent = node.parent;
						while (value == 0 && parent != null) {
							value = parent.stroke.value;
							parent = parent.parent;
						}
						int a = (value & 0xFF000000) >> 24;
						int r = (value & 0xFF0000) >> 16;
						int g = (value & 0xFF00) >> 8;
						int b = value & 0xFF;
						// Transformacion del color
						r = clamp((int) (255*transfer(r/255.0f)));
						g = clamp((int) (255*transfer(g/255.0f)));
						b = clamp((int) (255*transfer(b/255.0f)));
						node.stroke = new TinyColor((a<<24) | (r<<16) | (g<<8) | b);

						// Relleno
						value = node.fill.value;
						// Si no tiene color definido, se obtiene del nodo ancestro del que lo hereda
						parent = node.parent;
						while (value == 0 && parent != null) {
							value = parent.fill.value;
							parent = parent.parent;
						}
						a = (value & 0xFF000000) >> 24;
						r = (value & 0xFF0000) >> 16;
						g = (value & 0xFF00) >> 8;
						b = value & 0xFF;
						// Transformacion del color
						r = clamp((int) (255*transfer(r/255.0f)));
						g = clamp((int) (255*transfer(g/255.0f)));
						b = clamp((int) (255*transfer(b/255.0f)));
						node.fill = new TinyColor((a<<24) | (r<<16) | (g<<8) | b);
					}
					viewer.drawSVG();
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
