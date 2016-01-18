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
 * GraphicReportElement.java
 * 
 * Created on 28 febbraio 2003, 19.22
 *
 */

package it.businesslogic.ireport;

import java.awt.Graphics2D;

public abstract class GraphicReportElement extends ReportElement 
{

        /**
         * Key for element properties handled using the IReportHashMapBean
         */
        public static final String FILL =  "FILL";
        public static final String PEN =   "PEN";
        
        /**
         * Default values. If a value can change for different elements,
         * it is not listed here (i.e. MODE).
         */
        public static final String DEFAULT_FILL = "Solid";
        public static final String DEFAULT_PEN =  "Thin";
    
	//public String graphicElementPen;
	//public String fill;
	
	public GraphicReportElement(int x, int y, int width, int height)
	{
		super(x,y, Math.abs(width), Math.abs(height));
		//setGraphicElementPen("Thin");
		//fill="Solid";
		//stretchType="NoStretch";
                // Name the elements differently
                setKey("graphic");
	}
	
	public void drawObject(Graphics2D g,double zoom_factor, int x_shift_origin, int y_shift_origin)
	{
		super.drawGraphicsElement(g, getGraphicElementPen(), zoom_factor, x_shift_origin, y_shift_origin );
	}
        
        /** Getter for property fill.
         * @return Value of property fill.
         *
         */
        public java.lang.String getFill() {
            if (getPropertyValue(FILL) == null)
            {
                // Look for a fgcolor in the stylesheet...
                if (getStyle() != null)
                {
                   return getStyle().getAttributeString( getStyle().ATTRIBUTE_fill, DEFAULT_FILL, true);
                }
            }
            return getStringValue(FILL, DEFAULT_FILL );
        }
        
        /** Setter for property fill.
         * @param fill New value of property fill.
         *
         */
        public void setFill(java.lang.String fill) {
            setPropertyValue(FILL, fill);
        }
        
        /** Getter for property graphicElementPen.
         * @return Value of property graphicElementPen.
         *
         */
        public java.lang.String getGraphicElementPen() {
           if (getPropertyValue(PEN) == null)
            {
                // Look for a fgcolor in the stylesheet...
                if (getStyle() != null)
                {
                   return getStyle().getAttributeString( getStyle().ATTRIBUTE_pen, DEFAULT_PEN, true);
                }
            }
            return getStringValue(PEN, DEFAULT_PEN );
        }
        
        /** Setter for property graphicElementPen.
         * @param graphicElementPen New value of property graphicElementPen.
         *
         */
        public void setGraphicElementPen(java.lang.String graphicElementPen) {
            setPropertyValue(PEN, graphicElementPen);
        }
        
        public void copyBaseReportElement(ReportElement destination, ReportElement source)
        {
                super.copyBaseReportElement(destination, source);
                
                if (destination instanceof GraphicReportElement &&
                    source instanceof GraphicReportElement )
                {
                    
                    destination.setPropertyValue(PEN, getPropertyValue(PEN));
                    destination.setPropertyValue(FILL, getPropertyValue(FILL));
                    //((GraphicReportElement)destination).setFill( new String( ((GraphicReportElement)source).getFill() ));
                    //((GraphicReportElement)destination).setGraphicElementPen( new String( ((GraphicReportElement)source).getGraphicElementPen() ));
                    //((GraphicReportElement)destination).setStretchType( new String( ((GraphicReportElement)source).getStretchType()) );
                }
        }
        
         public void setStyle(Style style) {
                super.setStyle(style);
                if (style != null)
                {
                    //this.setFill( style.getAttributeString( style.ATTRIBUTE_fill, getFill(), true)  );
                    //this.setGraphicElementPen( style.getAttributeString( style.ATTRIBUTE_pen,getGraphicElementPen(), true) );   
                }
         }
}

