/*
 * Creado el 17-abr-2004
 *
 * Este codigo se distribuye bajo licencia GPL
 * de GNU. Para obtener una cópia integra de esta
 * licencia acude a www.gnu.org.
 * 
 * Este software se distribuye "como es". AGIL
 * solo  pretende desarrollar herramientas para
 * la promoción del GIS Libre.
 * AGIL no se responsabiliza de las perdidas económicas o de 
 * información derivadas del uso de este software.
 */
package com.geopista.ui.plugin.io.dxf.core.jump.io;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.geopista.ui.plugin.io.dxf.math.Point3D;
import com.geopista.ui.plugin.io.dxf.reader.Dxf3DFACE;
import com.geopista.ui.plugin.io.dxf.reader.DxfARC;
import com.geopista.ui.plugin.io.dxf.reader.DxfATTRIB;
import com.geopista.ui.plugin.io.dxf.reader.DxfBLOCK;
import com.geopista.ui.plugin.io.dxf.reader.DxfCIRCLE;
import com.geopista.ui.plugin.io.dxf.reader.DxfConverter;
import com.geopista.ui.plugin.io.dxf.reader.DxfDIMENSION;
import com.geopista.ui.plugin.io.dxf.reader.DxfEntity;
import com.geopista.ui.plugin.io.dxf.reader.DxfEntitySet;
import com.geopista.ui.plugin.io.dxf.reader.DxfException;
import com.geopista.ui.plugin.io.dxf.reader.DxfFile;
import com.geopista.ui.plugin.io.dxf.reader.DxfINSERT;
import com.geopista.ui.plugin.io.dxf.reader.DxfLINE;
import com.geopista.ui.plugin.io.dxf.reader.DxfPOINT;
import com.geopista.ui.plugin.io.dxf.reader.DxfPOLYLINE;
import com.geopista.ui.plugin.io.dxf.reader.DxfSOLID;
import com.geopista.ui.plugin.io.dxf.reader.DxfTEXT;
import com.geopista.ui.plugin.io.dxf.reader.DxfTRACE;
import com.geopista.ui.plugin.io.dxf.reader.DxfVERTEX;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;

/**
 * Clase que se encarga de convertir las entidades Dxf procedentes de la
 * lectura de un fichero de este tipo en Features.
 * <p/>
 * Puesto que entre el Dxf y el modelo de datos de OpenGIS no existe una buena
 * correspondencia, seguiremos el siguiente criterio:
 * <p/>
 * a) Consideraremos al menos una FeatureCollection para:
 * x) Puntos.
 * xx) Lineas.
 * xxx) Polígonos
 * xxxx) Textos.
 * <p/>
 * b) Dentro de cada tipo de FeatureCollection, para cada layer Dxf
 * consideraremos un nuevo FeatureCollection
 *
 * @author Alvaro Zabala (AGIL)
 * @todo Sería interesante convertir los bloques Dxf -decoración- en Style
 * -simbolos de Jump-. El problema es que el Style  no se asocia a las Feature,
 * sino a las Layer. Se podría devolver un Map FeatureId-Style.
 * @todo Otra posible mejora: cuando cargamos varios Dxf seguidos, en algunos casos puede darse el caso
 * de que tengan las mismas capas (por ejemplo, Jaen y Granada de la BCN50). Ahora mismo, por pertenecer
 * a ficheros distintos se estan guardando en capas distintas de JUMP. Sería interesante volcarlo todo
 * en la misma FeatureCollection
 * @todo Ahora mismo las POLYLINE cerradas se estan convirtiendo a LineString. Sería interesante
 * convertirlas a Polígonos (para que puedan tener rellenos)
 */
public class FeatureCollectionDxfConverter implements DxfConverter {

	/**
	 * Se encarga de construir geometrías JTS
	 */
	private GeometryFactory geometryFactory;

