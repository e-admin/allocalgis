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
