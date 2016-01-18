/**
 * Sortable.java
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
 *  $RCSfile: Sortable.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:54 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/Sortable.java,v $
 */
package pirolPlugIns.utilities;

/**
 * 
 * Abstract base class for sortable objects like punkt object.
 * Adds a natural ordering to those objects and allows to change what to sort for.
 * 
 * @author orahn
 *
 * FH Osnabrück - University of Applied Sciences Osnabrück
 * Project PIROL 2005
 * Daten- und Wissensmanagement
 * 
 * @see punkt
 */
public abstract class Sortable implements Comparable {
	protected int sortFor = CoordinateComparator.SORTFOR_X;
    
    public abstract int getSortFor();

    public abstract void setSortFor(int sortFor);

    public abstract int compareTo(Object arg0);
}