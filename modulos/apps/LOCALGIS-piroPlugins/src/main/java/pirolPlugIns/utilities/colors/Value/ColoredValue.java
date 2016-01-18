/**
 * ColoredValue.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 14.07.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: ColoredValue.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:04 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/colors/Value/ColoredValue.java,v $
 */
package pirolPlugIns.utilities.colors.Value;

import java.util.ArrayList;

import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

/**
 * A generic value to be mapped to a color.
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
public abstract class ColoredValue implements Comparable {
    
    protected static PersonalLogger logger = new PersonalLogger(DebugUserIds.USER_Ole);
    protected ArrayList valueChangeListener = new ArrayList();
    protected int index = -1;
    protected static String valueSeparatorString = ";";
    
    public ColoredValue(int index){
        this.setIndex(index);
    }
    
    /**
     * Decides if the color connected to this value is to be used for the given value.
     *@return true if the value matches the value (or range, etc.) represented by this object
     */
    public abstract boolean isResponsibleForValue(Object value);
    
    /**
     * Decides if the color connected to this value is to be used for the given value.
     *@return true if the value matches the value (or range, etc.) represented by this object
     */
    public abstract boolean isResponsibleForValue(double value);
    
    /**
     *@inheritDoc
     */
    public int compareTo(Object anOtherColoredValue) {
        int thisIndex = this.getIndex();
        int theOtherIndex = ((ColoredValue)anOtherColoredValue).getIndex();
        return new Integer(thisIndex).compareTo(new Integer(theOtherIndex));
    }
    
    /**
     *@return a value that represents "the" value of this object (used to compare it to other ColoredValues) - must be Comparable!.
     */
    public abstract Comparable getCompareValue();
    
    /**
     * @inheritDoc
     */
    public abstract String toString();
    
    /**
     * 
     *@param coloredValueAsString
     *@return the ColoredValue represented by the given String or null, if the String can not be parsed
     *@see ColoredValue#toParseableString()
     */
    public final static ColoredValue readParseableString(String coloredValueAsString){
        try {
            String valueClassName = coloredValueAsString.substring(0, coloredValueAsString.indexOf("("));
            String variablesSection = coloredValueAsString.substring(coloredValueAsString.indexOf("(")+1, coloredValueAsString.indexOf(")"));
            String valueSection = coloredValueAsString.substring(coloredValueAsString.indexOf("[")+1, coloredValueAsString.indexOf("]"));
            
            String indexString = variablesSection.split(",")[0];
            int ind = Integer.parseInt(indexString);
            
            ColoredValue value = null;
            
            if (valueClassName.equals(SingleValue.class.getName())){
                value = new SingleValue(ind);
            } else if (valueClassName.equals(ColoredRange.class.getName())){
                value = new ColoredRange(ind);
            }
            
            value.readParseableValueString(valueSection);
            
            return value;
        } catch (NumberFormatException e) {
            logger.printWarning("can not parse number in ColoredValue: " + coloredValueAsString + ", " + e.getMessage());
        } catch (RuntimeException e) {
            logger.printWarning("can not parse ColoredValue: " + coloredValueAsString + ", " + e.getMessage());
        }
        return null;
    }
    
    /**
     * To enable storing Attribute2ValueMaps to files and reading those files, each ColoredValue needs to be able, to create a String
     * that represents it (including the classname, the index and the value(s)).  
     *@return the String representing this object
     *@see ColoredValue#readParseableValueString(String)
     *@see ColoredValue#getParseableValueString()
     */
    public final String toParseableString(){
        return this.getClass().getName() + "(" + this.index + ", ["+ this.getParseableValueString() +"])";
    }
    
    /**
     * To enable storing Attribute2ValueMaps to files and reading those files, each ColoredValue needs to be able, to create a String
     * that represents it (including the classname, the index and the value(s)). This is the value part of that String, it will be 
     * included in the representation String.
     *@return the value portion of the String representing this object
     *@see ColoredValue#readParseableValueString(String)
     */
    protected abstract String getParseableValueString();
    
    /**
     * Read back the parseable value String.
     *@param valueString the value portion of the String representing this object
     *@see ColoredValue#getParseableValueString()
     */
    protected abstract void readParseableValueString(String valueString) throws RuntimeException;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    public void addValueChangeListener(ValueChangeListener listener){
        this.valueChangeListener.add(listener);        
    }
    
    public void removeValueChangeListener(ValueChangeListener listener){
        this.valueChangeListener.remove(listener);        
    }
    
    protected void fireValueChangeEvent(){
        ValueChangeListener[] listener = (ValueChangeListener[])this.valueChangeListener.toArray(new ValueChangeListener[0]);
        for (int i=0; i<listener.length; i++){
            listener[i].valueChanged(this);
        }
    }
    
    public abstract boolean isNumeric();
    
}
