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
 * MeterPlot.java
 * 
 * Created on 16 agosto 2005, 10.19
 *
 */

package it.businesslogic.ireport.chart;

import java.awt.Color;

/**
 *
 * @author Administrator
 */
public class MeterPlot extends Plot{
    
    private String shape = "pie";
    private int angle = 180;
    private String units = "";
    private double tickInterval = 10.0;
    private java.awt.Color meterColor = null;
    private java.awt.Color needleColor = null;
    private java.awt.Color tickColor = null;
    
    private java.util.List meterIntervals = new java.util.ArrayList();
    
    private ValueDisplay valueDisplay = new ValueDisplay();
    
    private DataRange dataRange = new DataRange();
    
    
    
    /** Creates a new instance of PiePlot */
    public MeterPlot() {
    }
  
   
    
    public Plot cloneMe()
    {
        MeterPlot obj = new MeterPlot();
        copyBasePlot(obj);
        obj.setShape( new String(getShape()) );
        obj.setAngle( getAngle() );
        obj.setUnits( new String(getUnits()) );
        obj.setTickInterval( getTickInterval() );
        if (getTickColor() != null) obj.setTickColor( new Color( getTickColor().getRGB())  );
        if (getNeedleColor() != null) obj.setNeedleColor( new Color( getNeedleColor().getRGB())  );
        if (getMeterColor() != null) obj.setMeterColor( new Color( getMeterColor().getRGB())  );
        obj.setValueDisplay( getValueDisplay().cloneMe() );
        obj.setDataRange(  getDataRange().cloneMe() );
        
        for (int i=0; i<getMeterIntervals().size(); ++i)
        {
            obj.getMeterIntervals().add( ((MeterInterval)getMeterIntervals().get(i)).cloneMe()  );
        }
        
        return obj;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public double getTickInterval() {
        return tickInterval;
    }

    public void setTickInterval(double tickInterval) {
        this.tickInterval = tickInterval;
    }

    public java.awt.Color getMeterColor() {
        return meterColor;
    }

    public void setMeterColor(java.awt.Color meterColor) {
        this.meterColor = meterColor;
    }

    public java.awt.Color getNeedleColor() {
        return needleColor;
    }

    public void setNeedleColor(java.awt.Color needleColor) {
        this.needleColor = needleColor;
    }

    public java.awt.Color getTickColor() {
        return tickColor;
    }

    public void setTickColor(java.awt.Color tickColor) {
        this.tickColor = tickColor;
    }

    public ValueDisplay getValueDisplay() {
        return valueDisplay;
    }

    public void setValueDisplay(ValueDisplay valueDisplay) {
        this.valueDisplay = valueDisplay;
    }

    public java.util.List getMeterIntervals() {
        return meterIntervals;
    }

    public void setMeterIntervals(java.util.List meterIntervals) {
        this.meterIntervals = meterIntervals;
    }

    public DataRange getDataRange() {
        return dataRange;
    }

    public void setDataRange(DataRange dataRange) {
        this.dataRange = dataRange;
    }

   
}
