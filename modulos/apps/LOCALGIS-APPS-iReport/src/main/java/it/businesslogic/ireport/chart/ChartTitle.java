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
 * ChartTitle.java
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
public class ChartTitle {
    
   private IReportFont font = null;
   
   private String titleExpression = "";
    
   private java.awt.Color color = java.awt.Color.BLACK;
   
   private String position = "Top";
   
   /** Creates a new instance of ChartTitle */
   public ChartTitle(String title) {
       
       this.titleExpression = title;
   }

    public IReportFont getFont() {
        return font;
    }

    public void setFont(IReportFont font) {
        this.font = font;
    }

    public String getTitleExpression() {
        return titleExpression;
    }

    public void setTitleExpression(String titleExpression) {
        this.titleExpression = titleExpression;
    }

    public java.awt.Color getColor() {
        return color;
    }

    public void setColor(java.awt.Color color) {
        this.color = color;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    
    public ChartTitle cloneMe() {
        
        ChartTitle ct = new ChartTitle(getTitleExpression());
        if (getFont() != null) ct.setFont( (IReportFont)getFont().clone() );
        if (getColor() != null) ct.setColor(  new java.awt.Color( getColor().getRGB() ));
        ct.setPosition( new String( getPosition()));
        ct.position = position;
        
        return ct;
    }
}
