/**
 * TestCoordinates.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.coordinates;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Coordinate;



public class TestCoordinates {
	
	
	public static Coordinate convertCoordinates( Coordinate coordSource, String refSource, String refTarget )
    {
		Coordinate coordDest = new Coordinate();	
		try {
			
			CoordinateReferenceSystem sourceCRS = CRS.decode(refSource, true);
			CoordinateReferenceSystem targetCRS = CRS.decode(refTarget, true);
			MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
			JTS.transform( coordSource,coordDest, transform);
		} catch (Exception e) {e.printStackTrace();}
        return coordDest;
    }
	
	
public static void main(String args[]){
		
		
		System.out.println("Resultado:(-2.13722591899663, 40.0722211627317)");
		System.out.println("Resultado:(-2.1383977112143775, 40.07104791719955)");
		Coordinate coordSource=new Coordinate(573572.3000000059,4436205.849999997);
		Coordinate coordDest=TestCoordinates.convertCoordinates(coordSource, "EPSG:23030", "EPSG:4230");
		System.out.println("Resultado:"+coordDest.toString());
		
		//ServletXMLUpload.verificaCeldasDesconexion();

		/*try {
			GeometryFactory geometryFactory = new GeometryFactory();
			Coordinate coord ;
			coord=new Coordinate(-21.9878301648474, -0.0014827350588441691);

			
			Point point = geometryFactory.createPoint(coord);
			final CoordinateSystem source = PredefinedCoordinateSystems.GEOGRAPHICS_WGS_84;
			final CoordinateSystem destination = PredefinedCoordinateSystems.UTM_30N_ED50;
			
			Coordinate coordinate=coord;
			
			//CRSAuthorityFactory crsFactory = FactoryFinder.getCRSAuthorityFactory("EPSG", null);
			CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:"+source.getEPSGCode());
			CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:"+destination.getEPSGCode());
			MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
			GeometryFactory h = AppContext.getApplicationContext().getGeometryFactory();
			h = new GeometryFactory(h.getPrecisionModel(),destination.getEPSGCode());
			Coordinate coordinateFinal = coordinate;
			JTS.transform(coordinate, coordinateFinal,transform);
			

			System.out.println(point.toString());
		} catch (NoSuchAuthorityCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
