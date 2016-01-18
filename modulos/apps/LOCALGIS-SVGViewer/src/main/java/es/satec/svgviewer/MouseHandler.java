/**
 * MouseHandler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.svgviewer;

import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.tinyline.svg.SVG;
import com.tinyline.svg.SVGDocument;
import com.tinyline.svg.SVGEllipseElem;
import com.tinyline.svg.SVGNode;
import com.tinyline.svg.SVGPolygonElem;
import com.tinyline.svg.SVGRaster;
import com.tinyline.svg.SVGRect;
import com.tinyline.svg.SVGSVGElem;
import com.tinyline.tiny2d.TinyColor;
import com.tinyline.tiny2d.TinyMatrix;
import com.tinyline.tiny2d.TinyPoint;
import com.tinyline.tiny2d.TinyString;
import com.tinyline.tiny2d.TinyUtil;
import com.tinyline.tiny2d.TinyVector;

import es.satec.svgviewer.event.SVGViewerLinkListener;
import es.satec.svgviewer.event.SVGViewerPointInsertedListener;

/**
 * Listener del ratón asociado al canvas en que se dibuja el SVG.
 * @author jpresa
 *
 */
public class MouseHandler implements MouseListener, MouseMoveListener {

	private static Logger logger = (Logger) Logger.getInstance(MouseHandler.class);
	
	/**
	 * Pila donde se guardan los rectangulos que definen las vistas que se
	 * han usado al hacer zoomIn, para despues poder restaurarlas al hacer un zoomOut.
	 */
	private Stack zoomHistory;

	/** Indica si el botón del ratón está pulsado */
	private boolean mousePressed = false;

	/** El canvas que usa este handler */
	SVGViewer canvas;

	public static final int LINK_MOUSE      = 0;
	public static final int PAN_MOUSE       = 1;
	public static final int ZOOM_IN_MOUSE   = 2;
	public static final int ZOOM_OUT_MOUSE  = 3;
	public static final int POLYLINE_MOUSE  = 4;
	public static final int POLYGON_MOUSE   = 5;
	public static final int POINT_MOUSE     = 6;

	protected int type = LINK_MOUSE;

	private static final int MIN_WIDTH_ZOOM_IN = 10;
	private static final int MIN_HEIGHT_ZOOM_IN = 10;

	/**
	 * The original values to calculate pan.
	 */
	int pressedX;
	int pressedY;
	int draggedX;
	int draggedY;

	/**
	 * Constante que indica que al mover el svg por la pantalla, dibujamos una linea que indica el
	 * movimiento a seguir y al levantar el raton movemos de una sola vez.
	 */
	public static final int PAN_LINE = 0;

	/**
	 * Constante que indica que al mover el svg por la pantalla seguiremos la posicion del raton y
	 * se redibujara mientras se mueva.
	 */
	public static final int PAN_MOVE = 1;

	/**
	 * Indica el tipo de movimiento que hacemos, siguiendo al raton o dibujando una linea y moviendo
	 * la levantar el raton.
	 */
	private int panType = PAN_LINE;

	/*
	 * Tamano del canvas en pixeles
	 */
	int widthCanvas;
	int heightCanvas;

	private String selectableAncestorId;
	
	private String [] allAncestorsIds;
	
	private String parentNodeId;
	
	private Vector drawingPoints;
	
	private SVGNode lastInsertedNode;

	/**
	 * Construye un manejador con el canvas dado.
	 */
	public MouseHandler(SVGViewer canvas)
	{
		this.canvas = canvas;
		zoomHistory = new Stack();
		heightCanvas = canvas.getSize().x;
		widthCanvas = canvas.getSize().y;
		drawingPoints = new Vector();
	}
	
	/**
	 * Invocado desde el viewer cuando se le cambia el tamaño.
	 */
	protected void viewerResized() {
		zoomHistory.removeAllElements(); // Se vacia el historial
		heightCanvas = canvas.getSize().x;
		widthCanvas = canvas.getSize().y;
	}

	public void mouseDoubleClick(MouseEvent e) {
	}

