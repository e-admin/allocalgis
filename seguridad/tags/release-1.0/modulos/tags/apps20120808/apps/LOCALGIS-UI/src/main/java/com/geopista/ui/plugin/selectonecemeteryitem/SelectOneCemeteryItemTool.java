
package com.geopista.ui.plugin.selectonecemeteryitem;

import java.awt.geom.NoninvertibleTransformException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.Icon;

import com.geopista.ui.cursortool.GeopistaSelectTool;
import com.geopista.ui.plugin.selectonecemeteryitem.images.IconLoader;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.cursortool.SelectFeaturesTool;
import com.vividsolutions.jump.workbench.ui.cursortool.SelectTool;
import com.vividsolutions.jump.workbench.ui.renderer.FeatureSelectionRenderer;

public class SelectOneCemeteryItemTool extends GeopistaSelectTool {
	    
	private LayerViewPanel layerViewPanel;
    
    public SelectOneCemeteryItemTool() {
        super(FeatureSelectionRenderer.CONTENT_ID);
    }

    public Icon getIcon() {
    	return IconLoader.icon("SelectOneCemetery.gif");
    }

    public String getName() {
        return I18N.get("Select-One-Cemetery-Item");
    }
    
  
    
    public void activate(ILayerViewPanel layerViewPanel) {
        this.layerViewPanel = (LayerViewPanel)layerViewPanel;
        super.activate(layerViewPanel);
        selection = this.layerViewPanel.getSelectionManager().getFeatureSelection();
    }

    protected void gestureFinished() throws NoninvertibleTransformException
    {
    	super.gestureFinished();
    	
    	Layer layer = layerViewPanel.getLayerManager().getLayer("unidad_enterramiento");
    	Collection selectedFeatures = selection.getFeaturesWithSelectedItems(layer);
    	
    	selection.unselectItems(layer);
    	selection.selectItems(layer, (Feature)selectedFeatures.iterator().next());
    	
    }
}
