/**
 * CircleVertexStyle.java
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

import java.awt.Shape;
import java.awt.geom.Arc2D;

/**
 * @author hamammi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CircleVertexStyle extends EIELVertexStyle {

	public CircleVertexStyle() {
		super( createDefaultCircle() );
	}

	protected static Shape createDefaultCircle(){
		Arc2D s = new Arc2D.Double(100, 100, 10, 10, 0, 360, Arc2D.OPEN );
		return s;
	}
	
}
