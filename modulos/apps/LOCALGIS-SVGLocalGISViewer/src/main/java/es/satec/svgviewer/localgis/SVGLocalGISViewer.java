/**
 * SVGLocalGISViewer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.model.filterencoding.Expression;
import org.deegree.model.filterencoding.Filter;
import org.deegree.model.filterencoding.FilterConstructionException;
import org.deegree.model.filterencoding.FilterEvaluationException;
import org.deegree.model.filterencoding.FilterEvaluationNoPropertyException;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.satec.sld.SVG.SVGNodeFeature;

import com.ermapper.ecw.JNCSException;
import com.ermapper.ecw.JNCSFile;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.GeopistaSchemaFactory;
import com.tinyline.svg.SVG;
import com.tinyline.svg.SVGDocument;
import com.tinyline.svg.SVGGroupElem;
import com.tinyline.svg.SVGNode;
import com.tinyline.tiny2d.TinyColor;
import com.tinyline.tiny2d.TinyMatrix;
import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyPoint;
import com.tinyline.tiny2d.TinyRect;
import com.tinyline.tiny2d.TinyString;
import com.tinyline.tiny2d.TinyUtil;
import com.tinyline.tiny2d.TinyVector;

import es.satec.gps.srs.CoordinateConverter;
import es.satec.gps.srs.GeodeticPoint;
import es.satec.gps.srs.UTMPoint;
import es.satec.svgviewer.SVGViewer;
import es.satec.svgviewer.event.SVGViewerRasterTransformListener;
import es.satec.svgviewer.localgis.ecw.EcwReader;
import es.satec.svgviewer.localgis.shp.ShpException;
import es.satec.svgviewer.localgis.shp.ShpReader;
import es.satec.svgviewer.localgis.shp.Theme;
import es.satec.svgviewer.localgis.sld.AbstractStyle;
import es.satec.svgviewer.localgis.sld.DeferredSymbolizer;
import es.satec.svgviewer.localgis.sld.FeatureTypeStyle;
import es.satec.svgviewer.localgis.sld.Fill;
import es.satec.svgviewer.localgis.sld.Font;
import es.satec.svgviewer.localgis.sld.LabelPlacement;
import es.satec.svgviewer.localgis.sld.LayerSLDInfo;
import es.satec.svgviewer.localgis.sld.LineSymbolizer;
import es.satec.svgviewer.localgis.sld.NamedLayer;
import es.satec.svgviewer.localgis.sld.PointSymbolizer;
import es.satec.svgviewer.localgis.sld.PolygonSymbolizer;
import es.satec.svgviewer.localgis.sld.Rule;
import es.satec.svgviewer.localgis.sld.SLDFactory;
import es.satec.svgviewer.localgis.sld.SLDTinyChange;
import es.satec.svgviewer.localgis.sld.Stroke;
import es.satec.svgviewer.localgis.sld.StyledLayerDescriptor;
import es.satec.svgviewer.localgis.sld.Symbolizer;
import es.satec.svgviewer.localgis.sld.TextSymbolizer;
import es.satec.svgviewer.localgis.sld.UserStyle;

/**
 * Especialización del visor SVG que obtiene imágenes de un conjunto de servidores
 * de mapas para visualizarlas por detrás del SVG.  
 * @author jpresa
 */
public class SVGLocalGISViewer extends SVGViewer  {

	private static Logger logger = (Logger) Logger.getInstance(SVGLocalGISViewer.class);
	
	private static final String FORMAT_JPEG = "image/jpeg";
	private static final String FORMAT_PNG = "image/png";
	private static final String FORMAT_GIF = "image/gif";
	
	/** Datos de los servidores de mapas */
	private Vector mapServers;
	
	/** Clase que proporciona la posicion del GPS */
	private GPSProvider gpsProvider;
	
	/** Posicion actual del GPS traducido a coordenadas de pantalla */
	private int gpsX;
	private int gpsY;
	
	/** Indica si el seguimiento gps esta activado */
	private boolean gpsTrackingActive;

	/** SLDs a aplicar para cada capa */
	private Vector layerSLDs;
	
	/** Vector para almacenar los cambios hechos a los nodos tras aplicar el estilo */
	private Vector sldTinyChanges;
	
	/** Vector para almacenar los symbolizers que se pintan al final */
	private Vector deferredSymbolizers;
	
	/** Geopista schemas para cada capa */
	private Hashtable layerSchemas;
	
	/** Ficheros SHP */
	private ShpReader shpReader;
	
	/** Ficheros ECW */
	private EcwReader ecwReader;
	
	/** Imagen Puntero GPS */
	private Image gpsPointer;

	public SVGLocalGISViewer(Composite parent, int style, boolean synchPaint) {
		super(parent, style, synchPaint);
		mapServers = new Vector();
		gpsX = -1;
		gpsY = -1;
		gpsTrackingActive = false;
		gpsProvider = null;//new LocalGISGPSProvider(gpsPropertiesResource);
		
		layerSLDs = new Vector();
		sldTinyChanges = new Vector();
		deferredSymbolizers = new Vector();
		layerSchemas = new Hashtable();
		
		shpReader = new ShpReader();
		ecwReader = new EcwReader();

		this.addSVGViewerRasterTransformListener(new SVGViewerRasterTransformListener() {
			public void rasterTransformed() {
				try {
					applySLDs();
				} catch (Exception e) {
					logger.error("Error al aplicar los estilos", e);
				}
			}
		});
	}

	public SVGLocalGISViewer(Composite parent, int style) {
		this(parent, style, false);
	}
	
	public boolean isWMSActive() {
		Enumeration e = mapServers.elements();
		while (e.hasMoreElements()) {
			WMSData d = (WMSData) e.nextElement();
			if (d.isActive()) return true;
		}
		return false;
	}
	
