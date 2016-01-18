/**
 * EntidadSelectedAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * MunicipioSelectedAction.java
 *
 * Created on 7 de octubre de 2007, 20:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.localgis.web.wm.struts.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager;
import com.localgis.web.core.manager.LocalgisUtilsManager;
import com.localgis.web.core.model.GeopistaEntidadSupramunicipal;
import com.localgis.web.core.security.sso.SSOAuthManager;
import com.localgis.web.wm.struts.filters.LoginFilter;
import com.localgis.web.wm.util.LocalgisManagerBuilderSingleton;

/**
 *
 * @author Adolfo
 */
public class EntidadSelectedAction extends Action {
    
	 private static Log log = LogFactory.getLog(EntidadSelectedAction.class);
	 
    /** Creates a new instance of EntidadSelectedAction */
    public EntidadSelectedAction() {
    }
    

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	DynaActionForm dynaActionForm = (DynaActionForm) form;
        Integer entidad = (Integer) dynaActionForm.get("idEntidad");
        HttpSession session = request.getSession();
        if (entidad!=null) {
            request.getSession().setAttribute("idEntidad",entidad);
            
          //Obtenemos el municipio
            LocalgisManagerBuilder localgisManagerBuilder = LocalgisManagerBuilderSingleton.getInstance();

        	LocalgisEntidadSupramunicipalManager localgisEntidadSupramunicipalManager = localgisManagerBuilder.getLocalgisEntidadSupramunicipalManager();
        	GeopistaEntidadSupramunicipal geopistaEntidad = localgisEntidadSupramunicipalManager.getEntidadSupramunicipal(entidad);
        	session.setAttribute("nombreEntidad",geopistaEntidad.getNombreoficial());

        	
        	//Fijamos unos parametros que se fijan en el LoginAction pero como si entra por EntidadSelectedAction 
        	//no pasa por ahi, lo fijamos en este momento, solo si se ha pasado por alli.
        	try {
        		if (request.getSession().getAttribute(SSOAuthManager.SSOACTIVE_ATTRIBUTE)!=null){
					boolean ssoAuth=(Boolean)request.getSession().getAttribute(SSOAuthManager.SSOACTIVE_ATTRIBUTE);
					if (ssoAuth==true){
						
						String loginUser=(String)request.getSession().getAttribute(SSOAuthManager.LOGIN_ATTRIBUTE);
						
						LocalgisUtilsManager localgisUtilsManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisUtilsManager();
						 //Verificamos si el usuario dispone del rol de publicacion global. Un poco
					    //peligroso pero util para realizar publicaciones masivas.
					    if (localgisUtilsManager.isValidUserEntityNotPass(loginUser, LoginAction.PUBLICADOR_GLOBAL_ROL)>-1){
					    	request.getSession().setAttribute("publicadorglobal", true); 
					    	log.info("El usuario dispone del rol de publicacion global");                	
					    }
					    else{
					    	log.info("El usuario no dispone del rol de publicacion global");
					    }
					}
        		}
			} catch (Exception e) {
				log.error("Error al verificar la autenticacion SSO del usuario");
			}
        	
        	
            ActionForward actionForward = mapping.findForward("success");            
            return actionForward;
        }
        ActionForward actionForward = mapping.findForward("entidadNoSelected");
		return actionForward;
    }

   
}
