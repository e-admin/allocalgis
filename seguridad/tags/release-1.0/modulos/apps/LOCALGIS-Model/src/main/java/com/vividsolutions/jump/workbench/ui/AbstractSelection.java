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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.geopista.model.IGeopistaLayer;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.CollectionMap;
import com.vividsolutions.jump.workbench.model.ILayer;

    /**
     * Superclass for holding a user-selected collection of {@link
     * Feature} items.
     */

public abstract class AbstractSelection implements IAbstractSelection {
    private Map layerMap = new HashMap();

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#getRendererContentID()
	 */
    @Override
	public abstract String getRendererContentID();

    private AbstractSelection child;

    private AbstractSelection parent;

    private ISelectionManager selectionManager;

    public AbstractSelection(ISelectionManager selectionManager) {
        this.selectionManager = selectionManager;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#items(com.vividsolutions.jts.geom.Geometry)
	 */
    @Override
	public abstract List items(Geometry geometry);

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#items(com.vividsolutions.jts.geom.Geometry, java.util.Collection)
	 */
    @Override
	public Collection items(Geometry geometry, Collection indices) {
        List allItems = items(geometry);
        ArrayList items = new ArrayList();
        for (Iterator i = indices.iterator(); i.hasNext();) {
            Integer index = (Integer) i.next();
            items.add(allItems.get(index.intValue()));
        }
        return items;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#getFeatureToSelectedItemIndexCollectionMap(com.vividsolutions.jump.workbench.model.ILayer)
	 */
    @Override
	public CollectionMap getFeatureToSelectedItemIndexCollectionMap(ILayer layer) {
        if (!layerMap.containsKey(layer)) {
            layerMap.put(layer, new CollectionMap(HashMap.class, HashSet.class));
        }
        return (CollectionMap) layerMap.get(layer);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#getSelectedItemIndices(com.vividsolutions.jump.workbench.model.ILayer, com.vividsolutions.jump.feature.Feature)
	 */
    @Override
	public Collection getSelectedItemIndices(ILayer layer, Feature feature) {
        Collection indices = getFeatureToSelectedItemIndexCollectionMap(layer).getItems(feature);
        return indices == null ? new ArrayList() : indices;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#getFeatureToSelectedItemCollectionMap(com.vividsolutions.jump.workbench.model.ILayer)
	 */
    @Override
	public CollectionMap getFeatureToSelectedItemCollectionMap(ILayer layer) {
        CollectionMap collectionMap = new CollectionMap();
        for (Iterator i = getFeatureToSelectedItemIndexCollectionMap(layer).keySet().iterator();
            i.hasNext();
            ) {
            Feature feature = (Feature) i.next();
            collectionMap.put(
                feature,
                items(
                    feature.getGeometry(),
                    getFeatureToSelectedItemIndexCollectionMap(layer).getItems(feature)));
        }
        return collectionMap;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#getILayersWithSelectedItems()
	 */
    @Override
	public Collection getLayersWithSelectedItems() {
        ArrayList layersWithSelectedItems = new ArrayList();
        for (Iterator i = layerMap.keySet().iterator(); i.hasNext();) {
            ILayer layer = (ILayer) i.next();
            if (!getFeaturesWithSelectedItems(layer).isEmpty()) {
                layersWithSelectedItems.add(layer);
            }
        }
        return layersWithSelectedItems;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#getFeaturesWithSelectedItems()
	 */
    @Override
	public Collection getFeaturesWithSelectedItems() {
        ArrayList featuresWithSelectedItems = new ArrayList();
        for (Iterator i = layerMap.keySet().iterator(); i.hasNext();) {
            ILayer layer = (ILayer) i.next();
            featuresWithSelectedItems.addAll(getFeaturesWithSelectedItems(layer));
        }
        return featuresWithSelectedItems;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#getFeaturesWithSelectedItems(com.vividsolutions.jump.workbench.model.ILayer)
	 */
    @Override
	public Collection getFeaturesWithSelectedItems(ILayer layer) {
        ArrayList featuresWithSelectedItems = new ArrayList();
        for (Iterator i = getFeatureToSelectedItemIndexCollectionMap(layer).keySet().iterator();
            i.hasNext();
            ) {
            Feature feature = (Feature) i.next();
            if (!getFeatureToSelectedItemIndexCollectionMap(layer).getItems(feature).isEmpty()) {
                featuresWithSelectedItems.add(feature);
            }
        }
        return featuresWithSelectedItems;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#getSelectedItems()
	 */
    @Override
	public Collection getSelectedItems() {
        ArrayList selectedItems = new ArrayList();
        for (Iterator i = new ArrayList(layerMap.keySet()).iterator(); i.hasNext();) {
            ILayer layer = (ILayer) i.next();
            selectedItems.addAll(getSelectedItems(layer));
        }
        return selectedItems;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#getSelectedItems(com.vividsolutions.jump.workbench.model.ILayer)
	 */
    @Override
	public Collection getSelectedItems(ILayer layer) {
        ArrayList selectedItems = new ArrayList();
        for (Iterator i = getFeatureToSelectedItemIndexCollectionMap(layer).keySet().iterator();
            i.hasNext();
            ) {
            Feature feature = (Feature) i.next();
            selectedItems.addAll(getSelectedItems(layer, feature));
        }
        return selectedItems;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#getSelectedItems(com.vividsolutions.jump.workbench.model.ILayer, com.vividsolutions.jump.feature.Feature)
	 */
    @Override
	public Collection getSelectedItems(ILayer layer, Feature feature) {
        return getSelectedItems(layer, feature, feature.getGeometry());
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#getSelectedItems(com.vividsolutions.jump.workbench.model.ILayer, com.vividsolutions.jump.feature.Feature, com.vividsolutions.jts.geom.Geometry)
	 */
    @Override
	public Collection getSelectedItems(ILayer layer, Feature feature, Geometry geometry) {
        return items(
            geometry,
            getFeatureToSelectedItemIndexCollectionMap(layer).getItems(feature));
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#indices(com.vividsolutions.jts.geom.Geometry, java.util.Collection)
	 */
    @Override
	public Collection indices(Geometry geometry, Collection items) {
        List allItems = items(geometry);
        ArrayList indices = new ArrayList();
        for (Iterator i = items.iterator(); i.hasNext();) {
            Object item = i.next();
            indices.add(new Integer(allItems.indexOf(item)));
        }
        return indices;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#unselectItems(com.vividsolutions.jump.workbench.model.ILayer, com.vividsolutions.jump.util.CollectionMap)
	 */
    @Override
	public void unselectItems(ILayer layer, CollectionMap featureToItemCollectionMap) {
        boolean originalPanelUpdatesEnabled = selectionManager.arePanelUpdatesEnabled();
        selectionManager.setPanelUpdatesEnabled(false);
        try {
            for (Iterator i = featureToItemCollectionMap.keySet().iterator(); i.hasNext();) {
                Feature feature = (Feature) i.next();
                unselectItems(layer, feature, featureToItemCollectionMap.getItems(feature));
            }
        } finally {
            selectionManager.setPanelUpdatesEnabled(originalPanelUpdatesEnabled);
        }
        updatePanel();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#selectItems(com.vividsolutions.jump.workbench.model.ILayer, com.vividsolutions.jump.util.CollectionMap)
	 */
    @Override
	public void selectItems(ILayer layer, CollectionMap featureToItemCollectionMap) {
        boolean originalPanelUpdatesEnabled = selectionManager.arePanelUpdatesEnabled();
        selectionManager.setPanelUpdatesEnabled(false);
        try {
            for (Iterator i = featureToItemCollectionMap.keySet().iterator(); i.hasNext();) {
                Feature feature = (Feature) i.next();
                selectItems(layer, feature, featureToItemCollectionMap.getItems(feature));
            }
        } finally {
            selectionManager.setPanelUpdatesEnabled(originalPanelUpdatesEnabled);
        }
        updatePanel();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#selectItems(com.vividsolutions.jump.workbench.model.ILayer, com.vividsolutions.jump.feature.Feature, java.util.Collection)
	 */
    @Override
	public void selectItems(ILayer layer, Feature feature, Collection items) {
    	
    	
    	if (layer instanceof IGeopistaLayer && !((IGeopistaLayer)layer).isActiva()) {
            return;
        }
    	
        Collection itemsToSelect = itemsNotSelectedInAncestors(layer, feature, items);
        boolean originalPanelUpdatesEnabled = selectionManager.arePanelUpdatesEnabled();
        selectionManager.setPanelUpdatesEnabled(false);
        try {
            unselectInDescendants(layer, feature, itemsToSelect);
            getFeatureToSelectedItemIndexCollectionMap(layer).addItems(
                feature,
                indices(feature.getGeometry(), itemsToSelect));
        } finally {
            selectionManager.setPanelUpdatesEnabled(originalPanelUpdatesEnabled);
        }
        updatePanel();
    }
    
    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#unselectItems(com.vividsolutions.jump.workbench.model.ILayer, com.vividsolutions.jump.feature.Feature, java.util.Collection)
	 */
    @Override
	public void unselectItems(ILayer layer, Feature feature, Collection items) {
        boolean originalPanelUpdatesEnabled = selectionManager.arePanelUpdatesEnabled();
        selectionManager.setPanelUpdatesEnabled(false);
        try {
            getFeatureToSelectedItemIndexCollectionMap(layer).removeItems(
                feature,
                indices(feature.getGeometry(), items));
        } finally {
            selectionManager.setPanelUpdatesEnabled(originalPanelUpdatesEnabled);
        }
        updatePanel();
    }    

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#itemsNotSelectedInAncestors(com.vividsolutions.jump.workbench.model.ILayer, com.vividsolutions.jump.feature.Feature, java.util.Collection)
	 */
    @Override
	public Collection itemsNotSelectedInAncestors(ILayer layer, Feature feature, Collection items) {
        ArrayList itemsNotSelectedInAncestors = new ArrayList();
        for (Iterator i = items.iterator(); i.hasNext();) {
            Geometry item = (Geometry) i.next();
            if (!selectedInAncestors(layer, feature, item)) {
                itemsNotSelectedInAncestors.add(item);
            }
        }
        return itemsNotSelectedInAncestors;
    }

    protected abstract boolean selectedInAncestors(ILayer layer, Feature feature, Geometry item);

    protected abstract void unselectInDescendants(ILayer layer, Feature feature, Collection items);

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#selectItems(com.vividsolutions.jump.workbench.model.ILayer, com.vividsolutions.jump.feature.Feature)
	 */
    @Override
	public void selectItems(ILayer layer, Feature feature) {
        selectItems(layer, feature, items(feature.getGeometry()));
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#selectItems(com.vividsolutions.jump.workbench.model.ILayer, java.util.Collection)
	 */
    @Override
	public void selectItems(ILayer layer, Collection features) {
        boolean originalPanelUpdatesEnabled = selectionManager.arePanelUpdatesEnabled();
        selectionManager.setPanelUpdatesEnabled(false);
        try {
            for (Iterator i = features.iterator(); i.hasNext();) {
                Feature feature = (Feature) i.next();
                selectItems(layer, feature);
            }
        } finally {
            selectionManager.setPanelUpdatesEnabled(originalPanelUpdatesEnabled);
        }
        updatePanel();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#unselectFromFeaturesWithModifiedItemCounts(com.vividsolutions.jump.workbench.model.ILayer, java.util.Collection, java.util.Collection)
	 */
    @Override
	public void unselectFromFeaturesWithModifiedItemCounts(
        ILayer layer,
        Collection features,
        Collection oldFeatureClones) {
        ArrayList featuresToUnselect = new ArrayList();
        Iterator j = oldFeatureClones.iterator();
        j.hasNext();
        for (Iterator i = features.iterator(); i.hasNext();) {
            Feature feature = (Feature) i.next();
            Feature oldFeatureClone = (Feature) j.next();
            if (items(feature.getGeometry()).size()
                != items(oldFeatureClone.getGeometry()).size()) {
                featuresToUnselect.add(feature);
            }
        }
        unselectItems(layer, featuresToUnselect);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#unselectItems()
	 */
    @Override
	public void unselectItems() {
        layerMap.clear();
        updatePanel();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#unselectItems(com.vividsolutions.jump.workbench.model.ILayer)
	 */
    @Override
	public void unselectItems(ILayer layer) {
        layerMap.remove(layer);
        updatePanel();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#unselectItems(com.vividsolutions.jump.workbench.model.ILayer, java.util.Collection)
	 */
    @Override
	public void unselectItems(ILayer layer, Collection features) {
        boolean originalPanelUpdatesEnabled = selectionManager.arePanelUpdatesEnabled();
        selectionManager.setPanelUpdatesEnabled(false);
        try {
            for (Iterator i = features.iterator(); i.hasNext();) {
                Feature feature = (Feature) i.next();
                unselectItems(layer, feature);
            }
        } finally {
            selectionManager.setPanelUpdatesEnabled(originalPanelUpdatesEnabled);
        }
        updatePanel();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#unselectItems(com.vividsolutions.jump.workbench.model.ILayer, com.vividsolutions.jump.feature.Feature)
	 */
    @Override
	public void unselectItems(ILayer layer, Feature feature) {
        getFeatureToSelectedItemIndexCollectionMap(layer).remove(feature);
        updatePanel();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#unselectItem(com.vividsolutions.jump.workbench.model.ILayer, com.vividsolutions.jump.feature.Feature, int)
	 */
    @Override
	public void unselectItem(ILayer layer, Feature feature, int selectedItemIndex) {
        getFeatureToSelectedItemIndexCollectionMap(layer).removeItem(
            feature,
            new Integer(selectedItemIndex));
    }

    private void updatePanel() {
        selectionManager.updatePanel();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#setChild(com.vividsolutions.jump.workbench.ui.AbstractSelection)
	 */
    @Override
	public void setChild(AbstractSelection child) {
        this.child = child;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IAbstractSelection#setParent(com.vividsolutions.jump.workbench.ui.AbstractSelection)
	 */
    @Override
	public void setParent(AbstractSelection parent) {
        this.parent = parent;
    }

    protected AbstractSelection getChild() {
        return child;
    }

    protected AbstractSelection getParent() {
        return parent;
    }

}
