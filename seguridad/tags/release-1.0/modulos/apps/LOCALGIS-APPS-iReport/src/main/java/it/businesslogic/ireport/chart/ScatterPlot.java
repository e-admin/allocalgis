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
 * ScatterPlot.java
 * 
 * Created on 16 agosto 2005, 10.19
 *
 */

package it.businesslogic.ireport.chart;

/**
 *
 * @author Administrator
 */
public class ScatterPlot extends Plot{
    
    private boolean showLines = true;
    private boolean showShapes = true;
    
    private String xAxisLabelExpression = "";
    private String yAxisLabelExpression = "";
    
    private AxisFormat xAxisFormat = new AxisFormat();
    private AxisFormat yAxisFormat = new AxisFormat();
    
    /** Creates a new instance of PiePlot */
    public ScatterPlot() {
    }
  

    public boolean isShowLines() {
        return showLines;
    }

    public void setShowLines(boolean showLines) {
        this.showLines = showLines;
    }

    public boolean isShowShapes() {
        return showShapes;
    }

    public void setShowShapes(boolean showShapes) {
        this.showShapes = showShapes;
    }

    public String getXAxisLabelExpression() {
        return xAxisLabelExpression;
    }

    public void setXAxisLabelExpression(String xAxisLabelExpression) {
        this.xAxisLabelExpression = xAxisLabelExpression;
    }

    public String getYAxisLabelExpression() {
        return yAxisLabelExpression;
    }

    public void setYAxisLabelExpression(String yAxisLabelExpression) {
        this.yAxisLabelExpression = yAxisLabelExpression;
    }
    
    public Plot cloneMe()
    {
        ScatterPlot obj = new ScatterPlot();
        copyBasePlot(obj);
        obj.setShowLines( this.isShowLines());
        obj.setShowShapes( this.isShowShapes());
        obj.setXAxisLabelExpression( this.getXAxisLabelExpression());
        obj.setYAxisLabelExpression( this.getYAxisLabelExpression());
        
        obj.setXAxisFormat( getXAxisFormat().cloneMe());
        obj.setYAxisFormat( getYAxisFormat().cloneMe());
       
        return obj;
    }

    public AxisFormat getYAxisFormat() {
        return yAxisFormat;
    }

    public void setYAxisFormat(AxisFormat yAxisFormat) {
        this.yAxisFormat = yAxisFormat;
    }

    public AxisFormat getXAxisFormat() {
        return xAxisFormat;
    }

    public void setXAxisFormat(AxisFormat xAxisFormat) {
        this.xAxisFormat = xAxisFormat;
    }
    
}
