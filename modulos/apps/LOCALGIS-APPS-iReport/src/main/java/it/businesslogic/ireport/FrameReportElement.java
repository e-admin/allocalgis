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
 * FrameReportElement.java
 * 
 * Created on 28 febbraio 2003, 19.20
 *
 */

package it.businesslogic.ireport;
import java.awt.Graphics2D;

public class FrameReportElement extends ReportElement implements BoxElement {

   private Box box = null;
   
   public FrameReportElement(int x, int y, int width, int height)
   {
	  super(x,y,width,height);
	  //see net.sf.jasperreports.engine.base.JRBaseFont
	  //setGraphicElementPen("Thin");
	  //this.bgcolor = Color.WHITE;
	  //this.fgcolor = Color.BLACK;
	  setBox(new Box());
          setKey("frame");
	  //set font
          transparentDefault = "Transparent";	  
   }
   
   public void drawObject(Graphics2D g,double zoom_factor, int x_shift_origin, int y_shift_origin)
   {
        position.x -= 10;
        position.y -= 10;
        x_shift_origin -= 10;
        y_shift_origin -= 10;

        this.zoom_factor = zoom_factor;

        g.setColor ( getBgcolor() );
        if (!getTransparent().equalsIgnoreCase ("Transparent"))
            g.fillRoundRect ( getZoomedDim (position.x)-x_shift_origin,
            getZoomedDim (position.y)-y_shift_origin,
            getZoomedDim (width),
            getZoomedDim (height),
            0,
            0);
        g.setColor ( getFgcolor() );

        position.x += 10;
        position.y += 10;
        x_shift_origin += 10;
        y_shift_origin += 10;
        
     drawBorder( g, zoom_factor,  x_shift_origin,y_shift_origin);
     drawBorder( g, zoom_factor,  x_shift_origin,y_shift_origin,getBox());
     //drawGraphicsElement(g,this.getGraphicElementPen(), zoom_factor,  x_shift_origin,y_shift_origin, 0);
   }
   
   public ReportElement cloneMe () {
        FrameReportElement newReportElement = new FrameReportElement (position.x, position.y, width, height);
        copyBaseReportElement (newReportElement, this);
        return newReportElement;
   }
     
   public void copyBaseReportElement(ReportElement destination, ReportElement source)
   {
      super.copyBaseReportElement(destination, source);
      
      if (destination instanceof BoxElement && source instanceof BoxElement )
      {
         ((BoxElement)destination).setBox( ((BoxElement)source).getBox().cloneMe() );
      }
   }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }
    
    public void setStyle(Style style) {
     
        super.setStyle(style);
        
        if (style != null)
        {            
            // BOX
            if (style.getAttributeString(style.ATTRIBUTE_border, null, true) != null) 
                this.getBox().setBorder(  style.getAttributeString(style.ATTRIBUTE_border, null, true) );
            if (style.getAttributeColor(style.ATTRIBUTE_borderColor, null, true) != null) 
                this.getBox().setBorderColor(  style.getAttributeColor(style.ATTRIBUTE_borderColor, null, true));
            if (style.getAttributeString(style.ATTRIBUTE_padding, null, true) != null) 
                this.getBox().setPadding( Integer.parseInt( style.getAttributeString(style.ATTRIBUTE_padding, null, true) ));
            
            if (style.getAttributeString(style.ATTRIBUTE_topBorder, null, true) != null) 
                this.getBox().setTopBorder(  style.getAttributeString(style.ATTRIBUTE_topBorder, null, true) );
            if (style.getAttributeColor(style.ATTRIBUTE_topBorderColor, null, true) != null) 
                this.getBox().setTopBorderColor(  style.getAttributeColor(style.ATTRIBUTE_topBorderColor, null, true));
            if (style.getAttributeString(style.ATTRIBUTE_topPadding, null, true) != null) 
                this.getBox().setTopPadding( Integer.parseInt( style.getAttributeString(style.ATTRIBUTE_topPadding, null, true) ));
            
            if (style.getAttributeString(style.ATTRIBUTE_leftBorder, null, true) != null) 
                this.getBox().setLeftBorder(  style.getAttributeString(style.ATTRIBUTE_leftBorder, null, true) );
            if (style.getAttributeColor(style.ATTRIBUTE_leftBorderColor, null, true) != null) 
                this.getBox().setLeftBorderColor(  style.getAttributeColor(style.ATTRIBUTE_leftBorderColor, null, true));
            if (style.getAttributeString(style.ATTRIBUTE_leftPadding, null, true) != null) 
                this.getBox().setLeftPadding( Integer.parseInt( style.getAttributeString(style.ATTRIBUTE_leftPadding, null, true) ));
            
            if (style.getAttributeString(style.ATTRIBUTE_rightBorder, null, true) != null) 
                this.getBox().setRightBorder(  style.getAttributeString(style.ATTRIBUTE_rightBorder, null, true) );
            if (style.getAttributeColor(style.ATTRIBUTE_rightBorderColor, null, true) != null) 
                this.getBox().setRightBorderColor(  style.getAttributeColor(style.ATTRIBUTE_rightBorderColor, null, true));
            if (style.getAttributeString(style.ATTRIBUTE_rightPadding, null, true) != null) 
                this.getBox().setRightPadding( Integer.parseInt( style.getAttributeString(style.ATTRIBUTE_rightPadding, null, true) ));
            
            if (style.getAttributeString(style.ATTRIBUTE_bottomBorder, null, true) != null) 
                this.getBox().setBottomBorder(  style.getAttributeString(style.ATTRIBUTE_bottomBorder, null, true) );
            if (style.getAttributeColor(style.ATTRIBUTE_bottomBorderColor, null, true) != null) 
                this.getBox().setBottomBorderColor(  style.getAttributeColor(style.ATTRIBUTE_bottomBorderColor, null, true));
            if (style.getAttributeString(style.ATTRIBUTE_bottomPadding, null, true) != null) 
                this.getBox().setBottomPadding( Integer.parseInt( style.getAttributeString(style.ATTRIBUTE_bottomPadding, null, true) ));
            
        }
     }

}