	public Vector getMapServers() {
		return mapServers;
	}
	
	public void addMapServer(WMSData wmsData) {
		mapServers.addElement(wmsData);
	}
	
	public void removeMapServer(WMSData wmsData) {
		mapServers.removeElement(wmsData);
	}
	
	public void removeMapServerAt(int index) {
		mapServers.removeElementAt(index);
	}
	
	public void removeAllMapServers() {
		mapServers.removeAllElements();
	}
	
	private boolean hasBackImage() {
		return isWMSActive() || shpReader.somethingToDisplay() || ecwReader.somethingToDisplay();
	}
	
	public void setGPSPointerImage(Image gpsPointer){
		this.gpsPointer = gpsPointer;
	}
	
	/**
	 * Obtiene la imagen de fondo a partir de sucesivas peticiones a los WMS.
	 */
	protected Image getBackImage() {
	
		if (!hasBackImage()) return null;
		
		logger.debug("Obteniendo imagen de fondo");
		Image backImage = null;
		GC imageGC = null;
		
		try {
			SVGNode root = raster.root;
		
			// Lectura del desplazamiento a aplicar a las coordenadas del SVG
			// Parametros despX y despY del nodo raiz
			double despX = 0.0;
			double despY = 0.0;
			if (root.nameAtts != null && root.nameAtts.size() == 2) {
				despX = Double.parseDouble((String) root.nameAtts.elementAt(0));
				despY = Double.parseDouble((String) root.nameAtts.elementAt(1));
				logger.debug("despX=" + despX + ", despY=" + despY);
			}

			BoundingBox bbox = new BoundingBox(despX+((float)currentZone.xmin/256f), despY-((float)currentZone.ymax/256f),
				despX+((float)currentZone.xmax/256f), despY-((float)currentZone.ymin/256f));

			// Imagen de fondo sobre la que se van superponiendo los sucesivos mapas
			backImage = new Image(getDisplay(), width, height);
			imageGC = new GC(backImage);
		
			if (isWMSActive()) {
				InputStream imInputStream = null;
				Enumeration en = mapServers.elements();
				while (en.hasMoreElements()) {
					WMSData wmsData = (WMSData) en.nextElement();
					if (wmsData.isActive()) {
						Image wmsImage = null;
						try {
							boolean transparent = false;
							if (wmsData.getFormat().equals(FORMAT_PNG) || wmsData.getFormat().equals(FORMAT_GIF))
								transparent = true;
							String urlString = creaURLGetMap(wmsData.getBaseURL(), wmsData.getVersion(), bbox, width, height,
								wmsData.getLayers(), wmsData.getFormat(), wmsData.getSrs(), transparent, wmsData.getStyles());
							//String urlString = creaURLGetMap("http://wms.mapa.es/wms/wms.aspx", "1.1.0", bbox, width, height, "ORTOFOTOS", "image/jpeg", "EPSG:23030", false, "");
							URL urlGetMap = new URL(urlString);
							logger.debug("URL imagen: " + urlGetMap);
							// Obtener y dibujar la imagen
							imInputStream = urlGetMap.openStream();
							wmsImage = new Image(getDisplay(), imInputStream);
							imageGC.drawImage(wmsImage, 0, 0);
						} catch (Exception ex) {
							logger.error("Error al obtener imagen del WMS", ex);
							//e.printStackTrace();
						} finally {
							if (imInputStream != null) {
								try {
									imInputStream.close();
								} catch (IOException ex) {
									logger.error(ex);
									//e.printStackTrace();
								}
							}
							if (wmsImage != null && !wmsImage.isDisposed()) {
								wmsImage.dispose();
							}
						}
					}
				}
			}
			
			// Imagenes ECW
			if (ecwReader.somethingToDisplay()) {
				Image ecwImage = null;
				try {
					ecwImage = ecwReader.paint(despX+((float)currentZone.xmin/256f), despY-((float)currentZone.ymax/256f),
							despX+((float)currentZone.xmax/256f), despY-((float)currentZone.ymin/256f), width, height);
					if (ecwImage != null)
						imageGC.drawImage(ecwImage, 0, 0);					
				} catch (Exception ex) {
					logger.error("Error al dibujar imagenes ECW", ex);
				} finally {
					if (ecwImage != null && !ecwImage.isDisposed()) {
						ecwImage.dispose();
					}
				}
			}

			// Shapefiles
			if (shpReader.somethingToDisplay()) {
				Image shpImage = null;
				try {
					shpImage = shpReader.paint(despX+((float)currentZone.xmin/256f), despY-((float)currentZone.ymax/256f),
							despX+((float)currentZone.xmax/256f), despY-((float)currentZone.ymin/256f), width, height);
					if (shpImage != null)
						imageGC.drawImage(shpImage, 0, 0);
				} catch (Exception ex) {
					logger.error("Error al dibujar ficheros SHP", ex);
				} finally {
					if (shpImage != null && !shpImage.isDisposed()) {
						shpImage.dispose();
					}
				}
			}

		} catch (Exception ex) {
			logger.error("Error al cargar imagen de fondo", ex);
			//e.printStackTrace();
		} finally {
			if (imageGC != null && !imageGC.isDisposed())
				imageGC.dispose();
		}
		
		return backImage;
	}

