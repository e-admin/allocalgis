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
 * LineReportElement.java
 * 
 * Created on 28 febbraio 2003, 20.02
 *
 */

package it.businesslogic.ireport;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

public class LineReportElement extends GraphicReportElement 
{

	public String direction;
	
	public LineReportElement(int x, int y, int width, int height)
	{
		//this(x,y,width,height,"TopDown");
        // I expect the delta width and height to be passed
        // so with + or - sign.
        this(x, y , width, height, "whatever");

        if (width * height >= 0) {
            setDirection( "TopDown" );
        } else {
            setDirection( "BottomUp" );
        }
	}
	
	public LineReportElement(int x, int y, int width, int height, String direction)
	{
		super(x, y, Math.abs(width) , Math.abs(height) );
		//graphicElementPen  = "Thin";
		//this.bgcolor = Color.WHITE;
		//this.fgcolor = Color.BLACK;
		this.direction = direction;
        setKey("line");
	}
	
	public void drawObject(Graphics2D g,double zoom_factor, int x_shift_origin, int y_shift_origin)
	{
		this.zoom_factor = zoom_factor;
		int height_draw = 0;
                if (height <= 1) height_draw = 0;
                else height_draw = height-1;
                int width_draw = 0;
                if (width <= 1) width_draw = 0;
                else width_draw = width-1;
		
                //g.fill( position.x,position.y,width,height,new Brush(bgcolor, BrushStyle.SOLID));
		Point a = new Point( getZoomedDim( position.x-10) +10-x_shift_origin, getZoomedDim( position.y-10) +10-y_shift_origin );
		Point b = new Point( getZoomedDim(position.x+width_draw-10)+10-x_shift_origin,getZoomedDim( position.y+height_draw-10)+10-y_shift_origin);
		
                if (!direction.equalsIgnoreCase("TopDown"))
		{
			b.y = getZoomedDim( position.y-10) +10-y_shift_origin;
			a.y = getZoomedDim( position.y+height-10)+10-y_shift_origin;
		}
		
                Stroke stroke = this.getPenStroke( getGraphicElementPen() ,zoom_factor );
		if(stroke != null){
	                Stroke oldStroke = g.getStroke();
                	g.setStroke(stroke);
                
			g.setColor( getFgcolor() );    
			g.drawLine(a.x,a.y,b.x,b.y);                
	                g.setStroke(oldStroke);
		}
	}
	
	public ReportElement cloneMe()
	{
		LineReportElement newReportElement = new LineReportElement(position.x, position.y, width, height);
		copyBaseReportElement(newReportElement, this);
		
		return newReportElement;
	}
        
        /** Getter for property direction.
         * @return Value of property direction.
         *
         */
        public java.lang.String getDirection() {
            return direction;
        }
        
        /** Setter for property direction.
         * @param direction New value of property direction.
         *
         */
        public void setDirection(java.lang.String direction) {
            this.direction = direction;
        }
        
        public void copyBaseReportElement(ReportElement destination, ReportElement source)
        {
                super.copyBaseReportElement(destination, source);
                
                if (destination instanceof LineReportElement &&
                    source instanceof LineReportElement )
                {
                    ((LineReportElement)destination).setDirection( new String(  ((LineReportElement)source).getDirection()));
                }
        }
        
         public boolean insideBand()
        {

            int r_height = (height == 0) ? 1 : height;
            if (band == null)
            {

                return false;
            }

            int yband = band.getBandYLocation();

            if (position.y - yband + r_height > band.getHeight())
            {
                return false;
            }
            return super.insideBand();
        }
}
