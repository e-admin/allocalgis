/**
 * LoginAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wm.struts.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.localgis.web.wm.struts.actionsforms.LoginActionForm;
import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager;
import com.localgis.web.core.manager.LocalgisMapsConfigurationManager;
import com.localgis.web.core.manager.LocalgisUtilsManager;
import com.localgis.web.core.model.GeopistaEntidadSupramunicipal;
import com.localgis.web.core.security.dnie.CertificateAuthManager;
import com.localgis.web.wm.struts.filters.LoginFilter;
import com.localgis.web.wm.util.LocalgisManagerBuilderSingleton;

public class LoginAction extends Action {

    private static Log log = LogFactory.getLog(LoginAction.class);

    private static final String SUCCESS_PAGE = "success";
    
    private static final String LOGIN_PAGE = "login";

    private static final String INVALID_LOGIN_PAGE = "invalidLogin";

    private static final String ERROR_PAGE = "error";

    private static final String LOGIN_ROL = "Geopista.Visualizador.Login";

    public static final String PUBLICADOR_GLOBAL_ROL = "Geopista.Map.PublicarGlobal";

    
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
                
                //Verificamos si el usuario dispone del rol de publicacion global. Un poco
                //peligroso pero util para realizar publicaciones masivas.
                if (localgisUtilsManager.isValidUserEntity(formBean.getUsername(), formBean.getPassword(), PUBLICADOR_GLOBAL_ROL)>-1){
                	request.getSession().setAttribute("publicadorglobal", true); 
                	log.info("El usuario dispone del rol de publicacion global");                	
                }
                else{
                	log.info("El usuario no dispone del rol de publicacion global");
                }

                
                request.getSession().setAttribute(LoginFilter.LOGIN_ATTRIBUTE, formBean.getUsername());
                log.info("Identificador de entidad del usuario:"+formBean.getUsername()+" idEntidad:"+idEntidad);
                if(idEntidad>0){
                	request.getSession().setAttribute("idEntidad", idEntidad); 
                	LocalgisManagerBuilder localgisManagerBuilder = LocalgisManagerBuilderSingleton.getInstance();

                	LocalgisEntidadSupramunicipalManager localgisEntidadSupramunicipalManager = localgisManagerBuilder.getLocalgisEntidadSupramunicipalManager();
                	GeopistaEntidadSupramunicipal geopistaEntidad = localgisEntidadSupramunicipalManager.getEntidadSupramunicipal(idEntidad);
                	request.getSession().setAttribute("nombreEntidad",geopistaEntidad.getNombreoficial());
                	//return mapping.findForward(SUCCESS_PAGE_ENTITY);
                }
                /*else{
                	LocalgisManagerBuilder localgisManagerBuilder = LocalgisManagerBuilderSingleton.getInstance();

                	LocalgisEntidadSupramunicipalManager localgisEntidadSupramunicipalManager = localgisManagerBuilder.getLocalgisEntidadSupramunicipalManager();
                	GeopistaEntidadSupramunicipal geopistaEntidad = localgisEntidadSupramunicipalManager.getEntidadSupramunicipal(idEntidad);
                	request.getSession().setAttribute("nombreEntidad",geopistaEntidad.getNombreoficial());
                }*/
                
               // else return mapping.findForward(SUCCESS_PAGE);
                return mapping.findForward(SUCCESS_PAGE);
                //return new ActionForward("/private/selectMap.do?idEntidad="+formBean.getIdEntidad());
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