	protected void drawForeground(GC gc) {
		if (deferredSymbolizers==null)
			return;
		Enumeration en = deferredSymbolizers.elements();
		while (en.hasMoreElements()) {
			DeferredSymbolizer deferredSymbolizer = (DeferredSymbolizer) en.nextElement();
			Symbolizer symbolizer = deferredSymbolizer.getSymbolizer();
			int devX = deferredSymbolizer.getDevX();
			int devY = deferredSymbolizer.getDevY();
			if (symbolizer instanceof TextSymbolizer) {
				TextSymbolizer textSymbolizer = (TextSymbolizer) symbolizer;
				String text = deferredSymbolizer.getText();
				org.eclipse.swt.graphics.Font swtFont = null;
				try {
					Font font = textSymbolizer.getFont();
					LabelPlacement lp = textSymbolizer.getLabelPlacement();
					Fill fill = textSymbolizer.getFill();

					// Fuente
					swtFont = new org.eclipse.swt.graphics.Font(getDisplay(),
							font.getFamily(), font.getSize(), font.getStyle() | font.getWeight());
					gc.setFont(swtFont);
					// Color
					if (font.getColor() != null) {
						gc.setForeground(font.getColor());
					}
					// Fondo
					boolean transparent = true;
					if (fill != null && fill.getFill() != null) {
						gc.setBackground(fill.getFill());
						transparent = false;
					}
					// Posicion
					int x = devX;
					int y = devY;
					if (lp != null) {
						Point textExtent = gc.textExtent(text);
						x = devX + lp.getDisplacementX() - (int)(lp.getAnchorPointX()*textExtent.x);
						y = devY - lp.getDisplacementY() - textExtent.y + (int)(lp.getAnchorPointY()*textExtent.y);
					}

					gc.drawString(text, x, y, transparent);
					
				} catch (Exception e) {
					logger.error(e);
				} finally {
					if (swtFont != null) swtFont.dispose();
				}
			}
			else if (symbolizer instanceof PointSymbolizer) {
				PointSymbolizer pointSymbolizer = (PointSymbolizer) symbolizer;
				if (pointSymbolizer.getGraphic() != null) {
					pointSymbolizer.getGraphic().paint(gc, devX, devY);
				}
			}
		}

		if (gpsTrackingActive) {
			// Dibuja la posicion actual del gps
			if (getGPSLocation() && gpsX >= 0 && gpsX < width && gpsY >= 0 && gpsY < height) {				
				//gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLUE));
				//gc.fillArc(gpsX, gpsY, 10, 10, 0, 360);
				gc.drawImage(gpsPointer, gpsX-gpsPointer.getBounds().width, gpsY-gpsPointer.getBounds().height);
			}
		}
	}

	private String creaURLGetMap(String baseURL, String version, BoundingBox bbox, int iWidth, int iHeight, String capas, String format, String srs, boolean transparent, String styles) {
		StringBuffer sbQueryString = new StringBuffer(baseURL);
		if (baseURL!=null && baseURL.length()>0 && baseURL.charAt(baseURL.length()-1)!='?')
			sbQueryString.append('?');
		sbQueryString.append("REQUEST=GetMap&SERVICE=WMS&VERSION=").append(version);
		sbQueryString.append("&WIDTH=").append(iWidth).append("&HEIGHT=").append(iHeight);
		sbQueryString.append("&LAYERS=").append(capas);
		sbQueryString.append("&FORMAT=").append(format);
		sbQueryString.append("&BBOX=").append(bbox);
		sbQueryString.append("&SRS=").append(srs);
		if (transparent)
			sbQueryString.append("&TRANSPARENT=TRUE");
		if (styles == null)
			styles = "";
		sbQueryString.append("&STYLES=").append(styles);

		return sbQueryString.toString();
	}

	////////////////////////////////////////////////////////////////
	// Funciones para el GPS
	////////////////////////////////////////////////////////////////

	public void setGPSProvider(GPSProvider provider) {
		gpsProvider = provider;
	}
	
	public boolean isGpsActive() {
		return gpsProvider.isStarted();
	}
	
	/**
	 * Activa o desactiva la lectura del GPS
	 * @param active
	 */
	public void setGpsActive(boolean active) throws Exception {
		if (gpsProvider == null) return;
		try  {
			if (active) {
				if (!gpsProvider.isStarted()) {
					logger.debug("Activando GPS");
					gpsProvider.startGPS();
				}
			}
			else {
				if (gpsProvider.isStarted()) {
					logger.debug("Desactivando GPS");
					gpsProvider.stopGPS();
				}
			}
		} catch (Exception e) {
			logger.error("Error al activar o desactivar GPS", e);
			throw e;
		}
	}
	
	public boolean isGpsTrackingActive() {
		return gpsTrackingActive;
	}
	
	/**
	 * Activa o desactiva el seguimiento GPS
	 * @param active
	 */
	public void setGPSTrackingActive(boolean active) {
		if (gpsProvider == null) return;
		if (active) {
			gpsTrackingActive = true;
			logger.debug("Seguimiento GPS activado");
			if (!gpsProvider.isStarted()) {
				logger.warn("El GPS está desactivado");
			}
		}
		else {
			gpsTrackingActive = false;
			logger.debug("Seguimiento GPS desactivado");
		}
	}
	
