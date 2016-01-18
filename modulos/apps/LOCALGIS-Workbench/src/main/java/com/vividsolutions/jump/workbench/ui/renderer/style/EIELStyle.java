/**
 * EIELStyle.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.renderer.style;

import java.awt.BasicStroke;
import java.awt.Color;



public class EIELStyle extends BasicStyle{
	
	public void setLineWidth(float with){
		lineStroke=new BasicStroke(with);
	}
	
	
	  public static interface FillStyle {
	        public void setFillColor(Color c);
	        public void setAlpha(int a);
	    }

	   
	    public static interface StrokeStyle {
	        public void setLineColor(Color c);
	        public void setLineWidth(int w);
	        public void setAlpha(int a);
	        public BasicStyle setRenderingLinePattern(boolean b);
	        public BasicStyle setLinePattern(String p);
	    }
	    

	    public static interface SizedStyle {
	        public void setSize(int s);

	    }

	   
	    public static interface StrokeFillStyle extends StrokeStyle, FillStyle, Style {
	    }

	   
	    public static interface SizedStrokeFillStyle extends StrokeFillStyle, SizedStyle {
	    }

}
