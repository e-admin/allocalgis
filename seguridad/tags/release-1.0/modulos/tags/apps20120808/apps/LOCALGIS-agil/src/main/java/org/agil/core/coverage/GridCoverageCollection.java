package org.agil.core.coverage;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.quadtree.Quadtree;

/**
 *  Título: GridCoverageCollection <br>
 *  Descripcion: <p>
 *
 *  Lo mismo que un FeatureCollection contiene Features, una
 *  GridCoverageCollection contiene GridCoverages.<br>
 *  Presenta ciertas diferencias, pero de esta forma podemos agrupar mosaicos
 *  de imagenes, entre otros. Dentro de nuestro modelo de datos, seria el
 *  equivalente a un Raster_Column_Def asociado a una Layer. (que tiene
 *  asociadas multiples Raster_Colum -teselas-) </p> Copyright: Copyright (c)
 *  2002 Empresa: Asociacion Para la Promoción del GIS Libre
 *
 *@author     azabala
 *@created    17 de septiembre de 2003
 *@version    1.0
 */
public class GridCoverageCollection
		 extends GridCoverage {
	/**
	 *  identificador numerico del origen de datos *
	 */
	private long origen;

	/**
	 *  Minimo rectangulo encuadrante del mosaico de GridCoverages
	 */
	private Envelope envelope;
	/**
	 *  indice de todas las GridCoverage contenidas por esta coleccion (teselas)
	 *  a partir de su nombre
	 */
	private HashMap imagenes_nombre;

	/**
	 *  indice espacial en memoria que nos permite recuperar las teselas sin
	 *  necesidad de recorrerlas todas
	 */
	private SpatialIndex spatialIndex;


	/**
	 *  Constructor for the GridCoverageCollection object
	 *
	 *@roseuid        3F68728D02AF
	 */
	public GridCoverageCollection() {
		envelope = new Envelope();
		imagenes_nombre = new HashMap();
		spatialIndex = new Quadtree();
		//por defecto usamos un quadtree
	}


	/**
	 *  Setea el indice espacial empleado para indexar las teselas
	 *
	 *@param  index  indice espacial
	 */
	public void setSpatialIndex(SpatialIndex index) {
		spatialIndex = index;
	}


	/**
	 *@param  envelope
	 */
	public void setEnvelope(Envelope envelope) {
		this.envelope = envelope;
	}


	/**
	 *@param  l
	 */
	public void setOrigen(long l) {
		origen = l;
	}


	/**
	 *  Devuelve un iterador que permite recorrer todas las teselas de una
	 *  GridCoverageCollection
	 *
	 *@return     iterador de teselas
	 *@roseuid    3F68728D031D
	 */
	public Iterator getIteradorImagenes() {
		return imagenes_nombre.values().iterator();
	}


	/**
	 *  Obtiene el GridCoverage especificado
	 *
	 *@param  nombreImagen  identificador del gridCoverage
	 *@return               GridCoverage solicitado
	 *@roseuid              3F687B37036C
	 */
	public GridCoverage getGridCoverage(String nombreImagen) {
		return (GridCoverage) imagenes_nombre.get(nombreImagen);
	}


	/**
	 *  METODOS SOBRE-ESCRITOS DE GRIDCOVERAGE
	 */
	/**
	 *@return     int
	 *@author     alvaro zabala 24-sep-2003
	 *@roseuid    3F6EAE7F0203
	 */
	public int getNumSampleDimensions() {
		return 0;
	}


	/**
	 *  Este metodo se añade para el caso de GridCoverages que han sido
	 *  calculados a partir de otros (ejemplo tipico: imagen obtenida como
	 *  diferencia de otras 2) De esta forma, podemos saber cuantas imagenes la
	 *  originaron, e incluso acceder a ellas
	 *
	 *@return     int
	 *@author     alvaro zabala 24-sep-2003
	 *@roseuid    3F6EAE7F0232
	 */
	public int getNumSources() {
		return 0;
	}


	/**
	 *@return     com.vividsolutions.jts.geom.Envelope
	 *@roseuid    3F6EAE7F0251
	 */
	public Envelope getEnvelope() {
		return envelope;
	}


	/**
	 *  <p>
	 *
	 *  Devuelve el numero de 'overviews' (imagenes en piramide que pueden
	 *  simplificar la representacion de la cobertura) asociado a la cobertura.
	 *  </p>
	 *
	 *@return     int
	 *@roseuid    3F6F014C0109
	 */
	public int getNumOverviews() {
		return 0;
	}


	/**
	 *@return     java.lang.String[]
	 *@roseuid    3F6EAE8000CB
	 */
	public String[] getDimensionNames() {
		return null;
	}


	/**
	 *  <p>
	 *
	 *  Un GridCoverage puede tener una serie de 'overviews' (representaciones
	 *  simplificadas) previamente calculadas. Estas representaciones están
	 *  ordenadas en forma de piramide, de mayor a menor detalle (cuanto menor
	 *  sea el detalle, menos precisas serán pero las operaciones con ellas serán
	 *  mas rapidas) </p>
	 *
	 *@return     org.agil.kernel.jump.coverage.GridCoverage
	 *@roseuid    3F6F018B01A5
	 */
	public GridCoverage getOverview() {
		return null;
	}


	/**
	 *@return     java.lang.String[]
	 *@roseuid    3F6EAE800109
	 */
	public String[] getMetadataNames() {
		return null;
	}


	/**
	 *@param  sourceDataIndex
	 *@return                  org.agil.kernel.jump.coverage.Coverage
	 *@roseuid                 3F6EAE800128
	 */
	public Coverage getSource(int sourceDataIndex) {
		return null;
	}


	/**
	 *@param  name
	 *@return       java.lang.String
	 *@roseuid      3F6EAE800196
	 */
	public String getMetadataValue(String name) {
		return null;
	}


	/**
	 *@param  width     Description of Parameter
	 *@param  height    Description of Parameter
	 *@param  envelope  Description of Parameter
	 *@return           java.awt.image.BufferedImage
	 *@roseuid          3F6ED16E02FD
	 */
	public BufferedImage getBufferedImage(int width, int height,
			Envelope envelope) {
		//TODO en principio las imagenes se crean RGB a capon. Mirar para que dependa de las BANDAS
		BufferedImage solucion = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) solucion.getGraphics();
		//como se sobredibujan los pixels
		//g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		//g.setBackground(java.awt.Color.WHITE);

		GISGraphics gisGraphics = new GISGraphics(envelope, height, width, g);
		/*
		 *  Esto hace que el fondo vaya en blanco, pero tiene el inconveniente de que
		 *  machaca las capas que haya por debajo (ES DECIR, DEBAJO DE UNA CAPA RASTER
		 *  NO SE PUEDE VER NADA)
		 *  VER COMO FUNCIONAN EL RESTO DE GISES
		 */
		//gisGraphics.fill(java.awt.Color.white);

		Iterator coveragesCandidatos = spatialIndex.query(envelope).iterator();
		while (coveragesCandidatos.hasNext()) {
			GridCoverage coverage = (GridCoverage) coveragesCandidatos.next();
			//verificamos si efectivamente es un buen candidato
			if (!envelope.intersects(coverage.getEnvelope())) {
//	System.out.println("tesela de un mosaico no es candidata valida");
				continue;
			}
			//if

			gisGraphics.drawImagen(coverage);
		}
		//while
		return solucion;
	}


	/**
	 *  Devuelve el indice espacial utilizado para indexar las teselas
	 *  (GridCoverages que forman esta coleccion)
	 *
	 *@return    indice espacial en memoria
	 */
	public SpatialIndex getSpatialIndex() {
		return spatialIndex;
	}


	/**
	 *@return
	 */
	public long getOrigen() {
		return origen;
	}


	/**
	 *  Añade un GridCoverage a la coleccion.
	 *
	 *@param  imagen         The feature to be added to the GridCoverage
	 *      attribute
	 *@param  nombreImagen   nombre que lo identifica
	 *@roseuid               3F687B37033C
	 */
	public void addGridCoverage(GridCoverage imagen, String nombreImagen) {
		imagenes_nombre.put(nombreImagen, imagen);
		spatialIndex.insert(imagen.getEnvelope(), imagen);
		envelope.expandToInclude(imagen.getEnvelope());
	}


	/**
	 *@param  point
	 *@return        java.util.List
	 *@roseuid       3F6EAE7F0271
	 */
	public List evaluate(Point point) {
		Coordinate coordinate = point.getCoordinate();
		Envelope envelope = new Envelope(coordinate);
		Iterator coveragesCandidatos = spatialIndex.query(envelope).iterator();
		while (coveragesCandidatos.hasNext()) {
			GridCoverage coverage = (GridCoverage) coveragesCandidatos.next();
			if (coverage.getEnvelope().contains(coordinate)) {
				return coverage.evaluate(point);
			}
			//if
		}
		//while
		return null;
	}

}
