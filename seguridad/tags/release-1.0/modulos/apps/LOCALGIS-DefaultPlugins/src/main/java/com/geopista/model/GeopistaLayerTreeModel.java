package com.geopista.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.TreePath;

import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.util.SimpleTreeModel;
import com.vividsolutions.jump.workbench.model.Category;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.ui.renderer.style.ColorThemingStyle;

public class GeopistaLayerTreeModel  extends SimpleTreeModel {

    public static class Root {
        private Root() {
        }
    }

    private LayerManagerProxy layerManagerProxy;

    public GeopistaLayerTreeModel(LayerManagerProxy layerManagerProxy) {
        super(new Root());
        this.layerManagerProxy = layerManagerProxy;
    }

    public List getChildren(Object parent) {
        if (parent == getRoot()) {
             return layerManagerProxy.getLayerManager().getLayers();
        }
        if (parent instanceof Layerable) {

           Map colorTheming =  ColorThemingStyle.get((Layer) parent).getAttributeValueToBasicStyleMap();
           Set fieldsNames = colorTheming.entrySet();

           /*Iterator fieldsNamesIterator = fieldsNames.iterator();
           ArrayList fields = new ArrayList();
           while(fieldsNamesIterator.hasNext())
           {
              String name = (String) fieldsNamesIterator.next();
              fields.add(name);
           }*/
            return new ArrayList(fieldsNames);
        }
        if(parent instanceof Map.Entry)
        {
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

}


