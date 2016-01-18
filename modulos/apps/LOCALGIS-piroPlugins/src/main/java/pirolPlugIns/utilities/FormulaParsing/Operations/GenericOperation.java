/**
 * GenericOperation.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
