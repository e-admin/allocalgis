/**
 * SquareRootOperation.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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

import pirolPlugIns.utilities.FormulaParsing.FormulaValue;

import com.vividsolutions.jump.feature.Feature;

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
