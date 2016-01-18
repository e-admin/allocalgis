/*
 * Created on 07.12.2004
 *
 * CVS header information:
 *  $RCSfile: featureSorter.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:54 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/featureSorter.java,v $
 */
package pirolPlugIns.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vividsolutions.jump.feature.Feature;

/**
 * Class to sort an array of Features using 
 * objects that are sortable wrappers for features.
 * 
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 */
public class featureSorter {

	public static List sortByFid( List features ){
	    ArrayList tmp = new ArrayList();
	    
	    Feature[] featureArray = FeatureCollectionTools.FeatureCollection2FeatureArray(features);
		
	    for ( int i=0; i<featureArray.length; i++){
			tmp.add(new idComparableFeature(featureArray[i]));
		}
		
		Collections.sort(tmp);
		
		ArrayList result = new ArrayList();
		
		idComparableFeature[] tmp_Array = (idComparableFeature[])tmp.toArray(new idComparableFeature[0]);
		
		for ( int i=0; i<tmp.size(); i++){
			result.add( tmp_Array[i].getFeature() );
		}
		
		return result;
		
	}
	
	public static List sortByX( List features ){
	    ArrayList tmp = new ArrayList();
		
	    Feature[] featureArray = FeatureCollectionTools.FeatureCollection2FeatureArray(features);
	    
		for ( int i=0; i<featureArray.length; i++){
			tmp.add(new xCoordinateComparableFeature(featureArray[i]));
		}
		
		Collections.sort(tmp);
		
		ArrayList result = new ArrayList();
		
		xCoordinateComparableFeature[] tmp_Array = (xCoordinateComparableFeature[])tmp.toArray(new xCoordinateComparableFeature[0]);
		
		for ( int i=0; i<tmp.size(); i++){
			result.add( tmp_Array[i].getFeature() );
		}
		
		return result;
		
	}
	
}
