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
 * RectangleReportElement.java
 * 
 * Created on 28 febbraio 2003, 19.20
 *
 */

package it.businesslogic.ireport;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class RectangleReportElement extends GraphicReportElement {
    
    //public int radius = 0;
    /**
      * Key for element properties handled using the IReportHashMapBean
      */
     public static final String RADIUS =  "RADIUS";
        
    /**
     * Default values. If a value can change for different elements,
     * it is not listed here (i.e. MODE).
     */
    public static final int DEFAULT_RADIUS = 0;
    
    public RectangleReportElement (int x, int y, int width, int height) {
        super (x,y, width, height);
        //setGraphicElementPen ("Thin");
        //this.bgcolor = Color.WHITE;
        //this.fgcolor = Color.BLACK;
        setKey ("rectangle");
    }
    
    public RectangleReportElement (int x, int y, int width, int height, int radius) {
        this (x,y,width,height);
        this.setPropertyValue(RADIUS, ""+radius);
        //this.radius = radius;
        //setKey("rectangle");
    }
    
    public void drawObject (Graphics2D g,double zoom_factor, int x_shift_origin, int y_shift_origin) {
        position.x -= 10;
        position.y -= 10;
        x_shift_origin -= 10;
        y_shift_origin -= 10;
        
        this.zoom_factor = zoom_factor;
        
        g.setColor ( getBgcolor() );
        if (!getTransparent ().equalsIgnoreCase ("Transparent"))
            g.fillRoundRect ( getZoomedDim (position.x)-x_shift_origin,
            getZoomedDim (position.y)-y_shift_origin,
            getZoomedDim (width),
            getZoomedDim (height),
            (int)(2*this.getRadius()*zoom_factor),
            (int)(2*this.getRadius()*zoom_factor));
        g.setColor ( getFgcolor());
        
        position.x += 10;
        position.y += 10;
        x_shift_origin += 10;
        y_shift_origin += 10;
        
        drawGraphicsElement (g,this.getGraphicElementPen (), zoom_factor,  x_shift_origin,y_shift_origin, (int)(2*this.getRadius()*zoom_factor));
    }
    
    public ReportElement cloneMe () {
        RectangleReportElement newReportElement = new RectangleReportElement (position.x, position.y, width, height);
        copyBaseReportElement (newReportElement, this);
        return newReportElement;
    }
    
    public void  drawGraphicsElement (Graphics2D g, String pen, double zoom_factor, int x_shift_origin, int y_shift_origin) {
        
        Stroke stroke = getPenStroke ( pen,zoom_factor );
        g.setColor ( getFgcolor() );
        
        this.zoom_factor = zoom_factor;
        if (stroke==null || pen.equalsIgnoreCase ("None")) return;
        
        position.x -= 10;
        position.y -= 10;
        x_shift_origin -= 10;
        y_shift_origin -= 10;
        
        Stroke oldStroke = g.getStroke ();
        g.setStroke (stroke);
        g.drawRoundRect (
        getZoomedDim (position.x)-x_shift_origin,
        getZoomedDim (position.y)-y_shift_origin,
        getZoomedDim (width),getZoomedDim (height),
        (int)(2*this.getRadius()*zoom_factor),
        (int)(2*this.getRadius()*zoom_factor));
        
        position.x += 10;
        position.y += 10;
        
        g.setStroke (oldStroke);
    }
    
    /** Getter for property radius.
     * @return Value of property radius.
     *
     */
    public int getRadius () {
        if (getPropertyValue(RADIUS) == null)
            {
                // Look for a fgcolor in the stylesheet...
                if (getStyle() != null)
                {
                   return getStyle().getAttributeInteger( getStyle().ATTRIBUTE_radius, DEFAULT_RADIUS, true);
                }
            }
            return getIntValue(RADIUS, DEFAULT_RADIUS );
    }
    
    /** Setter for property radius.
     * @param radius New value of property radius.
     *
     */
    public void setRadius (int radius) {
        setPropertyValue(RADIUS, radius+"");
    }
    
    public void copyBaseReportElement (ReportElement destination, ReportElement source) {
        super.copyBaseReportElement (destination, source);
        
        if (destination instanceof RectangleReportElement &&
        source instanceof RectangleReportElement ) {
            //((RectangleReportElement)destination).setRadius (  ((RectangleReportElement)source).getRadius ());
            destination.setPropertyValue(RADIUS, source.getPropertyValue(RADIUS));
        }
    }
    
     public void setStyle(Style style) {
     
        super.setStyle(style);
     }
    
}
