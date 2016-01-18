package com.geopista.ui.dialogs.global;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import com.geopista.model.IGeopistaLayer;


public class Utils {

	private final static Logger logger = Logger.getLogger(Utils.class);
	
	/**
	 * Busca un elemento en un array
	 * 
	 * @param objArray
	 * @param obj
	 * @return
	 */
	public static boolean isInArray(Object[] objArray, Object obj) {
		for (int i = 0; i < objArray.length; i++) {
			if (obj.equals(objArray[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Busca un elemento entre dos arrays
	 * 
	 * @param objArray
	 * @param objArray2
	 * @return
	 */
	public static boolean isInArray(Object[] objArray, Object[] objArray2) {
		for (int j = 0; j < objArray2.length; j++) {	
			if (Utils.isInArray(objArray,objArray2[j])) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<String> getExtractLayers(List<IGeopistaLayer> allExtractLayers) {
		ArrayList<String> extractLayers = new ArrayList<String>();
		IGeopistaLayer geoLayer = null;
		for (int i = 0; i < allExtractLayers.size(); i++) {
			geoLayer = allExtractLayers.get(i);
			extractLayers.add(geoLayer.getSystemId());		
		}
		return extractLayers;
	}
	
	/**
	 * Indica si entre las capas desconectadas se encuentran una capa
	 * concreta
	 * 
	 * @param extractLayersNames
	 * @param layerName
	 * @return
	 */
	public static boolean existen(ArrayList<String> extractLayersNames,
			String layerName) {
		Iterator it = extractLayersNames.iterator();
		while(it.hasNext()){
			String extractLayerName = (String) it.next();
			if (extractLayerName.equals(layerName)) {
				return true;
			}
		}
		return false;
	}	
	
	/**
	 * Indica si entre las capas desconectadas se encuentra un conjunto de capas
	 * concretas
	 * 
	 * @param extractLayersNames
	 * @param layerNames
	 * @return
	 */
	public static boolean existen(ArrayList<String> extractLayersNames,
			String [] layerNames) {
		Iterator it = extractLayersNames.iterator();
		while(it.hasNext()){
			String extractLayerName = (String) it.next();
			if (Utils.isInArray(layerNames,extractLayerName)) {
				return true;
			}
		}
		return false;
	}	
	
}
