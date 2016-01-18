/**
 * LoginAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.localgis.web.actionsforms.LoginActionForm;
import com.localgis.web.config.LocalgisWebConfiguration;
import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager;
import com.localgis.web.core.manager.LocalgisLayerManager;
import com.localgis.web.core.manager.LocalgisMapsConfigurationManager;
import com.localgis.web.core.manager.LocalgisUtilsManager;
import com.localgis.web.core.security.dnie.CertificateAuthManager;
import com.localgis.web.filters.LoginFilter;
import com.localgis.web.openlayers.LayerOpenlayers;
import com.localgis.web.util.LocalgisManagerBuilderSingleton;
import com.localgis.web.util.WebUtils;

public class LoginAction extends Action {

    private static Log log = LogFactory.getLog(LoginAction.class);

    private static final String SUCCESS_PAGE = "success";
    
    private static final String LOGIN_PAGE = "login";

    private static final String INVALID_LOGIN_PAGE = "invalidLogin";

    private static final String ERROR_PAGE = "error";

    private static final String LOGIN_ROL = "Geopista.Visualizador.Login";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LoginActionForm formBean = (LoginActionForm) form;
        //-----NUEVO----->
		CertificateAuthManager.dnieAuthManager(request);
		//---FIN NUEVO--->
        /*
         * Si no se ha enviado ni usuario ni contraseña entonces vamos al
         * formulario de login
         */        
        if (formBean.getUsername() == null && formBean.getPassword() == null) { 
	            try {
	                saveEntidadesInRequest(request);
	                return mapping.findForward(LOGIN_PAGE);
	            } catch (LocalgisDBException e) {
	                log.error("Error al obtener las entidades", e);
	                return mapping.findForward(ERROR_PAGE);
	            }
        } else {
            /*
             * Obtenemos el manager de localgis
             */
            LocalgisUtilsManager localgisUtilsManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisUtilsManager();
            Integer idEntidad = localgisUtilsManager.isValidUserEntity(formBean.getUsername(), formBean.getPassword(), LOGIN_ROL);
            if (idEntidad>-1) {
                log.debug("Autenticacion correcta del usuario [" + formBean.getUsername() + "] con rol ["+LOGIN_ROL+"]");
                request.getSession().setAttribute(LoginFilter.LOGIN_ATTRIBUTE, formBean.getUsername());
                if(idEntidad>0){
                	request.getSession().setAttribute("idEntidad", idEntidad);  
                }
                
                
                               

                return mapping.findForward(SUCCESS_PAGE);
            } else {
                log.error("Error al autenticar al usuario [" + formBean.getUsername() + "] con rol ["+LOGIN_ROL+"]");
                try {
                    saveEntidadesInRequest(request);
                    return mapping.findForward(INVALID_LOGIN_PAGE);
                } catch (LocalgisDBException e) {
                    log.error("Error al obtener las entidades", e);
                    return mapping.findForward(ERROR_PAGE);
                }
            }
        }
    }
    
    private void saveEntidadesInRequest(HttpServletRequest request) throws Exception {
        /*
         * Obtenemos el manager de localgis
         */
        LocalgisMapsConfigurationManager localgisMapsConfigurationManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();

        List entidades = localgisMapsConfigurationManager.getEntidadesSupramunicipales();
        request.setAttribute("entidades", entidades);
            
    }
}