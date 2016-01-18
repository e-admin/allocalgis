/**
 * MainButtonPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.toolbox;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.workbench.model.ILayer;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.model.LayerStyleUtil;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;


/**
 * Convenience superclass used in toolboxes that have one primary button.
 */
public abstract class MainButtonPlugIn extends ThreadedBasePlugIn {
    public static final String GENERATED_KEY = MainButtonPlugIn.class.getName() +
        " - GENERATED";
    private String taskMonitorTitle;
    private Component toolboxPanel;

    public MainButtonPlugIn(String taskMonitorTitle, Component toolboxPanel) {
        this.taskMonitorTitle = taskMonitorTitle;
        this.toolboxPanel = toolboxPanel;
    }

    public String getName() {
        return taskMonitorTitle;
    }

    protected ILayer generateLayer(String name, String category, Color color,
        LayerManagerProxy proxy, FeatureCollection featureCollection,
        String description) {
        return generateLayer(false, name, category, color, proxy,
            featureCollection, description);
    }

    protected ILayer generateLineLayer(String name, String category,
        Color color, LayerManagerProxy proxy,
        FeatureCollection featureCollection, String description) {
        return generateLayer(true, name, category, color, proxy,
            featureCollection, description);
    }

    private ILayer generateLayer(boolean line, String name, String category,
        Color color, LayerManagerProxy proxy,
        FeatureCollection featureCollection, String description) {
        ILayer layer = proxy.getLayerManager().getLayer(name);

        if (layer == null) {
            layer = new Layer(name, color, featureCollection,
                    proxy.getLayerManager());
            proxy.getLayerManager().addLayer(category, layer);
            layer.setVisible(false);                                

            if (line) {
                LayerStyleUtil.setLinearStyle((Layer)layer, color, 2, 4);
            }

        } else {
            layer.setFeatureCollection(featureCollection);
        }

        layer.setDescription(description);

        //May have been loaded from a file [Jon Aquino]
        layer.getBlackboard().put(GENERATED_KEY, new Object());

        return layer;
    }

    public boolean execute(PlugInContext context) throws Exception {
        if (validateInput() != null) {
            reportValidationError(validateInput());
        }

        return validateInput() == null;
    }

    private void reportValidationError(String errorMessage) {
        JOptionPane.showMessageDialog(SwingUtilities.windowForComponent(
                toolboxPanel), errorMessage, AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
    }

    protected void removeAndDisposeLayer(String name, PlugInContext context) {
        ILayer layer = context.getLayerManager().getLayer(name);

        if (layer == null) {
            return;
        }

        context.getLayerManager().remove(layer);
        layer.dispose();
    }

    public abstract String validateInput();
}
