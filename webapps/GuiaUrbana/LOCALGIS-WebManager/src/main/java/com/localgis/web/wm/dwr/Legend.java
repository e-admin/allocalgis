/**
 * Legend.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wm.dwr;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.directwebremoting.WebContextFactory;


import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.manager.LocalgisLayerManager;
import com.localgis.web.core.model.LocalgisLegend;
import com.localgis.web.core.model.LocalgisLegendKey;
import com.localgis.web.wm.util.LocalgisManagerBuilderSingleton;

public class Legend {
	
    private Logger logger = Logger.getLogger(Legend.class);

	public String getLegend(int idLayer,boolean mapPublic) throws LocalgisConfigurationException, LocalgisInitiationException, LocalgisDBException {
		try {
            HttpSession httpSession = WebContextFactory.get().getSession();
            LocalgisLegendKey legendKey = new LocalgisLegendKey();
            legendKey.setIdentidad((Integer) httpSession.getAttribute("idEntidad"));
            legendKey.setLayeridgeopista(new Integer(idLayer));
            legendKey.setMappublic(mapPublic ? new Short((short) 1) : new Short((short) 0));
            LocalgisLayerManager localgisLayerManager;
			localgisLayerManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisLayerManager();
            LocalgisLegend localgisLegend = localgisLayerManager.getLegend(legendKey);
            if (localgisLegend != null) {
                httpSession.setAttribute("localgisLegend", localgisLegend);
                Double imageNumber = new Double(System.currentTimeMillis() * 256);
                //BASE64Encoder encoder = new BASE64Encoder();
                //String image = new String(encoder.encode(imageNumber.toString().getBytes()));
                Base64 encoder=new Base64();
                String image = new String(encoder.encode(imageNumber.toString().getBytes()));
                
                return "../img/legend/" + image;
            } else {
                return null;
            }
			} catch (LocalgisConfigurationException e) {
				 logger.error("LocalgisConfigurationException al obtener la leyenda", e);
				 throw e;
			} catch (LocalgisInitiationException e) {
				 logger.error("LocalgisInitiationException al obtener la leyenda", e);
				 throw e;
			} catch (LocalgisDBException e) {
				 logger.error("LocalgisDBException al obtener la leyenda", e);
				 throw e;
			}
	}//fin del método getLeyend
	
	

	public void removeLegend(int idLayer, boolean mapPublic) throws LocalgisConfigurationException, LocalgisInitiationException, LocalgisDBException {
		try {
            HttpSession httpSession = WebContextFactory.get().getSession();
            LocalgisLegendKey legendKey = new LocalgisLegendKey();
            legendKey.setIdentidad((Integer) httpSession.getAttribute("idEntidad"));
            legendKey.setLayeridgeopista(new Integer(idLayer));
            legendKey.setMappublic(mapPublic ? new Short((short) 1) : new Short((short) 0));
            LocalgisLayerManager localgisLayerManager;
			localgisLayerManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisLayerManager();
            localgisLayerManager.removeLegend(legendKey);
		} catch (LocalgisConfigurationException e) {
			 logger.error("LocalgisConfigurationException al borrar la leyenda", e);
			 throw e;
		} catch (LocalgisInitiationException e) {
			 logger.error("LocalgisInitiationException al borrar la leyenda", e);
			 throw e;
		} catch (LocalgisDBException e) {
			 logger.error("LocalgisDBException al borrar la leyenda", e);
			 throw e;
		}
    }

}
