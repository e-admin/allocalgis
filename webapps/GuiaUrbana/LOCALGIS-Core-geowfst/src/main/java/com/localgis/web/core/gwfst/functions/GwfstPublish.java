/**
 * GwfstPublish.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.gwfst.functions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.localgis.web.core.LocalgisManagerBuilderGeoWfst;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.gwfst.util.LocalgisManagerBuilderSingletonFeature;
import com.localgis.web.core.manager.LocalgisGwftsPublishManager;

public class GwfstPublish {
	
	private static Log logger = LogFactory.getLog(GwfstPublish.class);

	public static Integer getMapTypeRelation(Integer idMunicipio, String featureType) {
		Integer idMap = null;
		try {
			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
			LocalgisGwftsPublishManager localgisAutoPublishManager = localgisManagerBuilderGeoWfst.getLocalgisAutoPublishManager();
			idMap = localgisAutoPublishManager.getIdMap(idMunicipio,featureType);
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
	
	public static void insertMapTypeRelation(Integer idMunicipio, String featureType, Integer idMap){
		try{
			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
			LocalgisGwftsPublishManager localgisAutoPublishManager = localgisManagerBuilderGeoWfst.getLocalgisAutoPublishManager();
		    localgisAutoPublishManager.insertPublish(idMunicipio, featureType, idMap);		
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
	}
	
	//Actualiza el campo idMap del registro existente de relacion
	public static void updateMapTypeRelation(Integer idMunicipio, String featureType, Integer idMap){
		try{
			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
			LocalgisGwftsPublishManager localgisAutoPublishManager = localgisManagerBuilderGeoWfst.getLocalgisAutoPublishManager();
			localgisAutoPublishManager.updatePublish(idMunicipio, featureType, idMap);			 
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
	}
	
}
