/**
 * GMLConversor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.mobile.svg.gml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.localgis.mobile.svg.Utils;
import com.localgis.mobile.svg.beans.Coordinates;
import com.localgis.mobile.svg.beans.FeatureGML;
import com.localgis.mobile.svg.beans.GMLDocument;

/**
 * Clase para parsear un fichero GML y transformarlo en SVG
 * 
 * 				GML  ===========> SVG (sin estilo)
 * 
 * @author irodriguez
 *
 */
public class GMLConversor {
	
	private static final Log logger	= LogFactory.getLog(GMLConversor.class);
	
	//documento actual
	private Document dom;
	
	//lista de capas correspondientes a cada uno de los ficheros GML analizados
	private Map<String, GMLDocument> gList;

	public static long SCREEN_WIDTH = 800;
	public static long SCREEN_HEIGHT = 600;
	public static String CHAR_ENCODING_ISO_LATIN = "ISO-8859-1";
	public static String CHAR_ENCODING_UTF_8 = "UTF-8";
	//public static int SCALE = 100;
	public static short DECIMALS = 4;
	
	//coordenadas del viewbox
	private double minX;
	private double minY;
	private double maxX;
	private double maxY;
	
	FeatureGML coordinatesGraticule;
	
	//contador para capa multilinestring
	private int count = 0;

	/** CONSTRUCTORES **/	
	public GMLConversor(){ 
		gList = new HashMap<String, GMLDocument>();
		//reseteo los límites del viewbox
		minX = Double.MAX_VALUE;
		minY = Double.MAX_VALUE;
		maxX = 0;
		maxY = 0;
	}
	
	/** MÉTODOS **/	
	/**
	 * Parsea el fichero pasado como parámetro inicializando los campos que utilizaremos para crear el fichero SVG
	 * Los parámetros son el fichero, el nombre de la capa y la lista de atributos que queremos desconectar (opcional)
	 */
	public void addLayer(File f, String layerName, String systemId, List<String> listaNombreAtr, boolean editable, 
			boolean active,int geometryType) throws SAXException, IOException, ParserConfigurationException{
		if(f.exists()){
			//parseamos el documento para obtener su representación del árbol DOM
			DocumentBuilder db = getDocumentBuilder();
			dom = db.parse(Utils.parseFromUtf8ToLatin(f));
			parseDocument(layerName, systemId, listaNombreAtr, editable,active, geometryType);
		}
	}
	
	/**
	 * Parsea la uri pasada como parámetro inicializando los campos que utilizaremos para crear el fichero SVG
	 * Los parámetros son el fichero, el nombre de la capa y la lista de atributos que queremos desconectar (opcional)
	 */
	public void addLayer(String uri, String layerName, String systemId, List<String> listaNombreAtr, boolean editable,
			boolean active,int geometryType) throws SAXException, IOException, ParserConfigurationException{
		//parseamos el documento para obtener su representación del árbol DOM
		DocumentBuilder db = getDocumentBuilder();
		dom = db.parse(uri);
		parseDocument(layerName, systemId, listaNombreAtr, editable,active, geometryType);
	}
	
	/**
	 * Parsea el InputStream pasado como parámetro inicializando los campos que utilizaremos para crear el fichero SVG
	 * Los parámetros son el fichero, el nombre de la capa y la lista de atributos que queremos desconectar (opcional)
	 */
	public void addLayer(InputStream input, String layerName, String systemId, List<String> listaNombreAtr, boolean editable, 
			boolean active,int geometryType) throws SAXException, IOException, ParserConfigurationException{
		//parseamos el documento para obtener su representación del árbol DOM
		DocumentBuilder db = getDocumentBuilder();
		dom = db.parse(input);
		parseDocument(layerName, systemId, listaNombreAtr, editable, active,geometryType);
	}
	
	public void removeLayer(String layerName){
		gList.remove(layerName);
	}
	
	public void clearLayers(){
		gList.clear();
	}
	
