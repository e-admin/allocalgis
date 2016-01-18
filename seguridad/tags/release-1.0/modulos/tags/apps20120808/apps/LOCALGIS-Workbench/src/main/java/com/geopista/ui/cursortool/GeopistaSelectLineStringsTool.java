package com.geopista.ui.cursortool;

import javax.swing.Icon;

import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.LineStringSelectionRenderer;


public class GeopistaSelectLineStringsTool extends GeopistaSelectTool {

    public GeopistaSelectLineStringsTool() {
        super(LineStringSelectionRenderer.CONTENT_ID);
    }

    public Icon getIcon() {
        return IconLoader.icon("SelectLineString.gif");
    }
    
    public String getName() {
        //The default implementation will return "Select Line Strings". [Jon Aquino]
        return "Select Linestrings";
    }
    
    public void activate(ILayerViewPanel layerViewPanel) {
        super.activate(layerViewPanel);
        selection = ((LayerViewPanel)layerViewPanel).getSelectionManager().getLineStringSelection();
    }    

}
