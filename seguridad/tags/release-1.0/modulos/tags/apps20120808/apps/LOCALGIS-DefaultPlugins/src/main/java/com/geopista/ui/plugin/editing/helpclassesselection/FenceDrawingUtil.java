package com.geopista.ui.plugin.editing.helpclassesselection;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import com.geopista.ui.cursortool.AbstractCursorTool;
import com.geopista.ui.plugin.editing.helpclassesselection.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.GeometryEditor;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;
import com.vividsolutions.jump.workbench.ui.cursortool.DelegatingTool;



public class FenceDrawingUtil {
	

    /**
     * 
     * Constructor
     *
     */
    public FenceDrawingUtil(LayerNamePanelProxy layerNamePanelProxy) {
        this.layerNamePanelProxy = layerNamePanelProxy;
    }
    
    private LayerNamePanelProxy layerNamePanelProxy;    
    private GeometryEditor editor = new GeometryEditor();
    
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
}