	private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException{
		//obtenemos la factoria
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		//obtenemos una instancia del documento
		DocumentBuilder db = dbf.newDocumentBuilder();
		return db;
	}	
	
	/**
	 * Transforma el árbol DOM en un conjunto de clases
	 * @param listaNombreAtrParam Parámetro opcional para identificar únicamente los atributos que queremos desconectar
	 */
	private void parseDocument(String layerName, String systemId, List<String> listaNombreAtrParam, 
				boolean editable, boolean active,int geometryType){
		//obtenemos el elemento raíz
		Element docEle = dom.getDocumentElement();
		Element el = null;
		List<String> listaNombreAtr = null;
		
		//si es null desconectaremos todos los atributos definidos en el propio GML
		if(listaNombreAtrParam==null){
			//obtenemos los atributos de las features que componen la capa
			NodeList nlAtr = docEle.getElementsByTagName("column");
			listaNombreAtr = new ArrayList<String>();
			if(nlAtr != null && nlAtr.getLength() > 0) {
				for(int i = 0 ; i < nlAtr.getLength();i++) {
					//recogemos el nombre de cada columna
					el = (Element)nlAtr.item(i);
					listaNombreAtr.add(el.getElementsByTagName("name").item(0).getFirstChild().getNodeValue());
				}
			}
		} 
		else {
			listaNombreAtr = listaNombreAtrParam;
		}

		//obtenemos la lista de nodos que coinciden con el tag que nos interesa
		NodeList nl = docEle.getElementsByTagName("feature");
		ArrayList<FeatureGML> featuresList = new ArrayList<FeatureGML>();
		FeatureGML f = null;
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				//parseamos cada nodo y lo añadimos a la lista
				el = (Element)nl.item(i);
				f = getFeature(el);
				featuresList.add(f);
				logger.trace("Feature " + i + " ->" + f);
			}
		}
		
		//añadimos la lista de features correspondientes a la capa parseada
		GMLDocument featureListGML = new GMLDocument(listaNombreAtr, featuresList, editable, active,systemId, geometryType);
		gList.put(layerName, featureListGML);
	}
	
	/**
	 * Realiza la transformación del fichero GML previamente parseado a SVG
	 */
	public String transformToSVG(){
		if(gList==null || gList.size()<1){
			return null;
		}
				
		StringBuffer strSVG = new StringBuffer();
		
		//encabezado
		strSVG.append("<?xml version=\"1.0\" encoding=\""+CHAR_ENCODING_UTF_8+"\"?>");
		//strSVG.append("\n<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">");
		strSVG.append("\n<svg width=\""+SCREEN_WIDTH+"\" height=\""+SCREEN_HEIGHT+"\" viewBox=\""+
							getViewBox()+"\" "+getOffsets()+">");
		
		//cuerpo
		strSVG.append(transformGList());
		
		strSVG.append(getGraticule());
		//cierre
		strSVG.append("\n</svg>");
		
		
		
		return strSVG.toString();
	}

	/**
	 * Transforma la lista de capas sacadas del fichero GML al código SVG dividido por grupos <g></g>
	 * @return
	 */
	private String transformGList() {
		StringBuffer strBuff = new StringBuffer();
		String nombreCapa = null;
		GMLDocument gmlDoc = null;
		Iterator<String> keyIterator = gList.keySet().iterator();
		List<FeatureGML> featuresList = null;
		List<String> listaNombreAtr = null; 
		FeatureGML f = null;
		boolean editable = false;
		boolean active=false;
		while(keyIterator.hasNext()){
			//capa actual
			nombreCapa = keyIterator.next();
			gmlDoc = gList.get(nombreCapa);
			listaNombreAtr = gmlDoc.getListaNombreAtr(); 
			featuresList = gmlDoc.getFeaturesList();
			editable = gmlDoc.isEditable();
			active=gmlDoc.isActive();
			strBuff.append("\n<g id=\"" + nombreCapa + "\" systemId=\"" + gmlDoc.getSystemId() + "\" fill=\"none\" editable=\"" + 
					editable + "\" active=\""+active+ "\" geometryType=\"" + gmlDoc.getGeometryType() + "\" " + transformFeatAtr(listaNombreAtr) + ">");
			
			//tratamiento de features
			for (int i = 0; i < featuresList .size(); i++) {
				f = featuresList.get(i);
				strBuff.append(transformFeatureSVG(f, listaNombreAtr));
			}
			
			//cierre de la capa
			strBuff.append("\n</g>");
		}
		
		return strBuff.toString();
	}

	/**
	 * Añade la lista de atributos de las features
	 * @param listaNombreAtr 
	 * @return
	 */
	private String transformFeatAtr(List<String> listaNombreAtr) {
		StringBuffer strBuff = new StringBuffer();
		for (int i = 0; i < listaNombreAtr.size(); i++) {
			strBuff.append("l" + (i+1) + "=\"" + listaNombreAtr.get(i) + "\" ");
		}
		
		return strBuff.toString();		
	}

	/**
	 * Devuelve el viewbox a insertar en el SVG
	 * @return
	 */
	private String getViewBox() {		
		List<Coordinates> coordinates = null;
		Coordinates currentCoordinate = null;
		
		//recorremos tooooodas las coordenadas de todas las capas para obtener sus extremos
		Iterator<String> keyIterator = gList.keySet().iterator();
		List<FeatureGML> featuresList = null;
		while(keyIterator.hasNext()){
			//capa actual
			featuresList = gList.get(keyIterator.next()).getFeaturesList();
			for (int i = 0; i < featuresList.size(); i++) {
				coordinates = featuresList.get(i).getCoordinates();
				for (int j = 0; j < coordinates.size(); j++) {
					currentCoordinate = coordinates.get(j);
					if(currentCoordinate==null){continue;}//pueden venir nulas por culpa de las multilineas
					if(currentCoordinate.getX()<minX){
						minX = currentCoordinate.getX();
					}
					if(currentCoordinate.getY()<minY){
						minY = currentCoordinate.getY();
					}
					if(currentCoordinate.getX()>maxX){
						maxX = currentCoordinate.getX();
					}
					if(currentCoordinate.getY()>maxY){
						maxY = currentCoordinate.getY();
					}
				}
			}	
		}
		logger.info("Coordenadas viewbox maximas:"+minX+" "+minY+" "+maxX+" "+maxY);
		logger.info("Coordenadas transformadas:"+transformXOffset(maxX) + " " + transformYOffset(minY));
		
		return 0 + " " + 0 + " " + transformXOffset(maxX) + " " + transformYOffset(minY);
	}
	
	/**
	 * Retorna unos atributos que indican el desplazamiento que se ha aplicado a las coordenadas X e Y
	 * @return
	 */
	private String getOffsets() {
		return "despX=\"" + minX + "\" despY=\"" + maxY + "\"";
	}
	
	/**
	 * Añadimos una capa no editable ni visualizable con la graticula de desconexion.
	 * @return
	 */
	private String getGraticule(){
		StringBuffer strBuffer=new StringBuffer();
				
		
		logger.info("Generating Graticule");
		//creación de la geometría de la feature
		if (coordinatesGraticule!=null){
			
			String nombreCapa="graticulemobile";
			String systemId="graticulemobile";
			String editable="false";
			String geometryType="5";
		
			strBuffer.append("\n<g id=\"" + nombreCapa + "\" systemId=\"" + systemId + "\" fill=\"none\" editable=\"" + 
					editable + "\" geometryType=\"" + geometryType + "\">\n");
						
			
			strBuffer.append("<path v1=\"0\" d=\"M");
			//creación de la geometría de la feature
			List<Coordinates> coordinatesList = coordinatesGraticule.getCoordinates();
			Coordinates coordinates = null;
			for (int i = 0; i < coordinatesList.size(); i++) {
				coordinates = coordinatesList.get(i);
				double x = coordinates.getX(); 
				double y = coordinates.getY();
				
				//añadimos las coordenadas transformadas de GML a SVG
				strBuffer.append(" " + transformXOffset(x) + " " + transformYOffset(y) + " ");
				
				//las separamos por una L menos el final por una Z
				if(i < coordinatesList.size()-1){
					strBuffer.append("L");
				}
				else {
					strBuffer.append("Z\"/>");
				}
				
			}
			
			strBuffer.append("\n</g>");
			
			
		}
		return strBuffer.toString();
	}

	/**
	 * Transforma un objeto feature en el String SVG correspondiente
	 * @param f
	 * @param listaNombreAtr 
	 */
	private String transformFeatureSVG(FeatureGML f, List<String> listaNombreAtr) {
		StringBuffer strFeatures = new StringBuffer();
		
		if(f.getGeometryType().equals(FeatureGML.GEOMETRY_TYPE_POINT)){
			transformFeaturePoint(f, strFeatures, listaNombreAtr);
		}
		else if(f.getGeometryType().equals(FeatureGML.GEOMETRY_TYPE_POLYGON)){
			transformFeaturePolygon(f, strFeatures, listaNombreAtr);
		}
		else if(f.getGeometryType().equals(FeatureGML.GEOMETRY_TYPE_MULTILINE)){
			transformFeatureMultiLine(f, strFeatures, listaNombreAtr);
		}
		else if(f.getGeometryType().equals(FeatureGML.GEOMETRY_TYPE_LINE)){
			transformFeatureLine(f, strFeatures, listaNombreAtr);
		}
		
		return strFeatures.toString();
	}

	/**
	 * Transforma la geometría de una feature de tipo LineString
	 * @param f
	 * @param strFeatures
	 * @param listaNombreAtr
	 */
	private void transformFeatureLine(FeatureGML f, StringBuffer strFeatures, List<String> listaNombreAtr) {
		strFeatures.append("\n\t<polyline " + getFeatureProperties(f, listaNombreAtr)+ "points=\"");
		
		//creación de la geometría de la feature
		List<Coordinates> coordinatesList = f.getCoordinates();
		Coordinates coordinates = null;
		for (int i = 0; i < coordinatesList.size(); i++) {
			coordinates = coordinatesList.get(i);
			double x = coordinates.getX(); 
			double y = coordinates.getY();
			
			//añadimos las coordenadas transformadas de GML a SVG
			strFeatures.append(transformXOffset(x) + "," + transformYOffset(y));
			
			if(i<coordinatesList.size()-1){
				strFeatures.append(" ");
			}
		}
		
		strFeatures.append("\"/>");
	}
	
