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
 * EllipseReportElement.java
 * 
 * Created on 28 febbraio 2003, 22.42
 *
 */

package it.businesslogic.ireport;

import it.businesslogic.ireport.gui.*;
import it.businesslogic.ireport.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;


public class EllipseReportElement extends GraphicReportElement 
{
	public EllipseReportElement(int x, int y, int width, int height)
	{
		super(x,y,width,height);
		//graphicElementPen  = "Thin";
		//this.bgcolor = Color.WHITE;
		//this.fgcolor = Color.BLACK;
                setKey("ellipse");
	}
	
	public void drawObject(Graphics2D g,double zoom_factor, int x_shift_origin, int y_shift_origin)
	{
           
                position.x -= 10;
		position.y -= 10;
		x_shift_origin -= 10;
		y_shift_origin -= 10;
		
		this.zoom_factor = zoom_factor;
                
                g.setColor( getBgcolor() );
                if (!getTransparent().equalsIgnoreCase("Transparent"))
                    g.fillOval( getZoomedDim(position.x)-x_shift_origin,
				getZoomedDim(position.y)-y_shift_origin,
				getZoomedDim(width),
				getZoomedDim(height));
                
		position.x += 10;
		position.y += 10;
		x_shift_origin += 10;
		y_shift_origin += 10;
                
                drawGraphicsElement(g,this.getGraphicElementPen(), zoom_factor,  x_shift_origin,y_shift_origin);
	}
	
	public ReportElement cloneMe()
	{
		EllipseReportElement newReportElement = new EllipseReportElement(position.x, position.y, width, height);
		copyBaseReportElement(newReportElement, this);
		
		return newReportElement;
	}
	
	public void  drawGraphicsElement(Graphics2D g, String pen, double zoom_factor, int x_shift_origin, int y_shift_origin)
	{
		
		Stroke stroke = getPenStroke( pen,zoom_factor );
		g.setColor( getFgcolor() );
                
		this.zoom_factor = zoom_factor;
		if (stroke==null || pen.equalsIgnoreCase("None")) return;
		
		position.x -= 10;
		position.y -= 10;
		x_shift_origin -= 10;
		y_shift_origin -= 10;
                
                Stroke oldStroke = g.getStroke();
                g.setStroke(stroke);
		g.drawOval(
                    getZoomedDim(position.x)-x_shift_origin,
		    getZoomedDim(position.y)-y_shift_origin,
                    getZoomedDim(width),getZoomedDim(height));

		position.x += 10;
		position.y += 10;               
                
                g.setStroke(oldStroke);
        }
}
