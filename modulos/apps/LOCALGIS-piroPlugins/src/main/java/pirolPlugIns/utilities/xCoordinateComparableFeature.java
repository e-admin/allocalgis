/**
 * xCoordinateComparableFeature.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
