/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * Unit.java
 * 
 * Created on 8 febbraio 2003, 17.53
 *
 */

package it.businesslogic.ireport.util;

/**
 *
 * @author  Administrator
 */
public class Unit {

    public static final double PIXEL = 1.0;
    public static final double INCHES = 72.0;
    public static final double CENTIMETERS = 28.3464;
    public static final double MILLIMETERS = 2.83464;

    /** Holds value of property unitName. */
    private String unitName;
    private String keyName;

    /** Holds value of property conversionValue. */
    private double conversionValue;

    /** Creates a new instance of Unit */
    public Unit(String unitName, double conversionValue) {
        this(unitName, conversionValue, unitName);
    }
    
    public Unit(String unitName, double conversionValue, String keyName) {
        this.unitName = unitName;
        this.conversionValue = conversionValue;
        this.setKeyName(keyName);
    }

    /** Getter for property unitName.
     * @return Value of property unitName.
     *
     */
    public String getUnitName() {
        return this.unitName;
    }

    /** Setter for property unitName.
     * @param unitName New value of property unitName.
     *
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    /** Getter for property conversionValue.
     * @return Value of property conversionValue.
     *
     */
    public double getConversionValue() {
        return this.conversionValue;
    }

    /** Setter for property conversionValue.
     * @param conversionValue New value of property conversionValue.
     *
     */
    public void setConversionValue(double conversionValue) {
        this.conversionValue = conversionValue;
    }

    public static Unit[] getStandardUnits()
    {
        Unit[] units = new Unit[4];

        units[0] = new Unit(it.businesslogic.ireport.util.I18n.getString("pixels", "pixels"),Unit.PIXEL, "pixels");
        units[1] = new Unit(it.businesslogic.ireport.util.I18n.getString("inches", "inches"),Unit.INCHES, "inches");
        units[2] = new Unit("cm",    Unit.CENTIMETERS);
        units[3] = new Unit("mm",    Unit.MILLIMETERS);

        return units;
    }

    public static int getUnitIndex(String unitName)
    {
        Unit[] units = getStandardUnits();
        for (int i=0; i<units.length; ++i)
        {
            if (units[i].getUnitName().equalsIgnoreCase(unitName)) return i;
        }
        return -1;
    }

    public String toString()
    {
        return getUnitName();
    }

    static public double convertPixelsToInches(long pixels)
	{
		return ((double)pixels)/INCHES;
	}

	static public long convertInchesToPixels(double inches)
	{
		return (long)(inches*INCHES);
	}

	static public double convertPixelsToCentimeters(long pixels)
	{
		return ((double)pixels)/CENTIMETERS;
	}

	static public long convertCentimetersToPixels(double centimeters)
	{
		return (long)(centimeters*CENTIMETERS);
	}

	static public double convertPixelsToMillimeters(long pixels)
	{
		return ((double)pixels)/MILLIMETERS;
	}

	static public long convertMillimetersToPixels(double millimeters)
	{
		return (long)(millimeters*CENTIMETERS);
	}

	static public long convertToPixels(double value, double convert)
	{
		return (long)(value*convert);
	}

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
}
