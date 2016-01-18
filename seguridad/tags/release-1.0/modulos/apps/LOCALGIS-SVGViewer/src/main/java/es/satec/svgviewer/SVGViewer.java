package es.satec.svgviewer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.events.EventTarget;

import com.tinyline.svg.ImageLoader;
import com.tinyline.svg.SVG;
import com.tinyline.svg.SVGAttr;
import com.tinyline.svg.SVGDocument;
import com.tinyline.svg.SVGImageElem;
import com.tinyline.svg.SVGNode;
import com.tinyline.svg.SVGParser;
import com.tinyline.svg.SVGRaster;
import com.tinyline.svg.SVGSVGElem;
import com.tinyline.svg.SVGUnknownElem;
import com.tinyline.tiny2d.TinyBitmap;
import com.tinyline.tiny2d.TinyMatrix;
import com.tinyline.tiny2d.TinyPixbuf;
import com.tinyline.tiny2d.TinyPoint;
import com.tinyline.tiny2d.TinyRect;
import com.tinyline.tiny2d.TinyString;
import com.tinyline.tiny2d.TinyUtil;
import com.tinyline.tiny2d.TinyVector;

import es.satec.svgviewer.event.SVGViewerDrawListener;
import es.satec.svgviewer.event.SVGViewerErrorEvent;
import es.satec.svgviewer.event.SVGViewerErrorListener;
import es.satec.svgviewer.event.SVGViewerLinkListener;
import es.satec.svgviewer.event.SVGViewerLoadListener;
import es.satec.svgviewer.event.SVGViewerPointInsertedListener;
import es.satec.svgviewer.event.SVGViewerRasterTransformListener;
import es.satec.svgviewer.event.SVGViewerRasterUpdateListener;

/**
 * Área de dibujo del SVG.
 * 
 * @author jpresa
 */
public class SVGViewer extends Canvas implements ImageConsumer, ImageLoader, Runnable, EventTarget {

	private static Logger logger = (Logger) Logger.getInstance(SVGViewer.class);
	
	protected int width, height;
	
	/** Imagen de fondo */
	protected Image backImage;
	
	/** Imagen para dibujar el SVG */
	private Image svgImage;
	
	/** Imagen para dibujar todo en segundo plano */
	private Image offscreenImage;
	
	/** The SVG renderer */
	protected SVGRaster raster;
	
	private ImageProducer imageProducer;

	/** Thread procesador de eventos */
	private Thread thread;

	/** Cola de eventos */
	protected SVGEventQueue eventQueue;

	/** Listeners de eventos */
	private TinyVector listeners;
	
	/** Manejador de eventos del raton */
	private MouseHandler mouseHandler;

	/** Cache de imagenes */
	private Hashtable imageCash;
	/** URL base de la imagen */
    protected URL baseURL;
	
	/** Documento SVG pendiente de cargar hasta que no se inicialice el componente */
	private URL pendingSVG;
	
	/** Documento SVG cargado actualmente */
	private URL currentSVG;
	
	private Vector errorListeners;
	private Vector linkListeners;
	private Vector rasterTransformListeners;
	private Vector rasterUpdateListeners;
	private Vector pointInsertedListeners;
	private Vector loadListeners;
	private Vector drawListeners;

	/** Indica si hay svg cargado */
	private boolean loaded;

	/** Zona actual dibujada en coordenadas SVG */
	protected TinyRect currentZone;
	
	/** Dibujado sincrono o asincrono */
	private boolean synchPaint; 
	
	/** Flag de antialiasing activo */
	private boolean antialiased;
	
