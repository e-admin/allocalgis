/**
 * SelectOneItemTool.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.ui.cursortool.selectoneitem;

import java.awt.geom.NoninvertibleTransformException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;

import com.geopista.editor.GeopistaEditor;
import com.geopista.ui.cursortool.selectoneitem.images.IconLoader;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.cursortool.SelectTool;
import com.vividsolutions.jump.workbench.ui.renderer.FeatureSelectionRenderer;

public class SelectOneItemTool extends SelectTool {
	    
    private LayerViewPanel layerViewPanel;
    private int maxFID = 2147483647;
    private int highFID = 0;
    Layer botLayer = null;
    Feature botFeature = null;
    boolean featureSelected = false;
    
    HashMap fidSeleccionados=new HashMap();
    
    public SelectOneItemTool() {
        super(FeatureSelectionRenderer.CONTENT_ID);
    }

    public Icon getIcon() {
    	return IconLoader.icon("SelectOne.gif");
    }

    public String getName() {
        return I18N.get("Select-One-Item");
    }
    
    private void reset()
    {
        maxFID = 2147483647;
        highFID = 0;
        botLayer = null;
        botFeature = null;
        featureSelected = false;
        fidSeleccionados.clear();
    }
    
    public void activate(ILayerViewPanel layerViewPanel) {
        this.layerViewPanel = (LayerViewPanel)layerViewPanel;
        super.activate(layerViewPanel);
        selection = this.layerViewPanel.getSelectionManager().getFeatureSelection();
        reset();
    }

    protected void gestureFinished() throws NoninvertibleTransformException
    {
        super.gestureFinished();
        List layerList = layerViewPanel.getLayerManager().getVisibleLayers(false);
        Layer topLayer = null;
        Feature topFeature = null;
        featureSelected = false;
        
        
        for (Iterator i = layerList.iterator(); i.hasNext();)
        {
            Layer layer = (Layer) i.next();
            Collection selectedFeatures = layerViewPanel.getSelectionManager().getFeaturesWithSelectedItems(layer);
            
            for (Iterator j = selectedFeatures.iterator(); j.hasNext();)
            {
                featureSelected = true;
                Feature feature = (Feature) j.next();
                int fID = feature.getID();
                layerViewPanel.getSelectionManager().getFeatureSelection().unselectItems(layer, feature);
                //System.out.println("Comparando: fID:"+fID+" con highFID:"+highFID+" maxFID:"+maxFID+" Capa:"+layer);
                if ((fID > highFID) && (fID < maxFID))
                {
                    topLayer = layer;
                    topFeature = feature;
                    highFID = fID;
                }
            }
        }
        
        //Si el FID ya ha sido seleccionado antes. Reiniciamos el contador
        if (fidSeleccionados.get(highFID)!=null){
        	 reset();
        }
        fidSeleccionados.put(highFID,highFID);

        
        if (!featureSelected) reset();
        
        //System.out.println("highFID:"+highFID+" maxFID:"+maxFID);
        
        if ((topLayer != null) && (topFeature != null))
        {
            if (highFID > 0) maxFID = highFID;
            highFID = 0;
            botLayer = topLayer;
            botFeature = topFeature;
            layerViewPanel.getSelectionManager().getFeatureSelection().selectItems(topLayer, topFeature);
            
            if(getIWorkbench().getGuiComponent() instanceof GeopistaEditor)
            {
              ((GeopistaEditor) getIWorkbench().getGuiComponent()).fireGeopistaSelectionChanged(layerViewPanel.getSelectionManager().getFeatureSelection());
            }
            //System.out.println("Seleccionado:"+topLayer);
        }
        else if ((botLayer != null) && (botFeature != null))
        {
            layerViewPanel.getSelectionManager().getFeatureSelection().selectItems(botLayer, botFeature);
            
            if(getIWorkbench().getGuiComponent() instanceof GeopistaEditor)
            {
              ((GeopistaEditor) getIWorkbench().getGuiComponent()).fireGeopistaSelectionChanged(layerViewPanel.getSelectionManager().getFeatureSelection());
            }
            //System.out.println("Seleccionado2:"+botLayer);
        }
    }
}
