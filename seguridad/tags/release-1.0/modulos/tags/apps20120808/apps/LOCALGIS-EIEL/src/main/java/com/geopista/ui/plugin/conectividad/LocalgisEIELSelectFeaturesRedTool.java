package com.geopista.ui.plugin.conectividad;

import javax.swing.Icon;

import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.FeatureSelectionRenderer;


public class LocalgisEIELSelectFeaturesRedTool extends LocalGISEIELRedTool {
    public LocalgisEIELSelectFeaturesRedTool() {
        super(FeatureSelectionRenderer.CONTENT_ID);
    }

    public Icon getIcon() {
        return IconLoader.icon("conectividad-red2.gif");
    }
    
    public void activate(ILayerViewPanel layerViewPanel) {
        super.activate(layerViewPanel);
        selection = ((LayerViewPanel)layerViewPanel).getSelectionManager().getFeatureSelection();
    }
}
