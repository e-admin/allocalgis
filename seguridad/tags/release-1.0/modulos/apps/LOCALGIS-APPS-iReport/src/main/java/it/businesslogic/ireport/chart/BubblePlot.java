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
 * BubblePlot.java
 * 
 * Created on 16 agosto 2005, 10.19
 *
 */

package it.businesslogic.ireport.chart;

/**
 *
 * @author Administrator
 */
public class BubblePlot extends Plot{
    
    private String scaleType = "RangeAxis";
    
    private String xAxisLabelExpression = "";
    private String yAxisLabelExpression = "";
    
    private AxisFormat xAxisFormat = new AxisFormat();
    private AxisFormat yAxisFormat = new AxisFormat();
    
    /** Creates a new instance of PiePlot */
    public BubblePlot() {
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

    public String getScaleType() {
        return scaleType;
    }

    public void setScaleType(String scaleType) {
        this.scaleType = scaleType;
    }
    
    public Plot cloneMe()
    {
        BubblePlot obj = new BubblePlot();
        copyBasePlot(obj);
        obj.setScaleType( this.getScaleType());
        obj.setYAxisLabelExpression( this.getYAxisLabelExpression());
        obj.setXAxisLabelExpression( this.getXAxisLabelExpression());
        
        obj.setXAxisFormat( getXAxisFormat().cloneMe());
        obj.setYAxisFormat( getYAxisFormat().cloneMe());
       
        return obj;
    }

    public AxisFormat getXAxisFormat() {
        return xAxisFormat;
    }

    public void setXAxisFormat(AxisFormat xAxisFormat) {
        this.xAxisFormat = xAxisFormat;
    }

    public AxisFormat getYAxisFormat() {
        return yAxisFormat;
    }

    public void setYAxisFormat(AxisFormat yAxisFormat) {
        this.yAxisFormat = yAxisFormat;
    }
    
}
