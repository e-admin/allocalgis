/**
 * WFSGUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
//import org.geotools.referencing.FactoryFinder;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Clase que proporiciona utilidades para el servicio WFS-G
 * @author albegarcia
 *
 */
public class WFSGUtils {

    private static Log logger = LogFactory.getLog(WFSGUtils.class);

    /**
     * Método para transformar coordenadas (x,y) de un sistema de referencia a otro
     * @param sourceCRSCode Sistema de referencia origen
     * @param targetCRSCode Sistema de referencia destiono
     * @param coordinates Coordenadas a transformar
     * @return Coordenadas transformadas
     */
    public static double[] transformCoordinates(String sourceCRSCode,String targetCRSCode, double[] coordinates) {
        
        try {
            CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:"+sourceCRSCode);
            CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:"+targetCRSCode);
            MathTransform mathTransform = CRS.findMathTransform(sourceCRS, targetCRS,true);
            GeneralDirectPosition positionSource = new GeneralDirectPosition(coordinates);
            org.opengis.geometry.DirectPosition positionTarget  = mathTransform.transform(positionSource, null);
            return positionTarget.getCoordinates();
        } catch (NoSuchAuthorityCodeException e) {
            logger.error("Error al transformar las coordenadas", e);
        } catch (FactoryException e) {
            logger.error("Error al transformar las coordenadas", e);
        }  catch (TransformException e) {
            logger.error("Error al transformar las coordenadas", e);
        }
        return null;
    }
    
 
     
    public static void main(String args[]){
    	double coordinates[]={42.5985868504226,-4.33087596918763,};
    	double[] coord2=WFSGUtils.transformCoordinates("4230","23030",coordinates);
    	System.out.println("Coordenadas X:"+coord2[0]);
    	System.out.println("Coordenadas Y:"+coord2[1]);
    
    }
    
}
