/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */

package com.vividsolutions.jump.workbench.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.FeatureEventType;
import com.vividsolutions.jump.workbench.model.ILayer;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerEventType;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.ui.renderer.SelectionBackgroundRenderer;

/**
 * Provides aggregate information for selected features, parts, and linestrings.
 * Note that there is a difference between "selected features" and "features with
 * selected items": the former consists of wholly selected features; the latter,
 * wholly and partially selected features. To access a specific level of selection,
 * use #getFeatureSelection, #getPartSelection, or #getLineStringSelection.
 * "Parts" are components of GeometryCollections.
 * <p>
 * To get wholly selected features (i.e. not those that just have selected
 * parts or linestrings), use <code>getFeatureSelection().getFeaturesWithSelectedItems()</code>
 * <p>
 * To get features that are selected or have selected parts or linestrings,
 * use <code>getFeaturesWithSelectedItems()</code>
 */
public class SelectionManager implements ISelectionManager {
    private FeatureSelection featureSelection;
    private PartSelection partSelection;
    private LineStringSelection lineStringSelection;
    private LayerManagerProxy layerManagerProxy;  
    private LayerViewPanel panel;

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#createFeaturesFromSelectedItems()
	 */
    @Override
	public Collection createFeaturesFromSelectedItems() {
        ArrayList newFeatures = new ArrayList();
        for (Iterator i = getLayersWithSelectedItems().iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();
            newFeatures.addAll(createFeaturesFromSelectedItems(layer));
        }
        return newFeatures;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#createFeaturesFromSelectedItems(com.vividsolutions.jump.workbench.model.Layer)
	 */
	public Collection createFeaturesFromSelectedItems(ILayer layer) {
        ArrayList newFeatures = new ArrayList();
        for (Iterator i = getFeaturesWithSelectedItems(layer).iterator(); i.hasNext();) {
            Feature feature = (Feature) i.next();
            for (Iterator j = getSelectedItems(layer, feature).iterator(); j.hasNext();) {
                Geometry item = (Geometry) j.next();
                Feature newFeature = (Feature) feature.clone();
                newFeature.setGeometry(item);
                newFeatures.add(newFeature);
            }
        }
        return newFeatures;
    }

    private boolean panelUpdatesEnabled = true;

    public SelectionManager(LayerViewPanel panel, LayerManagerProxy layerManagerProxy) {
        this.panel = panel;
        this.layerManagerProxy = layerManagerProxy;
        featureSelection = new FeatureSelection(this);
        lineStringSelection = new LineStringSelection(this);
        partSelection = new PartSelection(this);
        featureSelection.setParent(null);
        featureSelection.setChild(partSelection);
        partSelection.setParent(featureSelection);
        partSelection.setChild(lineStringSelection);
        lineStringSelection.setParent(partSelection);
        lineStringSelection.setChild(null);
        selections =
            Collections.unmodifiableList(
                Arrays.asList(
                    new Object[] { featureSelection, partSelection, lineStringSelection }));
        addLayerListenerTo(layerManagerProxy.getLayerManager());
    }

    private LayerListener layerListener = new LayerListener() {
        public void featuresChanged(FeatureEvent e) {
            if (e.getType() == FeatureEventType.DELETED) {
                unselectItems(e.getLayer(), e.getFeatures());
            }
            if (e.getType() == FeatureEventType.GEOMETRY_MODIFIED) {
                unselectFromFeaturesWithModifiedItemCounts(
                    e.getLayer(),
                    e.getFeatures(),
                    e.getOldFeatureClones());
            }
        }

        public void layerChanged(LayerEvent e) {
            if (!(e.getLayerable() instanceof Layer)) {
                return;
            }
            if (e.getType() == LayerEventType.REMOVED
                || e.getType() == LayerEventType.VISIBILITY_CHANGED) {
                unselectItems((Layer) e.getLayerable());
            }
        }

        public void categoryChanged(CategoryEvent e) {}
    };

    private void addLayerListenerTo(ILayerManager layerManager) {
        layerManager.addLayerListener(layerListener);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#clear()
	 */
    @Override
	public void clear() {
        boolean originalPanelUpdatesEnabled = arePanelUpdatesEnabled();
        setPanelUpdatesEnabled(false);
        try {
            for (Iterator i = selections.iterator(); i.hasNext();) {
                AbstractSelection selection = (AbstractSelection) i.next();
                selection.unselectItems();
            }
        } finally {
            setPanelUpdatesEnabled(originalPanelUpdatesEnabled);
        }
        updatePanel();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#getFeatureSelection()
	 */
    @Override
	public FeatureSelection getFeatureSelection() {
        return featureSelection;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#getLineStringSelection()
	 */
    @Override
	public LineStringSelection getLineStringSelection() {
        return lineStringSelection;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#getSelections()
	 */
    @Override
	public Collection getSelections() {
        return selections;
    }

    private List selections;
    
    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#getSelectedItems()
	 */
	public Collection getSelectedItems() {
        ArrayList selectedItems = new ArrayList();
        for (Iterator i = selections.iterator(); i.hasNext();) {
            AbstractSelection selection = (AbstractSelection) i.next();
            selectedItems.addAll(selection.getSelectedItems());
        }
        return selectedItems;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#getSelectedItems(com.vividsolutions.jump.workbench.model.Layer)
	 */
	public Collection getSelectedItems(ILayer layer) {
        ArrayList selectedItems = new ArrayList();
        for (Iterator i = selections.iterator(); i.hasNext();) {
            AbstractSelection selection = (AbstractSelection) i.next();
            selectedItems.addAll(selection.getSelectedItems((Layer)layer));
        }
        return selectedItems;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#getSelectedItems(com.vividsolutions.jump.workbench.model.Layer, com.vividsolutions.jump.feature.Feature)
	 */
 	public Collection getSelectedItems(ILayer layer, Feature feature) {
        ArrayList selectedItems = new ArrayList();
        for (Iterator i = selections.iterator(); i.hasNext();) {
            AbstractSelection selection = (AbstractSelection) i.next();
            selectedItems.addAll(selection.getSelectedItems((Layer)layer, feature));
        }
        return selectedItems;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#getSelectedItems(com.vividsolutions.jump.workbench.model.Layer, com.vividsolutions.jump.feature.Feature, com.vividsolutions.jts.geom.Geometry)
	 */
	public Collection getSelectedItems(ILayer layer, Feature feature, Geometry geometry) {
        ArrayList selectedItems = new ArrayList();
        for (Iterator i = selections.iterator(); i.hasNext();) {
            AbstractSelection selection = (AbstractSelection) i.next();
            selectedItems.addAll(selection.getSelectedItems((Layer)layer, feature, geometry));
        }
        return selectedItems;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#getLayersWithSelectedItems()
	 */
    @Override
	public Collection getLayersWithSelectedItems() {
        HashSet layersWithSelectedItems = new HashSet();
        for (Iterator i = selections.iterator(); i.hasNext();) {
            AbstractSelection selection = (AbstractSelection) i.next();
            layersWithSelectedItems.addAll(selection.getLayersWithSelectedItems());
        }
        return layersWithSelectedItems;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#getPartSelection()
	 */
    @Override
	public PartSelection getPartSelection() {
        return partSelection;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#updatePanel()
	 */
    @Override
	public void updatePanel() {
        if (!panelUpdatesEnabled) {
            return;
        }
        panel.fireSelectionChanged();
        panel.getRenderingManager().render(SelectionBackgroundRenderer.CONTENT_ID,true);
        for (Iterator i = selections.iterator(); i.hasNext();) {
            AbstractSelection selection = (AbstractSelection) i.next();
            panel.getRenderingManager().render(selection.getRendererContentID(),true);
        }
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#setPanelUpdatesEnabled(boolean)
	 */
    @Override
	public void setPanelUpdatesEnabled(boolean panelUpdatesEnabled) {
        this.panelUpdatesEnabled = panelUpdatesEnabled;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#getFeaturesWithSelectedItems(com.vividsolutions.jump.workbench.model.Layer)
	 */
    public Collection getFeaturesWithSelectedItems(ILayer layer) {
        HashSet featuresWithSelectedItems = new HashSet();
        for (Iterator i = selections.iterator(); i.hasNext();) {
            AbstractSelection selection = (AbstractSelection) i.next();
            featuresWithSelectedItems.addAll(selection.getFeaturesWithSelectedItems((Layer)layer));
        }
        return featuresWithSelectedItems;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#unselectItems(com.vividsolutions.jump.workbench.model.Layer)
	 */
    public void unselectItems(ILayer layer) {
        boolean originalPanelUpdatesEnabled = arePanelUpdatesEnabled();
        setPanelUpdatesEnabled(false);
        try {
            for (Iterator i = selections.iterator(); i.hasNext();) {
                AbstractSelection selection = (AbstractSelection) i.next();
                selection.unselectItems((Layer)layer);
            }
        } finally {
            setPanelUpdatesEnabled(originalPanelUpdatesEnabled);
        }
        updatePanel();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#unselectItems(com.vividsolutions.jump.workbench.model.Layer, java.util.Collection)
	 */
	public void unselectItems(ILayer layer, Collection features) {
        boolean originalPanelUpdatesEnabled = arePanelUpdatesEnabled();
        setPanelUpdatesEnabled(false);
        try {
            for (Iterator i = selections.iterator(); i.hasNext();) {
                AbstractSelection selection = (AbstractSelection) i.next();
                selection.unselectItems((Layer)layer, features);
            }
        } finally {
            setPanelUpdatesEnabled(originalPanelUpdatesEnabled);
        }
        updatePanel();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#unselectFromFeaturesWithModifiedItemCounts(com.vividsolutions.jump.workbench.model.Layer, java.util.Collection, java.util.Collection)
	 */
	public void unselectFromFeaturesWithModifiedItemCounts(
        ILayer layer,
        Collection features,
        Collection oldFeatureClones) {
        boolean originalPanelUpdatesEnabled = arePanelUpdatesEnabled();
        setPanelUpdatesEnabled(false);
        try {
            for (Iterator i = selections.iterator(); i.hasNext();) {
                AbstractSelection selection = (AbstractSelection) i.next();
                selection.unselectFromFeaturesWithModifiedItemCounts(
                    (Layer)layer,
                    features,
                    oldFeatureClones);
            }
        } finally {
            setPanelUpdatesEnabled(originalPanelUpdatesEnabled);
        }
        updatePanel();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#getFeaturesWithSelectedItems()
	 */
    @Override
	public Collection getFeaturesWithSelectedItems() {
        ArrayList featuresWithSelectedItems = new ArrayList();
        for (Iterator i = getLayersWithSelectedItems().iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();
            featuresWithSelectedItems.addAll(getFeaturesWithSelectedItems(layer));
        }
        return featuresWithSelectedItems;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#arePanelUpdatesEnabled()
	 */
    @Override
	public boolean arePanelUpdatesEnabled() {
        return panelUpdatesEnabled;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ISelectionManager#dispose()
	 */
    @Override
	public void dispose() {
    	if ((layerManagerProxy!=null) && (layerManagerProxy.getLayerManager()!=null))
        	layerManagerProxy.getLayerManager().removeLayerListener(layerListener);    
    }

}
