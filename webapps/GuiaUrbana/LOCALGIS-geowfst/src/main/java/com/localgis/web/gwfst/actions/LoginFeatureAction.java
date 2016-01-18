/**
 * LoginFeatureAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.actions;

import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.localgis.web.core.LocalgisManagerBuilderGeoWfst;
import com.localgis.web.core.manager.LocalgisGeoFeatureManager;
import com.localgis.web.core.manager.LocalgisUtilsManager;
import com.localgis.web.gwfst.actionsforms.LoginFeatureActionForm;
import com.localgis.web.gwfst.filters.LoginFilterFeatures;
import com.localgis.web.core.wm.util.LocalgisManagerBuilderSingleton;
import com.localgis.web.core.gwfst.util.LocalgisManagerBuilderSingletonFeature;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-05-2012
 * @version 1.0
 * @ClassComments Acción de autenticación
 */
public class LoginFeatureAction extends Action {

    /**
     * Logger
     */
    private static Logger logger = Logger.getLogger(LoginFeatureAction.class);
		
	/**
	 * Constantes
	 */
    private static final String LOGIN_PAGE = "login";
    private static final String INVALID_LOGIN_PAGE = "invalidLogin";
    private static final String ERROR_PAGE = "error";
    private static final String LOGIN_ROL = "Geopista.Visualizador.Login";

    /**
	 * Realiza la autenticación
	 * @param mapping: Mapeo de la acción
	 * @param form: Formulario asociado a la acción
	 * @param request: Objeto petición de la acción
	 * @param response: Objeto respuesta de la acción
	 * @throws Exception
	 * @return ActionForward: Devuelve la siguiente acción
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	LoginFeatureActionForm formBean = (LoginFeatureActionForm) form;
    	/*
         * Si no se ha enviado ni usuario ni contraseña entonces vamos al
         * formulario de login
         */
        if (formBean.getUsername() == null && formBean.getPassword() == null) {
        	return mapping.findForward(LOGIN_PAGE);
        } else {
            /*
             * Obtenemos el manager de localgis
             */        	
        	LocalgisManagerBuilderGeoWfst localgisManagerBuilder = LocalgisManagerBuilderSingletonFeature.getInstance();
        	LocalgisGeoFeatureManager localgisGeoFeatureManager = localgisManagerBuilder.getLocalgisGeoFeatureManager();
            
        	String idEntidadTemp = null;
        	String[] urlParts = formBean.getRequesturi().split("\\?");
            if (urlParts.length > 1) {
                String query = urlParts[1];
                for (String param : query.split("&")) {
                	String[] pair = param.split("=");
                	if(pair[0].equals("idEntidad")) idEntidadTemp = URLDecoder.decode(pair[1], "UTF-8");                    
                }
            }
        	
            if(idEntidadTemp!=null){
            	Integer idEntidad = localgisGeoFeatureManager.getIdEntidadLocalgis(idEntidadTemp);   
	            //Integer idMunicipio = localgisGeoFeatureManager.getIdEntidadLocalgis(idEntidadTemp);          
	            if(idEntidad!=null){
	    	        //Integer idEntidad = localgisGeoFeatureManager.getIdEntidadByIdMunicipio(idMunicipio);        	
		            LocalgisUtilsManager localgisUtilsManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisUtilsManager();
		            boolean isValidUser = localgisUtilsManager.isValidUser(formBean.getUsername(), formBean.getPassword(), LOGIN_ROL, idEntidad);
		            if (isValidUser) {
		            	logger.debug("Autenticacion correcta del usuario [" + formBean.getUsername() + "] en la entidad [" + idEntidad + "] con rol ["+LOGIN_ROL+"]");
		                request.getSession().setAttribute(LoginFilterFeatures.LOGIN_ATTRIBUTE, formBean.getUsername());
		                request.getSession().setAttribute("idEntidad", idEntidad);
		                response.sendRedirect(formBean.getRequesturi());
		                return null; 
		            } else {
		            	logger.error("Error al autenticar al usuario [" + formBean.getUsername() + "] en la entidad [" + idEntidad + "] con rol ["+LOGIN_ROL+"]");
		                return mapping.findForward(INVALID_LOGIN_PAGE);
		            }
	            }
	            else{
	            	logger.error("Error al autenticar al usuario [" + formBean.getUsername() + "] con rol ["+LOGIN_ROL+"]");
	                return mapping.findForward(INVALID_LOGIN_PAGE);
	            }
            }
            else{
            	logger.error("Error al autenticar al usuario [" + formBean.getUsername() + "] con rol ["+LOGIN_ROL+"]");
                return mapping.findForward(INVALID_LOGIN_PAGE);
            }
        }
    }
}
