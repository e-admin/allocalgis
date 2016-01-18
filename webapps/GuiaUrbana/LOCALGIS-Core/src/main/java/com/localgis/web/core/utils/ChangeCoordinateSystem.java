/**
 * ChangeCoordinateSystem.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;


import com.vividsolutions.jts.geom.Coordinate;


public class ChangeCoordinateSystem {

	private static Log logger = LogFactory.getLog(ChangeCoordinateSystem.class);
	/*
	private static String SRS_4326="GEOGCS[\"WGS 84\",DATUM[\"World Geodetic System 1984\"," +
			"SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, " +
			"AUTHORITY[\"EPSG\",\"7030\"]]," +
			"TOWGS84[95,108.2,121,0,0,0,0], AUTHORITY[\"EPSG\",\"6326\"]]," +
			"PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]]," +
			"UNIT[\"degree\", 0.017453292519943295], " +
			"AXIS[\"Geodetic longitude\", EAST], " +
			"AXIS[\"Geodetic latitude\", NORTH], AUTHORITY[\"EPSG\",\"4326\"]]";
	
	*/
	
	/** Transformación 23029 - 4236 */
	private static String SRS_4230="GEOGCS[\"ED50\",DATUM[\"European Datum 1950\"," +
			"SPHEROID[\"International 1924\", 6378388.0, 297.0, " +
			"AUTHORITY[\"EPSG\",\"7022\"]]," +
			//"TOWGS84[-157.89, -17.16, -78.41, 2.118, 2.697, -1.434, -1.1097046576093785]," +
			"TOWGS84[-95,-108.2,-121,0,0,0,0]," +
			"AUTHORITY[\"EPSG\",\"6230\"]],PRIMEM[\"Greenwich\", 0.0, " +
			"AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\", 0.017453292519943295]," +
			"AXIS[\"Geodetic longitude\", EAST],AXIS[\"Geodetic latitude\", NORTH],AUTHORITY[\"EPSG\",\"4230A\"]]";
	
	/** Transformación 23028 - 4236 */
	private static String SRS_23028_4326="GEOGCS[\"ED50\",DATUM[\"European Datum 1950\"," +
			"SPHEROID[\"International 1924\", 6378388.0, 297.0, " +
			"AUTHORITY[\"EPSG\",\"7022\"]]," +
			//"TOWGS84[-157.89, -17.16, -78.41, 2.118, 2.697, -1.434, -1.1097046576093785]," +
			"TOWGS84[-96.7,-112.2,-121,0,0,0,0]," +
			"AUTHORITY[\"EPSG\",\"6230\"]],PRIMEM[\"Greenwich\", 0.0, " +
			"AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\", 0.017453292519943295]," +
			"AXIS[\"Geodetic longitude\", EAST],AXIS[\"Geodetic latitude\", NORTH],AUTHORITY[\"EPSG\",\"4230A\"]]";

	private static String SRS_23029_4326="GEOGCS[\"ED50\",DATUM[\"European Datum 1950\"," +
	"SPHEROID[\"International 1924\", 6378388.0, 297.0, " +
	"AUTHORITY[\"EPSG\",\"7022\"]]," +
	//"TOWGS84[-157.89, -17.16, -78.41, 2.118, 2.697, -1.434, -1.1097046576093785]," +
	"TOWGS84[-95,-108.2,-121,0,0,0,0]," +
	"AUTHORITY[\"EPSG\",\"6230\"]],PRIMEM[\"Greenwich\", 0.0, " +
	"AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\", 0.017453292519943295]," +
	"AXIS[\"Geodetic longitude\", EAST],AXIS[\"Geodetic latitude\", NORTH],AUTHORITY[\"EPSG\",\"4230A\"]]";


	/** Transformación 23030 - 4236 */
	private static String SRS_23030_4326="GEOGCS[\"ED50\",DATUM[\"European Datum 1950\"," +
			"SPHEROID[\"International 1924\", 6378388.0, 297.0, " +
			"AUTHORITY[\"EPSG\",\"7022\"]]," +
			//"TOWGS84[-157.89, -17.16, -78.41, 2.118, 2.697, -1.434, -1.1097046576093785]," +
			"TOWGS84[-93.3,-104.8,-119.1,0,0,0,0]," +
			"AUTHORITY[\"EPSG\",\"6230\"]],PRIMEM[\"Greenwich\", 0.0, " +
			"AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\", 0.017453292519943295]," +
			"AXIS[\"Geodetic longitude\", EAST],AXIS[\"Geodetic latitude\", NORTH],AUTHORITY[\"EPSG\",\"4230A\"]]";

	/** Transformación 23030 - 4236 */
	private static String SRS_23031_4326="GEOGCS[\"ED50\",DATUM[\"European Datum 1950\"," +
			"SPHEROID[\"International 1924\", 6378388.0, 297.0, " +
			"AUTHORITY[\"EPSG\",\"7022\"]]," +
			//"TOWGS84[-157.89, -17.16, -78.41, 2.118, 2.697, -1.434, -1.1097046576093785]," +
			"TOWGS84[-91.6,-101.4,-118.2,0,0,0,0]," +
			"AUTHORITY[\"EPSG\",\"6230\"]],PRIMEM[\"Greenwich\", 0.0, " +
			"AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\", 0.017453292519943295]," +
			"AXIS[\"Geodetic longitude\", EAST],AXIS[\"Geodetic latitude\", NORTH],AUTHORITY[\"EPSG\",\"4230A\"]]";
	
	
	
	public static double[] transform(String sourceCRSCode,String targetCRSCode, double[] coordinates) {
		       	
		//Las coordenadas hay que enviarlas en orden inverso.
		double[] transformedCoordinates = transformCoordinates(sourceCRSCode, targetCRSCode, new double[] {coordinates[0],coordinates[1]},true);
	
		//No se porque es exactamente pero en un PC windows funciona distinto que en la maquina
		//hay que cambiar el orden de las coordenadas.
		//if ((transformedCoordinates[0]<0) || (transformedCoordinates[1]<0))
	    //transformedCoordinates = transformCoordinates(sourceCRSCode, targetCRSCode, new double[] {coordinates[1],coordinates[0]});
			
		//double[] transformedCoordinates = WFSGUtils.transformCoordinates(sourceCRSCode, targetCRSCode, new double[] {posY,posX});
		logger.info("SRID Origen-Destino:"+sourceCRSCode+" "+targetCRSCode+" Coordenadas de busqueda:("+coordinates[0]+","+coordinates[1]+") ("+transformedCoordinates[0]+" "+transformedCoordinates[1]+")");
		
		return transformedCoordinates;
	}
	
	
	public static double[] transformSpecial(String sourceCRSCode,String targetCRSCode, double[] coordinates) {
       	
		//Las coordenadas hay que enviarlas en orden inverso.
		double[] transformedCoordinates = transformCoordinates(sourceCRSCode, targetCRSCode, new double[] {coordinates[0],coordinates[1]},false);
	
		//No se porque es exactamente pero en un PC windows funciona distinto que en la maquina
		//hay que cambiar el orden de las coordenadas.
		//if ((transformedCoordinates[0]<0) || (transformedCoordinates[1]<0))
	    //transformedCoordinates = transformCoordinates(sourceCRSCode, targetCRSCode, new double[] {coordinates[1],coordinates[0]});
			
		//double[] transformedCoordinates = WFSGUtils.transformCoordinates(sourceCRSCode, targetCRSCode, new double[] {posY,posX});
		logger.info("SRID Origen-Destino:"+sourceCRSCode+" "+targetCRSCode+" Coordenadas de busqueda:("+coordinates[0]+","+coordinates[1]+") ("+transformedCoordinates[0]+" "+transformedCoordinates[1]+")");
		
		return transformedCoordinates;
	}
	
	
	
	public static double[] transformCoordinates(String sourceCRSCode,String targetCRSCode, double[] coordinates,boolean useDefaultSRS) {
	        
	        try {
	            /*CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:"+sourceCRSCode);
	            CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:"+targetCRSCode);
	            MathTransform mathTransform = CRS.findMathTransform(sourceCRS, targetCRS,true);
	            GeneralDirectPosition positionSource = new GeneralDirectPosition(coordinates);
	            org.opengis.geometry.DirectPosition positionTarget  = mathTransform.transform(positionSource, null);
	            final int dim = positionTarget.getDimension();
	            //logger.info("Dimension:"+dim);
	            double[] position=new double[dim];
	            for (int i=0; i<dim; i++) {
	                position[i]=positionTarget.getOrdinate(i); // no copy overhead
	            }
	            //return positionTarget.getCoordinates();
	            return position;*/

	        	CoordinateReferenceSystem sourceCRS=null;
	        	if (useDefaultSRS)
	        		sourceCRS = CRS.decode("EPSG:" + sourceCRSCode, true);
	        	else {
	        		//sourceCRS = CRS.parseWKT(SRS_4230);
	        		if ("23029".equals(sourceCRSCode))
	        			sourceCRS = CRS.parseWKT(SRS_23029_4326);
	        		else if ("23028".equals(sourceCRSCode)) 
	        			sourceCRS = CRS.parseWKT(SRS_23028_4326);
	        		else if ("23030".equals(sourceCRSCode)) 
	        			sourceCRS = CRS.parseWKT(SRS_23030_4326);
	        		else if ("23031".equals(sourceCRSCode)) 
	        			sourceCRS = CRS.parseWKT(SRS_23031_4326);
	        		else if ("4230".equals(sourceCRSCode)) 
	        			sourceCRS = CRS.parseWKT(SRS_4230);
	        		else
	        			sourceCRS = CRS.decode("EPSG:" + sourceCRSCode, true);
	        		
	        	}
	        	
	    		CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:" + targetCRSCode, true);
	    		
	    		//CoordinateReferenceSystem targetCRS = CRS.parseWKT(SRS_4326);
	    		
	    		
	    		MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
	    		Coordinate coordSourceMin = new Coordinate(coordinates[0], coordinates[1]);
	    		Coordinate coordTargetMin = new Coordinate();
	    		JTS.transform(coordSourceMin, coordTargetMin, transform);
	    		
	    		return new double[] {coordTargetMin.x,coordTargetMin.y};	    		

	        } catch (NoSuchAuthorityCodeException e) {
	            logger.error("Error al transformar las coordenadas", e);
	        } catch (FactoryException e) {
	            logger.error("Error al transformar las coordenadas", e);
	        }  catch (TransformException e) {
	            logger.error("Error al transformar las coordenadas", e);
	        }
	        catch (Throwable e){
	        	logger.error("Error al transformar las coordenadas", e);
	        }
	        return null;
	    }
	
	public static void main(String args[]){
    	
	
    	
    	try {
			//double coordinates[]={42.5985868504226,-4.33087596918763,};
			//double coordinates[]={-6.940566867175372,43.57078178391351,};
			double coordinates[]={-6.60920904916341,43.2723382440268};
			
			//CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4230", true);
			CoordinateReferenceSystem sourceCRS = CRS.parseWKT(SRS_4230);
			
			CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326", true);
			
			CoordinateReferenceSystem targetCRS1 = CRS.parseWKT(SRS_23029_4326);
			//String code = CRS.lookupIdentifier( targetTemp, true ); // should be "EPSG:4230"
			//CoordinateReferenceSystem targetCRS1 = CRS.decode(code, true);
			
			Coordinate coordSourceMin = new Coordinate(coordinates[0], coordinates[1]);
    		Coordinate coordTargetMin = new Coordinate();
			
			MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS,false);
			MathTransform transform1 = CRS.findMathTransform(sourceCRS, targetCRS1,false);
			
			JTS.transform(coordSourceMin, coordTargetMin, transform);
			System.out.println("Coordenadas X:"+coordTargetMin.x);
			System.out.println("Coordenadas Y:"+coordTargetMin.y);
		
			JTS.transform(coordSourceMin, coordTargetMin, transform1);
			System.out.println("Coordenadas X:"+coordTargetMin.x);
			System.out.println("Coordenadas Y:"+coordTargetMin.y);
			
			System.out.println("-6.610623909,43.271230245");
			
			System.out.println("BUENO -6.610665,43.271230");
			
			//double[] transformedCoordinates=ChangeCoordinateSystem.transform("4230","4326",coordinates);

    	} catch (NoSuchAuthorityCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
    }
}
