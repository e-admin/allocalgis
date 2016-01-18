/**
 * LayerComboBox.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui;

import java.awt.Color;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;


/**
 * Stays in sync with the LayerManager.
 */

public class LayerComboBox extends JComboBox {
    //For the layer name, use " " rather than "" so that the renderer is sized normally. 
    //See comments in #updateModel. [Jon Aquino]
    private static final Layer DUMMY_LAYER = new Layer(" ",
            new Color(0, 0, 0, 0),
            AddNewLayerPlugIn.createBlankFeatureCollection(), (ILayerManager)new LayerManager());
    private LayerManager layerManager = new LayerManager();
        
    public Object getSelectedItem() {
        return (super.getSelectedItem() != DUMMY_LAYER)
        ? super.getSelectedItem() : null;
    }        

    private LayerListener listener = new LayerListener() {
            public void featuresChanged(FeatureEvent e) {
            }

            public void layerChanged(LayerEvent e) {
                updateModel();
            }

            public void categoryChanged(CategoryEvent e) {
            }
        };

    public LayerComboBox() {
        super(new DefaultComboBoxModel());
        setRenderer(new LayerNameRenderer());
    }
    
    private DefaultComboBoxModel getMyModel() {
        return (DefaultComboBoxModel) getModel();
    }

    private void updateModel() {
        Layer selectedLayer = getSelectedLayer();
        getMyModel().removeAllElements();

        for (Iterator i = layerManager.iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();
            getMyModel().addElement(layer);
        }

        if (getMyModel().getSize() == 0) {
            //Don't leave the combobox empty -- an empty combobox is rendered a few 
            //pixels larger than a non-empty one. If it is in a packed frame's GridBagLayout,
            //the GridBagLayout will collapse all components to minimum size
            //(see Java bug 4247013). [Jon Aquino] 
            getMyModel().addElement(DUMMY_LAYER);
        }

        if (-1 != getMyModel().getIndexOf(selectedLayer)) {
            getMyModel().setSelectedItem(selectedLayer);
        }
    }

    public Layer getSelectedLayer() {
        return (Layer) getSelectedItem();
    }

    public void setLayerManager(LayerManager layerManager) {
        this.layerManager.removeLayerListener(listener);
        this.layerManager = layerManager;
        this.layerManager.addLayerListener(listener);
        updateModel();
    }

    public LayerManager getLayerManager() {
        return layerManager;
    }

    public void setSelectedLayer(Layer candidateLayer) {
        setSelectedItem(candidateLayer);
    }


}
