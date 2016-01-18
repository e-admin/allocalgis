/*
 * Created on 23.06.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: ConstantValue.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:06 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/FormulaParsing/Values/ConstantValue.java,v $
 */
package pirolPlugIns.utilities.FormulaParsing.Values;

import com.vividsolutions.jump.feature.Feature;

import pirolPlugIns.utilities.FormulaParsing.FormulaValue;

/**
 * A simple value class that just stores a constant value.
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
public class ConstantValue extends FormulaValue {
    
    protected double value;

    public ConstantValue(double value) {
        super();
        this.value = value;
    }
    
    /**
     *@param feature in this case we don't need the feature...
     *@return the constant value
     */
    public double getValue(Feature feature) {
        return this.value;
    }
    
    /**
     * @inheritDoc
     */
    public boolean isFeatureDependent() {
        return false;
    }
    
    /**
     * @inheritDoc
     */
    public String toString(){
        return "" + this.value;
    }

}
