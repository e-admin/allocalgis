package com.geopista.app.eiel.utils;

import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.panels.EIELInfoPanel;
import com.geopista.app.eiel.panels.GeopistaEditorPanel;
import com.geopista.app.eiel.panels.TreeRendereEIELDomains;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.reload.ReloadLayerPlugIn;
import com.vividsolutions.jump.I18N;

public class LoadedLayers {

	
	public static void contains(){
		
	}

	public static String getName(String patronSelected) {
		String layerName=ConstantesLocalGISEIEL.modelosCapas.get(patronSelected);
		if (layerName==null)
			layerName=ConstantesLocalGISEIEL.traduccionesEspeciales.get(layerName);
		
		return layerName;
	}

	/**
	 * Buscamos la capa cargada en funcion del nombre de la capa. Si existe la capa "TC" nos permite
	 * insertar.
	 * @param layerName
	 * @return
	 */
	public static GeopistaLayer getLayer(String patternSelected) {
		
		String layerName=getName(patternSelected);		
		 GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(layerName);
		 //Si no existe la capa la buscamos con nombre <ID>_TC que significa que es la capa que tiene mas informacion de atributos
		 //la cual se utiliza para publicar la informacion
		 if (geopistaLayer==null){
			 layerName=layerName+"_TC";
			 geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(layerName);
		 }
		 
		return geopistaLayer;
	}
	
	
	public static GeopistaLayer getLayerOrGlobal(String layerName) {
		 GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(layerName);
		 //Si no existe la capa la buscamos con nombre <ID>_TC que significa que es la capa que tiene mas informacion de atributos
		 //la cual se utiliza para publicar la informacion
		 if (geopistaLayer==null){
			 layerName=layerName+"_TC";
			 geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(layerName);
		 }
		 
		return geopistaLayer;
	}
	
	
	/**
	 * Devuelve la capa para poder escribir en ella. En este caso no vale que exista solo la capa que termina en TC
	 * @param layerName
	 * @return
	 * @throws Exception
	 */
	private static GeopistaLayer getLayerToStore(String layerName) throws LayerNotFoundException{
		 GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(layerName);
		 if (geopistaLayer==null)
			 throw new LayerNotFoundException();
		 return geopistaLayer;
	}
	
	public static GeopistaLayer forceLoadLayer(String patternSelected, EIELInfoPanel eielInfoPanel) throws Exception{
    	
    	GeopistaLayer geopistaLayer=null;
    	
    	
    	String clave=ConstantesLocalGISEIEL.modelosCapas.get(patternSelected);
    	
    	//Verificamos en primer lugar si la capa grafica esta cargada. si no lo está la cargamos
    	//para evitar problemas.
    	if (!ConstantesLocalGISEIEL.loadedLayers.containsKey(clave)){
    		//Si el mapa no es el global la capa esta cargada en caso contrario
    		//puede no estar cargada.
    		if (ConstantesLocalGISEIEL.GLOBAL_MAP){
        		String layerName=ConstantesLocalGISEIEL.modelosCapas.get(patternSelected);
        		geopistaLayer=LoadedLayers.getLayerToStore(layerName);
       		 	if (geopistaLayer!=null){
            		TreeRendereEIELDomains renderer=(TreeRendereEIELDomains)eielInfoPanel.getTree().getCellRenderer();
            		new ReloadLayerPlugIn().loadFeatures(geopistaLayer, GeopistaEditorPanel.getEditor().getLayerViewPanel(),false);
			 		//Modificamos el icono para indicar que se ha cargado la capa correctamente
    				renderer.setIconoLoaded(eielInfoPanel.getPatronSelected(),true);
    				ConstantesLocalGISEIEL.loadedLayers.put(layerName,true);
       		 	}	        		 	
    		}
    		else{
    			String layerName=ConstantesLocalGISEIEL.modelosCapas.get(patternSelected);
        		geopistaLayer=LoadedLayers.getLayerToStore(layerName);
    		}
    	}    
    	else{
    		String layerName=ConstantesLocalGISEIEL.modelosCapas.get(patternSelected);
    		geopistaLayer=LoadedLayers.getLayerToStore(layerName);
    	}
    	return geopistaLayer;
    }

	public static void remove() {
		ConstantesLocalGISEIEL.loadedLayers.clear();
		
	}
	
	
}
