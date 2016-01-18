/**
 * CoordinateComparator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 04.01.2005
 *
 * CVS header information:
 *  $RCSfile: CoordinateComparator.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:54 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/CoordinateComparator.java,v $
 */
package pirolPlugIns.utilities;

import java.util.Comparator;

/**
 * 
 * Comparator class for Sortable objects, sets comparision criteria
 * on the fly -> no need to set them manually before sorting
 *
 * @author orahn
 *
 * FH Osnabrück - University of Applied Sciences Osnabrück
 * Project PIROL 2005
 * Daten- und Wissensmanagement
 *  
 * @see Sortable
 * 
 */

public class CoordinateComparator implements Comparator {
    public static int SORTFOR_X = 0;
	public static int SORTFOR_Y = 1;
	public static int SORTFOR_Z = 2;
	/* added by oster
	 * this is useful to sort a point field for booth x and y
	 */
	public static int SORTFOR_XY = 3;
	
	protected int sortFor = CoordinateComparator.SORTFOR_X;
    
    public CoordinateComparator(int sortFor) {
        this.sortFor = sortFor;
    }
    
    public int getSortFor() {
        return sortFor;
    }
    public void setSortFor(int sortFor) {
        this.sortFor = sortFor;
    }
    
    public int compare(Object arg0, Object arg1) {
        Sortable one, two;
        one = (Sortable)arg0;
        two = (Sortable)arg1;
        
        one.setSortFor(this.sortFor);
        two.setSortFor(this.sortFor);
        
        return one.compareTo(two);
    }

}
