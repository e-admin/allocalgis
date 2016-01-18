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
 * BarPlot.java
 * 
 * Created on 16 agosto 2005, 10.19
 *
 */

package it.businesslogic.ireport.chart;

/**
 *
 * @author Administrator
 */
public class BarPlot extends Plot{
    
    private boolean showLabels = false;
    private boolean showTickMarks = true;
    private boolean showTickLabels = true;
    
    private String categoryAxisLabelExpression = "";
    private String valueAxisLabelExpression = "";
    
    private AxisFormat categoryAxisFormat = new AxisFormat();
    private AxisFormat valueAxisFormat = new AxisFormat();
    
    /** Creates a new instance of PiePlot */
    public BarPlot() {
    }

    public boolean isShowLabels() {
        return showLabels;
    }

    public void setShowLabels(boolean showLabels) {
        this.showLabels = showLabels;
    }

    public boolean isShowTickMarks() {
        return showTickMarks;
    }

    public void setShowTickMarks(boolean showTickMarks) {
        this.showTickMarks = showTickMarks;
    }

    public boolean isShowTickLabels() {
        return showTickLabels;
    }

    public void setShowTickLabels(boolean showTickLabels) {
        this.showTickLabels = showTickLabels;
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
    
     public Plot cloneMe()
    {
        BarPlot obj = new BarPlot();
        copyBasePlot(obj);
        obj.setShowLabels( this.isShowLabels());
        obj.setShowTickLabels( this.isShowTickLabels());
        obj.setShowTickMarks( this.isShowTickMarks());
        obj.setCategoryAxisLabelExpression( this.getCategoryAxisLabelExpression());
        obj.setValueAxisLabelExpression( this.getValueAxisLabelExpression());
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