//	/**
//	 * Transforma la geometría de una feature de tipo MultiLineString
//	 * @param f
//	 * @param strFeatures
//	 * @param listaNombreAtr
//	 */
//	private void transformFeatureMultiLine(FeatureGML f, StringBuffer strFeatures, List<String> listaNombreAtr) {
//		//agrupamos todas las líneas con los mismos atributos
//		strFeatures.append("\n\t<g " + getFeatureProperties(f, listaNombreAtr) + ">");
//		
//		//creación de la geometría de la feature
//		List<Coordinates> coordinatesList = f.getCoordinates();
//		Coordinates coordinates = null;
//		for (int i = 0; i < coordinatesList.size(); i++) {
//			coordinates = coordinatesList.get(i);
//			//si las coordinadas son nulas inicialmente implica que es una nueva línea
//			if(coordinates==null && i==0){
//				strFeatures.append("\n\t<polyline points=\"");
//			}
//			//si las coordinadas son nulas por en medio implica cerrar y abrir una nueva línea
//			else if(coordinates==null && i>0){
//				strFeatures.append("\"/>\n\t<polyline points=\"");
//			}
//			//si no son nulas las insertamos
//			else {
//				double x = coordinates.getX(); 
//				double y = coordinates.getY();
//				
//				//añadimos las coordenadas transformadas de GML a SVG
//				strFeatures.append(transformXOffset(x) + "," + transformYOffset(y) + " ");
//			}
//		}
//		
//		//cerramos la última línea y el grupo de líneas
//		strFeatures.append("\"/>\n\t</g>");
//		
//	}
	
	/**
	 * Transforma la geometría de una feature de tipo MultiLineString
	 * @param f
	 * @param strFeatures
	 * @param listaNombreAtr
	 */
	private void transformFeatureMultiLine(FeatureGML f, StringBuffer strFeatures, List<String> listaNombreAtr) {	
		String properties = getFeatureProperties(f, listaNombreAtr);
		
		//creación de la geometría de la feature
		List<Coordinates> coordinatesList = f.getCoordinates();
		Coordinates coordinates = null;
		count++;
		for (int i = 0; i < coordinatesList.size(); i++) {
			coordinates = coordinatesList.get(i);
			//si las coordinadas son nulas inicialmente implica que es una nueva línea
			if(coordinates==null && i==0){
				strFeatures.append("\n\t<polyline g=\""+count+"\" "+properties+"points=\"");
			}
			//si las coordinadas son nulas por en medio implica cerrar y abrir una nueva línea
			else if(coordinates==null && i>0){
				strFeatures.append("\"/>\n\t<polyline g=\""+count+"\" "+properties+"points=\"");
			}
			//si no son nulas las insertamos
			else {
				double x = coordinates.getX(); 
				double y = coordinates.getY();
				
				//añadimos las coordenadas transformadas de GML a SVG
				strFeatures.append(transformXOffset(x) + "," + transformYOffset(y) + " ");
			}
		}
		
		strFeatures.append("\"/>");		
	}

	/**
	 * Transforma la geometría de una feature de tipo Point
	 * @param f
	 * @param strFeatures
	 * @param listaNombreAtr 
	 */
	private void transformFeaturePoint(FeatureGML f, StringBuffer strFeatures, List<String> listaNombreAtr) {
		strFeatures.append("\n\t<circle " + getFeatureProperties(f, listaNombreAtr));
		//strFeatures.append("\n\t<line " + getFeatureProperties(f, listaNombreAtr));
		//strFeatures.append("\n\t<line id=\""+f.getId()+"\"");
		//strFeatures.append("\n\t<line id=\""+f.getId()+"\" stroke=\"green\" stroke-width=\"10\"");
		
		//creación de la geometría de la feature
		List<Coordinates> coordinatesList = f.getCoordinates();
		Coordinates coordinates = null;
		coordinates = coordinatesList.get(0);
		double x = transformXOffset(coordinates.getX()); 
		double y = transformYOffset(coordinates.getY());
			
		
		String defaultRadio="2px";
		//añadimos las coordenadas transformadas de GML a SVG
		//el punto es un círculo de radio 3 px
		strFeatures.append(" cx=\""+x+"\" cy=\""+y+"\" r=\""+defaultRadio+"\"/>");
		//el punto es una línea con mismos extremos
		//strFeatures.append(" x1=\""+x+"\" y1=\""+y+"\" x2=\""+x+"\" y2=\""+y+"\"/>");
	}

	/**
	 * Transforma la geometría de una feature de tipo Polygon
	 * @param f
	 * @param strFeatures
	 * @param listaNombreAtr 
	 */
	private void transformFeaturePolygon(FeatureGML f, StringBuffer strFeatures, List<String> listaNombreAtr) {
		strFeatures.append("\n\t<path " + getFeatureProperties(f, listaNombreAtr)+ "d=\"M");
		//strFeatures.append("\n\t<path id=\""+f.getId()+"\" d=\"M");
		//strFeatures.append("\n\t<path id=\""+f.getId()+"\" "+getStyle()+" d=\"M");
		
		//creación de la geometría de la feature
		List<Coordinates> coordinatesList = f.getCoordinates();
		Coordinates coordinates = null;
		for (int i = 0; i < coordinatesList.size(); i++) {
			coordinates = coordinatesList.get(i);
			double x = coordinates.getX(); 
			double y = coordinates.getY();
			
			//añadimos las coordenadas transformadas de GML a SVG
			strFeatures.append(" " + transformXOffset(x) + " " + transformYOffset(y) + " ");
			
			//las separamos por una L menos el final por una Z
			if(i < coordinatesList.size()-1){
				strFeatures.append("L");
			}
			else {
				strFeatures.append("Z\"/>");
			}
		}
	}
	
	/**
	 * Escribe la lista de properties correspondientes a los atributos de las features
	 * @param f
	 * @param listaNombreAtr 
	 * @return
	 */
	private String getFeatureProperties(FeatureGML f, List<String> listaNombreAtr) {
		StringBuffer strBuff = new StringBuffer();
		String nombreAtr = null;
		Map<String, String> fPropMap = f.getProperties();
		for (int i = 0; i < listaNombreAtr.size(); i++) {
			nombreAtr = (String) listaNombreAtr.get(i);
			if(fPropMap.containsKey(nombreAtr)){
				strBuff.append("v" + (i+1) + "=" + "\"" + fPropMap.get(nombreAtr) + "\" ");
			}
		}
		
		return strBuff.toString();
	}