	/**
	 * Se invoca cuando se presiona un botón del ratón sobre el canvas.
	 */
	public void mouseDown(MouseEvent e) {
		mousePressed = true;		
		if(type == PAN_MOUSE || type==ZOOM_IN_MOUSE) {
			iniPan(e.x, e.y);
			pressedX = e.x;
			pressedY = e.y;
			draggedX = pressedX;
			draggedY = pressedY;
			logger.debug("[mousePressed()] pressedX=" + pressedX);
			logger.debug("[mousePressed()] pressedY=" + pressedY);
		}
	}
	
	/**
	 * Se invoca cuando se suelta el botón del ratón sobre el canvas.
	 */
	public void mouseUp(MouseEvent e) {
		logger.debug("[mouseReleased()] releasedX = " + e.x);
		logger.debug("[mouseReleased()] releasedY = " + e.y);
		if (!mousePressed) return;
		mousePressed = false;
		if(type == ZOOM_OUT_MOUSE) {
			zoomOut();
		}
		else if(type == ZOOM_IN_MOUSE) {
			zoomIn(pressedX,  pressedY, draggedX, draggedY,false);
		}
		else if(type == LINK_MOUSE) {
			link(e.x, e.y);
		}
		//CAMBIO
//		else if (type == PAN_MOUSE) {
//			if(panType == PAN_LINE) {
//				pan(pressedX -e.x,pressedY -e.y);
//			} else {
//				//En este caso no es necesario hacer nada, ya que vamos moviendo la imagen segun
//				//movemos el raton.
//			}
//		}
		else if (type == POLYLINE_MOUSE) {
			drawXORPolyline(drawingPoints);
			drawingPoints.addElement(new TinyPoint(e.x, e.y));
			drawXORPolyline(drawingPoints);
		}
		else if (type == POLYGON_MOUSE) {
			drawXORPolygon(drawingPoints);
			drawingPoints.addElement(new TinyPoint(e.x, e.y));
			drawXORPolygon(drawingPoints);
		}
		else if (type == POINT_MOUSE) {
			// Polilinea con dos puntos
			//drawingPoints.removeAllElements();
			//drawingPoints.addElement(new TinyPoint(e.x, e.y));
			//drawingPoints.addElement(new TinyPoint(e.x+5, e.y+5));
			//endDraw();
			createPoint(e.x, e.y);
		}
	}

	/**
	 * Invoked when a mouse button is pressed within the Canvas canvas and then
	 * dragged.
	 */
	public void mouseMove(MouseEvent e)
	{		
		if (mousePressed) {
			if (type == PAN_MOUSE) {	
				if (panType == PAN_MOVE) {
					pan(draggedX - e.x, draggedY - e.y);
					draggedX = e.x;
					draggedY = e.y;
				} 
				//CAMBIO
//				else if(panType == PAN_LINE) {					
//					drawXORLine(pressedX,pressedY,draggedX,draggedY);
//					draggedX = e.x;
//					draggedY = e.y;
//					drawXORLine(pressedX,pressedY,draggedX,draggedY);
//				}
			} else if(type == ZOOM_IN_MOUSE) {
				//Borramos el rectangula anterior
				drawXORRectangle(pressedX,  pressedY, draggedX, draggedY);
				draggedX = e.x;
				draggedY = e.y;
				//Pintamos un rectangulo nuevo
				drawXORRectangle(pressedX,  pressedY, draggedX, draggedY);
			}
		}	
	}

	/**
     * Dibuja una linea en el canvas entre los puntos indicados.
     *
     * @param x0 La coordenada x del primer punto
     * @param y0 La coordenada y del primer punto
     * @param x1 La coordenada x del segundo punto
     * @param y1 La coordenada y del segundo punto
     */
	public void drawXORLine(int x0, int y0, int x1, int y1)
	{
		GC gc = new GC(canvas);
		if (gc != null) {
			gc.setXORMode(true);
			gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			gc.drawLine(x0, y0, x1, y1);
			gc.dispose();
		}
	}

