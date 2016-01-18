/**
 * Sigm.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.ws.sigm.functions;

import ieci.tecdoc.sgm.entidades.ws.server.Entidad;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.LocalgisManagerBuilderGeoWfst;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.gwfst.util.LocalgisManagerBuilderSingletonFeature;
import com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager;
import com.localgis.web.core.manager.LocalgisGeoFeatureManager;
import com.localgis.web.core.model.GeopistaEntidadSupramunicipal;
import com.localgis.web.core.wm.util.LocalgisManagerBuilderSingleton;
import com.localgis.web.gwfst.ws.sigm.SigmEntidadesWS;

public class Sigm {

	public static boolean doRelacionEntidades(String url) throws LocalgisInitiationException, LocalgisConfigurationException, LocalgisDBException, RemoteException, MalformedURLException, ServiceException{
		LocalgisManagerBuilder localgisManagerBuilder = LocalgisManagerBuilderSingleton.getInstance();        
		LocalgisEntidadSupramunicipalManager localgisEntidadSupramunicipalManager = localgisManagerBuilder.getLocalgisEntidadSupramunicipalManager();
		LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
        LocalgisGeoFeatureManager localgisGeoFeatureManager = localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager();      
		List<Entidad> entidadSigmList = SigmEntidadesWS.getAllEntidades(url);
		List<GeopistaEntidadSupramunicipal> entidadLocalgisList = localgisEntidadSupramunicipalManager.getAllEntidadesSupramunicipales();
		if(entidadSigmList!=null){
			Iterator<GeopistaEntidadSupramunicipal> itEntidadLocalgis = entidadLocalgisList.iterator();
			while(itEntidadLocalgis.hasNext()){
				GeopistaEntidadSupramunicipal entidadLocalgis = itEntidadLocalgis.next();
				Iterator<Entidad> itEntidadSigm = entidadSigmList.iterator();
				while(itEntidadSigm.hasNext()){
					Entidad entidadSigm = itEntidadSigm.next();
					if(localgisGeoFeatureManager.getIdEntidadLocalgis(entidadSigm.getIdentificador())!=null && entidadLocalgis.getNombreoficial().equalsIgnoreCase(entidadSigm.getNombreCorto())){
						localgisGeoFeatureManager.setIdEntidadLocalgisIdEntidadExt(entidadLocalgis.getIdEntidad(), entidadSigm.getIdentificador());
					}						
				}
			}		
		}
		return true;		
	}
	
}
