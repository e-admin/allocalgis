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