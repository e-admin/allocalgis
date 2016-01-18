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
 * ChartLegend.java
 * 
 * Created on 8 luglio 2005, 17.36
 *
 */

package it.businesslogic.ireport.chart;

import it.businesslogic.ireport.IReportFont;

/**
 *
 * @author Administrator
 */
public class ChartLegend {
    
   private IReportFont font = null;
    
   private java.awt.Color textColor = java.awt.Color.BLACK;
   private java.awt.Color backgroundColor = java.awt.Color.WHITE;
   
   
   /** Creates a new instance of ChartTitle */
   public ChartLegend() {       
   }

    public IReportFont getFont() {
        return font;
    }

    public void setFont(IReportFont font) {
        this.font = font;
    }

    
    public ChartLegend cloneMe() {
        
        ChartLegend ct = new ChartLegend();
        if (getFont() != null) ct.setFont( (IReportFont)getFont().clone() );
        if (getBackgroundColor() != null) ct.setBackgroundColor(  new java.awt.Color( getBackgroundColor().getRGB() ));
        if (getTextColor() != null) ct.setTextColor(  new java.awt.Color( getTextColor().getRGB() ));
        
        return ct;
    }

    public java.awt.Color getTextColor() {
        return textColor;
    }

    public void setTextColor(java.awt.Color textColor) {
        this.textColor = textColor;
    }

    public java.awt.Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(java.awt.Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