//	/**
//	 * Aplica el estilo SLD de cada feature
//	 * @return
//	 */
//	private String getStyle() {
//		//return "stroke=\"red\" fill=\"none\" stroke-width=\"0.5\"";
//		return "";
//	}

	/**
	 * Transforma coordenada X de GML a SVG
	 * @param x
	 * @return
	 */
	private double transformXOffset(double x) {
		double newX = (x - minX);
		return Utils.redondear(newX, DECIMALS);
	}
	
	/**
	 * Transforma coordenada Y de GML a SVG
	 * @param x
	 * @return
	 */
	private double transformYOffset(double y) {
		double newY = (y - maxY);
		if(newY < 0){
			newY = (-1) * newY;
		}
		return Utils.redondear(newY, DECIMALS);
	}

	/**
	 * Leemos todos los elementos de una feature y retornamos el objeto creado
	 * @param empEl
	 * @return
	 */
	private FeatureGML getFeature(Element featElem) {
		//para cada elemento obtenemos sus datos
		Map<String, String> propertiesMap = getNodeProperties(featElem);
		String geometryType = getNodeGeometryType(featElem);
		List<Coordinates> coordinates = getNodeCoordinatesList(featElem, "gml:coordinates");
		//String idFeature = getIdFeature(propertiesMap, "ID de parcela");

		//creamos un nuevo objeto con los elementos leídos
		FeatureGML f = new FeatureGML(geometryType, coordinates, propertiesMap);

		return f;
	}

	/**
	 * Obtiene el tipo de geometría de las que tenemos definidas y podemos parsear
	 * @return
	 */
	private String getNodeGeometryType(Element featElem) {
		//punto
		if(featElem.getElementsByTagName(FeatureGML.GEOMETRY_TYPE_POINT).getLength()>0){
			return FeatureGML.GEOMETRY_TYPE_POINT;
		}
		//polígono
		else if(featElem.getElementsByTagName(FeatureGML.GEOMETRY_TYPE_POLYGON).getLength()>0){
			return FeatureGML.GEOMETRY_TYPE_POLYGON;
		}
		//multilínea
		else if(featElem.getElementsByTagName(FeatureGML.GEOMETRY_TYPE_MULTILINE).getLength()>0){
			return FeatureGML.GEOMETRY_TYPE_MULTILINE;
		}
		//línea
		else if(featElem.getElementsByTagName(FeatureGML.GEOMETRY_TYPE_LINE).getLength()>0){
			return FeatureGML.GEOMETRY_TYPE_LINE;
		}
		
		return null;
	}

