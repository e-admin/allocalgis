package com.geopista.ui.cursortool;


import java.awt.Color;
import java.awt.Cursor;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.geopista.model.GeopistaLayer;
import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.model.FenceLayerFinder;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.IInfoFrame;
import com.vividsolutions.jump.workbench.ui.InfoFrame;
import com.vividsolutions.jump.workbench.ui.cursortool.SpecifyFeaturesTool;

public class GeopistaFeatureInfoTool extends SpecifyFeaturesTool {

    public static final ImageIcon ICON = IconLoader.icon("Info.gif");
    public GeopistaFeatureInfoTool() {
        setColor(Color.magenta);
    }

    public Icon getIcon() {
        return ICON;
    }

    public Cursor getCursor() {
        return createCursor(IconLoader.icon("InfoCursor.gif").getImage());
    }

    protected void gestureFinished() throws Exception {
        reportNothingToUndoYet();

        IInfoFrame infoFrame = getTaskFrame().getInfoFrame();
       //InfoFrame infoFrame = new InfoFrame();

        if (!wasShiftPressed()) {
            infoFrame.getModel().clear();
        }

        for (Iterator i = layerToSpecifiedFeaturesMap().keySet().iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();
            if (layer.getName().equals(FenceLayerFinder.LAYER_NAME)) {
                continue;
            }
            if(((GeopistaLayer)layer).isActiva() ==false) { 
              continue;
            }
            
            Collection features = (Collection) layerToSpecifiedFeaturesMap().get(layer);
            infoFrame.getModel().add(layer, features);
        }

        infoFrame.surface();
    }
}

