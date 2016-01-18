/**
 * OneLayerAttributeTab.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui;

import com.geopista.editor.TaskComponent;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.FeatureEventType;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;


/**
 * Displays and stays in sync with a single Layer.
 */
public class OneLayerAttributeTab extends AttributeTab {
    public OneLayerAttributeTab(WorkbenchContext context, TaskComponent taskFrame,
        LayerManagerProxy layerManagerProxy) {
        super(new InfoModel(), context, taskFrame, layerManagerProxy);
        context.getLayerManager().addLayerListener(new LayerListener() {
                public void featuresChanged(FeatureEvent e) {
                    if (getLayerTableModel() == null) {
                        //Get here after attribute viewer window is closed [Jon Aquino]
                        return;
                    }
                    if ((e.getLayer() == getLayerTableModel().getLayer()) &&
                            (e.getType() == FeatureEventType.ADDED)) {
                        //DELETED events are already handled in LayerTableModel
                        getLayerTableModel().addAll(e.getFeatures());
                    }
                }

                public void layerChanged(LayerEvent e) {
                }

                public void categoryChanged(CategoryEvent e) {
                }
            });
    }

    public OneLayerAttributeTab setLayer(Layer layer) {
        if (!getModel().getLayers().isEmpty()) {
            getModel().remove(getLayer());
        }

        //InfoModel#add must be called after the AttributeTab is created; otherwise
        //layer won't be added to the Attribute Tab -- the AttributeTab listens for
        //the event fired by InfoModel#add. [Jon Aquino]
        getModel().add(layer, layer.getFeatureCollectionWrapper().getFeatures());

        return this;
    }

    public Layer getLayer() {
        //null LayerTableModel if for example the user has just removed the layer
        //from the LayerManager [Jon Aquino]
        return (getLayerTableModel() != null) ? getLayerTableModel().getLayer()
                                              : null;
    }

    public LayerTableModel getLayerTableModel() {
        return (!getModel().getLayerTableModels().isEmpty())
        ? (LayerTableModel) getModel().getLayerTableModels().iterator().next()
        : null;
    }
}
