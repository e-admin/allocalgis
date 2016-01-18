/**
 * Sigm.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.ws.sigm.dwr;

import ieci.tdw.ispac.services.ws.server.Cadena;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.HashMap;

import javax.xml.rpc.ServiceException;

import com.localgis.web.core.LocalgisManagerBuilderGeoWfst;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.gwfst.util.LocalgisManagerBuilderSingletonFeature;
import com.localgis.web.core.manager.LocalgisGeoFeatureManager;
import com.localgis.web.gwfst.ws.sigm.SigmTramitacionWS;
import com.localgis.web.gwfst.ws.sigm.beans.PropertyAndName;
import com.localgis.web.gwfst.ws.sigm.beans.PropertyAndValue;
import com.localgis.web.gwfst.ws.sigm.util.SigmUtils;


public class Sigm {

	/**
	 * Variables
	 */
	private static final String SPAC_EXPEDIENTES = "SPAC_EXPEDIENTES";
	
	public static PropertyAndName[] getPropertyAndName(String procedureName) throws LocalgisInitiationException, LocalgisConfigurationException {		
		//return SigmUtils.getPropertyAndNameFromXML("/conf/" + procedureName + "PropertyAndName.xml");
		return SigmUtils.getPropertyAndName(procedureName);
	}
	
	public static PropertyAndValue [] getInfoAll(String url, Integer idEntidad, String procedureName, String idFeature) 
	throws RemoteException, ServiceException, MalformedURLException, LocalgisInitiationException, LocalgisConfigurationException{		
        LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
		LocalgisGeoFeatureManager localgisGeoFeatureManager = localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager();      
		Cadena cadena = SigmTramitacionWS.obtenerRegistrosEntidad(url, localgisGeoFeatureManager.getIdEntidadExt(idEntidad), SPAC_EXPEDIENTES, idFeature);
		HashMap<String, String> procedures = new HashMap<String, String>();
		if(cadena!=null)
			procedures.put(SPAC_EXPEDIENTES, cadena.getValor());
		cadena = SigmTramitacionWS.obtenerRegistrosEntidad(url, localgisGeoFeatureManager.getIdEntidadExt(idEntidad), procedureName, idFeature);
		if(cadena!=null)
			procedures.put(procedureName, cadena.getValor());
		PropertyAndValue propertyAndValue [] = SigmUtils.getPropertyAndValueFromProcedures(procedures); 
		return propertyAndValue;  
	}
	
	public static String [] getSearchAll(String url, Integer idEntidad, String procedureName, PropertyAndValue [] searchPropertyAndValues) 
	throws RemoteException, ServiceException, MalformedURLException, LocalgisInitiationException, LocalgisConfigurationException{
		String searchXML = SigmUtils.getSearchXML(searchPropertyAndValues, getPropertyAndName(procedureName));		
        LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
		LocalgisGeoFeatureManager localgisGeoFeatureManager = localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager();     
		Cadena cadena = SigmTramitacionWS.busquedaAvanzada(url, localgisGeoFeatureManager.getIdEntidadExt(idEntidad), procedureName, searchXML);
        return SigmUtils.getSearchIdFeatures(cadena.getValor());         
	}
	
	public static String getInfoByPrimaryKey(String url, Integer idEntidad, String procedureName, String idFeature, String property) 
	throws RemoteException, ServiceException, MalformedURLException, LocalgisInitiationException, LocalgisConfigurationException{
		LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
		LocalgisGeoFeatureManager localgisGeoFeatureManager = localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager();      
		PropertyAndValue [] propertyAndValue = Sigm.getInfoAll(url, idEntidad, procedureName, idFeature);
		for(int i=0; i<propertyAndValue.length;i++)
			if(propertyAndValue[i].getProperty().equals(property))
				return propertyAndValue[i].getValue();
		return null;
	}
	
}