	ControlListener adapter;
	
	
    public SVGViewer(Composite parent, int style, boolean synchPaint) {
		super(parent, style);
		
		this.synchPaint = synchPaint;
    	
		this.addControlListener(new org.eclipse.swt.events.ControlAdapter() {
			public void controlResized(org.eclipse.swt.events.ControlEvent e) {
				viewerResized();
			}
		});


		this.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				if(svgImage == null) {
					//raster.setCamera();
					//raster.update();
					//raster.sendPixels();
					drawSVG();
				}

				GC gc = new GC(offscreenImage);
				
				gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
				gc.fillRectangle(0, 0, width, height);
				
				drawBackground(gc);
				
				// Dibujo de la imagen con el SVG
				if(svgImage != null && !svgImage.isDisposed()) {
					gc.drawImage(svgImage, 0, 0);
				}

				drawForeground(gc);
				gc.dispose();
				
				e.gc.drawImage(offscreenImage, 0, 0);
			}
		});

		// Establece la implementación de ImageLoader para cargar imagenes
		SVGImageElem.setImageLoader(this);

		// Colas de eventos
		eventQueue  =  new SVGEventQueue();
		listeners   =  new TinyVector(4);
		// Cache de imagenes
		imageCash = new Hashtable();
		
		// Listeners eventos sobre el SVG
		errorListeners = new Vector();
		linkListeners = new Vector();
		rasterTransformListeners = new Vector();
		rasterUpdateListeners = new Vector();
		pointInsertedListeners = new Vector();
		loadListeners = new Vector();
		drawListeners = new Vector();

		currentZone = new TinyRect();
		
		// Manejador de eventos
		mouseHandler = new MouseHandler(this);
		mouseHandler.type = MouseHandler.LINK_MOUSE;
		//NUEVO
		//mouseHandler.setPanType(MouseHandler.PAN_LINE);
		mouseHandler.setPanType(MouseHandler.PAN_MOVE);
		//FIN NUEVO
		addMouseListener(mouseHandler);
		addMouseMoveListener(mouseHandler);

		antialiased = true;
		
