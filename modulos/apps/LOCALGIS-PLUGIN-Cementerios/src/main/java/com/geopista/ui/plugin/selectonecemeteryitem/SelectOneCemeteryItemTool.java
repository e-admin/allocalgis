/**
 * SelectOneCemeteryItemTool.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.ui.plugin.selectonecemeteryitem;

import java.awt.geom.NoninvertibleTransformException;
import java.util.Collection;

import javax.swing.Icon;

import com.geopista.ui.cursortool.GeopistaSelectTool;
import com.geopista.ui.plugin.selectonecemeteryitem.images.IconLoader;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
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
