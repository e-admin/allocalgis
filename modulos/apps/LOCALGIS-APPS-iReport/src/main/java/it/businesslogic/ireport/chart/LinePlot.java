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
 * LinePlot.java
 * 
 * Created on 16 agosto 2005, 10.19
 *
 */

package it.businesslogic.ireport.chart;

/**
 *
 * @author Administrator
 */
public class LinePlot extends Plot{
    
    private boolean showLines = true;
    private boolean showShapes = true;
    
    private String categoryAxisLabelExpression = "";
    private String valueAxisLabelExpression = "";
    
    private AxisFormat categoryAxisFormat = new AxisFormat();
    private AxisFormat valueAxisFormat = new AxisFormat();
    
    /** Creates a new instance of PiePlot */
    public LinePlot() {
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
    
    public Plot cloneMe()
    {
        LinePlot obj = new LinePlot();
        copyBasePlot(obj);
        obj.setCategoryAxisLabelExpression( this.getCategoryAxisLabelExpression());
        obj.setValueAxisLabelExpression( this.getValueAxisLabelExpression());
        obj.setShowLines( this.isShowLines());
        obj.setShowShapes( this.isShowShapes());
        
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
