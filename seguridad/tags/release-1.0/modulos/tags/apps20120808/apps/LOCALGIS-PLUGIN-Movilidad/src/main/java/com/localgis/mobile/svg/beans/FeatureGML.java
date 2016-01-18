package com.localgis.mobile.svg.beans;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Clase envoltorio de las features obtenidas del fichero GML
 * @author irodriguez
 *
 */
public class FeatureGML {
	
	private String geometryType;
	private List<Coordinates> coordinates;
	private Map<String, String> properties;
//	private String id;
	
	//tipos de geometrías que contemplamos
	public static String GEOMETRY_TYPE_POLYGON = "gml:Polygon";
	public static String GEOMETRY_TYPE_POINT = "gml:Point";
	public static String GEOMETRY_TYPE_MULTILINE = "gml:MultiLineString";
	public static String GEOMETRY_TYPE_LINE = "gml:LineString";

	/**
	 * Constructor de la feature
	 * @param geometryType	tipo de geometría de la feature [polygon, point, ...]
	 * @param coordinates	lista de coordenadas de la feature (por pares)
	 * @param properties	propiedades de la feature
	 */
	public FeatureGML(String geometryType, List<Coordinates> coordinates, Map<String, String> properties) {
		super();
		this.geometryType = geometryType;
		this.coordinates = coordinates;
		this.properties = properties;
//		this.id = id;
	}
	
	public FeatureGML(List<Coordinates> coordinates){
		this.coordinates = coordinates;
	}

	public String getGeometryType() {
		return geometryType;
	}

	public void setGeometryType(String geometryType) {
		this.geometryType = geometryType;
	}

	public List<Coordinates> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Coordinates> coordinates) {
		this.coordinates = coordinates;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
	
	@Override
	public String toString() {
		StringBuffer strBuff = new StringBuffer();
//		strBuff.append("id: " + id);
		strBuff.append(" || geometryType: " + geometryType);
		strBuff.append(" || coordinates: ");
		for (int i = 0; i < coordinates.size(); i++) {
			strBuff.append(coordinates.get(i) + " ");
		}
		strBuff.append(" || properties: ");
		Iterator<String> keySetIt = properties.keySet().iterator();
		String currentKey = null;
		while(keySetIt.hasNext()){
			currentKey = (String) keySetIt.next();
			strBuff.append(currentKey + "=" + properties.get(currentKey) + " ; ");
		}
		
		return strBuff.toString();
	}
}
