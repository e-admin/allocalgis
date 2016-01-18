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
 * AxisFormat.java
 * 
 * Created on September 27, 2006, 3:17 PM
 *
 */

package it.businesslogic.ireport.chart;

import it.businesslogic.ireport.IReportFont;

import java.awt.Color;

/**
 *
 * @author gtoffoli
 */
public class AxisFormat {
    
    private java.awt.Color labelColor = null;
    private java.awt.Color tickLabelColor = null;
    private java.awt.Color axisLineColor = null;
    private String tickLabelMask = "";
    private IReportFont labelFont = null;
    private IReportFont tickLabelFont = null;
    
    
    public AxisFormat cloneMe()
    {
        AxisFormat af = new AxisFormat();
        if (labelColor != null) af.setLabelColor( new Color(  labelColor.getRGB()  ));
        if (tickLabelColor != null) af.setTickLabelColor( new Color(  tickLabelColor.getRGB()  ));
        if (axisLineColor != null) af.setAxisLineColor( new Color(  axisLineColor.getRGB()  ));
        af.setTickLabelMask(tickLabelMask);
        if (labelFont != null) af.setLabelFont( (IReportFont)labelFont.clone());
        if (tickLabelFont != null) af.setTickLabelFont( (IReportFont)tickLabelFont.clone());
        
        return af;
    }
    
    /** Creates a new instance of AxisFormat */
    public AxisFormat() {
    }

    public java.awt.Color getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(java.awt.Color labelColor) {
        this.labelColor = labelColor;
    }

    public java.awt.Color getTickLabelColor() {
        return tickLabelColor;
    }

    public void setTickLabelColor(java.awt.Color tickLabelColor) {
        this.tickLabelColor = tickLabelColor;
    }

    public java.awt.Color getAxisLineColor() {
        return axisLineColor;
    }

    public void setAxisLineColor(java.awt.Color axisLineColor) {
        this.axisLineColor = axisLineColor;
    }

    public String getTickLabelMask() {
        return tickLabelMask;
    }

    public void setTickLabelMask(String tickLabelMask) {
        this.tickLabelMask = tickLabelMask;
    }

    public IReportFont getLabelFont() {
        return labelFont;
    }

    public void setLabelFont(IReportFont labelFont) {
        this.labelFont = labelFont;
    }

    public IReportFont getTickLabelFont() {
        return tickLabelFont;
    }

    public void setTickLabelFont(IReportFont tickLabelFont) {
        this.tickLabelFont = tickLabelFont;
    }
    
}