	/**
	 * Obtiene la posicion actual del GPS en el sistema de coordenadas que queremos.
	 * @return true si se ha podido leer la posicion actual; false en caso contrario
	 */
	private boolean getGPSLocation() {
		gpsX = -1;
		gpsY = -1;
		boolean hasLocation = false;
		try {
			if (gpsProvider != null && gpsProvider.isStarted()) {
				GPSLocation gpsLocation = gpsProvider.getGPSLocation();
				if (gpsLocation == null) {
					logger.debug("No se ha podido obtener la posición del GPS");
				}
				else {
					logger.debug("Posicion del GPS: " + gpsLocation.getLatitude() + ", " + gpsLocation.getLongitude());
					
					// Conversion a UTM huso 30
					UTMPoint posED50 = CoordinateConverter.fromGeodeticToUTM(
						CoordinateConverter.datumTransformation(new GeodeticPoint(gpsLocation.getLongitude(), gpsLocation.getLatitude()),
						CoordinateConverter.SPAIN_NORTHWEST_WGS84_ED50), CoordinateConverter.HAYFORD);
					logger.debug("Posicion en UTM30: " + posED50.getX() + ", " + posED50.getY());

					SVGNode root = raster.root;
					// Lectura del desplazamiento a aplicar a las coordenadas del SVG
					// Parametros despX y despY del nodo raiz
					double despX = 0.0;
					double despY = 0.0;
					if (root.nameAtts != null && root.nameAtts.size() == 2) {
						despX = Double.parseDouble((String) root.nameAtts.elementAt(0));
						despY = Double.parseDouble((String) root.nameAtts.elementAt(1));
						logger.debug("despX=" + despX + ", despY=" + despY);
					}
					// Cordenadas del gps relativas al svg original 
					double svgX = posED50.getX() - despX;
					double svgY = despY - posED50.getY();
					logger.debug("Coordenadas en el SVG: " + svgX + ", " +  svgY);
					
					// Coordenadas del gps en aritmetica de punto fijo de TinyLine
					int fixX = TinyNumber.parseFix(new TinyString(String.valueOf(svgX).toCharArray()));
					int fixY = TinyNumber.parseFix(new TinyString(String.valueOf(svgY).toCharArray()));
					logger.debug("Coordenadas en punto fijo: " +  fixX + ", " + fixY);
					
					// Coordenadas del gps en el device space (pixeles)
					TinyMatrix matrix = root.getGlobalTransform();
					TinyPoint gpsPoint = new TinyPoint(fixX, fixY);
					matrix.transform(gpsPoint);
					gpsX = gpsPoint.x >> TinyUtil.FIX_BITS;
					gpsY = gpsPoint.y >> TinyUtil.FIX_BITS;
					hasLocation = true;
					logger.debug("Coordenadas en pantalla: " + gpsX + ", " + gpsY);
				}
			}
		} catch (Exception e) {
			logger.error("Error al leer posición de GPS", e);
		}
		return hasLocation;
	}
	
	/**
	 * Centra el mapa en la posicion del GPS
	 * @return true si se ha podido leer la posicion actual; false en caso contrario
	 */
	public boolean goGPS() {
		boolean hasLocation = false;
		if (gpsProvider != null && gpsProvider.isStarted()) {
			hasLocation = getGPSLocation();
			if (hasLocation && (gpsX==-1 && gpsY==-1)) {
				pan(gpsX-width/2, gpsY-height/2);
			}
		}
		return hasLocation;
	}
	
	/**
	 * Devuelve el denominador de la escala actual, relativo al tamaño de pixel estandar
	 */
	public float getCurrentScaleSLD() {
		SVGNode root = raster.document.root;
		if (root == null) return -1.f;
		
		// Resolucion de la pantalla
		int dpi = getDisplay().getDPI().x;
		logger.debug("DPI: " + dpi);
		// Tamaño del pixel en mm
		float pixelSize = 25.4f/dpi;
		// Escala del svg actual en pixeles/metro
		float svgScale = root.getGlobalTransform().a/(256f*256f);
		logger.debug("Escala SVG: " + svgScale + " pixeles/metro");
		// Denominador de la escala real
		float actualScaleDen = 1000.f/(pixelSize*svgScale);
		// Denominador de la escala estandarizada (0.28 mm/pixel)
		float standardScaleDen = actualScaleDen*0.28f/pixelSize;
		
		return standardScaleDen;
	}
	
	/**
	 * Establece la capa activa
	 * @param activeLayer
	 */
	public void setActiveLayer(String activeLayer) {
		setSelectableAncestorId(activeLayer);
	}
	
	/**
	 * Obtiene el nombre de la capa activa
	 */
	public String getActiveLayer() {
		return getSelectableAncestorId();
	}
	
	/**
	 * Obtiene la capa activa
	 */
	public SVGNode getActiveLayerNode() {
		String activeLayer = getSelectableAncestorId();
		if (activeLayer == null) {
			return null;
		}
		SVGDocument document = getSVGDocument();
		
		if (document.root == null || document.root.children == null) return null;

		return SVGNode.getNodeById(document.root, new TinyString(activeLayer.toCharArray()));
	}
		
	/**
	 * Obtiene la capa activa
	 */
	public void setActiveLayerNode(String selectableAncestorId) {
		if(selectableAncestorId!=null)
			setSelectableAncestorId(selectableAncestorId);
		SVGDocument document = getSVGDocument();		
		if (document.root == null || document.root.children == null)
			SVGNode.getNodeById(document.root, new TinyString(selectableAncestorId.toCharArray()));
	}

