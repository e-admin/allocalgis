/**
 * LoadEntidadesAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * LofonAction.java
 *
 * Created on 6 de octubre de 2007, 23:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.localgis.web.wm.struts.actions;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.manager.LocalgisMapsConfigurationManager;
import com.localgis.web.core.model.GeopistaEntidadSupramunicipal;
import com.localgis.web.wm.struts.beans.EntidadSupramunicipalBean;
import com.localgis.web.wm.struts.beans.EntidadesSupramunicipalesList;
import com.localgis.web.wm.util.EntidadSupramunicipalComparator;
import com.localgis.web.wm.util.LocalgisManagerBuilderSingleton;

/**
 *
 * @author Adolfo
 */

public class LoadEntidadesAction extends Action {
    
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
    	ActionForward actionForward;
    	HttpSession session = request.getSession();
    	EntidadesSupramunicipalesList entidadList = null;
		try {
			entidadList = getEntidades();
		}catch(LocalgisDBException dbE){
			dbE.printStackTrace();
			String message = "No se pudo obtener la información de municipios, debido a un problema de acceso a la base de datos.";
			ActionMessages actionMessages = new ActionMessages();
			actionMessages.add("error", new ActionMessage("errors.detail",message));
			saveMessages(request, actionMessages);
			actionForward = mapping.findForward(ERROR);
			return actionForward;
		} //fin del catch
		catch(LocalgisConfigurationException lcE){
			lcE.printStackTrace();
			String message = "No se pudo obtener la información de municipios, debido a un problema de configuración: ";
			if(lcE.getMessage()!=null)
				message+=": "+lcE.getMessage()+".";
			ActionMessages actionMessages = new ActionMessages();
			actionMessages.add("error", new ActionMessage("errors.detail",message));
			saveMessages(request, actionMessages);
			actionForward = mapping.findForward(ERROR);
			return actionForward;
		}//fin del catch
		catch(LocalgisInitiationException liE){
			liE.printStackTrace();
			String message = "Error de inicializacion al obtener la instancia del LocalgisManager.";
			ActionMessages actionMessages = new ActionMessages();
			actionMessages.add("error", new ActionMessage("errors.detail",message));
			saveMessages(request, actionMessages);
			actionForward = mapping.findForward(ERROR);
			return actionForward;
		}//fin del catch
		catch (Exception e) {
			e.printStackTrace();
			String message = "No se pudo obtener la información de municipios.";
			ActionMessages actionMessages = new ActionMessages();
			actionMessages.add("error", new ActionMessage("errors.detail",message));
			saveMessages(request, actionMessages);
			actionForward = mapping.findForward(ERROR);
			return actionForward;
		}
    	session.setAttribute("entidades",entidadList.getEntidades());
    	actionForward = mapping.findForward(SUCCESS);
    	return actionForward;
    }

    private EntidadesSupramunicipalesList getEntidades() throws LocalgisConfigurationException, LocalgisInitiationException, LocalgisDBException {
        EntidadesSupramunicipalesList entidadesList = new EntidadesSupramunicipalesList();
        LocalgisMapsConfigurationManager localgisMapsConfigurationManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
        List geopistaEntidadesList = localgisMapsConfigurationManager.getEntidadesSupramunicipales();
        for (Iterator iterator = geopistaEntidadesList.iterator(); iterator
				.hasNext();) {
			GeopistaEntidadSupramunicipal item = (GeopistaEntidadSupramunicipal) iterator.next();
			EntidadSupramunicipalBean entidad = new EntidadSupramunicipalBean();
			entidad.setId(item.getIdEntidad());
			entidad.setName(item.getNombreoficial());
			entidadesList.setEntidad(entidad);
		}
        Collections.sort(entidadesList.getEntidades(),new EntidadSupramunicipalComparator());
        return entidadesList;
    }
}
