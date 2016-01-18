/**
 * idComparableFeature.java
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
 *  $RCSfile: idComparableFeature.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:54 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/idComparableFeature.java,v $
 */
package pirolPlugIns.utilities;

import com.vividsolutions.jump.feature.Feature;

/**
 * @author orahn
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class idComparableFeature implements Comparable {
	
	//private FeatureUtil.IDComparator idComparator;
	private Feature feature;

	public idComparableFeature(Feature feat) {
		//idComparator = new FeatureUtil.IDComparator();
		this.feature = feat;
	}
	
	public int getID(){
		return this.feature.getID();
	}

	public int compareTo(Object o) {
		idComparableFeature obj = (idComparableFeature)o;
		if (this.feature.getID() < obj.getID())
			return -1;
		else if (this.feature.getID() > obj.getID())
			return 1;
		else
			return 0;
	}
	
	public Feature getFeature() {
		return feature;
	}
}
