/**
 * featureSorter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
