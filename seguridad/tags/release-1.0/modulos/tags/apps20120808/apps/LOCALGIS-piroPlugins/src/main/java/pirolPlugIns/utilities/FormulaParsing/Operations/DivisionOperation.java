/*
 * Created on 23.06.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: DivisionOperation.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:47 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/FormulaParsing/Operations/DivisionOperation.java,v $
 */
package pirolPlugIns.utilities.FormulaParsing.Operations;

import com.vividsolutions.jump.feature.Feature;

import pirolPlugIns.utilities.FormulaParsing.FormulaValue;

/**
 * Class to handle divisions within a formula.
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
public class DivisionOperation extends GenericOperation {

    /**
     *@inheritDoc
     */
    public DivisionOperation(FormulaValue value1, FormulaValue value2) {
        super(value1, value2);
        this.opString = "/";
    }
    
    /**
     * Returns the divided values of the sub-values or sub-operations of this operation
     *@param feature
     *@return divided values of the sub-values or sub-operations
     */
    public double getValue(Feature feature) {
        return this.value1.getValue(feature) / this.value2.getValue(feature);
    }

}
