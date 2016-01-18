/**
 * DelaunayLoopItem.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 05.01.2005
 *
 * CVS header information:
 *  $RCSfile: DelaunayLoopItem.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:55 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/Delaunay/DelaunayLoopItem.java,v $
 */
package pirolPlugIns.utilities.Delaunay;


/**
 * Class tha describes the needed values to start an iteration in the culculation
 * of a delaunay diagramm.
 * 
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $s
 */
public class DelaunayLoopItem {
    private int triangleCount;
    private DelaunayPunkt punkt1, punkt2, alterPunkt;
    
    public DelaunayLoopItem(DelaunayPunkt punkt1, DelaunayPunkt punkt2, DelaunayPunkt alterPunkt,
            int triangleCount) {
        super();
        this.punkt1 = punkt1;
        this.punkt2 = punkt2;
        this.alterPunkt = alterPunkt;
        this.triangleCount = triangleCount;
    }
    
    public DelaunayLoopItem(DelaunayPunkt punkt1, DelaunayPunkt punkt2, DelaunayPunkt alterPunkt) {
        super();
        this.punkt1 = punkt1;
        this.punkt2 = punkt2;
        this.alterPunkt = alterPunkt;
        this.triangleCount = -1;
    }
    
    
    public DelaunayPunkt getAlterPunkt() {
        return alterPunkt;
    }
    public DelaunayPunkt getPunkt1() {
        return punkt1;
    }
    public DelaunayPunkt getPunkt2() {
        return punkt2;
    }
    public int getTriangleCount() {
        return triangleCount;
    }
}
