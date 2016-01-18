/**
 * SvgConversorXMLUpload.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.projectsync.xml;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import es.satec.localgismobile.server.projectsync.xml.beans.AttributeXMLUpload;
import es.satec.localgismobile.server.projectsync.xml.beans.FeatureXMLUpload;

/**
 * Conversor de la geometría de un svg a una válida para localgis
 * @author irodriguez
 *
 */
public class SvgConversorXMLUpload {

	private static final String POINT_SEPARATOR = " ";
	private static String SVG_LINE_TYPE = "polyline";
	private static String SVG_POLYGON_TYPE = "polygon";
	private static String SVG_POINT_TYPE = "ellipse";
	private GeometryFactory geoFact;
	private double despX;
	private double despY;
	
	public SvgConversorXMLUpload(){
		geoFact = new GeometryFactory();
	}
	
	/**
	 * Método de conversión
	 * @param featXML
	 * @return
	 */
	public Geometry convertSVG(FeatureXMLUpload featXML, double despX, double despY){
		Geometry geom = null;
		List<AttributeXMLUpload> attFeatList = featXML.getAttFeatList();
		this.despX = despX;
		this.despY = despY;
		if(featXML.getName().equals(SVG_LINE_TYPE)){
			geom = parseXmlLine(attFeatList);
		}else if(featXML.getName().equals(SVG_POLYGON_TYPE)){
			geom = parseXmlPolygon(attFeatList);
		}else if(featXML.getName().equals(SVG_POINT_TYPE)){
			geom = parseXmlPoint(attFeatList);
		}
		
		return geom;
	}

	private Geometry parseXmlLine(List<AttributeXMLUpload> attFeatList) {
		//obtenemos la lista de coordenadas
		List<Coordinate> coordinateList = getCoordinateList(attFeatList);
		
		//creamos la geometría correspondiente
		Coordinate[] coordArray = (Coordinate[]) coordinateList.toArray(new Coordinate[] {});
		LineString lineString = geoFact.createLineString(coordArray);
		return lineString;
	}

	private Geometry parseXmlPolygon(List<AttributeXMLUpload> attFeatList) {
		//obtenemos la lista de coordenadas
		List<Coordinate> coordinateList = getCoordinateList(attFeatList);
		coordinateList.add(coordinateList.get(0));
		
		//creamos la geometría correspondiente
		Coordinate[] coordArray = (Coordinate[]) coordinateList.toArray(new Coordinate[] {});
		Polygon polygon = geoFact.createPolygon(geoFact.createLinearRing(coordArray),null);
		
		return polygon;
	}

	private Geometry parseXmlPoint(List<AttributeXMLUpload> attFeatList) {
		//búsqueda de la geometría en el svg
		AttributeXMLUpload attr = null;
		String strCx = null;
		String strCy = null;
		for (int i = 0; i < attFeatList.size(); i++) {
			attr = attFeatList.get(i);
			if(attr.getKey().toLowerCase().equals("cx")){
				strCx = attr.getValue();
			}
			else if(attr.getKey().toLowerCase().equals("cy")){
				strCy = attr.getValue();
			}
		}
		
		if(strCx==null || strCy==null){
			return null;
		}
		
		//coordenadas transformadas
		Coordinate coordPoint = transformCoordinates(new Coordinate(Double.parseDouble(strCx), Double.parseDouble(strCy)));
		
		//creamos la geometría correspondiente
		Point point =  geoFact.createPoint(coordPoint);
		
		return point;
	}
	
	/**
	 * Obtiene una lista de coordenadas apartir de los atributos del SVG
	 * @param attFeatList
	 * @return
	 */
	private List<Coordinate> getCoordinateList(List<AttributeXMLUpload> attFeatList) {
		//búsqueda de la geometría en el svg
		AttributeXMLUpload attr = null;
		String strPoints = null;
		for (int i = 0; i < attFeatList.size(); i++) {
			attr = attFeatList.get(i);
			if(attr.getKey().toLowerCase().equals("points")){
				strPoints = attr.getValue();
				break;
			}
		}
		
		if(strPoints==null){
			return null;
		}
		
		//lista de coordenadas transformadas
		List<Coordinate> coordList = new ArrayList<Coordinate>();
		String[] splitPoints = strPoints.split(POINT_SEPARATOR);
		if(splitPoints.length%2!=0){ //deben ser pares de valores
			return null;
		}
		Coordinate coordAux = null;
		for (int i = 0; i < splitPoints.length; i=i+2) {
			coordAux = new Coordinate(Double.parseDouble(splitPoints[i]), Double.parseDouble(splitPoints[i+1]));
			coordList.add(transformCoordinates(coordAux));
		}
		
		return coordList;
	}
	
	/**
	 * Aplica una reconversión de las coordenadas a los datos internos
	 * @param coordSrc
	 * @return
	 */
	private Coordinate transformCoordinates(Coordinate coordSrc){		
		return transformCoordinates(coordSrc, despX, despY);
	}
	
	/**
	 * Para aplicar la reconversión a cualquier coordenada dados unos desplazamientos previos
	 * @param coordSrc
	 * @param desplazaX
	 * @param desplazaY
	 * @return
	 */
	public static Coordinate transformCoordinates(Coordinate coordSrc, double desplazaX, double desplazaY){
		Coordinate coordRet = new Coordinate(0,0);
		coordRet.x = coordSrc.x + desplazaX;
		if(coordSrc.y > 0){
			coordRet.y = coordSrc.y * (-1);
		}
		coordRet.y = coordRet.y + desplazaY;
		
		return coordRet;
	}
	
}
