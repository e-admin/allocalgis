/**
 * CorrelationDataPair.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 18.05.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: CorrelationDataPair.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:57 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/statistics/CorrelationDataPair.java,v $
 */
package pirolPlugIns.utilities.statistics;

import pirolPlugIns.utilities.ScaleChanger;
import pirolPlugIns.utilities.punkt;
import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

/**
 * 
 *
 * @author Ole Rahn
 * @author FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * Project: PIROL (2005),
 * Subproject: Daten- und Wissensmanagement
 * 
 */
public class CorrelationDataPair extends punkt {

    protected PersonalLogger logger = new PersonalLogger(DebugUserIds.USER_Ole);

    /**
     *@param coords
     */
    public CorrelationDataPair(double[] coords) {
        super(coords);
    }

    /**
     *@param coords
     *@param index
     */
    public CorrelationDataPair(double[] coords, int index) {
        super(coords, index);
    }

    /**
     *@param coords
     *@param index
     *@param scaler
     *@param prescaled
     */
    public CorrelationDataPair(double[] coords, int index, ScaleChanger scaler,
            boolean prescaled) {
        super(coords, index, scaler, prescaled);
    }

    /**
     *@param coords
     *@param index
     *@param scaler
     */
    public CorrelationDataPair(double[] coords, int index, ScaleChanger scaler) {
        super(coords, index, scaler);
    }
    
    /**
     * function to compare value of a (scaled) data pair
     *@param valIndex1 index of first value to compare
     *@param valIndex2 index of first value to compare
     *@return 1 if first value > second value, 0 if equal, else -1  
     * @throws Exception
     */
    public int compareValues(int valIndex1, int valIndex2) throws Exception{
        double value1, value2;

        value1 = this.getCoordinate(valIndex1);
        value2 = this.getCoordinate(valIndex2);

        if (value1 > value2) return 1;
        else if (value1==value2) return 0;
        else return -1;
    }

}