//	/**
//	 * Retorna el identificador único de la feature (ej: NOMBRE_DE_COLUMNA-12345)
//	 * @param propertiesMap
//	 * @param propertyName
//	 * @return
//	 */
//	private String getIdFeature(Map propertiesMap, String propertyName) {
//		return propertyName.replaceAll(" ", "_") + "-" + propertiesMap.get(propertyName);
//	}

	/**
	 * Transforma un String de pares de coordenadas separadas por saltos de línea
	 * 			x1,y1
	 * 			x2,y2
	 * 			x3,y3
	 * 			...
	 * 			
	 * @param featElem
	 * @param tagName
	 * @return
	 */
	private List<Coordinates> getNodeCoordinatesList(Element featElem, String tagName) {
		List<Coordinates> coordinatesList = new ArrayList<Coordinates>();
		//tratamiento diferente para el caso de multilíneas
		if(featElem.getElementsByTagName(FeatureGML.GEOMETRY_TYPE_MULTILINE).getLength()>0){
			NodeList listLines = featElem.getElementsByTagName(FeatureGML.GEOMETRY_TYPE_LINE);
			for (int i = 0; i < listLines.getLength(); i++) {
				coordinatesList.add(null); //separamos cada línea por un null
				Element line = (Element)listLines.item(i);
				coordinatesList.addAll(getNodeCoordinatesListAtomic(line, tagName));
			}
		}
		else {
			coordinatesList = getNodeCoordinatesListAtomic(featElem, tagName);
		}
		
		return coordinatesList;
	}
	
	/**
	 * Función interna para sacar las coordenadas de un tag
	 * @param featElem
	 * @param tagName
	 * @return
	 */
	private List<Coordinates> getNodeCoordinatesListAtomic(Element featElem, String tagName){
		String str = getNodeTextValue(featElem, tagName);
		String[] splitStr = str.split("\n"); //separamos los saltos de línea

		String[] doubleStrPair = null;
		List<Coordinates> coordinatesList = new ArrayList<Coordinates>();
		Coordinates coord = null;
		for (int i = 0; i < splitStr.length; i++) {
			doubleStrPair = splitStr[i].split(","); //separamos las comas
			coord = new Coordinates(Double.parseDouble(doubleStrPair[0]), Double.parseDouble(doubleStrPair[1]));
			coordinatesList.add(coord);
		}
		
		return coordinatesList;
	}

	/**
	 * Retorna el hash de propiedades del nodo
	 * @param featElem
	 * @return
	 */
	private Map<String, String> getNodeProperties(Element featElem) {
		Map<String, String> propertiesMap = new HashMap<String, String>();
		NodeList nl = featElem.getElementsByTagName("property");
		if(nl != null && nl.getLength() > 0) {
			Element el = null;
			String currentPropName = null;
			String currentPropValue = null;
			for (int i = 0; i < nl.getLength(); i++) {
				el = (Element) nl.item(i);				
				currentPropName = el.getAttribute("name");
				if(el.getFirstChild() != null){
					currentPropValue = el.getFirstChild().getNodeValue();
				}
				propertiesMap.put(currentPropName, currentPropValue);
			}
		}
		
		return propertiesMap;
	}

	/**
	 * Retorna el texto correspondiente a este nodo
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private String getNodeTextValue(Element featElem, String tagName) {
		String textVal = null;
		NodeList nl = featElem.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			if(el.getFirstChild() != null){
				textVal = el.getFirstChild().getNodeValue();
			}
		}

		return textVal;
	}


	/**
	 * Retorna el entero correspondiente a este nodo
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private int getNodeIntValue(Element featElem, String tagName) {
		return Integer.parseInt(getNodeTextValue(featElem,tagName));
	}
	
	public void setCoordinatesGraticule(FeatureGML coordinatesGraticule){
		this.coordinatesGraticule=coordinatesGraticule;
	}

	/**
	 * Punto de entrada de pruebas para la clase
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			/** conversión de ficheros **/
			String PATH_BASE = "c:/LocalGIS/Datos/Mapa de Catastro de Urbana.16078/";
			System.out.println("<<< 1. CONVERSION GML -> SVG Tiny >>>");
			//inicialización
			File gmlFile1 = new File(PATH_BASE+"Parcelas.gml");
			File gmlFile2 = new File(PATH_BASE+"Números de policía.gml");
			File gmlFile3 = new File(PATH_BASE+"Tramos de calle.gml");
			//File gmlFile4 = new File(PATH_BASE+"Arboleda.gml");
			File gmlFile5 = new File(PATH_BASE+"Calles.gml");
			GMLConversor gmlConv = new GMLConversor();
			gmlConv.addLayer(gmlFile1, "Parcelas", "parcelas", null, true,true, 1);
			gmlConv.addLayer(gmlFile2, "Números de policía", "numeros_policía", null, true,false, 2);
			gmlConv.addLayer(gmlFile3, "Tramos de calle", "tramos_calle", null, true, false,3);
			//gmlConv.addLayer(gmlFile4, "Arboleda", "arboleda", null, true, 4);
			gmlConv.addLayer(gmlFile5, "Calles", "calles", null, true, false,5);
			String strSVG = gmlConv.transformToSVG();
			FileOutputStream fOut = new FileOutputStream(PATH_BASE+"Salida.svg");
			fOut.write(strSVG.getBytes(CHAR_ENCODING_UTF_8)); //codificamos el fichero
			fOut.flush();
			fOut.close();
