/*
 * Created on 18.01.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: xCoordinateComparableFeature.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:54 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/xCoordinateComparableFeature.java,v $
 */
package pirolPlugIns.utilities;

import com.vividsolutions.jump.feature.Feature;

/**
 * @author Ole Rahn
 * 
 * FH Osnabrück - University of Applied Sciences Osnabrück
 * Project PIROL 2005
 * Daten- und Wissensmanagement
 * 
 */
public class xCoordinateComparableFeature implements Comparable {
	
	private Feature feature;

	public xCoordinateComparableFeature(Feature feat) {
		this.feature = feat;
	}
	
	public int getID(){
		return this.feature.getID();
	}
	
	public double getXCoordinate(){
	    return this.feature.getGeometry().getCoordinates()[0].x;
	}

	public int compareTo(Object o) {
	    xCoordinateComparableFeature obj = (xCoordinateComparableFeature)o;
		if (this.getXCoordinate() < obj.getXCoordinate())
			return -1;
		else if (this.getXCoordinate() > obj.getXCoordinate())
			return 1;
		else
			return 0;
	}
	
	public Feature getFeature() {
		return feature;
	}
}
