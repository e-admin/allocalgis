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
 * ThermometerPlot.java
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
public class ThermometerPlot extends Plot{
    

    private String valueLocation = "bulb";
    private boolean showValueLines = false;
    private java.awt.Color mercuryColor = null;
    
    private ValueDisplay valueDisplay = new ValueDisplay();
    
    private DataRange dataRange = new DataRange();
    private DataRange lowRange = new DataRange();
    private DataRange mediumRange = new DataRange();
    private DataRange highRange = new DataRange();
    
    
    
    /** Creates a new instance of PiePlot */
    public ThermometerPlot() {
    }
  
   
    
    public Plot cloneMe()
    {
        ThermometerPlot obj = new ThermometerPlot();
        copyBasePlot(obj);
        obj.setValueLocation( new String(getValueLocation()) );
        obj.setShowValueLines( isShowValueLines() );
        if (getMercuryColor() != null) obj.setMercuryColor( new Color( getMercuryColor().getRGB())  );

        obj.setValueDisplay( getValueDisplay().cloneMe() );

        obj.setDataRange(  getDataRange().cloneMe() );
        obj.setLowRange(  getLowRange().cloneMe() );
        obj.setMediumRange(  getMediumRange().cloneMe() );
        obj.setHighRange(  getHighRange().cloneMe() );

        
        return obj;
    }

    public String getValueLocation() {
        return valueLocation;
    }

    public void setValueLocation(String valueLocation) {
        this.valueLocation = valueLocation;
    }

    public boolean isShowValueLines() {
        return showValueLines;
    }

    public void setShowValueLines(boolean showValueLines) {
        this.showValueLines = showValueLines;
    }

    public java.awt.Color getMercuryColor() {
        return mercuryColor;
    }

    public void setMercuryColor(java.awt.Color mercuryColor) {
        this.mercuryColor = mercuryColor;
    }

    public ValueDisplay getValueDisplay() {
        return valueDisplay;
    }

    public void setValueDisplay(ValueDisplay valueDisplay) {
        this.valueDisplay = valueDisplay;
    }

    public DataRange getDataRange() {
        return dataRange;
    }

    public void setDataRange(DataRange dataRange) {
        this.dataRange = dataRange;
    }

    public DataRange getLowRange() {
        return lowRange;
    }

    public void setLowRange(DataRange lowRange) {
        this.lowRange = lowRange;
    }

    public DataRange getMediumRange() {
        return mediumRange;
    }

    public void setMediumRange(DataRange mediumRange) {
        this.mediumRange = mediumRange;
    }

    public DataRange getHighRange() {
        return highRange;
    }

    public void setHighRange(DataRange highRange) {
        this.highRange = highRange;
    }

   
   
}
