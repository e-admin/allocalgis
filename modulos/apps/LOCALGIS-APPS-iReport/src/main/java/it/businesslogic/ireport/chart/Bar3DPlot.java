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
 * Bar3DPlot.java
 * 
 * Created on 16 agosto 2005, 10.19
 *
 */

package it.businesslogic.ireport.chart;

/**
 *
 * @author Administrator
 */
public class Bar3DPlot extends Plot{
    
    private boolean showLabels = false;
    private double xOffset = org.jfree.chart.renderer.category.BarRenderer3D.DEFAULT_X_OFFSET;
    private double yOffset = org.jfree.chart.renderer.category.BarRenderer3D.DEFAULT_Y_OFFSET;
    
    private String categoryAxisLabelExpression = "";
    private String valueAxisLabelExpression = "";
    
    private AxisFormat categoryAxisFormat = new AxisFormat();
    private AxisFormat valueAxisFormat = new AxisFormat();
    
    /** Creates a new instance of PiePlot */
    public Bar3DPlot() {
    }

    public boolean isShowLabels() {
        return showLabels;
    }

    public void setShowLabels(boolean showLabels) {
        this.showLabels = showLabels;
    }

    public String getCategoryAxisLabelExpression() {
        return categoryAxisLabelExpression;
    }

    public void setCategoryAxisLabelExpression(String categoryAxisLabelExpression) {
        this.categoryAxisLabelExpression = categoryAxisLabelExpression;
    }

    public String getValueAxisLabelExpression() {
        return valueAxisLabelExpression;
    }

    public void setValueAxisLabelExpression(String valueAxisLabelExpression) {
        this.valueAxisLabelExpression = valueAxisLabelExpression;
    }

    public double getXOffset() {
        return xOffset;
    }

    public void setXOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    public double getYOffset() {
        return yOffset;
    }

    public void setYOffset(double yOffset) {
        this.yOffset = yOffset;
    }
    
    public Plot cloneMe()
    {
        Bar3DPlot obj = new Bar3DPlot();
        copyBasePlot(obj);
        obj.setXOffset( this.getXOffset());
        obj.setYOffset( this.getYOffset());
        obj.setCategoryAxisLabelExpression( this.getCategoryAxisLabelExpression());
        obj.setValueAxisLabelExpression( this.getValueAxisLabelExpression());
        obj.setShowLabels( this.isShowLabels());
        obj.setCategoryAxisFormat( getCategoryAxisFormat().cloneMe());
        obj.setValueAxisFormat( getValueAxisFormat().cloneMe());
        
        return obj;
    }

    public AxisFormat getCategoryAxisFormat() {
        return categoryAxisFormat;
    }

    public void setCategoryAxisFormat(AxisFormat categoryAxisFormat) {
        this.categoryAxisFormat = categoryAxisFormat;
    }

    public AxisFormat getValueAxisFormat() {
        return valueAxisFormat;
    }

    public void setValueAxisFormat(AxisFormat valueAxisFormat) {
        this.valueAxisFormat = valueAxisFormat;
    }
    
}
