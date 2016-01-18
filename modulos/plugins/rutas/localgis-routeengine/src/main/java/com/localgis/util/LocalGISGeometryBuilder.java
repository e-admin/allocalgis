/**
 * LocalGISGeometryBuilder.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.geotools.geometry.GeometryBuilder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.NamedIdentifier;
import org.opengis.geometry.Geometry;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.uva.route.util.GeographicNodeUtil;

import com.localgis.route.graph.structure.basic.VirtualNodeInfo;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTWriter;
import com.vividsolutions.jts.operation.distance.DistanceOp;

/**
 * @author rubengomez
 * Clase estï¿½tica que realiza operaciones con features
 */
public final class LocalGISGeometryBuilder {
	

	/**
	 * Metodo que construye un Point de JTS con las coordenadas y el srid especifico.
	 * @param coordinate - Coordenada x e y
	 * @param srid - srid correspondiente a esas coordenadas
	 * @return Point de tipo JTS
	 */
	public static Point getPointFromCoordinate(Coordinate coordinate, int srid) {
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(),srid);
		Point point = geometryFactory.createPoint(coordinate);
		return point;
	}
	
	/**
	 * Metodo que genera un MultiLineString de un array de linestrings y un srid.
	 * @param lineStringArray - Array de linestrings
	 * @param srid - srid de las features
	 * @return - Multilinestring de JTS
	 */
	public static MultiLineString getMultiLineStringFromLineStringArray(LineString[] lineStringArray,int srid) {
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(),srid);
		MultiLineString mls = geometryFactory.createMultiLineString(lineStringArray);
		return mls;
	}
	
	/**
	 * Metodo que genera un LineString de tipo JTS desde las coordenadas y el srid
	 * @param coords - Array de coordenadas x e y
	 * @param srid - srid correspondiente a las coordenadas
	 * @return
	 */
	public static LineString getLineStringFromCoordinates(Coordinate[] coords,int srid){
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(),srid);
		LineString ls = geometryFactory.createLineString(coords);
		return ls;
	}
	
	/**
	 * Metodo que invierte la direccion de un linestring.
	 * @param linestring
	 * @return
	 */
	public static LineString reverseLineString(LineString linestring){
		
		Coordinate[] coordinates = linestring.getCoordinates();
		Coordinate[] reverse = new Coordinate[coordinates.length];
		for(int i = 0,j=coordinates.length-1; i<coordinates.length;i++,j--){
			reverse[i] = coordinates[j];
		}
		return getLineStringFromCoordinates(reverse, linestring.getSRID());
	}
	
	/**
	 * Metodo que devuelve un array de coordenadas de un arraylist de coordenadas
	 * @param lineStringCoords - Arraylist de coordenadas
	 * @return - Array de coordenadas
	 */
	public static Coordinate[] getArrayCoords(ArrayList<Coordinate> lineStringCoords) {
		
		Coordinate[] temp = new Coordinate[lineStringCoords.size()];
		int i=0;
		Iterator<Coordinate> ls = lineStringCoords.iterator();
		while(ls.hasNext()){
			temp[i++]=(Coordinate)ls.next();
		}
		
		return temp;
	}	
	
	/**
	 * Metodo que devuelve la distancia entre puntos
	 * @param pointA - Punto a
	 * @param pointB - Punto b
	 * @return Distancia entre a y b, como double
	 */
	public static double getDistanceBetweenPoints(Point pointA,Point pointB){
		return DistanceOp.distance(pointA, pointB);
	}
	public static Point getJTSPointFromPGPoint(org.postgis.Point point){
		return getPointFromCoordinate(new Coordinate(point.x,point.y), point.srid);
	}
	public static org.opengis.geometry.primitive.Point getOpengisPointFromJTSPoint(Point point){
		
		CoordinateReferenceSystem crs = null;
		try {
			crs = CRS.decode("EPSG:"+point.getSRID());
		} catch (NoSuchAuthorityCodeException e) {
			e.printStackTrace();
		} catch (FactoryException e) {
			e.printStackTrace();
		}
		GeometryBuilder builder=new GeometryBuilder(crs);
		org.opengis.geometry.primitive.Point newPoint=builder.createPoint(point.getCoordinate().x, point.getCoordinate().y);
		return newPoint;
	}
	
	public static MultiLineString getOrientation(MultiLineString mls,Coordinate orig){
		Coordinate[] coordStart = mls.getCoordinates();
		Coordinate first =coordStart[0];
		if(orig.distance(first) > 0){
			mls = reverseMultiLineString(mls);
		}
		return mls;
	}
	public static MultiLineString reverseMultiLineString(MultiLineString toReverse){
		return (MultiLineString) toReverse.reverse();
	}
	public static String getGeometryColumn(int idLayer) {
        //TODO: Obtener el nombre de la columna geometrï¿½a a partir de el Network y sus properties.
        return "\"GEOMETRY\"";
	}
	public static Point getJTSPointFromPrimitivePoint(org.opengis.geometry.primitive.Point point){
		double coords[]=point.getDirectPosition().getCoordinate();
		String srid=guessSRIDCode(point);
		GeometryFactory fact = new GeometryFactory(new PrecisionModel(),Integer.parseInt(srid));
		return fact.createPoint(new Coordinate(coords[0],coords[1]));
	}
	@SuppressWarnings("unchecked")
	public static String guessSRIDCode(Geometry point)
	{
		Collection aliases = point.getCoordinateReferenceSystem().getIdentifiers();
		String srid="-1";
		Iterator<NamedIdentifier> al = aliases.iterator();
		while (al.hasNext()){
			NamedIdentifier obj = al.next();
			if(obj.getCodeSpace().equals("EPSG")){
				srid = obj.getCode();
				break;
			}
		}
		return srid;
	}
	/**
	 * Simplifica a un punto representativo TODO generalizar
	 * @param point
	 * @param srid
	 * @return
	 */
	public static String getWKTSQLSectionGeometry(Geometry point, String srid)
	{
		double[] coords;
		if (point instanceof org.opengis.geometry.primitive.Point)
		{
			org.opengis.geometry.primitive.Point gPoint = (org.opengis.geometry.primitive.Point) point;
			coords=gPoint.getDirectPosition().getCoordinate();
		}
		else
		{
			coords=point.getRepresentativePoint().getCoordinate();
		}
			
	   
	    return "PointFromText('POINT( "+coords[0]+" "+coords[1]+")',"+srid+")";
	}
	
	public static ArrayList<Coordinate> getCoordinates(Coordinate[] values){
		
		ArrayList<Coordinate> coordsJTSTemporal = new ArrayList<Coordinate>();
        for (int i = 0; i < values.length; i++)
        {
            coordsJTSTemporal.add(values[i]);
        }
        
		return coordsJTSTemporal;
		
	}
	public static boolean estaEnElSegmentoJTS(GeometryFactory geometryFactory, Coordinate coordinate1, Coordinate coordinate2, Coordinate masCercano, double tol)
    {

        Coordinate[] coordinateLinestring = {coordinate1,coordinate2};
        LineString lineString = geometryFactory.createLineString(coordinateLinestring);
        com.vividsolutions.jts.geom.Point puntoCercano = geometryFactory.createPoint(masCercano);
        
        double distancia = DistanceOp.distance(lineString, puntoCercano);
        
        if(distancia<=tol*1.15)
        {
            return true;
        }
        
        return false;
        
    }

	public static void getVirtualDistance(LineString nearestEdgeLinestring,Point referencePoint, VirtualNodeInfo virtualNodeInfo) throws NoSuchAuthorityCodeException, FactoryException {
		int srid = referencePoint.getSRID();
    	GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(),srid);
    	
    	ArrayList<Coordinate> coordinates = LocalGISGeometryBuilder.getCoordinates(nearestEdgeLinestring.getCoordinates());
		LineString lineaRutaTemporal = geometryFactory.createLineString((Coordinate[]) coordinates.toArray(new Coordinate[coordinates.size()]));
        Point pointOrigCoordinate = referencePoint;        
        Coordinate[] coordMasCercano = DistanceOp.closestPoints(lineaRutaTemporal, pointOrigCoordinate);
        com.vividsolutions.jts.geom.Point puntoMasCercano = geometryFactory.createPoint(coordMasCercano[0]);
        double distanciaPunto = DistanceOp.distance(lineaRutaTemporal,puntoMasCercano);
        int position =0;
        
        for (int i = 0; i < coordinates.size()-1; i++)
        {
            
            Coordinate actualCoord = (Coordinate) coordinates.get(i);
            Coordinate nextCoord = (Coordinate) coordinates.get(i+1);
           
            if(LocalGISGeometryBuilder.estaEnElSegmentoJTS(geometryFactory,actualCoord,nextCoord,coordMasCercano[0],distanciaPunto))
            {
            	position = i;
            	i = coordinates.size();
            }
        }
        // Ojo, debe haber al menos un segmento para que funcione.
        AbstractList<Coordinate> list = (AbstractList<Coordinate>) coordinates.subList(0,position + 1);
        AbstractList<Coordinate> list2 = (AbstractList<Coordinate>) coordinates.subList(position + 1,coordinates.size());
        coordinates = new ArrayList<Coordinate>(list);
        coordinates.add(coordMasCercano[0]);        
        LineString ls = geometryFactory.createLineString((Coordinate[]) coordinates.toArray(new Coordinate[coordinates.size()]));
        LineString ls2 = null;
        list2.add(0,coordMasCercano[0]);
        if(list2.size() > 1){
        	ls2 = geometryFactory.createLineString((Coordinate[]) list2.toArray(new Coordinate[list2.size()]));
        }
        else
        	ls2 = ls;
        virtualNodeInfo.setVirtualNodePoint(puntoMasCercano);
        virtualNodeInfo.setLinestringAtoV(ls);
        virtualNodeInfo.setLinestringVtoB(ls2);
        virtualNodeInfo.setRatio(ls.getLength() / lineaRutaTemporal.getLength());
        CoordinateReferenceSystem crs = CRS.decode("EPSG:"+srid);
        virtualNodeInfo.setPoint(GeographicNodeUtil.createISOPoint(puntoMasCercano,crs));
	}

	public static String getWKTSQLSectionGeometry(com.vividsolutions.jts.geom.Geometry where, String srid)
	{
	    WKTWriter writer=new WKTWriter();
	    return writer.write(where);
	}
}