	/**
	 * Dibuja un rectangulo en el canvas entre los puntos indicados.
	 *
	 * @param x0 La coordenada x del primer punto
	 * @param y0 La coordenada y del primer punto
	 * @param x1 La coordenada x del segundo punto
	 * @param y1 La coordenada y del segundo punto
	 */
	private void drawXORRectangle(int x0, int y0, int x1, int y1) {
		GC gc = new GC(canvas);
		if(gc != null) {
			Rectangle aux = makeRect(x0, y0, x1, y1);
			gc.setXORMode(true);
			gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			gc.drawRectangle(aux.x, aux.y, aux.width, aux.height);
			gc.dispose();
		}
	}
	
	/**
	 * Dibuja una polilinea en el canvas entre los puntos indicados.
	 *
	 * @param points Vector de objetos TinyPoint con los puntos
	 */
	private void drawXORPolyline(Vector points) {
		if (points != null) {
			GC gc = new GC(canvas);
			if(gc != null) {
				int p[] = new int[points.size()*2];
				for (int i=0; i<points.size(); i++) {
					TinyPoint tp = (TinyPoint) points.elementAt(i);
					p[i*2] = tp.x;
					p[i*2+1] = tp.y;
				}
				gc.setXORMode(true);
				gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
				gc.drawPolyline(p);
				gc.dispose();
			}
		}
	}
	
	/**
	 * Dibuja un poligono en el canvas entre los puntos indicados.
	 *
	 * @param points Vector de objetos TinyPoint con los puntos
	 */
	private void drawXORPolygon(Vector points) {
		if (points != null) {
			GC gc = new GC(canvas);
			if(gc != null) {
				int p[] = new int[points.size()*2];
				for (int i=0; i<points.size(); i++) {
					TinyPoint tp = (TinyPoint) points.elementAt(i);
					p[i*2] = tp.x;
					p[i*2+1] = tp.y;
				}
				gc.setXORMode(true);
				gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
				gc.drawPolygon(p);
				gc.dispose();
			}
		}
	}

	/**
     * Realiza un zoom al rectangulo delimitado por dos puntos. Si se realizan varios zooms
     * consecutivos, se van guardando los distintos rectangulos de manera que luego se realicen
     * los zoomOut a los distintos rectangulos almacenados.
     *
     * @param x0 La coordenada x del primer punto
     * @param y0 La coordenada y del primer punto
     * @param x1 La coordenada x del segundo punto
     * @param y1 La coordenada y del segundo punto
     *
     * @see MouseHandler#zoomOut()
     */
	public void zoomIn(int x0, int y0, int x1, int y1,boolean force) {

		logger.debug("[zoomIn()] aux1 = " + x0+" "+y0+" "+x1+" "+y1);
		Rectangle aux = makeRect(x0, y0, x1, y1);
		logger.debug("[zoomIn()] aux2 = " + aux);

		// Hacer el tamaño del recuadro del zoom proporcional al tamaño del canvas, para evitar
		// efectos extraños en sucesivos zooms
		int x = aux.x;
		int y = aux.y;
		int zoomWidth = aux.width;
		int zoomHeight = aux.height;
		int mapWidth = canvas.getSize().x;
		int mapHeight = canvas.getSize().y;
		
		double propX = mapWidth*1.0/zoomWidth*1.0;
		double propY = mapHeight*1.0/zoomHeight*1.0;

		if (propX > propY) {
			int nuevoZoomWidth = (int) (mapWidth*zoomHeight*1.0/mapHeight*1.0);
			x -= (nuevoZoomWidth-zoomWidth)/2;
			zoomWidth = nuevoZoomWidth;
		}
		else {
			int nuevoZoomHeight = (int) (mapHeight*zoomWidth*1.0/mapWidth*1.0);
			y -= (nuevoZoomHeight-zoomHeight)/2;
			zoomHeight = nuevoZoomHeight;
		}

		if (!force){
			//Si el cuadrado sobre que el que hacemos zoom es demasiado pequeño no hacemos nada. Así evitamos
			//los megazoom cuando solo se pincha en un punto.
			if(zoomWidth < MIN_WIDTH_ZOOM_IN || zoomHeight < MIN_HEIGHT_ZOOM_IN) {
				logger.debug("[zoomIn()] Zona de zoom in demasiado pequeña");
				drawSvg();
				return;
			}
		}

		SVGRect view = canvas.raster.view;
		SVGRect zhView = new SVGRect(view);
		zoomHistory.push(zhView);

		SVGDocument doc = canvas.raster.getSVGDocument();
		SVGNode root = doc.root;
		int scale = ((SVGSVGElem) root).getCurrentScale();

		view.x += TinyUtil.div(x << TinyUtil.FIX_BITS, scale);
		view.y += TinyUtil.div(y << TinyUtil.FIX_BITS, scale);
		view.width = TinyUtil.div(zoomWidth<< TinyUtil.FIX_BITS, scale);
		view.height = TinyUtil.div(zoomHeight << TinyUtil.FIX_BITS, scale);

		drawSvg();
	}

