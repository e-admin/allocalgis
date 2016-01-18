package com.geopista.ui.plugin.wfs;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
//import org.geotools.referencing.FactoryFinder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

//import com.geopista.app.AppContext;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import java.util.Set;
import java.util.Iterator;

import javax.swing.JOptionPane;


/**
 * The source and destination coordinate reference systems can be in a
 * different datum (for example, WGS 84).
 */
public class CoordinateConversion {
    private static CoordinateConversion instance = new CoordinateConversion();

    private CoordinateConversion() {
    }

    public static CoordinateConversion instance() {
        return instance;
    }

    public boolean wouldChangeValues(CoordinateSystem source,
        CoordinateSystem destination) {
        if (source == CoordinateSystem.UNSPECIFIED) {
            return false;
        }

        if (destination == CoordinateSystem.UNSPECIFIED) {
            return false;
        }

        if (source == destination) {
            return false;
        }

        return true;
    }

    public Geometry reproject(Geometry geometry, final CoordinateSystem source, final CoordinateSystem destiny) throws Exception{
    	try{

    		//CRSAuthorityFactory crsFactory = FactoryFinder.getCRSAuthorityFactory("EPSG", null);
    		geometry.apply(new CoordinateFilter() {
				public void filter(Coordinate coord) {
					try{
						reproject(coord, source, destiny);
					}catch(Exception e){
						JOptionPane.showMessageDialog(null, e.getMessage());        			        	
					}
				}
			});        			
		    return geometry;
    	}catch(VerifyError e){
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public void reproject(Coordinate coordinate, final CoordinateSystem source, final CoordinateSystem destiny){
    	try{

    		
    		CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:"+source.getEPSGCode());
	    	CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:"+destiny.getEPSGCode());
		    MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
		    Coordinate coordinateFinal = coordinate;
		    JTS.transform(coordinate, coordinateFinal,transform);
    	}catch(NoSuchAuthorityCodeException e){
    		e.printStackTrace();
    		JOptionPane.showMessageDialog(null, e.getMessage());        			        	
    	}catch(FactoryException e){
    		e.printStackTrace();
    		JOptionPane.showMessageDialog(null, e.getMessage());        			        	
    	}catch(VerifyError e){
    		e.printStackTrace();
    		JOptionPane.showMessageDialog(null, e.getMessage());        			        	
		}catch(Exception e){
			e.printStackTrace();
    		JOptionPane.showMessageDialog(null, e.getMessage());        			        	
		}
    }
    
}
