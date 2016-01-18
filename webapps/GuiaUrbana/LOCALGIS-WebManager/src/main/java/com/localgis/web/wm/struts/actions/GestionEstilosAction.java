/**
 * GestionEstilosAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wm.struts.actions;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager;
import com.localgis.web.wm.config.LocalgisWebManagerConfiguration;
import com.localgis.web.wm.util.LocalgisManagerBuilderSingleton;

public class GestionEstilosAction extends Action {

    /**
     * Logger para las trazas
     */
    private static Logger logger = Logger.getLogger(GestionEstilosAction.class);
    private static final String ERROR = "error";
    
    
    private static final String TYPE_PUBLIC = "publico";
    private static final String TYPE_PRIVATE = "privado";


	public ActionForward execute(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {
		ActionForward actionForward;
		Integer idEntidad = (Integer) request.getSession().getAttribute("idEntidad");
    	if (idEntidad == null) {
			actionForward = mapping.findForward("entidadNoSelected");
			return actionForward;
		}
    	
    	String type = (String) request.getParameter("type");
        Hashtable maps = new Hashtable();
        boolean publicMap = false;
        HttpSession session = request.getSession();

        /*if ((type!=null)&&(type.equals(TYPE_PUBLIC))) {
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
			actionForward = mapping.findForward(ERROR);
			return actionForward;
        }*/
    	
    	
    	//Obtenemos el estilo del municipio
        try {
            LocalgisEntidadSupramunicipalManager localgisMunicipioManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisEntidadSupramunicipalManager();
            String css = localgisMunicipioManager.getCSS(idEntidad);
            if (css != null) {
                request.setAttribute("css", css);
            }
            //Guardamos la URL de la hoja de estilos dinámica de la guia urbana
            request.setAttribute("dynamicCSS", LocalgisWebManagerConfiguration.getPropertyString(LocalgisWebManagerConfiguration.PROPERTY_GUIAURBANA_DYNAMIC_CSS));
            try {
                request.setAttribute("staticCSS", LocalgisWebManagerConfiguration.getPropertyString(LocalgisWebManagerConfiguration.PROPERTY_GUIAURBANA_STATIC_CSS));
				request.setAttribute("dynamicCSSOpenLayers", LocalgisWebManagerConfiguration.getPropertyString(LocalgisWebManagerConfiguration.PROPERTY_GUIAURBANA_DYNAMIC_CSS_OPENLAYERS));
				request.setAttribute("dynamicCSSSample", LocalgisWebManagerConfiguration.getPropertyString(LocalgisWebManagerConfiguration.PROPERTY_GUIAURBANA_DYNAMIC_CSS_SAMPLE));
				request.setAttribute("cabeceraActual", LocalgisWebManagerConfiguration.getPropertyString(LocalgisWebManagerConfiguration.PROPERTY_GUIAURBANA_CABECERA_ACTUAL)+"/"+idEntidad+".png");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } catch (LocalgisConfigurationException lcE) {
            logger.error("Se ha producido un error de configuracion", lcE);
            lcE.printStackTrace();
			String message = "Se ha producido un error de configuracion";
					if(lcE.getMessage()!=null)
						message+=": "+lcE.getMessage()+".";
			ActionMessages actionMessages = new ActionMessages();
			actionMessages.add("error", new ActionMessage("errors.detail",message));
			saveMessages(request, actionMessages);
			actionForward = mapping.findForward(ERROR);
			return actionForward;
        } catch (LocalgisInitiationException e) {
            logger.error("Error de inicializacion al obtener la instancia de LocalgisManager", e);
            e.printStackTrace();
			String message = "Error de inicializacion al obtener la instancia de LocalgisManager.";
			ActionMessages actionMessages = new ActionMessages();
			actionMessages.add("error", new ActionMessage("errors.detail",message));
			saveMessages(request, actionMessages);
			actionForward = mapping.findForward(ERROR);
			return actionForward;
        } catch (LocalgisInvalidParameterException e) {
            logger.error("Error de parametros inválidos al obtener el CSS del municipio", e);
            e.printStackTrace();
			String message = "Error de parametros inválidos al obtener el CSS del municipio.";
			ActionMessages actionMessages = new ActionMessages();
			actionMessages.add("error", new ActionMessage("errors.detail",message));
			saveMessages(request, actionMessages);
			actionForward = mapping.findForward(ERROR);
			return actionForward;
        } catch (LocalgisDBException dbE) {
            logger.error("Error de base de datos al obtener el CSS del municipio", dbE);
            dbE.printStackTrace();
			String message = "No se pudo obtener el CSS del municipio, debido a un problema de acceso a la base de datos.";
			ActionMessages actionMessages = new ActionMessages();
			actionMessages.add("error", new ActionMessage("errors.detail",message));
			saveMessages(request, actionMessages);
			actionForward = mapping.findForward(ERROR);
			return actionForward;
        }     
		actionForward = mapping.findForward("success");
    	return actionForward;		
	}
	
}
