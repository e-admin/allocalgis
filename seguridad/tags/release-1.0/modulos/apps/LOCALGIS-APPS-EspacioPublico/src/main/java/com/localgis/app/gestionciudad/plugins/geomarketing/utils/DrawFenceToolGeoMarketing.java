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
