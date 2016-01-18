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
 * RoundRectangleReportElement.java
 * 
 * Created on 22 July 2004 19.20
 *
 */

package it.businesslogic.ireport;

public class RoundRectangleReportElement extends RectangleReportElement
{
    // default radius for the rounded corner
    // this way you don't have to specify it in the element factory
    // or anywhere else
    private static int defaultRadius = 5;
    
	public RoundRectangleReportElement (int x, int y, int width, int height)
	{
            this(x,y,width,height, defaultRadius);
	}
    
	public RoundRectangleReportElement (int x, int y, int width, int height, int radius)
	{
		super(x,y,width,height);
                setRadius( radius ); 
        }
        
        public ReportElement cloneMe () {
            RoundRectangleReportElement newReportElement = new RoundRectangleReportElement (position.x, position.y, width, height);
            copyBaseReportElement (newReportElement, this);
            return newReportElement;
        }
}

