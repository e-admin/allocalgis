/**
 * FenceDrawingUtilGeoMarketing.java
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
import com.geopista.ui.cursortool.AbstractCursorTool;
import com.localgis.app.gestionciudad.plugins.geomarketing.images.IconLoader;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.workbench.ui.GeometryEditor;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;
import com.vividsolutions.jump.workbench.ui.cursortool.DelegatingTool;



public class FenceDrawingUtilGeoMarketing {
	

    /**
     * 
     * Constructor
     *
     */
    public FenceDrawingUtilGeoMarketing(LayerNamePanelProxy layerNamePanelProxy) {
        this.layerNamePanelProxy = layerNamePanelProxy;
    }
    
    private LayerNamePanelProxy layerNamePanelProxy;    
    private GeometryEditor editor = new GeometryEditor();
    
    private DrawFenceToolGeoMarketing drawTool = null;
    /**
     * Apply settings common to all feature-drawing tools.
     */
    public CursorTool prepare(final AbstractCursorTool drawFeatureTool, boolean allowSnapping) {
        drawFeatureTool.setColor(Color.red);
        if (allowSnapping) { drawFeatureTool.allowSnapping(); } 
        return new DelegatingTool(drawFeatureTool) {
            public String getName() {
                return drawFeatureTool.getName();
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
                    drawFeatureTool.getName());
            }
        };
    }
    
    public Geometry getDrawedGeometry(){
    	try{
    	if (drawTool != null){
    		return this.drawTool.getPolygonGeometry();
    	}
    	}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    	return null;
    	
    }
}