//		PlayerListener defaultListener = new PlayerListener(this);
//		this.addEventListener("default", defaultListener, false);
//		this.start();
	}
    
    public SVGViewer(Composite parent, int style) {
		this(parent, style, false);
	}    
	
    protected void drawBackground(GC gc) {
		if (backImage != null && !backImage.isDisposed())
			gc.drawImage(backImage, 0, 0);
	}

	protected void drawForeground(GC gc) {
	}

	private void viewerResized() {
		width = getSize().x;
		height = getSize().y;
		logger.debug("Tamaño del viewer: " + width + "x" +  height);

		if (svgImage != null && !svgImage.isDisposed()) svgImage.dispose();
		
		if (offscreenImage != null && !offscreenImage.isDisposed()) {
			offscreenImage.dispose();
		}
		offscreenImage = new Image(getDisplay(), width, height);

		// Crea el raster SVG
		TinyPixbuf buffer = new TinyPixbuf(width, height);
		raster = new SVGRaster(buffer);
		imageProducer = new ImageProducer(raster);
		imageProducer.setConsumer(this);
		raster.setSVGImageProducer(imageProducer);

        // Antialiasing
		raster.setAntialiased(antialiased);
		
		// Poner fondo transparente
		raster.setBackground(0x00FFFFFF);
		
		// Avisar al manejador de eventos de que el tamaño ha cambiado
		if (mouseHandler != null) {
			mouseHandler.viewerResized();
		}
		
		// Cargar el documento pendiente, si existe
		if (pendingSVG != null) {
			loadSVGUrl(pendingSVG);
			pendingSVG = null;
		}
		else {
			if (currentSVG != null) {
				loadSVGUrl(currentSVG);
			}
		}
	}
	
	public MouseHandler getMouseHandler(){
		return mouseHandler;
	}
	
	public boolean isSynchPaint() {
		return synchPaint;
	}
	
	public void setSynchPaint(boolean synchPaint) {
		this.synchPaint = synchPaint;
	}

	public SVGDocument getSVGDocument() {
		if (raster!=null)
			return raster.document;
		else
			return null;
	}
	
	/**
	 * Realiza la carga desde una url de una imagen en formato SVGTiny,
	 * en este caso no se espera que tenga información basada en capas o layers.
	 * @param url
	 */
	public void loadSVGUrl(URL url) {
//		SVGEvent event = new SVGEvent(SVGEvent.EVENT_LOAD, url);
//		postEvent(event);
		
		// Si el componente no esta inicializado, el documento se guarda como pendiente de cargar
		if (raster == null) {
			pendingSVG = url;
			return;
		}
		
		currentSVG = url;
		
		// 1. Reset the event queue
		eventQueue.reset();
		// 2. Load the document
		SVGDocument document = loadSVG(url);
		if(document == null) return;
		Enumeration e = loadListeners.elements();
		while (e.hasMoreElements()) {
			((SVGViewerLoadListener) e.nextElement()).documentLoaded(document);
		}
		// 3. Set the loaded document to be current document
		//canvas.currentURL = new String(url);
		raster.setSVGDocument(document);
		// 4. Set the original view
		raster.view.x      = raster.origview.x;
		raster.view.y      = raster.origview.y;
		raster.view.width  = raster.origview.width;
		raster.view.height = raster.origview.height;
		// 5. Set the camera transform
		//raster.setCamera();
		// 6. Draw
		drawSVG();
	}

	/**
	 * Loads an SVGT document from the given URL.
	 * @param urlStr  The SVGT document URL or path.
	 * @return     An SVGT document.
	 */
	protected SVGDocument loadSVG(URL url)
	{
		InputStream is = null;
		try
		{
			String completeUrl = url.toExternalForm();
			int p = completeUrl.lastIndexOf('/');
			if (p!=-1) {
				baseURL = new URL(completeUrl.substring(0, p+1));
			}
			is = url.openStream();
			if(url.toString().endsWith("svgz")) {
				is = new GZIPInputStream(is);
			}
		}
		catch(Exception ex) {
			logger.error(ex);
			//ex.printStackTrace();
			Enumeration en = errorListeners.elements();
			while (en.hasMoreElements()) {
				SVGViewerErrorListener l = (SVGViewerErrorListener) en.nextElement();
				l.error(new SVGViewerErrorEvent(ex));
			}
		}
		
		return loadSVG(is);
	}

	/**
	 * Loads an SVGT document from the given InputStream.
	 * @param   is  The InputStream.
	 * @return     An SVGT document.
	 */
	private SVGDocument loadSVG(InputStream is)
	{
		loaded = false;
		String errorMessage = null;
		Throwable throwable = null;
		SVGDocument doc = raster.createSVGDocument();
		try {
			// Read and parse the SVGT stream
			TinyPixbuf pixbuf = raster.getPixelBuffer();
			// Create the SVGT attributes parser
			SVGAttr attrParser = new SVGAttr(pixbuf.width, pixbuf.height);
			// Create the SVGT stream parser
			SVGParser parser = new SVGParser(attrParser);
			// Parse the input SVGT stream parser into the document
			int errorCode = parser.load(doc, is);
			errorCode = errorCode >> 10;
			if (errorCode != 0) {
				logger.error("Error al parsear SVG. Código:" + errorCode);
				errorMessage = "Error de sintaxis XML";
			}
			else {
				if (doc.root.children==null || doc.root.children.data[0] instanceof SVGUnknownElem) {
					errorMessage = "El fichero no es un SVG válido";
				}
				else {
					// Fichero correcto
					logger.debug("Fichero cargado correctamente");
					loaded = true;
				}
			}
		}
		catch(OutOfMemoryError memerror) {
			doc = null;
			Runtime.getRuntime().gc();
			logger.error("Not enought memory", memerror);
			errorMessage = "Memoria insuficiente";
			throwable = memerror;
			//memerror.printStackTrace();
		}
		catch(SecurityException secex) {
			doc = null;
			logger.error("Security violation", secex);
			errorMessage = "Violación de seguridad";
			throwable = secex;
			//secex.printStackTrace();
		}
		catch(Exception ex) {
			doc = null;
			logger.error("Not in SVGT format", ex);
			errorMessage = "El fichero no está en formato SVG Tiny";
			throwable = ex;
			//ex.printStackTrace();
		}
		catch(Throwable t) {
			doc = null;
			logger.error("Not in SVGT format", t);
			errorMessage = "El fichero no está en formato SVG Tiny";
			throwable = t;
			//thr.printStackTrace();
		}
		finally {
			if (errorMessage != null) {
				Enumeration en = errorListeners.elements();
				while (en.hasMoreElements()) {
					SVGViewerErrorListener l = (SVGViewerErrorListener) en.nextElement();
					l.error(new SVGViewerErrorEvent(errorMessage, throwable));
				}
			}
			try {
				if (is != null) is.close();
			}
			catch(IOException ioe) {
				logger.error(ioe);
				//ioe.printStackTrace();
			}
		}
		return doc;
	}

	/**
	 * Carga una imagen en la posición X,Y dada.
	 */
	public void loadImage(int x, int y, int width, int height, String path) {
//		SVGEvent event = new SVGEvent(SVGEvent.EVENT_LOAD_IMAGE, new SVGLoadImageEventData(x, y, width, height, path));
//		postEvent(event);
		
		SVGDocument document = raster.document;
		SVGSVGElem rootElem = (SVGSVGElem)document.root;
		
		SVGNode selNode = null;
		//Parece que la siguiente linea no es necesaria. La matriz de transformacion es la misma que la del root
		//selNode = rootElem.nodeHitAt(canvas.raster, new TinyPoint(evData.getX(), evData.getY()));
		if (selNode == null) selNode = raster.root;

		// Calculo de la posicion de la imagen en el SVG
		TinyMatrix inverse = selNode.getGlobalTransform().inverse();
		TinyPoint p = new TinyPoint(x<<TinyUtil.FIX_BITS, y<<TinyUtil.FIX_BITS);
		inverse.transform(p);
		TinyPoint p2 = new TinyPoint((x+width)<<TinyUtil.FIX_BITS, (y+height)<<TinyUtil.FIX_BITS);
		inverse.transform(p2);
		// Nuevo elemento imagen
		SVGImageElem imageElem = (SVGImageElem) document.createElement(SVG.ELEM_IMAGE);
		imageElem.x = p.x;
		imageElem.y = p.y;
		imageElem.width = p2.x - p.x;
		imageElem.height = p2.y - p.y;
		imageElem.xlink_href = new TinyString(path.toCharArray());

		// Insertar la imagen en el root
		rootElem.addChildAndRecordEvent(imageElem, rootElem.children.count);
		
		drawSVG();
	}

	/**
	 * Se encarga de devolver los identificadores de cada uno de los layers cargados.
	 * @return
	 */
	public String[] getAllIDLayers() {
		SVGDocument document = raster.document;
		int numLayers = document.root.children.count;
		String[] idLayers= new String[numLayers];
		for (int i=0; i<numLayers; i++) {
			SVGNode layer = (SVGNode) document.root.children.data[i];
			if (layer.id != null) {
				idLayers[i] = new String(layer.id.data);
			}
		}
		return idLayers;
	}

	/**
	 * Mueve un nodo a la posición dada entre el conjunto de nodos que están a su mismo nivel.
	 * @param parent Nodo padre
	 * @param node Nodo hijo a mover
	 * @param newPos Nueva posición
	 */
	public void moveNode(SVGNode parent, SVGNode node, int newPos) {
		TinyVector children = parent.children;
		if (children != null) {
			if (newPos >= 0 && newPos < children.count) {
				logger.debug("Moviendo nodo...");
				int curPos = children.indexOf(node, 0); // Posicion actual del nodo a mover
				logger.debug("Posicion actual: " + curPos + ", posicion nueva: " + newPos);
				if (curPos == -1) return;
				if (curPos < newPos) {
					// Desplazamiento hacia delante de los nodos hermanos
					for (int i=curPos; i<newPos; i++) {
						children.data[i] = children.data[i+1];
					}
				}
				else if (curPos > newPos) {
					// Desplazamiento hacia atras de los nodos hermanos
					for (int i=curPos; i>newPos; i--) {
						children.data[i] = children.data[i-1];
					}
				}
				// Insercion del nodo en su nueva posicion
				children.data[newPos] = node;
			}
		}
	}
	
	/**
	 * Activa el modo Zoom In de visualización de la imagen. 
	 */
	public void setModeZoomIn() {
		mouseHandler.type = MouseHandler.ZOOM_IN_MOUSE;
		logger.debug("Modo zoom in");
	}
	
	/**
	 * Activa el modo Zoom Out de visualización de la imagen. 
	 */
	public void setModeZoomOut() {
		mouseHandler.type = MouseHandler.ZOOM_OUT_MOUSE;
		logger.debug("Modeo zoom out");
	}

	/**
	 * Hace zoom out directamente, sin necesidad de pulsar sobre el canvas.
	 */
	public void zoomOut() {
		mouseHandler.zoomOut();
	}
	
	public void zoomIn(int x0, int y0, int x1, int y1,boolean force) {
		mouseHandler.zoomIn(x0,y0,x1,y1,force);
	}
	public void zoomIn(int x0, int y0, int x1, int y1) {
		mouseHandler.zoomIn(x0,y0,x1,y1,false);
	}
	
	
	/**
	 * Activa el modo de visualización de movimiento de la imagen vectorial.
	 */
	public void setModePan() {
		mouseHandler.type = MouseHandler.PAN_MOUSE;
		logger.debug("Modo pan");
	}
	
	/**
	 * Desplaza la imagen
	 * @param dx Desplazamiento horizontal en pixeles
	 * @param dy Desplazamiento vertical en pixeles
	 */
	public void pan(int dx, int dy) {
		mouseHandler.pan(dx, dy);
	}
	
