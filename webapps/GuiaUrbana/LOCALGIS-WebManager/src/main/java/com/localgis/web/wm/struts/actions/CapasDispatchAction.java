/**
 * CapasDispatchAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wm.struts.actions;

import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager;
import com.localgis.web.wm.util.LocalgisManagerBuilderSingleton;

public class CapasDispatchAction extends DispatchAction {

    private static final String TYPE_PUBLIC = "publico";
    private static final String TYPE_PRIVATE = "privado";
    
    private static final String ERROR = "error";
	
	public ActionForward listLayers(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Integer idEntidad = (Integer) request.getSession().getAttribute("idEntidad");
    	String type = (String) request.getParameter("type");
        HttpSession session = request.getSession();

        Hashtable maps = new Hashtable();
        boolean publicMap = false;
        if ((type!=null)&&(type.equals(TYPE_PUBLIC))) {
           publicMap = true;
      		session.setAttribute("tipoMapas", "Mapas públicos");
        }
        else if ((type!=null)&&(type.equals(TYPE_PRIVATE))){
           publicMap = false;
     		session.setAttribute("tipoMapas", "Mapas privados");

        }
        else {
			String message = "El tipo de mapa seleccionado no es correcto";
			ActionMessages actionMessages = new ActionMessages();
			actionMessages.add("error", new ActionMessage("errors.detail",message));
			saveMessages(request, actionMessages);
			ActionForward actionForward = mapping.findForward(ERROR);
			return actionForward;
        }
		if (idEntidad == null) {
			ActionForward actionForward = mapping.findForward("entidadNoSelected");
			return actionForward;
		}
		ActionForward actionForward;
		LocalgisEntidadSupramunicipalManager localgisMunicipioManager;
		
			try {
			localgisMunicipioManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisEntidadSupramunicipalManager();
			List layers = localgisMunicipioManager.getLayers(idEntidad);
			request.setAttribute("layers", layers);
			request.setAttribute("type", type);
			} catch (LocalgisConfigurationException e) {
				e.printStackTrace();
				String message = "Se ha producido un error de configuración en la creación del LocalgisMunicipioManage.";
				ActionMessages actionMessages = new ActionMessages();
				actionMessages.add("error", new ActionMessage("errors.detail",message));
				saveMessages(request, actionMessages);
				actionForward = mapping.findForward(ERROR);
				return actionForward;
			} catch (LocalgisInitiationException e) {
				e.printStackTrace();
				String message = "Se ha producido un error inicializando el LocalgisMunicipioManage.";
				ActionMessages actionMessages = new ActionMessages();
				actionMessages.add("error", new ActionMessage("errors.detail",message));
				saveMessages(request, actionMessages);
				actionForward = mapping.findForward(ERROR);
				return actionForward;
			} catch (LocalgisDBException e) {
				e.printStackTrace();
				String message = "Se ha producido un error de acceso a la base de datos.";
				ActionMessages actionMessages = new ActionMessages();
				actionMessages.add("error", new ActionMessage("errors.detail",message));
				saveMessages(request, actionMessages);
				actionForward = mapping.findForward(ERROR);
				return actionForward;
			}
		
		actionForward= mapping.findForward("listLayers");
    	return actionForward;		
	}//fin del método
}
