/*
 * Created on 29.06.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: SquareRootOperation.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:47 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/FormulaParsing/Operations/SquareRootOperation.java,v $
 */
package pirolPlugIns.utilities.FormulaParsing.Operations;

import com.vividsolutions.jump.feature.Feature;

import pirolPlugIns.utilities.FormulaParsing.FormulaValue;

/**
 * Class that represents a square root operation on the given value.
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
public class SquareRootOperation extends FormulaValue {
    
    protected FormulaValue value = null;

    public SquareRootOperation(FormulaValue value) {
        super();
        this.value = value;
    }
    /**
     *@param feature
     *@return the square root of the given value
     */
    public double getValue(Feature feature) {
        return Math.sqrt(this.value.getValue(feature));
    }

    /**
     *@inheritDoc
     */
    public boolean isFeatureDependent() {
        return this.value.isFeatureDependent();
    }
    
    /**
     *@inheritDoc
     */
    public String toString() {
        return "Math.sqrt("+ this.value.toString() +")";
    }

}