	/**
	 * Obtiene el nombre de LocalGIS de la capa activa
	 */
	public String getActiveLayerSystemId() {
		SVGNode activeLayerNode = getActiveLayerNode();
		try {
			return ((SVGGroupElem) activeLayerNode).getSystemId();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Obtiene las capas del documento (grupos de primer nivel)
	 * @return
	 */
	public String[] getSVGLayers() {
		SVGDocument document = getSVGDocument();
		
		if (document.root == null || document.root.children == null) return null;
		
		String[] layerIds = new String[document.root.children.count];
		// Recorrido de los nodos de primer nivel
		for (int i=0, j=0; i<document.root.children.count; i++) {
			SVGNode node = (SVGNode) document.root.children.data[i];
			// Comprobacion de si es un elemento de grupo (capa)
			if (node instanceof SVGGroupElem && node.id != null) {
				layerIds[j++] = new String(node.id.data);
			}
		}
		return layerIds;
	}

	/**
	 * Establece el orden de las capas y su visibilidad
	 * @param layers
	 */
	public void setLayerOrderAndVisibility(Vector layers) {
		SVGDocument document = getSVGDocument();
		SVGNode root = document.root;

		if (root == null) return;
		
		TinyVector children = root.children;
		if (children == null || children.count == 0) return;
		logger.debug("Ordenando capas");

		// Nuevo vector de nodos capa
		TinyVector newChildren = new TinyVector(children.count);
		// Recorrido de los hijos para meter al principio los que no tienen id
		// o los que no se especifica su id en el vector recibido
		for (int i=0; i<children.count; i++) {
			SVGNode child = (SVGNode) children.data[i];
			if (child.id == null) { // no tiene id
				newChildren.addElement(child);
				logger.debug("Añadida capa sin id");
			}
			else {
				String id = new String(child.id.data);
				boolean found = false;
				for (int j=0; j<layers.size() && !found; j++) {
					LayerVisibility l = (LayerVisibility) layers.elementAt(j);
					if (id.equals(l.getLayerId())) found = true;
				}
				if (!found) { // id no encontrado
					newChildren.addElement(child);
					logger.debug("Añadida capa sin especificar posición");
				}
			}
		}
		
		// Se añaden los hijos en el orden indicado
		// y se establece la visibilidad
		for (int i=0; i<layers.size(); i++) {
			LayerVisibility l = (LayerVisibility) layers.elementAt(i);
			boolean found = false;
			for (int j=0; j<children.count && !found; j++) {
				SVGNode child = (SVGNode) children.data[j];
				if (child.id != null) {
					String id = new String(child.id.data);
					if (id.equals(l.getLayerId())) { // Nodo hijo encontrado
						child.visibility = l.isVisible() ? SVG.VAL_VISIBLE : SVG.VAL_HIDDEN;
						newChildren.addElement(child);
						logger.debug("Añadida capa con id " + id + " Visible=" + l.isVisible());
						found = true;
					}
				}
			}
		}
		
		// Se cambia el vector de nodos hijos del root y se actualiza el dibujo
		root.children = newChildren;
		
		drawSVG();
	}

	/**
	 * Carga un SLD para aplicar a la capa indicada del SVG actual
	 * @param issld
	 * @throws XMLParsingException
	 */
	public StyledLayerDescriptor loadSLD(InputStream issld, String layerName, String currentStyle, boolean active, boolean search) throws XMLParsingException {
		logger.debug("Cargando estilo para capa " + layerName);
		StyledLayerDescriptor sld = SLDFactory.createSLD(issld, baseURL);
		putSLD(sld, layerName, currentStyle, active, search);
		return sld;
	}
	
	/**
	 * Almacena un objeto SLD para aplicar a la capa indicada.
	 */
	public void putSLD(StyledLayerDescriptor sld, String layerName, String currentStyle, boolean active, boolean search) {
		logger.debug("Almacenando estilo para capa " + layerName);
		boolean lastIsSearch = false; // Indice si el ultimo estilo de la lista es de busqueda
		if (!layerSLDs.isEmpty()) {
			lastIsSearch = ((LayerSLDInfo) layerSLDs.elementAt(layerSLDs.size()-1)).isSearch();
		}
		if (search) {
			if (lastIsSearch) {
				layerSLDs.removeElementAt(layerSLDs.size()-1); // Se elimina para reemplazarlo
			}
			layerSLDs.addElement(new LayerSLDInfo(layerName, sld, currentStyle, active, search));
		}
		else {
			if (lastIsSearch) {
				// Se inserta antes del estilo de busqueda
				layerSLDs.insertElementAt(new LayerSLDInfo(layerName, sld, currentStyle, active, search), layerSLDs.size()-1);
			}
			else {
				layerSLDs.addElement(new LayerSLDInfo(layerName, sld, currentStyle, active, search));
			}
		}
	}
	
	/**
	 * Activa o desactiva el estilo asociado a una capa.
	 * @param layerName
	 * @param active
	 */
	public void setLayerSLDActive(String layerName, boolean active) {
		Enumeration e = layerSLDs.elements();
		while (e.hasMoreElements()) {
			LayerSLDInfo layerSLDInfo = (LayerSLDInfo) e.nextElement();
			if (layerSLDInfo.getLayerName().equals(layerName)) {
				layerSLDInfo.setActive(active);
				break;
			}
		}
	}
	
	/**
	 * Activa o desactiva el estilo asociado a la busqueda.
	 */
	public void setSearchSLDActive(boolean active) {
		for (int i=layerSLDs.size()-1; i>=0; i--) {
			LayerSLDInfo layerSLDInfo = (LayerSLDInfo) layerSLDs.elementAt(i);
			if (layerSLDInfo.isSearch()) {
				layerSLDInfo.setActive(active);
				break;
			}
		}
	}

	/**
	 * Devuelve informacion sobre los estilos aplicados al documento.
	 */
	public Vector getLayerSLDs() {
		return layerSLDs;
	}

	/**
	 * Aplica los estilos al svg que se va a pintar.
	 */
	private void applySLDs() {
		
		if (raster.document == null || raster.document.root == null) return;
		
		// Restaurar los valores cambiados
		resetSLDChanges();
		
		// Vaciado de los simbolizadores que se pintan al final
		deferredSymbolizers.removeAllElements();
		
		// Recorrer los SLDs de cada capa y aplicar los activos
		Enumeration e = layerSLDs.elements();
		while (e.hasMoreElements()) {
			LayerSLDInfo layerSLDInfo = (LayerSLDInfo) e.nextElement();
			if (layerSLDInfo.isActive()) {
				applySLD(layerSLDInfo);
			}
		}
	}
	
	/**
	 * Restaurar los valores cambiados por los estilos recorriendolos al reves
	 */
	public void resetSLDChanges() {
		logger.debug("Restaurando valores cambiados en el Tiny");
		for (int i=sldTinyChanges.size()-1; i>=0; i--) {
			SLDTinyChange change = (SLDTinyChange) sldTinyChanges.elementAt(i);
			change.restore();
		}
		sldTinyChanges.removeAllElements();
	}

	/**
	 * Aplica el estilo de una capa
	 * @param layerSLDInfo
	 */
	private void applySLD(LayerSLDInfo layerSLDInfo) {
		// Obtener la capa en el svg
		TinyVector svgLayers = raster.root.children;
		SVGNode svgLayer = null;
		for (int i=0; i<svgLayers.count; i++) {
			SVGNode layer = (SVGNode) svgLayers.data[i];
			if (layer.id != null && layer.id.data != null) {
				String id = new String(layer.id.data);
				if (id.equals(layerSLDInfo.getLayerName())) {
					svgLayer = layer;
					logger.debug("Aplicando estilo a la capa " + layerSLDInfo.getLayerName());
					break;
				}
			}
		}
		if (svgLayer == null || svgLayer.children == null || svgLayer.children.count == 0) {
			logger.warn("Capa " + layerSLDInfo.getLayerName() + " no encontrada. No puede aplicar el estilo.");
			return;
		}
		if(svgLayer.visibility==SVG.VAL_HIDDEN){
			logger.warn("Capa " + layerSLDInfo.getLayerName() + " No visible.");
			return;
		}

		StyledLayerDescriptor sld = layerSLDInfo.getSld();
		// Solo deberia haber un NamedLayer
		NamedLayer[] namedLayers = sld.getNamedLayers();
		if (namedLayers.length == 0) {
			logger.warn("No existe ningun elemento NamedLayer");
			return;
		}
		// Quedarse con el current style. Si el current es null, coger el primero (solo vendra uno)
		AbstractStyle[] styles = namedLayers[0].getStyles();
		if (styles.length == 0) {
			logger.warn("No existe ningun elemento Style");
			return;
		}
		UserStyle currentUserStyle = null;
		String currentUserStyleName = layerSLDInfo.getCurrentStyleName();
		for (int i=0; i<styles.length; i++) {
			if (styles[i] instanceof UserStyle) {
				UserStyle userStyle = (UserStyle) styles[i];
				if (currentUserStyleName == null || currentUserStyleName.equals("")
					|| currentUserStyleName.equals(userStyle.getName())) {
					currentUserStyle = userStyle;
					break;
				}
			}
		}
		if (currentUserStyle == null) {
			logger.warn("No se ha encontrado el UserStyle actual: " + layerSLDInfo.getCurrentStyleName());
			return;
		}
		// Solo deberia haber un FeatureTypeStyle
		FeatureTypeStyle[] featureTypeStyles = currentUserStyle.getFeatureTypeStyles();
		if (featureTypeStyles.length == 0) {
			logger.warn("No existe ningun elemento FeatureTypeStyle");
			return;
		}
		// Seleccionar las reglas que caen dentro de la escala actual
		Rule[] rules = featureTypeStyles[0].getRules();
		Vector rulesInScale = new Vector();
		float currentScale = getCurrentScaleSLD();
		logger.debug("Denominador de escala actual: " + currentScale);
		for (int i=0; i<rules.length; i++) {
			if (currentScale >= rules[i].getMinScaleDenominator()
				&& currentScale < rules[i].getMaxScaleDenominator()) {
				rulesInScale.addElement(rules[i]);
			}
		}
		// Recorrer nodos y aplicar reglas
		if (rulesInScale.isEmpty()) {
			logger.info("No hay ninguna regla aplicable para la escala actual");
			return;
		}
		TinyVector svgNodes = svgLayer.children; 
	 
		for (int i=0; i<svgNodes.count  ; i++) {
			SVGNode node = (SVGNode) svgNodes.data[i];
			TinyRect devBounds = node.getDevBounds(raster);
			if (devBounds == null || !(devBounds.xmax < 0 || devBounds.ymax < 0 || devBounds.xmin >= width || devBounds.ymin >= height)) {
				// El nodo cae dentro de la pantalla
				Enumeration e = rulesInScale.elements();
				while (e.hasMoreElements()  ) {
					Rule nextRule = (Rule) e.nextElement();
					try {
						// Aplicar regla
						//logger.debug("compruebo rule " + nextRule.hashCode() + " is valid" + nextRule.isValidForExecute());
						if (nextRule.isValidForExecute()) {
							applyRule(node, devBounds, nextRule );
						}
					 
					} catch (FilterEvaluationNoPropertyException ex) {
						logger.debug("Error al evaluar el filtro  ...", ex);
						logger.debug("Marco la rule  ..." + nextRule.hashCode()+ " "+ nextRule.getName() + " Invalida");
						
						// si me pega este error marco la RULE como invalida para no procesarla más en el futuro.
						nextRule.setValidForExecute(false);
						
					} catch (FilterEvaluationException ex) {
						logger.error("Error al evaluar el filtro", ex);
					}
					//NUEVO
					node.opacity=200;
					node.fillOpacity=200;
					node.strokeOpacity=200;

				}
			 
			}
		}
	}
	


	/**
	 * Aplica una regla a un nodo del SVG
	 * @param node
	 * @param devBounds
	 * @param rule
	 * @throws FilterEvaluationException
	 */
	private void applyRule(SVGNode node, TinyRect devBounds, Rule rule) throws FilterEvaluationException {
		// Evaluar filtro
		if (rule.getFilter() != null) {
			if (!rule.getFilter().evaluate(new SVGNodeFeature(node)))
				return;
		}
		
		// Aplicar symbolizers
		Symbolizer[] symbolizers = rule.getSymbolizers();
		if (symbolizers.length == 0) return;
		for (int i=0; i<symbolizers.length; i++) {
			Symbolizer symbolizer = symbolizers[i];
			if (symbolizer instanceof LineSymbolizer) {
				LineSymbolizer lineSymbolizer = (LineSymbolizer) symbolizer;
				changeTinyStroke(node, lineSymbolizer.getStroke());
			}
			else if (symbolizer instanceof PolygonSymbolizer) {
				PolygonSymbolizer polygonSymbolizer = (PolygonSymbolizer) symbolizer;
				// Stroke
				changeTinyStroke(node, polygonSymbolizer.getStroke());
				// Relleno
				Fill fill = polygonSymbolizer.getFill();
				// Color
				if (fill.getFill()!=null) {
					sldTinyChanges.addElement(new SLDTinyChange(node, SVG.ATT_FILL, node.fill));
					node.fill = new TinyColor(0xFF000000 + (fill.getFill().getRed() << 16) +
						(fill.getFill().getGreen() << 8)+ fill.getFill().getBlue());
				}
				// Opacidad
				if (fill.getOpacity() >= 0) {
					sldTinyChanges.addElement(new SLDTinyChange(node, SVG.ATT_FILL_OPACITY, new Integer(node.fillOpacity)));
					int opacity = (int) (fill.getOpacity()*256);
					if (opacity>255) opacity = 255;
					if (opacity<0) opacity = 0;
		//NUEVO			
//					int opacity = 10;
					node.fillOpacity = opacity;
				}
			}
			else if (symbolizer instanceof TextSymbolizer) {
				TextSymbolizer textSymbolizer = (TextSymbolizer) symbolizer;
				String text = null;
				Expression expLabel = textSymbolizer.getExpLabel();
				if (expLabel != null) {
					Object o = expLabel.evaluate(new SVGNodeFeature(node));
					if (o != null) {
						text = o.toString();
					}
				}
				Font font = textSymbolizer.getFont();
				if (text != null && text.length() > 0 && font != null) {
					// Obtener el centroide del elemento a coordenadas del dispositivo
					//TinyRect devBounds = node.getDevBounds(raster);
					if (devBounds == null) return;
					int devX = devBounds.xmin + (devBounds.xmax - devBounds.xmin)/2;
					int devY = devBounds.ymin + (devBounds.ymax - devBounds.ymin)/2;
					// Dibujar el elemento en la imagen
					if (devX >= 0 && devX < width && devY >= 0 && devY < height) {
						deferredSymbolizers.addElement(new DeferredSymbolizer(symbolizer, devX, devY, text));
					}
				}
			}
			else if (symbolizer instanceof PointSymbolizer) {
				PointSymbolizer pointSymbolizer = (PointSymbolizer) symbolizer;
				if (pointSymbolizer.getGraphic() != null) {
					// Obtener el centroide del elemento a coordenadas del dispositivo
					//TinyRect devBounds = node.getDevBounds(raster);
					if (devBounds == null) return;
					int devX = devBounds.xmin + (devBounds.xmax - devBounds.xmin)/2;
					int devY = devBounds.ymin + (devBounds.ymax - devBounds.ymin)/2;
					// Dibujar el elemento en la imagen
					if (devX >= 0 && devX < width && devY >= 0 && devY < height) {
						deferredSymbolizers.addElement(new DeferredSymbolizer(symbolizer, devX, devY));
					}
				}
			}
		}
	}
	
	/**
	 * Cambia el stroke de un nodo svg por el que proporciona el simbolizador
	 * @param node
	 * @param stroke
	 */
	private void changeTinyStroke(SVGNode node, Stroke stroke) {
		// Color
		if (stroke.getStroke()!=null) {
			sldTinyChanges.addElement(new SLDTinyChange(node, SVG.ATT_STROKE, node.stroke));
			node.stroke = new TinyColor(0xFF000000 + (stroke.getStroke().getRed() << 16) +
				(stroke.getStroke().getGreen() << 8)+ stroke.getStroke().getBlue());
		}
		// Opacidad
		if (stroke.getOpacity() >= 0) {
			sldTinyChanges.addElement(new SLDTinyChange(node, SVG.ATT_STROKE_OPACITY, new Integer(node.opacity)));
			int opacity = (int) (stroke.getOpacity()*256);
			if (opacity>255) opacity = 255;
			if (opacity<0) opacity = 0;
			//NUEVO
//			int opacity = 150;
			node.opacity = opacity;			
		}
		if (stroke.getWidth() >= 0) {
			sldTinyChanges.addElement(new SLDTinyChange(node, SVG.ATT_STROKE_WIDTH, new Integer(node.strokeWidth)));
			node.strokeWidth = (int) (stroke.getWidth()*256);
		}
		if (stroke.getLineJoin() != -1) {
			sldTinyChanges.addElement(new SLDTinyChange(node, SVG.ATT_STROKE_LINEJOIN, new Integer(node.strokeLineJoin)));
			switch (stroke.getLineJoin()) {
			case Stroke.LJ_BEVEL:
				node.strokeLineJoin = SVG.VAL_BEVEL;
				break;
			case Stroke.LJ_MITRE:
				node.strokeLineJoin = SVG.VAL_MITER;
				break;
			case Stroke.LJ_ROUND:
				node.strokeLineJoin = SVG.VAL_ROUND;
				break;
			}
		}
		if (stroke.getLineCap() != -1) {
			sldTinyChanges.addElement(new SLDTinyChange(node, SVG.ATT_STROKE_LINECAP, new Integer(node.strokeLineCap)));
			switch (stroke.getLineCap()) {
			case Stroke.LC_BUTT:
				node.strokeLineCap = SVG.VAL_BUTT;
				break;
			case Stroke.LC_ROUND:
				node.strokeLineCap = SVG.VAL_ROUND;
				break;
			case Stroke.LC_SQUARE:
				node.strokeLineCap = SVG.VAL_SQUARE;
				break;
			}
		}
		if (stroke.getDashArray() != null) {
			sldTinyChanges.addElement(new SLDTinyChange(node, SVG.ATT_STROKE_DASHARRAY, node.strokeDashArray));
			node.strokeDashArray = new int[stroke.getDashArray().length];
			for (int j=0; j<stroke.getDashArray().length; j++) {
				node.strokeDashArray[j] = (int) (stroke.getDashArray()[j]*256);
			}
		}
		if (stroke.getDashOffset() >= 0) {
			sldTinyChanges.addElement(new SLDTinyChange(node, SVG.ATT_STROKE_DASHOFFSET, new Integer(node.strokeDashOffset)));
			node.strokeDashOffset = (int) (stroke.getDashOffset()*256);
		}
	}
	
	/**
	 * Guarda los valores originales de fill y stroke del nodo que ha sido seleccionado
	 * (comprobando que no hayan sido guardados ya), para restaurarlos antes del pintado
	 */
	protected void saveSelectedNodeState(SVGNode node) {
		boolean fillSaved = false;
		boolean strokeSaved = false;
		// Comprobacion de los valores originales que ya han sido guardados
		Enumeration en = sldTinyChanges.elements();
		while (en.hasMoreElements() && (!fillSaved || !strokeSaved)) {
			SLDTinyChange change = (SLDTinyChange) en.nextElement();
			if (change.getChangedNode() == node) {
				switch (change.getChangedAttribute()) {
				case SVG.ATT_FILL:
					fillSaved = true; // fill ya guardado
					break;
				case SVG.ATT_STROKE:
					strokeSaved = true; // stroke ya guardado
					break;
				}
			}
		}
		// Se guardan los valores
		if (!fillSaved) {
			sldTinyChanges.addElement(new SLDTinyChange(node, SVG.ATT_FILL, node.fill));
		}
		if (!strokeSaved) {
			sldTinyChanges.addElement(new SLDTinyChange(node, SVG.ATT_STROKE, node.stroke));
		}
	}
	
	/**
	 * Aplica un filtro sobre una capa y devuelve una lista de nodos que cumplan la
	 * condicion del Nodo
	 * 
	 */
	public Vector applyFilter(Filter filter, String idLayer) throws FilterConstructionException { 
		
		if (raster.document == null || raster.root == null) return null;
		
		// Obtener la capa en el svg
		TinyVector svgLayers = raster.root.children;
		SVGNode svgLayer = null;
		if (svgLayers != null) {
			for (int i=0; i<svgLayers.count; i++) {
				SVGNode layer = (SVGNode) svgLayers.data[i];
				if (layer.id != null && layer.id.data != null) {
					String id = new String(layer.id.data);
					if (id.equals(idLayer)) {
						svgLayer = layer;
						break;
					}
				}
			}
		}
		if (svgLayer == null || svgLayer.children == null || svgLayer.children.count == 0) {
			logger.warn("Capa " + idLayer + " no encontrada o vacia. No se puede aplicar el filtro.");
			return null;
		}
//		svgLayer.fillOpacity=100;	
//		svgLayer.opacity=100;
		
		Vector result = new Vector();
		TinyVector svgNodes = svgLayer.children; 
		for (int i=0; i<svgNodes.count; i++) {
			SVGNode node = (SVGNode) svgNodes.data[i];
			
			try {
				if (filter.evaluate(new SVGNodeFeature(node))) {
					result.add(node);
				}
			} catch (FilterEvaluationException e) {
				 
				logger.error("Error al aplicar el filtro sobre nodo con atts " + node.nameAtts, e);
			}
		}
		
		return result;
	}

	/**
	 * Carga un geopista schema para la capa indicada del SVG actual
	 */
	public GeopistaSchema loadGeopistaSchema(InputStream is, String layerName) throws XMLParsingException {
		logger.debug("Cargando schema para capa " + layerName);
		GeopistaSchema geopistaSchema = GeopistaSchemaFactory.loadGeopistaSchema(is);
		putGeopistaSchema(geopistaSchema, layerName);
		return geopistaSchema;
	}
	
	/**
	 * Almacena un geopista schema para la capa indicada
	 */
	public void putGeopistaSchema(GeopistaSchema sch, String layerName) {
		logger.debug("Almacenando schema para capa " + layerName);
		layerSchemas.put(layerName, sch);
	}

	/**
	 * Obtiene el geopista schema de la capa indicada.
	 * @param layerName
	 * @return
	 */
	public GeopistaSchema getGeopistaSchema(String layerName) {
		return (GeopistaSchema) layerSchemas.get(layerName);
	}

//	/**
//	 * Genera el xml con los cambios realizados en el svg cargado para enviar al servidor.
//	 */
//	public void serializeToUpload(OutputStream outputStream, Vector metaInfos) throws IOException {
//		serializeToUpload(outputStream, getSVGDocument(), metaInfos);
//	}
	
	///////////////////////////////////////////////////////////////////////////
	// Funciones para la carga de Shapefiles
	///////////////////////////////////////////////////////////////////////////
	public ShpReader getShpReader() {
		return shpReader;
	}
	
	public Theme addShp(String shpFilePath) throws ShpException {
		return shpReader.loadShp(shpFilePath);
	}
	
	public void removeShp(int index) {
		shpReader.unloadShp(index);
	}
	
	public Vector getLoadedShps() {
		return shpReader.getLoadedFiles();
	}
	
	///////////////////////////////////////////////////////////////////////////
	// Funciones para la carga de ECWs
	///////////////////////////////////////////////////////////////////////////
	public EcwReader getEcwReader() {
		return ecwReader;
	}
	
	public JNCSFile addEcw(String ecwFilePath) throws JNCSException {
		return ecwReader.loadEcw(ecwFilePath);
	}
	
	public void removeEcw(int index) {
		ecwReader.unloadEcw(index);
	}
	
	public Vector getLoadedEcws() {
		return ecwReader.getLoadedFiles();
	}
	public TinyRect getRasterBounds(SVGNode node){
		return node.getDevBounds(raster);
	}
	
	public void dispose(){
		super.dispose();
	}
}
