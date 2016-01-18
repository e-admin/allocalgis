/**
 * CSS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wm.dwr;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContextFactory;

import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager;
import com.localgis.web.wm.util.LocalgisManagerBuilderSingleton;


public class CSS {

    private Logger logger = Logger.getLogger(CSS.class);

	public void check() {
		
	}
	
	public String save() throws LocalgisConfigurationException, LocalgisInitiationException, LocalgisInvalidParameterException, LocalgisDBException {
		HttpSession httpSession = WebContextFactory.get().getSession();
		String css = (String) httpSession.getAttribute("css");
		Integer idEntidad = (Integer) httpSession.getAttribute("idEntidad");
		try {
			saveCSS(css, idEntidad);
			httpSession.removeAttribute("css");
			return "Guardado";
		} catch (LocalgisConfigurationException e) {
		    logger.error("Error al salvar el CSS", e);
		    throw e;
		} catch (LocalgisInitiationException e) {
            logger.error("Error al salvar el CSS", e);
            throw e;
        } catch (LocalgisInvalidParameterException e) {
            logger.error("Error al salvar el CSS", e);
            throw e;
        } catch (LocalgisDBException e) {
            logger.error("Error al salvar el CSS", e);
            throw e;
        }
	}
	
    public void removeCSS(Integer idEntidad) throws LocalgisConfigurationException, LocalgisInitiationException, LocalgisInvalidParameterException, LocalgisDBException  {
			try {
			LocalgisEntidadSupramunicipalManager localgisMunicipioManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisEntidadSupramunicipalManager();
            localgisMunicipioManager.removeCSS(idEntidad);
			} catch (LocalgisConfigurationException e) {
				logger.error("LocalgisConfigurationException al borrar el CSS", e);
	            throw e;
			} catch (LocalgisInitiationException e) {
				logger.error("LocalgisInitiationException al borrar el CSS", e);
	            throw e;
			} catch (LocalgisInvalidParameterException e) {
				logger.error("LocalgisInvalidParameterException al borrar el CSS", e);
	            throw e;
			} catch (LocalgisDBException e) {
				logger.error("LocalgisDBException al borrar el CSS", e);
	            throw e;
			}
        
    }

	public void saveCSS(String css,Integer idEntidad) throws  LocalgisConfigurationException, LocalgisInitiationException, LocalgisInvalidParameterException, LocalgisDBException {
		try{
		LocalgisEntidadSupramunicipalManager localgisMunicipioManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisEntidadSupramunicipalManager();
		String oldCSS = localgisMunicipioManager.getCSS(idEntidad);
		if (oldCSS == null) {
		    localgisMunicipioManager.insertCSS(idEntidad, css);
		}
		else {
		    localgisMunicipioManager.updateCSS(idEntidad, css);
		}
		} catch (LocalgisConfigurationException e) {
			logger.error("LocalgisConfigurationException al salvar el CSS", e);
		    throw e;
		} catch (LocalgisInitiationException e) {
			logger.error("LocalgisInitiationException al salvar el CSS", e);
		    throw e;
		} catch (LocalgisInvalidParameterException e) {
			logger.error("LocalgisInvalidParameterException al salvar el CSS", e);
		    throw e;
		} catch (LocalgisDBException e) {
			logger.error("LocalgisDBException al salvar el CSS", e);
		    throw e;
		}
		
	}

}
