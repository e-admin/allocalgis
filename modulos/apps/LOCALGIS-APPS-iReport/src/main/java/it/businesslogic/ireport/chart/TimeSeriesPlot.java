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
 * TimeSeriesPlot.java
 * 
 * Created on 16 agosto 2005, 10.19
 *
 */

package it.businesslogic.ireport.chart;

/**
 *
 * @author Administrator
 */
public class TimeSeriesPlot extends Plot{
    
    private boolean showLines = true;
    private boolean showShapes = true;
    
    private String timeAxisLabelExpression = "";
    private String valueAxisLabelExpression = "";
    
    private AxisFormat timeAxisFormat = new AxisFormat();
    private AxisFormat valueAxisFormat = new AxisFormat();
    
    /** Creates a new instance of PiePlot */
    public TimeSeriesPlot() {
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

    public String getTimeAxisLabelExpression() {
        return timeAxisLabelExpression;
    }

    public void setTimeAxisLabelExpression(String timeAxisLabelExpression) {
        this.timeAxisLabelExpression = timeAxisLabelExpression;
    }

    public String getValueAxisLabelExpression() {
        return valueAxisLabelExpression;
    }

    public void setValueAxisLabelExpression(String valueAxisLabelExpression) {
        this.valueAxisLabelExpression = valueAxisLabelExpression;
    }
    
    public Plot cloneMe()
    {
        TimeSeriesPlot obj = new TimeSeriesPlot();
        copyBasePlot(obj);
        obj.setShowLines( this.isShowLines());
        obj.setShowShapes( this.isShowShapes());
        obj.setTimeAxisLabelExpression( this.getTimeAxisLabelExpression());
        obj.setValueAxisLabelExpression( this.getValueAxisLabelExpression());
        
        obj.setTimeAxisFormat( getTimeAxisFormat().cloneMe());
        obj.setValueAxisFormat( getValueAxisFormat().cloneMe());
       
        return obj;
    }

    public AxisFormat getTimeAxisFormat() {
        return timeAxisFormat;
    }

    public void setTimeAxisFormat(AxisFormat timeAxisFormat) {
        this.timeAxisFormat = timeAxisFormat;
    }

    public AxisFormat getValueAxisFormat() {
        return valueAxisFormat;
    }

    public void setValueAxisFormat(AxisFormat valueAxisFormat) {
        this.valueAxisFormat = valueAxisFormat;
    }
}
