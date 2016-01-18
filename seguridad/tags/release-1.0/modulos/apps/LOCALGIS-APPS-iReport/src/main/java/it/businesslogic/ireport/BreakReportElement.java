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

import it.businesslogic.ireport.gui.*;
import it.businesslogic.ireport.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BreakReportElement extends ReportElement 
{

        public static ImageIcon img=null;
        
        static {
               img = new ImageIcon( BreakReportElement.class.getResource("/it/businesslogic/ireport/icons/tree/elements/break.png"));
        }
        
	private String type = "Page";
	
	public BreakReportElement(int x, int y, int width, int height)
	{
            super(x, y, Math.abs(width) , Math.abs(height) );
	}
	
	public BreakReportElement(int x, int y, int width, int height, String type)
	{
		super(x, y, Math.abs(width) , Math.abs(height) );
		//graphicElementPen  = "Thin";
		//this.bgcolor = Color.WHITE;
		//this.fgcolor = Color.BLACK;
		this.type = type;
                setKey("break");
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
		
                int leftX = 0;
                int rightX = 0;
                if (this.getBand() != null && this.getBand().getParent() != null)
                {
                   Report report = this.getBand().getParent();
                   rightX = (int)(report.getLeftMargin()*zoom_factor);
                   leftX = (int)((report.getWidth() - report.getRightMargin()) *zoom_factor);
                }
                
                //g.fill( position.x,position.y,width,height,new Brush(bgcolor, BrushStyle.SOLID));
		Point a = new Point( rightX + 10 - x_shift_origin, getZoomedDim( position.y-10) +10-y_shift_origin );
		Point b = new Point( leftX + 10 - x_shift_origin, (int)a.getY());
		
                
                Stroke stroke = this.getPenStroke( "Dotted" ,1 );
		if(stroke != null){
	                Stroke oldStroke = g.getStroke();
                	g.setStroke(stroke);
                
			g.setColor( getFgcolor() );    
			g.drawLine(a.x,a.y,b.x,b.y);                
	                g.setStroke(oldStroke);
		}
                
                if (img != null)
                    g.drawImage( img.getImage(),a.x-18, a.y-7, img.getImageObserver());
	}
	
	public ReportElement cloneMe()
	{
		BreakReportElement newReportElement = new BreakReportElement(position.x, position.y, width, height);
		copyBaseReportElement(newReportElement, this);
		
		return newReportElement;
	}
                
        public void copyBaseReportElement(ReportElement destination, ReportElement source)
        {
                super.copyBaseReportElement(destination, source);
                
                if (destination instanceof BreakReportElement &&
                    source instanceof BreakReportElement )
                {
                    ((BreakReportElement)destination).setType( new String(  ((BreakReportElement)source).getType()));
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
