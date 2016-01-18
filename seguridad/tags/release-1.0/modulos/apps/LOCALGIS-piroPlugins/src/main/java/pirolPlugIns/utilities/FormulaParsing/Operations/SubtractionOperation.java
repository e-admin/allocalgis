/*
 * Created on 23.06.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: SubtractionOperation.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:47 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/FormulaParsing/Operations/SubtractionOperation.java,v $
 */
package pirolPlugIns.utilities.FormulaParsing.Operations;

import pirolPlugIns.utilities.FormulaParsing.FormulaValue;

import com.vividsolutions.jump.feature.Feature;

/**
 * Class to handle subtractions within a formula.
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
public class SubtractionOperation extends GenericOperation {

    /**
     *@inheritDoc
     */
    public SubtractionOperation(FormulaValue value1, FormulaValue value2) {
        super(value1, value2);
        this.opString = "-";
    }

    /**
     * Returns the subtracted values of the sub-values or sub-operations of this operation
     *@param feature
     *@return subtracted values of the sub-values or sub-operations
     */
    public double getValue(Feature feature) {
        return this.value1.getValue(feature) - this.value2.getValue(feature);
    }

}
