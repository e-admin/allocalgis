package com.geopista.ui.cursortool;

import javax.swing.Icon;

import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.FeatureSelectionRenderer;


public class GeopistaSelectFeaturesEIELTool extends GeopistaSelectEIELTool {
    public GeopistaSelectFeaturesEIELTool() {
        super(FeatureSelectionRenderer.CONTENT_ID);
    }

    public Icon getIcon() {
        return IconLoader.icon("SelectEIEL.gif");
    }
    
    public void activate(ILayerViewPanel layerViewPanel) {
        super.activate(layerViewPanel);
        selection = ((LayerViewPanel)layerViewPanel).getSelectionManager().getFeatureSelection();
    }
}