	/**
	 * Indexa cada FeatureCollection de puntos
	 * por el nombre de la capa geopistadxf a partir de la que se
	 * generó.
	 */
	private Map pointFeatureCollections;
	/**
	 * Igual que el anterior pero para FeatureCollection de lineas
	 */
	private Map lineFeatureCollections;
	/**
	 * Igual para FeatureCollections de polígonos
	 * <p/>
	 * TODO Ahora mismo no se generan poligonos. Habria que hacer que todas
	 * las polilineas cerradas se convirtiesen en poligonos
	 */
	private Map polygonFeatureCollections;
	/**
	 * Igual para FeatureCollections que contendrán puntos
	 * con textos asociados.
	 */
	private Map textFeatureCollections;
	/**
	 * Relaciona cada FeatureCollection con el nombre de la capa
	 * que la origino. Esto permite asignar nombre a las capas
	 * JUMP que se originen a partir de estas FeatureCollection
	 */
	private Map featureCollectionToLayerName;

	/**
	 * Coleccion vacia que se devuelve cuando se piden FeatureCollection
	 * de un tipo que no existe en el fichero geopistadxf
	 */
	private ArrayList dummyArrayList;

	/**
	 * Constructor por defecto.
	 */
	public FeatureCollectionDxfConverter() {
		//System.out.println("[FeatureCollectionDxfConverter.FeatureCollectionDxfConverter()] Inicio.");
		geometryFactory = new GeometryFactory(new PrecisionModel(), 0);
		dummyArrayList = new ArrayList();
		this.featureCollectionToLayerName = new HashMap();
	}

	public String getFeatureCollectionName(FeatureCollection featureCollection) {

		return (String) this.featureCollectionToLayerName.get(featureCollection);

	}

	public Collection getPointFeatureCollection() {
		if (pointFeatureCollections != null)
			return this.pointFeatureCollections.values();
		else
			return dummyArrayList;
	}
	
	public Collection getPointFeatureKeysCollection() {
		if (pointFeatureCollections != null)
			return this.pointFeatureCollections.keySet();
		else
			return dummyArrayList;
	}	

	public Collection getLineFeatureCollection() {
		if (this.lineFeatureCollections != null)
			return this.lineFeatureCollections.values();
		else
			return dummyArrayList;
	}
	public Collection getLineFeatureKeysCollection() {
		if (this.lineFeatureCollections != null)
			return this.lineFeatureCollections.keySet();
		else
			return dummyArrayList;
	}
	public Collection getPolygonFeatureCollection() {
		if (this.polygonFeatureCollections != null)
			return this.polygonFeatureCollections.values();
		else
			return dummyArrayList;
	}

	public Collection getTextFeatureCollection() {
		if (this.textFeatureCollections != null)
			return this.textFeatureCollections.values();
		else
			return dummyArrayList;
	}
	public Collection getTextFeatureKeysCollection() {
		if (this.textFeatureCollections != null)
			return this.textFeatureCollections.keySet();
		else
			return dummyArrayList;
	}
	
