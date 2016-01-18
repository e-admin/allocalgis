/**
 * CrossVertexStyle.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 30.08.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.vividsolutions.jump.workbench.ui.renderer.style;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Point2D;

/**
 * @author hamammi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CrossVertexStyle extends EIELVertexStyle {
	
	public CrossVertexStyle(){
		super( createDefaultCross() );
	}
	
	public void paint(Graphics2D g, Point2D p) {
		//setFrame
		//render		
		int thickness = 0;
		
		int p1X = (int)p.getX() + thickness;
		int p1Y = (int)p.getY() - thickness - size;
        int p2Y = (int)p.getY() -thickness;
        int p3X = (int)p.getX() + thickness + size ;
        int p4Y = (int)p.getY() + thickness;
        int p6Y =(int)p.getY() + thickness + size;
        int p7X = (int)p.getX() - thickness;
        int p9X = (int)p.getX() - thickness - size;
	       
		( ( Polygon ) this.shape).xpoints = 
		    new int []{p1X, p1X, 
		               p3X , p3X,
				       p1X , p1X,
				       p7X , p7X,
				       p9X , p9X,
				       p7X,  p7X}; 
		
		( ( Polygon ) this.shape).ypoints = 
		    new int [] {p1Y, p2Y,
				        p2Y , p4Y,
				        p4Y , p6Y,
				        p6Y , p4Y,   
				        p4Y , p2Y,
		                p2Y, p1Y};
		
		((Polygon)this.shape).npoints=( ( Polygon ) this.shape).xpoints.length;
		render(g);
	}
	
	protected static Shape createDefaultCross(){
		return new Polygon();
	}

}