	/**
	 * Realiza un zoomOut. Estos se van a ir realizando en orden inverso a los zoomIn que
	 * se hayan realizado. Si no se ha hecho ningun zoomIn no se realiza el zoomOut.
	 */
	public void zoomOut() {
		SVGRect view = canvas.raster.view;
		if (!zoomHistory.empty()) {
			SVGRect zhView = (SVGRect) zoomHistory.pop();
			
			if (zoomHistory.empty()) {
				// Mostramos el svg inicial
				view.x = zhView.x;
				view.y = zhView.y;
				view.width = zhView.width;
				view.height = zhView.height;
			}
			else {
				// Restauramos el nivel de zoom anterior, pero conservamos los desplazamientos
				view.x = view.x+(view.width/2)-(zhView.width/2);
				view.y = view.y+(view.height/2)-(zhView.height/2);
				view.width = zhView.width;
				view.height = zhView.height;
			}

			drawSvg();
		}
	}

	/**
	 * Mueve el svg sobre el canvas.
	 *
	 * @param x La coordenada x del punto donde vamos a mover el svg.
	 * @param y La coordenada y del punto donde vamos a mover el svg. 
	 */
	public void pan(int x, int y) {
		SVGRaster raster = canvas.raster;
		SVGRect view = raster.view;
		SVGDocument doc = raster.getSVGDocument();
		SVGNode root = doc.root;
		int scale = ((SVGSVGElem) root).getCurrentScale();
		view.x += TinyUtil.div(x << TinyUtil.FIX_BITS, scale);

		view.y += TinyUtil.div(y << TinyUtil.FIX_BITS, scale);
		drawSvg();
	}
	
	private void link(int x, int y) {
		
		SVGNode ancestorNode = null;
		if (selectableAncestorId == null) {
			ancestorNode = canvas.raster.document.root;
		}
		else { 
			ancestorNode = SVGNode.getNodeById(canvas.raster.document.root, new TinyString(selectableAncestorId.toCharArray()));
		}	
		
		if (ancestorNode == null) {
			logger.debug("Nodo no encontrado");
			return;
		}	
		
		SVGNode selectedNode = ancestorNode.nodeHitAt(canvas.raster, new TinyPoint(x, y));

		// Notificar a los listeners
		logger.debug("Nodo seleccionado. Notificando a los listeners");
		Enumeration en = canvas.getSVGViewerLinkListeners().elements();
		while (en.hasMoreElements()) {
			SVGViewerLinkListener l = (SVGViewerLinkListener) en.nextElement();
			l.nodeSelected(selectedNode, new Point(x, y));				
		}			
	}
	
	//NUEVO
	private void iniPan(int x, int y) {		
		SVGNode ancestorNode = null;
		if (selectableAncestorId == null) {
			ancestorNode = canvas.raster.document.root;
		}
		else { 
			ancestorNode = SVGNode.getNodeById(canvas.raster.document.root, new TinyString(selectableAncestorId.toCharArray()));
		}	
		
		if (ancestorNode == null) {
			logger.debug("Nodo no encontrado");
			return;
		}	
		
		SVGNode	selectedNode=canvas.raster.document.root;
		selectedNode.opacity=0;	
		
		// Notificar a los listeners
		logger.debug("Nodo seleccionado. Notificando a los listeners");
		Enumeration en = canvas.getSVGViewerLinkListeners().elements();
		while (en.hasMoreElements()) {
			SVGViewerLinkListener l = (SVGViewerLinkListener) en.nextElement();
			l.nodeSelected(selectedNode, new Point(x, y));				
		}			
	}
	//FIN NUEVO

