package com.geopista.ui.cursortool;

import javax.swing.Icon;

import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.PartSelectionRenderer;

public class GeopistaSelectPartsTool extends GeopistaSelectTool {

    public GeopistaSelectPartsTool() {
        super(PartSelectionRenderer.CONTENT_ID);
    }

    public Icon getIcon() {
        return IconLoader.icon("SelectPart.gif");
    }
    
    public void activate(ILayerViewPanel layerViewPanel) {
        super.activate(layerViewPanel);
        selection = ((LayerViewPanel)layerViewPanel).getSelectionManager().getLineStringSelection();
    }    

}
