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
package com.vividsolutions.jump.workbench.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreePath;

import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.util.SimpleTreeModel;

/**
 * JTree model for displaying the Layers, WMSLayers, and other Layerables
 * contained in a LayerManager.
 */
public class LayerTreeModel extends SimpleTreeModel {

    public static class Root {
        private Root() {
        }
    }

    private LayerManagerProxy layerManagerProxy;

    public LayerTreeModel(LayerManagerProxy layerManagerProxy) {
        super(new Root());
        this.layerManagerProxy = layerManagerProxy;
    }

    public List getChildren(Object parent) {
        if (parent == getRoot()) {
            return layerManagerProxy.getLayerManager().getCategories();
        }
        if (parent instanceof Category) {
            return ((Category) parent).getLayerables();
        }
        if (parent instanceof Layerable) {
            return new ArrayList();
        }
        Assert.shouldNeverReachHere(parent.getClass().getName());

        return null;
    }
    
    public void valueForPathChanged(TreePath path, Object newValue) {
        if (path.getLastPathComponent() instanceof Layerable) {
            ((Layerable)path.getLastPathComponent()).setName((String)newValue);
        }
        else if (path.getLastPathComponent() instanceof Category) {
            ((Category)path.getLastPathComponent()).setName((String)newValue);
        }
        else {
            Assert.shouldNeverReachHere();
        }
    }    
    
    /**
    	Destruccion del elemento.
    	@baseSatec Destruccion del elemento.
    */
    public void dispose(){
    	layerManagerProxy=null;
    	removeAllListeners();
    }

}
