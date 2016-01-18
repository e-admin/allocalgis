/**
 * AutoPublish.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.wm.functions;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.exceptions.LocalgisWMSException;
import com.localgis.web.core.manager.LocalgisMapsConfigurationManager;
import com.localgis.web.core.model.GeopistaMap;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.core.wm.util.LocalgisManagerBuilderSingleton;

public class AutoPublish {
	
	private static Log logger = LogFactory.getLog(AutoPublish.class);
	
	public static Integer autoPublish(Integer idEntidad, Integer idMunicipio, String featureType, Boolean publicMap){
		Integer idMap = null;
		Integer idMapPublish = null;
		idMapPublish = isTypePublish(idEntidad, featureType, publicMap);		
		if(idMapPublish==null){ //Si no está publicado
			//Si no está publicado
			idMapPublish = mapTypeRelationPublish(idEntidad, featureType, publicMap);		
		} 	
		if(idMapPublish!=null){	
			//idMap = getMapTypeRelation(idMunicipio, featureType);	
//			if(idMap==null){		
//				//Se inserta el registro relacional
//				//insertMapTypeRelation(idMunicipio, featureType,idMapPublish);
//			}else if(!idMapPublish.equals(idMap)){
//				//updateMapTypeRelation(idMunicipio, featureType,idMapPublish);
//			}
			idMap = idMapPublish;
		}
		//---BORRAR----->
//		if(idMap==null){
//			idMap=0;
//		}
		//--FIN BORRAR-->
		return idMap;
	}
	
	//Esta en entidad_tipo_map
//	public static Integer getMapTypeRelation(Integer idMunicipio, String featureType) {
//		Integer idMap = null;
//		try {
//			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
//			LocalgisAutoPublishManager localgisAutoPublishManager = localgisManagerBuilderGeoWfst.getLocalgisAutoPublishManager();
//			idMap = localgisAutoPublishManager.getIdMap(idMunicipio,featureType);
//		} catch (LocalgisConfigurationException e) {
//			System.out.println(e.getMessage());
//			logger.error(e.getMessage());
//		} catch (LocalgisInitiationException e) {
//			System.out.println(e.getMessage());
//			logger.error(e.getMessage());
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			logger.error(e.getMessage());
//		}	      
//		return idMap;		
//	}	
	
	//Esta publicado en localgis wms manager (guia urbana)
	public static Integer isTypePublish(Integer idEntidad, String featureType, Boolean publicMap) {
		Integer idMap=null;
		try {
			LocalgisMapsConfigurationManager localgisMapsConfigurationManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
			List localgisPublishedMapsList = localgisMapsConfigurationManager.getPublishedMaps(idEntidad,publicMap);
			Iterator iteratorAvailable = localgisPublishedMapsList.iterator();
			while(iteratorAvailable.hasNext()){
				LocalgisMap localgisMap = (LocalgisMap) iteratorAvailable.next();
				if(localgisMap.getName().equals(featureType)){
					idMap = (Integer) localgisMap.getMapid();
				}
			}	
		} catch (LocalgisInvalidParameterException e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
		} catch (LocalgisDBException e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
		} catch (LocalgisConfigurationException e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
		} catch (LocalgisInitiationException e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
		}	      
		return idMap;
	}
	
	//Publica el mapa en localgis wms manager
	public static Integer mapTypeRelationPublish(Integer idEntidad, String featureType, Boolean publicMap){
		Integer idMap=null;
		try{
			LocalgisMapsConfigurationManager localgisMapsConfigurationManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
		    List geopistaAvailableMapsList = localgisMapsConfigurationManager.getAvailableMaps(idEntidad);
		    Iterator iteratorAvailable = geopistaAvailableMapsList.iterator();
			while(iteratorAvailable.hasNext()){
				GeopistaMap geopistaMap = (GeopistaMap) iteratorAvailable.next();
				if(!geopistaMap.equals(null) && geopistaMap.getName().equals(featureType)){
					idMap = (Integer) geopistaMap.getIdMap();
					break;
				}
			}			
		    publishMaps(idEntidad,featureType,idMap, publicMap);
		    idMap = isTypePublish(idEntidad, featureType, publicMap);
		} catch (LocalgisInvalidParameterException e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
		} catch (LocalgisDBException e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
		} catch (LocalgisConfigurationException e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
		} catch (LocalgisInitiationException e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
		}	      
		return idMap;
	}
	
	//Inserta en entidad_tipo_map un registro con los datos de relacion
//	public static void insertMapTypeRelation(Integer idMunicipio, String featureType, Integer idMap){
//		try{
//			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
//			LocalgisAutoPublishManager localgisAutoPublishManager = localgisManagerBuilderGeoWfst.getLocalgisAutoPublishManager();
//		    localgisAutoPublishManager.insertPublish(idMunicipio, featureType, idMap);		
//		} catch (LocalgisConfigurationException e) {
//			System.out.println(e.getMessage());
//			logger.error(e.getMessage());
//		} catch (LocalgisInitiationException e) {
//			System.out.println(e.getMessage());
//			logger.error(e.getMessage());
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			logger.error(e.getMessage());
//		}	      
//	}
//	
//	//Actualiza el campo idMap del registro existente de relacion
//	public static void updateMapTypeRelation(Integer idMunicipio, String featureType, Integer idMap){
//		try{
//			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
//			LocalgisAutoPublishManager localgisAutoPublishManager = localgisManagerBuilderGeoWfst.getLocalgisAutoPublishManager();
//			localgisAutoPublishManager.updatePublish(idMunicipio, featureType, idMap);			 
//		} catch (LocalgisConfigurationException e) {
//			System.out.println(e.getMessage());
//			logger.error(e.getMessage());
//		} catch (LocalgisInitiationException e) {
//			System.out.println(e.getMessage());
//			logger.error(e.getMessage());
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			logger.error(e.getMessage());
//		}	      
//	}
		
	
		private static void publishMaps(Integer idEntidad, String featureType, Integer idMap, Boolean publicMap){
	 		try{
	            GeopistaMap geopistaMap = new GeopistaMap();
	            geopistaMap.setIdMap(idMap);
	            geopistaMap.setIdEntidad(idEntidad);
	            geopistaMap.setName(featureType);
	            LocalgisMapsConfigurationManager localgisMapsConfigurationManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
	            localgisMapsConfigurationManager.addMapToWMSServer(geopistaMap, idEntidad, publicMap);
			} catch (LocalgisInvalidParameterException e) {
				System.out.println(e.getMessage());
				logger.error(e.getMessage());
			} catch (LocalgisDBException e) {
				System.out.println(e.getMessage());
				logger.error(e.getMessage());
			} catch (LocalgisConfigurationException e) {
				System.out.println(e.getMessage());
				logger.error(e.getMessage());
			} catch (LocalgisInitiationException e) {
				System.out.println(e.getMessage());
				logger.error(e.getMessage());
			} catch (LocalgisWMSException e) {
				System.out.println(e.getMessage());
				logger.error(e.getMessage());			
			} catch (Exception e) {
				System.out.println(e.getMessage());
				logger.error(e.getMessage());
			}	            
		}
	 
	
}
