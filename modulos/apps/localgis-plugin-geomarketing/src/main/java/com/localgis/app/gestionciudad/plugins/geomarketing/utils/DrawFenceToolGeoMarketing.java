/**
 * DrawFenceToolGeoMarketing.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.plugins.geomarketing.utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.Icon;
import com.geopista.ui.cursortool.PolygonTool;
import com.localgis.app.gestionciudad.plugins.geomarketing.images.IconLoader;
import com.localgis.app.gestionciudad.plugins.geomarketing.DrawGeometryGeomarketingPlugIn;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
public class DrawFenceToolGeoMarketing extends PolygonTool {
	
	
	private PlugInContext context;
	private static Geometry poligon = null;
	private static boolean active = false;
	
	private WorkbenchContext contextW;

	//sst: change: PlugInContext added
	
	public DrawFenceToolGeoMarketing( PlugInContext context, boolean allowSnapping) {
		this.context = context;
		this.active = true;
		
		  this.setColor(Color.red);
		  if (allowSnapping) { this.allowSnapping(); } 


		  Toolkit.getDefaultToolkit().createCustomCursor(
				  IconLoader.icon("Pen.gif").getImage(),
				  new java.awt.Point(1, 31),
				  this.getName());
	}
	
	public Cursor getCursor() {
        if (Toolkit
            .getDefaultToolkit()
            .getBestCursorSize(32, 32)
            .equals(new Dimension(0, 0))) {
            return Cursor.getDefaultCursor();
        }
        return Toolkit.getDefaultToolkit().createCustomCursor(
        		IconLoader.icon("Pen.gif").getImage(),
        		new java.awt.Point(1, 31),
        		this.getName());
 
	}
	
	protected DrawFenceToolGeoMarketing( WorkbenchContext context) {
		this.contextW = context;
		this.active = true;
	}
	



	/*public Icon getIcon() {
		//return IconLoader.icon("DrawPolygon.gif");
		return IconLoader.icon("");
	}*/
	
	public String getName(){
    	String name = I18N.get("SelectItemsByFenceFromSelectedLayersPlugIn","SelectItemsByFenceFromSelectedLayersPlugIn");
    	return name;
    }
    
    public Icon getIcon() {
        return IconLoader.icon("selecteditemsbyfence.gif");
    }

	protected void gestureFinished() throws Exception {
		reportNothingToUndoYet();

		//context.getLayerViewPanel().setCurrentCursorTool(this);
		
		if (!checkPolygon()){
			return;
			}
		
//		int count = 0;
		
		if (!wasShiftPressed()) {
            getPanel().getSelectionManager().clear();
        }
		
		poligon = this.getPolygon();
			    
		active = false;
		
		DrawGeometryGeomarketingPlugIn.onDrawGeometryFinish();
	}
	

	public Geometry getPolygonGeometry(){
		return poligon;
	}
	
	public static boolean isActive(){
		return active;
	}

}
