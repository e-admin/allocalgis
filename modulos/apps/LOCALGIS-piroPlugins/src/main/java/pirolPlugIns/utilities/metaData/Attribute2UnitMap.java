/**
 * Attribute2UnitMap.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 13.07.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: Attribute2UnitMap.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:01 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/metaData/Attribute2UnitMap.java,v $
 */
package pirolPlugIns.utilities.metaData;

import java.util.Collection;
import java.util.HashMap;

/**
 * Meta information object to store the units for the attributes in a layer.
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
public class Attribute2UnitMap {

    protected HashMap attribute2unit = new HashMap();
    protected boolean useHTMLLineBreaks = false;
    /**
     * key to be used in the meta information map
     */
    protected static final String KEY_ATTRIBUTE2UNIT = "attribute2unit";
    
    public void clear() {
        attribute2unit.clear();
    }
    public boolean containsAttribute(String attributeName) {
        return attribute2unit.containsKey(attributeName);
    }
    public boolean containsUnit(String unitString) {
        return attribute2unit.containsValue(unitString);
    }
    public String getUnitString(String attributeName) {
        return (String)attribute2unit.get(attributeName);
    }
    public String put(String attributeName, String unitString) {
        return (String)attribute2unit.put(attributeName, unitString);
    }
    public String removeAttribute(String attributeName) {
        return (String)attribute2unit.remove(attributeName);
    }
    public Collection values() {
        return attribute2unit.values();
    }
    public String toString() {
        
        Object[] keys = this.attribute2unit.keySet().toArray();
        String result = this.getClass().getName() + (useHTMLLineBreaks?":<br>\n":":\n");
        
        for (int i=0; i<this.attribute2unit.size(); i++){
            result += keys[i].toString() + " - "+ this.attribute2unit.get(keys[i]).toString() + (useHTMLLineBreaks?"<br>\n":"\n");
        }
        
        return result;
    }
    /**
     * tells you if @link{Attribute2UnitMap#toString()} uses &lt;br&gt; or just backslash+n to begin a new line.
     *@return value of useHTMLLineBreaks
     */
    public boolean isUseHTMLLineBreaks() {
        return useHTMLLineBreaks;
    }
    /**
     * Controlls if @link{Attribute2UnitMap#toString()} uses &lt;br&gt; or just backslash+n to begin a new line.
     */
    public void setUseHTMLLineBreaks(boolean useHTMLLineBreaks) {
        this.useHTMLLineBreaks = useHTMLLineBreaks;
    }
    
    /**
     * for java2xml
     *@return attribute2unit map
     */
    public HashMap getAttribute2unit() {
        return attribute2unit;
    }
    /**
     * for java2xml
     *@param attribute2unit map
     */
    public void setAttribute2unit(HashMap attribute2unit) {
        this.attribute2unit = attribute2unit;
    }

}
