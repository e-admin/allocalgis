/*
 * Created on 23.06.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: GenericOperation.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:47 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/FormulaParsing/Operations/GenericOperation.java,v $
 */
package pirolPlugIns.utilities.FormulaParsing.Operations;

import pirolPlugIns.utilities.FormulaParsing.FormulaValue;

/**
 * Base class for mathmatic operations like division, addition, etc.
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
public abstract class GenericOperation extends FormulaValue {
    
    protected FormulaValue value1=null, value2=null;
    protected String opString = "#";

    /**
     * Sets the value, that will be operated on.
     *@param value1
     *@param value2
     */
    public GenericOperation(FormulaValue value1, FormulaValue value2) {
        super();
        this.value1 = value1;
        this.value2 = value2;
    }
    
    /**
     * @inheritDoc
     */
    public boolean isFeatureDependent() {
        return this.value1.isFeatureDependent() || this.value2.isFeatureDependent();
    }
    
    /**
     * @inheritDoc
     */
    public String toString(){
        return "(" + this.value1.toString() + ") " + this.opString + " ("+this.value2+")";
    }
    

}