	/**
	 * Actualiza el dibujo del SVG en el canvas.
	 */
	private void drawSvg() {
//		canvas.raster.setCamera();
//		Enumeration en = canvas.getSVGViewerRasterTransformListeners().elements();
//		while (en.hasMoreElements()) {
//			SVGViewerRasterTransformListener l = (SVGViewerRasterTransformListener) en.nextElement();
//			l.rasterTransformed();
//		}
//		canvas.raster.update();
//		en = canvas.getSVGViewerRasterUpdateListeners().elements();
//		while (en.hasMoreElements()) {
//			SVGViewerRasterUpdateListener l = (SVGViewerRasterUpdateListener) en.nextElement();
//			l.rasterUpdated();
//		}
//		canvas.raster.sendPixels();
		canvas.drawSVG();
	}

	/**
	 * Devuelve el rectangulo definido por las coordenadas de dos vertices opuestos. Siempre
	 * se devuelve el punto menor como punto de partida y luego la anchura y altura.
	 *
	 * @param x0 Parte x de la primera coordenada
	 * @param y0 Parte y de la primera coordenada
	 * @param x1 Parte x de la segunda coordenada
	 * @param y1 Parte y de la segunda coordenada
	 * @return Un objeto SVGRect que representa el rectangulo como punto de inicio (x, y) y
	 * anchura, altura.
	 */
	private Rectangle makeRect(int x0, int y0, int x1, int y1) {
		int newX, newY, width, height;
		Rectangle result = new Rectangle(0, 0, 0, 0);

		if(x0 < x1) {
			newX = x0;
			width = x1 - x0;
		} else {
			newX = x1;
			width = x0 - x1;
		}

		if(y0 < y1) {
			newY = y0;
			height = y1 - y0;
		} else {
			newY = y1;
			height = y0 - y1;
		}

		result.x = newX;
		result.y = newY;
		result.width = width;
		result.height = height;

		return result;
	}

	/**
	 * Asigna un tipo de movimiento al svg por la pantalla.
	 *
	 * @param panType El tipo de movimiento que tendra el svg por la pantalla. Los unicos valores validos son
	 * MouseHandler.PAN_LINE y MouseHandler.PAN_MOVE.
	 *
	 * @see MouseHandler#PAN_LINE
	 * @see MouseHandler#PAN_MOVE
	 */
	public void setPanType(int panType) {
		if(panType == PAN_LINE || panType == PAN_MOVE) {
			this.panType = panType;
		}
	}

	public String getSelectableAncestorId() {
		return selectableAncestorId;
	}
	
	public void setSelectableAncestorId(String selectableAncestorId) {
		this.selectableAncestorId = selectableAncestorId;
	}
	
	public String [] getAllAncestorsId() {
		return allAncestorsIds;
	}
	
	public void setAllAncestorsIds(String [] allAncestorIds) {
		this.allAncestorsIds = allAncestorIds;
	}	

	public void initDraw(String parentNodeId) {
		this.parentNodeId = parentNodeId;
		if (drawingPoints == null) {
			drawingPoints = new Vector();
		}
		else {
			drawingPoints.removeAllElements();
		}
	}
	
	public String getParentNodeId() {
		return parentNodeId;
	}
	
