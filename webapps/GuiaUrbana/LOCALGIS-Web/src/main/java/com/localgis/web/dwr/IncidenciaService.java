/**
 * IncidenciaService.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.dwr;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContextFactory;

import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.manager.LocalgisMapManager;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.filters.LoginFilter;
import com.localgis.web.util.LocalgisManagerBuilderSingleton;

public class IncidenciaService {

	private static Log logger = LogFactory.getLog(IncidenciaService.class);
	
    public Integer addIncidencia(Integer mapid, String layerName, Integer idFeature, String tipoIncidencia, String gravedadIncidencia, String emailContacto, String descripcion, Double x, Double y, Integer idMunicipio) throws LocalgisInitiationException, LocalgisConfigurationException, LocalgisInvalidParameterException, LocalgisDBException {
    	HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        logger.debug("Creando Incidencia "+request.getRemoteUser());
        LocalgisMapManager localgisMapManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapManager();
        String identificador = (String)request.getSession().getAttribute(LoginFilter.LOGIN_ATTRIBUTE);
        if (identificador==null) {
        	identificador="publico";
        }
        return localgisMapManager.addIncidencia(identificador, mapid, layerName, idFeature, tipoIncidencia, gravedadIncidencia, emailContacto, descripcion, x, y, idMunicipio);

    }
}
