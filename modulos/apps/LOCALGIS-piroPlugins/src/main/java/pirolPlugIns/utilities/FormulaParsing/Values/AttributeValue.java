/**
 * AttributeValue.java
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
 *  $RCSfile: AttributeValue.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:06 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/FormulaParsing/Values/AttributeValue.java,v $
 */
package pirolPlugIns.utilities.FormulaParsing.Values;

import pirolPlugIns.utilities.ObjectComparator;
import pirolPlugIns.utilities.FormulaParsing.FormulaValue;

import com.vividsolutions.jump.feature.Feature;

/**
 * Class to extract integer or double values (as double) out of the given Feature...
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
public class AttributeValue extends FormulaValue {
    
    protected String attributeName = "";
    protected int attributeIndex = -1;

    public AttributeValue(String attributeName) {
        super();
        this.attributeName = attributeName;
    }
    
    /**
     * Gets the value (as a double) of the specified attribute out of the given feature.
     *@param feature the Feature we want to get the attribute value from
     *@return value of the specified attribute
     */
    public double getValue(Feature feature) {
        if (this.attributeIndex < 0){
            this.attributeIndex = feature.getSchema().getAttributeIndex(this.attributeName);
        }
        return ObjectComparator.getDoubleValue(feature.getAttribute(this.attributeIndex));
    }

    /**
     * @inheritDoc
     */
    public boolean isFeatureDependent() {
        return true;
    }
    
    /**
     * @inheritDoc
     */
    public String toString(){
        return this.attributeName;
    }

}
