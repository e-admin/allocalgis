package com.geopista.editor;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.geopista.model.GeopistaLayerManager;
import com.geopista.model.GeopistaMap;

import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;


public class LightGeopistaEditor {
    private static final Log logger = LogFactory.getLog(LightGeopistaEditor.class);
	private GeopistaMap task = new GeopistaMap(); // Representa en proyecto con el que se trabaja en este componente
	private GeopistaLayerManager layerManager = (GeopistaLayerManager) task.getLayerManager();
	  
	
	public LightGeopistaEditor()
    {
            task.setName("Default Map");
	        task.setExtracted(true);
	        task.setTimeStamp(new Date());
	        task.setDescription("Geopista Default Map");
	        layerManager.addCategory(StandardCategoryNames.SYSTEM); // Añade la categoría por defecto
	}
	public LayerManager getLayerManager() {
	      return layerManager;
	}
	public Collection searchByAttribute(String layerName, String attributeName, String value)
	{
	      Collection finalFeatures = new ArrayList();
	      Layer localLayer = layerManager.getLayer(layerName);
	      Iterator allFeaturesListIter =localLayer.getFeatureCollectionWrapper().getFeatures().iterator();
	      while(allFeaturesListIter.hasNext())
	      {
	    	  	Feature localFeature = (Feature)allFeaturesListIter.next();
	    	  	String nombreAtributo = localFeature.getString(attributeName).trim();
	    	  	if(nombreAtributo.equals(value))
	    	  			finalFeatures.add(localFeature);
	      }
	      return finalFeatures;
	 }
}
