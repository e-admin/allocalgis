/*
 * Created on 23.06.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: FormulaValue.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:06 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/FormulaParsing/FormulaValue.java,v $
 */
package pirolPlugIns.utilities.FormulaParsing;

import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

import com.vividsolutions.jump.feature.Feature;

/**
 * Base class for each sub-formula or value of a formula, since we don't want to parse the formula again and again for each value...
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
public abstract class FormulaValue {
    
    protected PersonalLogger logger = new PersonalLogger(DebugUserIds.USER_Ole);

    /**
     * Returns the value (as a double) of this part of the formula.
     * It may be the rsult of a sub-formula, a feature-specific attribute value or just a constant value...
     * Since the value may depend on a feature, we give the feature to the method to get a unified interface...
     *@return value of this part of the formula
     */
    public abstract double getValue(Feature feature);
    
    /**
     * Helps to determine, if the value depends on a feature's attribute value. 
     * @return true, if the value depends on a feature
     */
    public abstract boolean isFeatureDependent();
    
    /**
     * @inheritDoc
     */
    public String toString(){
        return "";
    }

}
