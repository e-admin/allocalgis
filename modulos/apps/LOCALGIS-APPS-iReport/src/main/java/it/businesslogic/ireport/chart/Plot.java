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
 * Plot.java
 * 
 * Created on 8 luglio 2005, 17.55
 *
 */

package it.businesslogic.ireport.chart;

/**
 *
 * @author Administrator
 */
public class Plot {
    
    private java.awt.Color backcolor = null;
    private String orientation = "Vertical";
    private double backgroundAlpha = 1;
    private double foregroundAlpha = 1;
    private double labelRotation = 0;
    private java.util.List seriesColors = new java.util.ArrayList();
    
    
    /** Creates a new instance of Plot */
    public Plot() {
    }

    public java.awt.Color getBackcolor() {
        return backcolor;
    }

    public void setBackcolor(java.awt.Color backcolor) {
        this.backcolor = backcolor;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public double getBackgroundAlpha() {
        return backgroundAlpha;
    }

    public void setBackgroundAlpha(double backgroundAlpha) {
        this.backgroundAlpha = backgroundAlpha;
    }

    public double getForegroundAlpha() {
        return foregroundAlpha;
    }

    public void setForegroundAlpha(double foregroundAlpha) {
        this.foregroundAlpha = foregroundAlpha;
    }
    
    public Plot cloneMe()
    {
        return new Plot();
    }
    
    public void copyBasePlot(Plot plot)
    {
        if (getBackcolor() != null)  plot.setBackcolor(new java.awt.Color( getBackcolor().getRGB()) );
        plot.setForegroundAlpha( getForegroundAlpha() );
        plot.setBackgroundAlpha( getBackgroundAlpha() );
        plot.setOrientation( new String(getOrientation()) );
    }

    public double getLabelRotation() {
        return labelRotation;
    }

    public void setLabelRotation(double labelRotation) {
        this.labelRotation = labelRotation;
    }

    public java.util.List getSeriesColors() {
        return seriesColors;
    }

    public void setSeriesColors(java.util.List seriesColors) {
        this.seriesColors = seriesColors;
    }
    
}