//			
//			/** zipeamos el fichero **/
//			System.out.println("<<< 2. CREACION DE FICHERO ZIP >>>");
//		    //especificamos los ficheros a comprimir
//		    String[] filesToZip = new String[1];
//		    filesToZip[0] = PATH_BASE+"Salida.svg";
////		    filesToZip[1] = "secondfile.txt";
////		    filesToZip[2] = "temp\thirdfile.txt";
//		    byte[] buffer = new byte[18024];
//		    //especificamos el nombre del fichero zip
//		    String zipFileName = PATH_BASE+"Salida.zip";
//		    try {
//		       ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
//		       //ajustamos el ratio de compresión
//		       out.setLevel(Deflater.DEFAULT_COMPRESSION);
//		       //añadimos cada fichero al zip
//		       for (int i = 0; i < filesToZip.length; i++) {
//		         System.out.println(i);
//		         FileInputStream in = new FileInputStream(filesToZip[i]);
//		         //añadimos una entrada al zip
//		         out.putNextEntry(new ZipEntry("Salida.svg"));
//		         //transferimos los bytes al fichero
//		         int len;
//			     while ((len = in.read(buffer)) > 0){
//			        out.write(buffer, 0, len);
//			     }
//		         out.closeEntry();
//		         in.close();
//		       }
//		       out.close();
//		     }
//		     catch (IllegalArgumentException iae) {
//		       iae.printStackTrace();
//		     }
//		     catch (FileNotFoundException fnfe) {
//		       fnfe.printStackTrace();
//		     }
//		     catch (IOException ioe)
//		     {
//		       ioe.printStackTrace();
//		     }			
//			
//			/** subimos el fichero por HTTP a un servidor **/
//		    System.out.println("<<< 3 . SUBIDA DEL FICHERO POR HTTP >>>");
//			HttpClient client = new HttpClient();
//		    MultipartPostMethod mPost = new MultipartPostMethod("http://localhost:8082/localgismobile/SVGManager.do?method=upload");
//		    client.setConnectionTimeout(8000);
//		    //envío de archivos
//		    File f1 = new File(PATH_BASE+"Salida.zip");
////		    File f2 = new File("academy.xml");
////		    File f3 = new File("academyRules.xml");
//		    System.out.println("File1 Length = " + f1.length());
////		    System.out.println("File2 Length = " + f2.length());
////		    System.out.println("File3 Length = " + f3.length());
//		    mPost.addParameter(f1.getName(), f1);
////		    mPost.addParameter(f2.getName(), f2);
////		    mPost.addParameter(f3.getName(), f3);
//		    int statusCode1 = client.executeMethod(mPost);
//		    System.out.println("statusLine>>> " + mPost.getStatusLine() + " || statusCode>>> "+statusCode1);
//		    mPost.releaseConnection();
		    
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
