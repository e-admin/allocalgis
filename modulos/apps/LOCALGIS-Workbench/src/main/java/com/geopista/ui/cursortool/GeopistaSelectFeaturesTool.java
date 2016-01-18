/**
 * GeopistaSelectFeaturesTool.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.cursortool;

import javax.swing.Icon;

import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.FeatureSelectionRenderer;


public class GeopistaSelectFeaturesTool extends GeopistaSelectTool {
    public GeopistaSelectFeaturesTool() {
        super(FeatureSelectionRenderer.CONTENT_ID);
    }

    public Icon getIcon() {
        return IconLoader.icon("Select.gif");
    }
    
    public void activate(ILayerViewPanel layerViewPanel) {
        super.activate(layerViewPanel);
        selection = ((LayerViewPanel)layerViewPanel).getSelectionManager().getFeatureSelection();
    }
}
