/**
 * ColoredRange.java
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
 *  $RCSfile: ColoredRange.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:04 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/colors/Value/ColoredRange.java,v $
 */
package pirolPlugIns.utilities.colors.Value;

import pirolPlugIns.utilities.ObjectComparator;


/**
 * Represents a range of numeric values, that can have a color.
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
public class ColoredRange extends ColoredValue {
    
    protected double minValue, maxValue;
    protected boolean includingMin = true, includingMax = true;
    
    public ColoredRange(double minValue, boolean includeMin, double maxValue, boolean includeMax, int index) {
        super(index);
        this.minValue = minValue;
        this.includingMin = includeMin;
        this.maxValue = maxValue;
        this.includingMax = includeMax;
        
        if (this.minValue > this.maxValue)
            throw new IllegalArgumentException("invalid range, min>max: " + this.minValue + ">" + this.maxValue);
    }
    
    /**
     * for storing/reading to/from a file
     *@param index index
     */
    protected ColoredRange(int index) {
        super(index);
    }

    public boolean isIncludingMax() {
        return includingMax;
    }

    public void setIncludingMax(boolean includingMax) {
        this.includingMax = includingMax;
        this.fireValueChangeEvent();
    }

    public boolean isIncludingMin() {
        return includingMin;
    }

    public void setIncludingMin(boolean includingMin) {
        this.includingMin = includingMin;
        this.fireValueChangeEvent();
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        if (maxValue != this.maxValue){
            if (maxValue < this.minValue)
                this.minValue = maxValue;
            this.maxValue = maxValue;
            this.fireValueChangeEvent();
        }
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        if (minValue != this.minValue){
            if (minValue > this.maxValue)
                this.maxValue = minValue;
            this.minValue = minValue;
            this.fireValueChangeEvent();
        }
    }



    /**
     *@inheritDoc
     */
    public boolean isResponsibleForValue(Object value) {
        double dValue = ObjectComparator.getDoubleValue(value);
        return this.isResponsibleForValue(dValue);
    }

    /**
     *@inheritDoc
     */
    public boolean isResponsibleForValue(double value) {
        boolean minOk = (this.includingMin)?value>=this.minValue:value>this.minValue;
        boolean maxOk = (this.includingMax)?value<=this.maxValue:value<this.maxValue;
        return minOk && maxOk;
    }

    /**
     *@inheritDoc
     */
    public int compareTo(Object anOtherColoredValue) {
        int thisIndex = this.getIndex();
        int theOtherIndex = ((ColoredValue)anOtherColoredValue).getIndex();
        return new Integer(thisIndex).compareTo(new Integer(theOtherIndex));
    }

    /**
     *@inheritDoc
     */
    public Comparable getCompareValue() {
        return new Double((this.minValue+this.maxValue)/2.0);
    }

    /**
     *@inheritDoc
     */
    public String toString() {
        return (this.includingMin?"[":"(") + this.doubleToString(this.minValue, 5) + " - " + this.doubleToString(this.maxValue,5) + (this.includingMax?"]":")");
    }
    
    /**
     * Creates a String representing a given double with a max. number of <code>postkomma</code> digits after the decimal separator.
     *@param toString double to be represented
     *@param postkomma numbers of postkomma digits in the String
     *@return String representing a given double.
     */
    protected String doubleToString(double toString, int postkomma){
        String representation = Double.toString(toString);
        int pointPos = representation.indexOf(".");
        
        if (pointPos > -1){
            pointPos += 1;
            int EPos = representation.indexOf("E");
            
            if (EPos < 0) EPos = representation.length();
            
            if (EPos > pointPos + postkomma){
                representation = representation.substring(0, pointPos + postkomma) +  representation.substring(EPos);
            }
        }
        
        return representation;
    }
    
    public boolean overlapps(ColoredRange theOtherRange){
        ColoredRange lowerRange = this.getIndex() < theOtherRange.getIndex() ? this : theOtherRange;
        ColoredRange upperRange = this.getIndex() > theOtherRange.getIndex() ? this : theOtherRange;
        // TODO: take care of inculdingMin/-Max!
        
        return (lowerRange.getMaxValue() > upperRange.getMinValue()); 
    }

    /**
     *@inheritDoc
     */
    protected String getParseableValueString() {
        return "" + this.minValue + ColoredValue.valueSeparatorString + this.maxValue + ColoredValue.valueSeparatorString + this.includingMin + ColoredValue.valueSeparatorString + this.includingMax;
    }

    /**
     *@inheritDoc
     */
    protected void readParseableValueString(String valueString) {
        String[] fields = valueString.split(ColoredValue.valueSeparatorString);
        this.minValue = Double.parseDouble(fields[0]);
        this.maxValue = Double.parseDouble(fields[1]);
        this.includingMin = Boolean.valueOf(fields[2]).booleanValue();
        this.includingMax = Boolean.valueOf(fields[3]).booleanValue();
        
    }

    /**
     *@inheritDoc
     */
    public boolean isNumeric() {
        return true;
    }
    
    

}