	public SVGNode endDraw() {
		logger.debug("Insertando elemento dibujado en el SVG");
		
		if (drawingPoints.size() < 2) {
			logger.warn("Descartada linea o poligono de menos de 2 puntos");
			return null;
		}
		
		SVGDocument document = canvas.raster.document;
		SVGSVGElem rootElem = (SVGSVGElem) document.root;

		SVGNode parentNode = SVGNode.getNodeById(rootElem, new TinyString(parentNodeId.toCharArray()));

		if (parentNode == null) {
			//TODO informar del error?
			logger.warn("No se ha encontrado el nodo padre para insertar el dibujo");
			return null;
		}

		// Calculo de los puntos en el SVG
		TinyMatrix inverse = parentNode.getGlobalTransform().inverse();
		TinyVector tinyPoints = new TinyVector(drawingPoints.size());
		for (int i=0; i<drawingPoints.size(); i++) {
			TinyPoint p = (TinyPoint) drawingPoints.elementAt(i);
			p.x = p.x << TinyUtil.FIX_BITS;
			p.y = p.y << TinyUtil.FIX_BITS;
			inverse.transform(p);
			tinyPoints.addElement(p);
		}
		// Nuevo elemento
		SVGPolygonElem polyElem = null;
		if (type == POLYLINE_MOUSE) {
			polyElem = (SVGPolygonElem) document.createElement(SVG.ELEM_POLYLINE);
			polyElem.fill = TinyColor.NONE; // A las lineas no se les pone relleno
		}
		else if (type == POLYGON_MOUSE || type == POINT_MOUSE) {
			polyElem = (SVGPolygonElem) document.createElement(SVG.ELEM_POLYGON);
		}
		polyElem.points = tinyPoints;

		// Insertar en el nodo padre
		parentNode.addChildAndRecordEvent(polyElem, parentNode.children.count);
		
		lastInsertedNode = polyElem;
		
		drawSvg();

		return polyElem;
	}
	
	private void createPoint(int x, int y) {
		logger.debug("Insertando punto en el SVG");
		
		SVGDocument document =  canvas.raster.document;
		SVGSVGElem rootElem = (SVGSVGElem)document.root;
		
		SVGNode parentNode = SVGNode.getNodeById(rootElem, new TinyString(parentNodeId.toCharArray()));

		if (parentNode == null) {
			logger.warn("No se ha encontrado el nodo padre para insertar el dibujo");
			//TODO informar del error?
			return;
		}

		// Calculo de los puntos en el SVG
		TinyMatrix inverse = parentNode.getGlobalTransform().inverse();
		TinyPoint p = new TinyPoint(x << TinyUtil.FIX_BITS, y << TinyUtil.FIX_BITS);
		inverse.transform(p);
		TinyPoint r = new TinyPoint(x+3 << TinyUtil.FIX_BITS, y << TinyUtil.FIX_BITS);
		inverse.transform(r);

		// Nuevo elemento
		SVGEllipseElem ellipseElem = (SVGEllipseElem) document.createElement(SVG.ELEM_CIRCLE);
		ellipseElem.cx = p.x;
		ellipseElem.cy = p.y;
		ellipseElem.rx = r.x-p.x;
		ellipseElem.ry = ellipseElem.rx;

		// Insertar en el nodo padre
		parentNode.addChildAndRecordEvent(ellipseElem, parentNode.children.count);
		
		lastInsertedNode = ellipseElem;
		
		drawSvg();

		// Notificar a la aplicacion cliente
		Enumeration en = canvas.getSVGViewerPointInsertedListeners().elements();
		while (en.hasMoreElements()) {
			SVGViewerPointInsertedListener l = (SVGViewerPointInsertedListener) en.nextElement();
			l.pointInserted(ellipseElem);
		}
	}
	
	public void cancelDraw() {
		if (type == POLYLINE_MOUSE) {
			drawXORPolyline(drawingPoints);
			drawingPoints.removeAllElements();
		}
		else if (type == POLYGON_MOUSE) {
			drawXORPolygon(drawingPoints);
			drawingPoints.removeAllElements();
		}
		else {
			logger.debug("Eliminando dibujo insertado. lastInsertedNode: " + lastInsertedNode + ", parentNodeId: " + parentNodeId);
			if (lastInsertedNode != null && parentNodeId != null) {
				SVGNode parentNode = SVGNode.getNodeById(canvas.raster.root, new TinyString(parentNodeId.toCharArray()));
				logger.debug("Nodo padre: " + parentNode);
				if (parentNode != null && parentNode.children != null) {
					for (int i=0; i<parentNode.children.count; i++) {
						if (parentNode.children.data[i] == lastInsertedNode) {
							logger.debug("Nodo a eliminar encontrado");
							parentNode.removeChild(i);
							lastInsertedNode = null;
							drawSvg();
							break;
						}
					}
				}
			}

		}
	}
	
	public SVGNode getLastInsertedNode() {
		return lastInsertedNode;
	}
}
