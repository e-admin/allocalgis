/**
 * ColoringChangedEvent.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 19.07.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: ColoringChangedEvent.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:05 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/colors/ColoringChangedEvent.java,v $
 */
package pirolPlugIns.utilities.colors;

/**
 * Simple "Event" that wrapps a ColorGenerator and some min/max values...
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * 
 */
public class ColoringChangedEvent {
    protected ColorGenerator colorGenerator = null;
    double min, max;
    
    
    
    public ColoringChangedEvent() {
        super();
    }
    public ColorGenerator getColorGenerator() {
        return colorGenerator;
    }
    public void setColorGenerator(ColorGenerator colorGenerator) {
        this.colorGenerator = colorGenerator;
    }
    public double getMax() {
        return max;
    }
    public void setMax(double max) {
        this.max = max;
    }
    public double getMin() {
        return min;
    }
    public void setMin(double min) {
        this.min = min;
    }
    
    
}