//	/**
//	 * Activa el modo de seleccion.
//	 */
//	public void setModeLink(String selectableAncestorId) {
//		if(selectableAncestorId!=null)
//			mouseHandler.setSelectableAncestorId(selectableAncestorId);
//		mouseHandler.setAllAncestorsIds(getAllIDLayers());
//		mouseHandler.type = MouseHandler.LINK_MOUSE;
//		logger.debug("Modo seleccion. Id nodo ancestro: " + selectableAncestorId);
//	}
//	
//	/**
//	 * Activa el modo de seleccion.
//	 */
//	public void setModeLink() {
//		setModeLink(null);
//	}
	
	public void setModeLink(String selectableAncestorId) {
		mouseHandler.setSelectableAncestorId(selectableAncestorId);
		mouseHandler.type = MouseHandler.LINK_MOUSE;
		logger.debug("Modo seleccion. Id nodo ancestro: " + selectableAncestorId);
	}
	
	/**
	 * Activa el modo de seleccion.
	 */
	public void setModeLink() {
		mouseHandler.type = MouseHandler.LINK_MOUSE;
		logger.debug("Modo seleccion");
	}
	
	/**
	 * Dibuja un nodo que representa una linea.
	 * @param parentNodeId Id del nodo padre
	 */
	public void setModeDrawLine(String parentNodeId) {
		mouseHandler.initDraw(parentNodeId);
		mouseHandler.type = MouseHandler.POLYLINE_MOUSE;
		logger.debug("Modo dibujar linea. Id nodo padre: " + parentNodeId);
	}
	
	/**
	 * Dibuja un nodo que representa un poligono.
	 * @param parentNodeId Id del nodo padre
	 */
	public void setModeDrawPolygon(String parentNodeId) {
		mouseHandler.initDraw(parentNodeId);
		mouseHandler.type = MouseHandler.POLYGON_MOUSE;
		logger.debug("Modo dibujar poligono. Id nodo padre: " + parentNodeId);
	}
	
	/**
	 * Dibuja un nodo que representa un punto.
	 * @param parentNodeId Id del nodo padre
	 */
	public void setModeDrawPoint(String parentNodeId) {
		mouseHandler.initDraw(parentNodeId);
		mouseHandler.type = MouseHandler.POINT_MOUSE;
		logger.debug("Modo dibujar punto. Id nodo padre: " + parentNodeId);
	}

	/**
	 * Confirma la inserción de un elemento de dibujo en el SVG, cuando éste
	 * puede tener un número variable de vértices.
	 * @return El nodo dibujado
	 */
	public SVGNode endDraw() {
		return mouseHandler.endDraw();
	}
	
	/**
	 * Cancela la inserción del último elemento dibujado.
	 */
	public void cancelDraw() {
		mouseHandler.cancelDraw();
	}
	
	/**
	 * Obtiene el ultimo nodo dibujado e insertado.
	 */
	public SVGNode getLastInsertedNode() {
		return mouseHandler.getLastInsertedNode();
	}
	
	/**
	 * Obtiene el nodo sobre el que se pueden seleccionar elementos
	 */
	public String getSelectableAncestorId() {
		return mouseHandler.getSelectableAncestorId();
	}

	/**
	 * Establece el nodo sobre el que se pueden seleccionar elementos
	 */
	public void setSelectableAncestorId(String selectableAncestorId) {
		mouseHandler.setSelectableAncestorId(selectableAncestorId);
	}

	/**
	 * Escribe en el stream de salida que se le pasa por parámetro el contenido del árbol DOM en xml.
	 */
	public void serializeSVG2XML(OutputStream outputStream) throws IOException {
		logger.debug("Serializando SVG...");
		SVGDocument document = raster.getSVGDocument();
		document.serializeSVG2XML(outputStream);
		logger.debug("SVG serializado");
	}

	/**
	 * Escribe en el stream de salida que se le pasa por parámetro el contenido del árbol DOM en xml,
	 * solamente para los nodos del SVG que han sido modificados.
	 * Devuelve un Vector con los nodos que contienen imagenes asociadas.
	 */
	public Vector serializeModifiedNodes2XML(OutputStream outputStream,
											boolean generateIds, 
											boolean generateHeader,
											String idValueNotSerialized
											) throws IOException {
		logger.debug("Serializando Cambios del SVG...");
		SVGDocument document = raster.getSVGDocument();
		return document.serializeModifiedNodes2XML(outputStream, generateIds, generateHeader,idValueNotSerialized);
	}

	/**
	 * Añade un evento a la cola.
	 *
	 * @param theEvent instancia de la clase Event o de una subclase.
	 */
	public synchronized void postEvent(SVGEvent theEvent)
	{
		//IMPORTANT
		theEvent.eventTarget = this;
		eventQueue.postEvent(theEvent);
	}

	//////////////////////////////////////////////////////////////////////////////////////////
	// Metodos de la interfaz ImageLoader
	//////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Crea un TinyBitmap para la URL o path dado.
     */
	public TinyBitmap createTinyBitmap(TinyString uri) {
		String imgRef = new String(uri.data);
		logger.debug("Creando bitmap de URI: " + imgRef);
		SVGBitmap bitmap = null;
		try {
			URL url = null;
			try {
				url = new URL(imgRef);
			} catch (MalformedURLException e) {
				logger.debug(imgRef + " no es una URL completa");
				url = new URL(baseURL, imgRef);
				logger.debug("URL compuesta: " + url.toExternalForm());
			}
			// check in the cash
			bitmap = (SVGBitmap) imageCash.get(url);
			// not found
			if(bitmap == null) {
				bitmap = new SVGBitmap(url);
				imageCash.put(url, bitmap);
			}
		}
		catch (Exception ex){
			//ex.printStackTrace();
			logger.error("Error al crear bitmap", ex);
		}
		return bitmap;
	}

	/**
	 * Crea un TinyBitmap que decodifica la imagen almacenada en el array de bytes
	 * especificado, con el offset y la longitud indicadas.
	 */
	public TinyBitmap createTinyBitmap(byte[] imageData, int imageOffset, int imageLength) {
		return new SVGBitmap(imageData, imageOffset, imageLength);
	}

	//////////////////////////////////////////////////////////////////////////////////////////
	// Metodos de la interfaz ImageConsumer
	//////////////////////////////////////////////////////////////////////////////////////////
	
	public void setPixels(final int xmin, final int ymin, final int width, final int height, final int[] pixels32, final int pixeloffset, final int pixelscan) {
		logger.debug("Actualizando imagen de pantalla");
		// Zona actual e imagen de fondo
		if (loaded) {
			SVGNode root = raster.document.root;
			// Zona actual
			TinyMatrix inverse = root.getGlobalTransform().inverse();
			TinyPoint p1 = new TinyPoint(0, 0);
			inverse.transform(p1);
			TinyPoint p2 = new TinyPoint(width << TinyUtil.FIX_BITS, height << TinyUtil.FIX_BITS);
			inverse.transform(p2);
			
			if (currentZone.isEmpty() || currentZone.xmin != p1.x || currentZone.ymin != p1.y || currentZone.xmax != p2.x || currentZone.ymax != p2.y) {
				currentZone.xmin = p1.x;
				currentZone.ymin = p1.y;
				currentZone.xmax = p2.x;
				currentZone.ymax = p2.y;
				// Nueva imagen de fondo
				if (backImage != null && !backImage.isDisposed()) {
					backImage.dispose();
					backImage = null;
				}
				backImage = getBackImage();
			}
		}

		// Imagen del SVG
		int totalHeight=0;
		try {
			totalHeight = pixels32.length/pixelscan;

			// Establecer los pixeles y los alphas
			ImageData imData = new ImageData(pixelscan, totalHeight, 32, new PaletteData(0xFF0000, 0xFF00, 0xFF));

			byte[] alphaData = new byte[pixels32.length];
			for (int i=0; i<pixels32.length; i++) {
				alphaData[i] = (byte) (pixels32[i] >> 24);
			}
			imData.setPixels(0, 0, pixels32.length, pixels32, 0);
			imData.setAlphas(0, 0, pixels32.length, alphaData, 0);
			if (svgImage != null && !svgImage.isDisposed()) svgImage.dispose();
			//logger.debug("Antes de generar la imagen1:");	
			//logger.debug("Antes de generar la imagen2:"+imData.height);	
			svgImage = new Image(Display.getDefault(), imData);
			logger.debug("Imagen generada");	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("ERROR");
		}

		logger.debug("Verificando clipping");	
		// Clipping
		if (width<pixelscan || height<totalHeight) {
			logger.debug("Hay clipping");
			GC gc = new GC(svgImage);
			int rasterBackground = raster.getBackground();
			Color rasterBackgroundColor = new Color(Display.getDefault(), (rasterBackground & 0x00FF0000) >> 16,
					(rasterBackground & 0x0000FF00) >> 8, (rasterBackground & 0x000000FF));
			gc.setBackground(rasterBackgroundColor);
			if (xmin > 0) {
				gc.fillRectangle(0, 0, xmin, totalHeight);
			}
			if (xmin + width < pixelscan) {
				gc.fillRectangle(xmin + width, 0, pixelscan-xmin-width, totalHeight);
			}
			if (ymin > 0) {
				gc.fillRectangle(0, 0, pixelscan, ymin);
			}
			if (ymin + height < totalHeight) {
				gc.fillRectangle(0, ymin + height, pixelscan, totalHeight-ymin-height);
			}
			rasterBackgroundColor.dispose();
			gc.dispose();
		}

		//logger.debug("Arrancando thread de display");
		if (synchPaint) {
			getDisplay().syncExec(new redrawThread());
		}
		else {
			getDisplay().asyncExec(new redrawThread());
		}
		logger.debug("Imagen de pantalla actualizada");
	}
	
	/** Thread para hacer el dibujado */
	private class redrawThread implements Runnable {
		public void run() {
			if (!isDisposed()) {
				redraw();
				//update();
			}
		}
	}
	
	protected Image getBackImage() {
		return null;
	}
	
	public void reloadCurrentZone() {
		currentZone = new TinyRect();
	}
	
	public void drawSVG() {
		Enumeration en = drawListeners.elements();
		while (en.hasMoreElements()) {
			((SVGViewerDrawListener) en.nextElement()).beginDraw();
		}
		
		try {
			raster.setCamera();

			en = rasterTransformListeners.elements();
			while (en.hasMoreElements()) {
				SVGViewerRasterTransformListener l = (SVGViewerRasterTransformListener) en.nextElement();
				l.rasterTransformed();
			}
			raster.update();
			en = rasterUpdateListeners.elements();
			while (en.hasMoreElements()) {
				SVGViewerRasterUpdateListener l = (SVGViewerRasterUpdateListener) en.nextElement();
				l.rasterUpdated();
			}
			raster.sendPixels();
			logger.debug("Draw svg finalizado");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			en = drawListeners.elements();
			while (en.hasMoreElements()) {
				((SVGViewerDrawListener) en.nextElement()).endDraw();
			}
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////
	// Metodos de la interfaz Runnable
	//////////////////////////////////////////////////////////////////////////////////////////

	/** Inicia el thread procesador de eventos */
	public synchronized void start() {
		thread = new Thread(this);
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}


	/** Detiene el thread procesador de eventos */
	public synchronized void stop() {
		thread = null;
		SVGEvent event = new SVGEvent(SVGEvent.EVENT_UNLOAD, null);
		postEvent(event);
	}

	/**
	 * run() del thread procesador de eventos
	 */
	public void run() {

		Thread currentThread = Thread.currentThread();
		try {
			while (currentThread == thread) {
				eventQueue.handleEvent(eventQueue.getNextEvent());
			}
		} catch (InterruptedException e) {
			return;
		} catch( Throwable thr) {
			//thr.printStackTrace();
			logger.error(thr);
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////
	// Metodos de la interfaz EventTarget
	//////////////////////////////////////////////////////////////////////////////////////////
	/**
	 *
	 * <b>uDOM:</b> This method allows the registration of event listeners on the event target.
	 *
	 * @param type The event type for which the user is registering
	 *
	 * @param listener The listener parameter takes an interface implemented by the
	 * user which contains the methods to be called when the event occurs.
	 *
	 * @param useCapture If true, useCapture indicates that the user wishes
	 * to initiate capture. After initiating capture, all events of the
	 * specified type will be dispatched to the registered EventListener
	 * before being dispatched to any EventTargets beneath them in the tree.
	 * Events which are bubbling upward through the tree will not trigger an
	 * EventListener designated to use capture.
	 */
	public void addEventListener(java.lang.String type,
			org.w3c.dom.events.EventListener listener,
			boolean useCapture)
	{
		listeners.addElement(listener);
	}

	/**
	 *
	 * <b>uDOM:</b> This method allows the removal of event listeners from the event target.
	 *
	 * @param type Specifies the event type of the EventListener being removed.
	 * @param listener The listener parameter indicates the EventListener to be removed.
	 * @param useCapture Specifies whether the EventListener being removed was
	 * registered as a capturing listener or not.
	 */
	public void removeEventListener(java.lang.String type,
			org.w3c.dom.events.EventListener listener,
			boolean useCapture)

	{
		int i = listeners.indexOf(listener,0);
		if(i>0)
		{
			listeners.removeElementAt(i);
		}
	}
	/**
	 * <b>uDOM:</b> This method allows the dispatch of events into the implementations event model.
	 * Events dispatched in this manner will have the same behavior as events dispatched
	 * directly by the implementation.
	 *
	 * @param evt  Specifies the event type, behavior, and contextual
	 * information to be used in processing the event.
	 * @return    The return value of dispatchEvent indicates whether
	 * any of the listeners which handled the event called preventDefault.
	 * If preventDefault was called the value is false, else the value is true.
	 */
	public boolean dispatchEvent(org.w3c.dom.events.Event evt)
	{
		org.w3c.dom.events.EventListener h;
		for(int i=0; i < listeners.count; i++)
		{
			h = (org.w3c.dom.events.EventListener)listeners.data[i];
			if(h!=null) h.handleEvent(evt);
		}
		return true;
	}
	
	public boolean isAntialiased() {
		return antialiased;
	}
	
	public void setAntialiased(boolean antialiased) {
		if (this.antialiased != antialiased) {
			this.antialiased = antialiased;
			raster.setAntialiased(antialiased);
		}
	}

	public void addSVGViewerErrorListener(SVGViewerErrorListener listener) {
		errorListeners.addElement(listener);
	}
	
	public void removeSVGViewerErrorListener(SVGViewerErrorListener listener) {
		errorListeners.removeElement(listener);
	}
	
	public Vector getSVGViewerErrorListeners() {
		return errorListeners;
	}

	public void addSVGViewerRasterTransformListener(SVGViewerRasterTransformListener listener) {
		rasterTransformListeners.addElement(listener);
	}
	
	public void removeSVGViewerRasterTransformListener(SVGViewerRasterTransformListener listener) {
		rasterTransformListeners.removeElement(listener);
	}
	
	public Vector getSVGViewerRasterTransformListeners() {
		return rasterTransformListeners;
	}

	public void addSVGViewerRasterUpdateListener(SVGViewerRasterUpdateListener listener) {
		rasterUpdateListeners.addElement(listener);
	}
	
	public void removeSVGViewerRasterUpdateListener(SVGViewerRasterUpdateListener listener) {
		rasterUpdateListeners.removeElement(listener);
	}
	
	public Vector getSVGViewerRasterUpdateListeners() {
		return rasterUpdateListeners;
	}

	public void addSVGViewerLinkListener(SVGViewerLinkListener listener) {
		linkListeners.addElement(listener);
	}
	
	public void removeSVGViewerLinkListener(SVGViewerLinkListener listener) {
		linkListeners.removeElement(listener);
		
	}
	
	public Vector getSVGViewerLinkListeners() {
		return linkListeners;
	}
	
	public void addSVGViewerPointInsertedListener(SVGViewerPointInsertedListener listener) {
		pointInsertedListeners.addElement(listener);
	}
	
	public void removeSVGViewerPointInsertedListener(SVGViewerPointInsertedListener listener) {
		pointInsertedListeners.removeElement(listener);
	}
	
	public Vector getSVGViewerPointInsertedListeners() {
		return pointInsertedListeners;
	}

	public void addSVGViewerLoadListener(SVGViewerLoadListener listener) {
		loadListeners.addElement(listener);
	}
	
	public void removeSVGViewerLoadListener(SVGViewerLoadListener listener) {
		loadListeners.removeElement(listener);
	}
	
	public Vector getSVGViewerLoadListeners() {
		return loadListeners;
	}

	public void addSVGViewerDrawListener(SVGViewerDrawListener listener) {
		drawListeners.addElement(listener);
	}
	
	public void removeSVGViewerDrawListener(SVGViewerDrawListener listener) {
		drawListeners.removeElement(listener);
	}
	
	public Vector getSVGViewerDrawListeners() {
		return drawListeners;
	}
	
	/**
	 * Borramos la informacion y los handles asociados.
	 */
	public void dispose(){

		if (offscreenImage != null && !offscreenImage.isDisposed()) {
			offscreenImage.dispose();
		}
		if (svgImage!=null)
			svgImage.dispose();
		
		if (adapter!=null)
			this.removeControlListener(adapter);
	}
}