	public void convert(String dxfPath) {
		try {
			DxfFile dxfFile = new DxfFile(dxfPath);
			convert(dxfFile);
		} catch (DxfException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Se encarga de leer las entidades Dxf contenidas en un fichero
	 * de este tipo y de convertirlas a Features.
	 */
	public void convert(DxfFile dxfFile) {
		Enumeration set = dxfFile.getEntities().getEntitySet().getEntities();
		System.out.println("[FeatureCollectionDxfConverter.convert()] dxfFile.getEntities().getEntitySet().getNrOfEntities(): "+dxfFile.getEntities().getEntitySet().getNrOfEntities());
		System.out.println("[FeatureCollectionDxfConverter.convert()] dxfFile.getEntities().getEntitySet().getNrOfEntities(): "+dxfFile.getEntities().getEntitySet().getNrOfEntities());

		Enumeration enumeration=dxfFile.getEntities().getEntitySet().getCollectedEntityTypes();
		while (enumeration.hasMoreElements()) {
			Object o = (Object) enumeration.nextElement();
			System.out.println("o: "+o);
			System.out.println("o.toString(): "+o.toString());
		}

		while (set.hasMoreElements()) {
			DxfEntity ent = (DxfEntity) set.nextElement();
			ent.convert(this, dxfFile, null);
		}//while
	}

	/* (non-Javadoc)
	 * @see org.geopista.geopistadxf.reader.DxfConverter#convert(org.geopista.geopistadxf.reader.Dxf3DFACE, org.geopista.geopistadxf.reader.DxfFile, java.lang.Object)
	 */
	public void convert(Dxf3DFACE face, DxfFile dxf, Object collector) {
		// TODO Auto-generated method stub

	}


	public void convert(DxfARC arc, DxfFile dxf, Object collector) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.geopista.geopistadxf.reader.DxfConverter#convert(org.geopista.geopistadxf.reader.DxfBLOCK, org.geopista.geopistadxf.reader.DxfFile, java.lang.Object)
	 */
	public void convert(DxfBLOCK block, DxfFile dxf, Object collector) {
		// TODO Auto-generated method stub

	}


	public void convert(DxfCIRCLE circle, DxfFile dxf, Object collector) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.geopista.geopistadxf.reader.DxfConverter#convert(org.geopista.geopistadxf.reader.DxfDIMENSION, org.geopista.geopistadxf.reader.DxfFile, java.lang.Object)
	 */
	public void convert(DxfDIMENSION dim, DxfFile dxf, Object collector) {
		// TODO Auto-generated method stub

	}

	/**
	 * TODO Implementar
	 */
	public void convert(DxfINSERT insert, DxfFile dxf, Object collector) {
		// TODO Auto-generated method stub

	}

	/**
	 * Construye un Feature de geometria lineal a partir de una entidad
	 * geopistadxf LINE
	 *
	 * @param line
	 * @param dxf
	 * @param collector
	 */
	public void convert(DxfLINE line, DxfFile dxf, Object collector) {

		if (this.lineFeatureCollections == null)
			this.lineFeatureCollections = new HashMap();

		String layerName = line.getLayerName();
		FeatureCollection forLayer =
				(FeatureCollection) lineFeatureCollections.get(layerName);
		if (forLayer == null) {
			FeatureSchema schema = new FeatureSchema();
			schema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
			schema.addAttribute("COLOR", AttributeType.INTEGER);
			schema.addAttribute("LINE_TYPE", AttributeType.STRING);
			forLayer = new FeatureDataset(schema);
			this.lineFeatureCollections.put(layerName, forLayer);
			this.featureCollectionToLayerName.put(forLayer, layerName);
		}
		String lineType = line.getLtypeName();
		short color = line.getColor();
		Geometry geometry = null;
		Point3D inicio = line.getStartPoint();
		Point3D fin = line.getEndPoint();
		Coordinate[] coordinate = new Coordinate[2];
		coordinate[0] = new Coordinate(inicio.x, inicio.y);
		coordinate[1] = new Coordinate(fin.x, fin.y);
		geometry = this.geometryFactory.createLineString(coordinate);
		Feature feature = new BasicFeature(forLayer.getFeatureSchema());
		feature.setGeometry(geometry);
		feature.setAttribute("COLOR", new Integer(color));
		feature.setAttribute("LINE_TYPE", lineType);
		forLayer.add(feature);

	}

	/**
	 * Construye un Feature de geometria puntual a partir de una entidad
	 * geopistadxf POINT
	 *
	 * @param point
	 * @param dxf
	 * @param collector
	 */
	public void convert(DxfPOINT point, DxfFile dxf, Object collector) {
		if (this.pointFeatureCollections == null)
			this.pointFeatureCollections = new HashMap();

		String layerName = point.getLayerName();
		FeatureCollection forLayer =
				(FeatureCollection) pointFeatureCollections.get(layerName);
		if (forLayer == null) {
			FeatureSchema schema = new FeatureSchema();
			schema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
			schema.addAttribute("COLOR", AttributeType.INTEGER);
			forLayer = new FeatureDataset(schema);
			this.pointFeatureCollections.put(layerName, forLayer);
			this.featureCollectionToLayerName.put(forLayer, layerName);

		}
		Point3D p = point.getPosition();
		short color = point.getColor();
		Coordinate coordinate = new Coordinate(p.x, p.y);
		Geometry geometry = this.geometryFactory.createPoint(coordinate);
		Feature feature = new BasicFeature(forLayer.getFeatureSchema());
		feature.setGeometry(geometry);
		feature.setAttribute("COLOR", new Integer(color));
		forLayer.add(feature);
	}

	/**
	 * El formato Dxf considera diferentes tipos de POLYLINE.
	 * Este metodo se encarga de construir una geometria JTS a partir de una
	 * POLYLINE de tipo WEB
	 *
	 * @param poly
	 * @return
	 */
	private Geometry toJtsPolyWeb(DxfPOLYLINE poly) {

		ArrayList multiGeometry = new ArrayList();
		DxfEntitySet vertices = poly.getVertices();
		Enumeration e = vertices.getEntities();
		if (!e.hasMoreElements()) {
			return null;
		} else {
			int m, n;
			int nrN, nrM;
			int x, y;/* contadores en las direcciones u,v */
			int v = 0;  /*contador de vertices */
			boolean isClosedM = ((poly.getType() & DxfPOLYLINE.CLOSED) != 0);
			boolean isClosedN = ((poly.getType() & DxfPOLYLINE.WEB_CLOSED) != 0);

			if ((poly.getType() & DxfPOLYLINE.B_SPLINE) != 0) {
				m = poly.getNrApproxM();
				n = poly.getNrApproxN();
			} else {
				m = poly.getNrCtrlU();
				n = poly.getNrCtrlV();
			}//if

			nrM = m + ((isClosedM) ? 1 : 0);
			nrN = n + ((isClosedN) ? 1 : 0);

			// tenemos nrN líneas en la dirección m. Cada estará representado por un List
			//con sus coordinates
			ArrayList[] linesM = new ArrayList[nrN];
			// con nrM puntos cada una de ellas
			for (y = 0; y < nrN; y++) {
				linesM[y] = new ArrayList(nrM);
			}//for

			//idem para la dirección N
			ArrayList[] linesN = new ArrayList[nrM];
			
			// con nrN puntos
			for (x = 0; x < nrM; x++) {
				linesN[x] = new ArrayList(nrN);
			}
			
		
			// Actual vertex
			DxfVERTEX vertex;
			for (x = 0; x < m; x++) {// m lines with n points
				for (y = 0; y < n; y++) {// skip the control points of splines
					do {
						vertex = (DxfVERTEX) e.nextElement();
					} while ((vertex.getType() & DxfVERTEX.CONTROL) != 0);
					Point3D p = vertex.getPosition();
					Coordinate coordinate = new Coordinate(p.x, p.y);
					linesM[y].add(coordinate);
					linesN[x].add(coordinate);
				}//for
				if (isClosedN) {
					Coordinate p = (Coordinate) linesN[x].get(0);
					linesN[x].add(p);
				}
				Coordinate[] coords = new Coordinate[linesN[x].size()];
				coords = (Coordinate[]) linesN[x].toArray(coords);
				LineString lineString = this.geometryFactory.createLineString(coords);
				multiGeometry.add(lineString);
			}//for

			for (y = 0; y < n; y++) {
				if (isClosedM) {
					Coordinate p = (Coordinate) linesM[y].get(0);
					linesM[y].add(p);
				}
				Coordinate[] coords = new Coordinate[linesM[x].size()];
				coords = (Coordinate[]) linesM[x].toArray(coords);
				LineString lineString = this.geometryFactory.createLineString(coords);
				multiGeometry.add(lineString);
			}
			LineString[] geometries = new LineString[multiGeometry.size()];
			geometries = (LineString[]) multiGeometry.toArray(geometries);
			GeometryCollection solucion = this.geometryFactory.createGeometryCollection(geometries);
			return solucion;
		}//if
	}

	/**
	 * Devuelve una geometria JTS a partir de una POLYLINE geopistadxf del tipo NET
	 *
	 * @param poly
	 * @return
	 */
	private Geometry toJtsPolyNet(DxfPOLYLINE poly) {

		ArrayList multiGeometry = new ArrayList();

		int nrKnots = (poly.getNrCtrlU() <= 0) ? 16 : poly.getNrCtrlU();
		int nrK = 0;
		Point3D[] knot = new Point3D[nrKnots];
		int nrFaces = (poly.getNrCtrlV() <= 0) ? 4 : poly.getNrCtrlV();
		int nrF = 0;
		int[] vertex = new int[DxfVERTEX.PFACEVMAX * nrFaces];
		int nrV = 0;
		boolean lastWasInvisible = true;

		DxfEntitySet vertices = poly.getVertices();

		for (Enumeration e = vertices.getEntities(); e.hasMoreElements();) {
			DxfVERTEX v = (DxfVERTEX) e.nextElement();
			if ((v.getType() & DxfVERTEX.WEB_3D) != 0) {
				// knot
				if (nrK == nrKnots) {
					// more than stated in the file
					Point3D[] tmp = new Point3D[2 * nrKnots];
					System.arraycopy(knot, 0, tmp, 0, nrKnots);
					nrKnots *= 2;
					knot = tmp;
				}//if
				knot[nrK++] = v.getPosition();
			} else {
				// vertex
				if (nrF++ == nrFaces) {
					// more than stated in the file
					int[] tmp = new int[2 * DxfVERTEX.PFACEVMAX * nrFaces];
					System.arraycopy(vertex, 0, tmp, 0, nrFaces);
					nrFaces *= 2;
					vertex = tmp;
				}//if
				lastWasInvisible = true;

				for (int k = 0; k < DxfVERTEX.PFACEVMAX; k++) {
					int[] index = v.getReferences();
					vertex[nrV++] = index[k];
					if (index[k] > 0) {
						if (lastWasInvisible) {
							lastWasInvisible = false;
						}
					} else if (index[k] < 0) {
						lastWasInvisible = true;
					} else {
						nrV += DxfVERTEX.PFACEVMAX - k - 1;
						break;
					}
				}//for
			}//if
		}//for

		if (nrK != nrKnots) {
			nrKnots = nrK;
		}
		if (nrF != nrFaces) {
			nrFaces = nrF;
		}

		if (nrFaces == 0)
			return null;

		ArrayList lineStringCoordinates = new ArrayList();
		for (nrF = 0, nrV = 0; nrF < nrFaces; nrF++, nrV += DxfVERTEX.PFACEVMAX) {
			lastWasInvisible = true;
			for (int k = 0; k < DxfVERTEX.PFACEVMAX; k++) {
				if (vertex[nrV + k] < -nrKnots || vertex[nrV + k] > nrKnots) {
					break;
				}//if
				if (vertex[nrV + k] > 0) {
					if (lastWasInvisible) {
						lineStringCoordinates = new ArrayList(DxfVERTEX.PFACEVMAX - k + 1);
						lastWasInvisible = false;
					}
					Point3D p = knot[vertex[nrV + k] - 1];
					Coordinate coordinate = new Coordinate(p.x, p.y);
					lineStringCoordinates.add(coordinate);
				} else if (vertex[nrV + k] < 0) {
					if (!lastWasInvisible) {
						lastWasInvisible = true;
						Point3D p = knot[-vertex[nrV + k] - 1];
						Coordinate coordinate = new Coordinate(p.x, p.y);
						lineStringCoordinates.add(coordinate);
						Coordinate[] coordinates = new Coordinate[lineStringCoordinates.size()];
						coordinates = (Coordinate[]) lineStringCoordinates.toArray(coordinates);
						LineString lineString = this.geometryFactory.createLineString(coordinates);
						multiGeometry.add(lineString);
					}//if
				}//if
			}//for

			if (!lastWasInvisible) {
				Point3D p = null;
				if (vertex[nrV] < 0) {
					p = knot[-vertex[nrV] - 1];
				} else {
					p = knot[vertex[nrV] - 1];

				}//if
				Coordinate coordinate = new Coordinate(p.x, p.y);
				lineStringCoordinates.add(coordinate);
				Coordinate[] coords = new Coordinate[lineStringCoordinates.size()];
				coords = (Coordinate[]) lineStringCoordinates.toArray(coords);
				LineString lineString = this.geometryFactory.createLineString(coords);
				multiGeometry.add(lineString);
			}//if
		}//for
		LineString[] geometries = new LineString[multiGeometry.size()];
		geometries = (LineString[]) multiGeometry.toArray(geometries);
		return this.geometryFactory.createMultiLineString(geometries);
	}

	/**
	 * Construye una geometria JTS a partir de una entidad geopistadxf POLYLINE
	 * normal
	 *
	 * @param poly
	 * @return
	 */
	private Geometry toJtsPolyLine(DxfPOLYLINE poly) {

		ArrayList coordinates = new ArrayList();
		boolean isSpline = ((poly.getType() & DxfPOLYLINE.B_SPLINE) != 0);
		boolean isClosed = ((poly.getType() & DxfPOLYLINE.CLOSED) != 0);

		DxfEntitySet vertices = poly.getVertices();
		if (isSpline) {
			for (int i = 0; i < vertices.getNrOfEntities(); i++) {
				DxfVERTEX v = (DxfVERTEX) vertices.getEntity(i);
				if ((v.getType() & DxfVERTEX.CONTROL) == 0) {
					Point3D p = v.getPosition();
					coordinates.add(new Coordinate(p.x, p.y));
				}//if
			}//for
		} else {
			DxfVERTEX vertex, firstVertex, nextVertex;
			vertex = firstVertex = nextVertex = null;
			int nr = vertices.getNrOfEntities();
			for (Enumeration e = vertices.getEntities(); nr-- > 0;) {
				if (nextVertex == null) {
					nextVertex = (DxfVERTEX) e.nextElement();
				}
				vertex = nextVertex;
				if (firstVertex == null) {
					firstVertex = vertex;
				}
				if (!e.hasMoreElements()) {  // this is equivalent to nr==0 here
					nextVertex = isClosed ? firstVertex : null;
				} else {
					nextVertex = (DxfVERTEX) e.nextElement();
				}

				if (vertex.getRounding() == 0 || nextVertex == null) {
					// los puntos se conectan con lineas rectas
					Point3D p = vertex.getPosition();
					coordinates.add(new Coordinate(p.x, p.y));

				} else {
					// los puntos se conectan con curvas
					int CIRCLE_SEGMENTS = 36;//numero de segmentos que representan un arco
					double alpha, start, radius, dist;
					double[] vec = new double[2], mid = new double[2];
					int segs;
					Point3D thisPosition = vertex.getPosition(),
							nextPosition = nextVertex.getPosition();

					alpha = (double) (4.0 * Math.atan(vertex.getRounding()));
					vec[0] = nextPosition.x - thisPosition.x;
					vec[1] = nextPosition.y - thisPosition.y;
					dist = (double) (Math.sqrt(vec[0] * vec[0] + vec[1] * vec[1]));

					if (dist == 0) {
						continue;
					}

					vec[0] /= dist;
					vec[1] /= dist;

					// ahora es igual que si tuviesemos un DxfARC
					radius = dist / (double) (2.0 * Math.sin(alpha / 2));
					mid[0] = (thisPosition.x + nextPosition.x) / 2 -
							(radius - vertex.getRounding() * dist / 2) * vec[1];
					mid[1] = (thisPosition.y + nextPosition.y) / 2 +
							(radius - vertex.getRounding() * dist / 2) * vec[0];

					segs = (int) Math.ceil(CIRCLE_SEGMENTS * Math.abs(alpha) / Math.PI);
					start = (double) Math.atan2(thisPosition.y - mid[1], thisPosition.x - mid[0]);
					alpha /= segs;
					if (radius < 0) {
						radius = -radius;
					}

					for (int j = 0; j < segs; j++, start += alpha) {
						Coordinate coordinate =
								new Coordinate(mid[0] + radius * (double) Math.cos(start),
										mid[1] + radius * (double) Math.sin(start));
						coordinates.add(coordinate);
					}//for
				}//if vertex rounding
			}//for vertex
		}//if spline

		//Por ultimo comprobamos si cerrar la polilinea
		if (isClosed) {
			DxfVERTEX v = (DxfVERTEX) vertices.getEntity(0);
			Point3D p = v.getPosition();
			coordinates.add(new Coordinate(p.x, p.y));
		}
		Coordinate[] coords = new Coordinate[coordinates.size()];
		coords = (Coordinate[]) coordinates.toArray(coords);
		LineString lineString = this.geometryFactory.createLineString(coords);
		return lineString;
	}

	/**
	 * Convierte una polilinea Dxf en una geometria JTS
	 *
	 * @param poly
	 * @param dxf
	 * @param collector
	 */

	public void convert(DxfPOLYLINE poly, DxfFile dxf, Object collector) {
		if (this.lineFeatureCollections == null)
			this.lineFeatureCollections = new HashMap();

		String layerName = poly.getLayerName();
		FeatureCollection forLayer =
				(FeatureCollection) lineFeatureCollections.get(layerName);
		if (forLayer == null) {
			FeatureSchema schema = new FeatureSchema();
			schema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
			schema.addAttribute("COLOR", AttributeType.INTEGER);
			schema.addAttribute("LINE_TYPE", AttributeType.STRING);
			forLayer = new FeatureDataset(schema);
			this.lineFeatureCollections.put(layerName, forLayer);
			this.featureCollectionToLayerName.put(forLayer, layerName);
		}
		String lineType = poly.getLtypeName();
		short color = poly.getColor();
		int type = poly.getType();
		Geometry geometry = null;
		if ((type & DxfPOLYLINE.NET) != 0) {
			geometry = toJtsPolyNet(poly);
		} else if ((type & DxfPOLYLINE.ANY_WEB) != 0) {
			geometry = toJtsPolyWeb(poly);
		} else {
			geometry = toJtsPolyLine(poly);
		}
		Feature feature = new BasicFeature(forLayer.getFeatureSchema());
		feature.setGeometry(geometry);
		feature.setAttribute("COLOR", new Integer(color));
		feature.setAttribute("LINE_TYPE", lineType);
		forLayer.add(feature);
	}

	/* (non-Javadoc)
	 * @see org.geopista.geopistadxf.reader.DxfConverter#convert(org.geopista.geopistadxf.reader.DxfSOLID, org.geopista.geopistadxf.reader.DxfFile, java.lang.Object)
	 */
	public void convert(DxfSOLID solid, DxfFile dxf, Object collector) {
		// TODO Auto-generated method stub

	}

	/**
	 * Convierte un texto Dxf en un Feature puntual con un atributo denominado
	 * texto.
	 *
	 * @param text      entidad geopistadxf del tipo TEXT
	 * @param dxf       objeto del tipo DxfFile que representa al fichero Dxf
	 * @param collector objeto auxiliar
	 * @todo PODRIAMOS CREAR TAMBIÉN UN Map QUE ASOCIE UN LABELSTYLE A CADA
	 * FEATURECOLLECTION, PARA LUEGO USARLO AL CONSTRUIR LAS LAYERS
	 */
	public void convert(DxfTEXT text, DxfFile dxf, Object collector) {
		if (this.textFeatureCollections == null)
			this.textFeatureCollections = new HashMap();

		String layerName = text.getLayerName();
		FeatureCollection forLayer = (FeatureCollection) textFeatureCollections.get(layerName);
		if (forLayer == null) {
			FeatureSchema schema = new FeatureSchema();
			schema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
			schema.addAttribute("TEXTO", AttributeType.STRING);
			schema.addAttribute("ANGULO", AttributeType.DOUBLE);
			schema.addAttribute("ALTURA_TEXTO", AttributeType.DOUBLE);
			schema.addAttribute("COLOR", AttributeType.INTEGER);
			forLayer = new FeatureDataset(schema);
			this.textFeatureCollections.put(layerName, forLayer);
			this.featureCollectionToLayerName.put(forLayer, layerName);
		}
		Point3D punto3d = text.getPosition();
		double angulo = text.getRotation();
		String textContent = text.getText();
		double textHeight = text.getTextHeight();
		short color = text.getColor();
		
		//De momento no consideramos la tercera dimensión
		Coordinate insertionPoint = new Coordinate(punto3d.x, punto3d.y);
		Geometry geometry = this.geometryFactory.createPoint(insertionPoint);
		Feature feature = new BasicFeature(forLayer.getFeatureSchema());
		feature.setGeometry(geometry);
		feature.setAttribute("TEXTO", textContent);
		feature.setAttribute("ANGULO", new Double(angulo));
		feature.setAttribute("ALTURA_TEXTO", new Double(textHeight));
		feature.setAttribute("COLOR", new Integer(color));
		forLayer.add(feature);
	}


	/* (non-Javadoc)
	 * @see org.geopista.geopistadxf.reader.DxfConverter#convert(org.geopista.geopistadxf.reader.DxfTRACE, org.geopista.geopistadxf.reader.DxfFile, java.lang.Object)
	 */
	public void convert(DxfTRACE trace, DxfFile dxf, Object collector) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.geopista.geopistadxf.reader.DxfConverter#convert(org.geopista.geopistadxf.reader.DxfEntitySet, org.geopista.geopistadxf.reader.DxfFile, java.lang.Object)
	 */
	public void convert(DxfEntitySet set, DxfFile dxf, Object collector) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.geopista.geopistadxf.reader.DxfConverter#convert(org.geopista.geopistadxf.reader.DxfATTRIB, org.geopista.geopistadxf.reader.DxfFile, java.lang.Object)
	 */
	public void convert(DxfATTRIB converter, DxfFile dxf, Object collector) {
		// TODO Auto-generated method stub

	}

}
