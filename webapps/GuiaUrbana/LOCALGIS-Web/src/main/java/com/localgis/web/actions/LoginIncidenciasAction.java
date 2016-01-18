/**
 * LoginIncidenciasAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
//package com.localgis.web.actions;
//
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.apache.struts.action.Action;
//import org.apache.struts.action.ActionForm;
//import org.apache.struts.action.ActionForward;
//import org.apache.struts.action.ActionMapping;
//
//import com.localgis.web.actionsforms.LoginActionForm;
//import com.localgis.web.clienteWS.WsUsersLocator;
//import com.localgis.web.clienteWS.WsUsersSoap;
//import com.localgis.web.core.exceptions.LocalgisDBException;
//import com.localgis.web.core.manager.LocalgisMapsConfigurationManager;
//import com.localgis.web.core.manager.LocalgisUtilsManager;
//import com.localgis.web.filters.LoginFilter;
//import com.localgis.web.util.LocalgisManagerBuilderSingleton;
//
//public class LoginIncidenciasAction extends Action {
//
//    private static Log log = LogFactory.getLog(LoginIncidenciasAction.class);
//
//    private static final String SUCCESS_PAGE = "success";
//
//    private static final String LOGIN_PAGE = "login";
//
//    private static final String INVALID_LOGIN_PAGE = "invalidLogin";
//
//    private static final String ERROR_PAGE = "error";
//
//    private static final String LOGIN_ROL = "Geopista.Visualizador.Login";
//    private static final String CODIGO_VALIDO = "<CODIGO>SUCCESFUL</CODIGO>";
//   /* public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        LoginActionForm formBean = (LoginActionForm) form;
//        /*
//         * Si no se ha enviado ni usuario ni contraseña entonces vamos al
//         * formulario de login
//         */
//        if ((request.getParameter("userId") != null) && (request.getParameter("pwd") != null)){
//        	
//          //  LocalgisUtilsManager localgisUtilsManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisUtilsManager();
//            // boolean isValidUser = localgisUtilsManager.isValidUserIncidenciasExterno(request.getParameter("idUsuario"));
//        	WsUsersLocator wsUserLocator = new WsUsersLocator();
//        	WsUsersSoap wsUser = wsUserLocator.getwsUsersSoap();
//        	String resultado = wsUser.verificacionUsuario(request.getParameter("userId"), request.getParameter("pwd"));
//
//            if (resultado.indexOf(CODIGO_VALIDO) != -1) {
//                log.debug("Autenticacion correcta del usuario [" + formBean.getUsername() + "] en la entidad [" + formBean.getIdEntidad() + "] con rol ["+LOGIN_ROL+"]");
//            	request.getSession().setAttribute(LoginFilter.LOGIN_ATTRIBUTE, request.getParameter("userId"));
//            	request.getSession().setAttribute("idEntidad", request.getParameter("idEntidad"));
//                return mapping.findForward(SUCCESS_PAGE);
//                //return new ActionForward("/private/selectMap.do?idEntidad="+formBean.getIdEntidad());
//            } else {
//                log.error("Error al autenticar al usuario [" + formBean.getUsername() + "] en la entidad [" + formBean.getIdEntidad() + "] con rol ["+LOGIN_ROL+"]");
//                try {
//                    saveEntidadesInRequest(request);
//                    return mapping.findForward(INVALID_LOGIN_PAGE);
//                } catch (LocalgisDBException e) {
//                    log.error("Error al obtener las entidades", e);
//                    return mapping.findForward(ERROR_PAGE);
//                }
//            }
//        }
//        else{
//	        if (formBean.getUsername() == null && formBean.getPassword() == null) {
//	            try {
//	                saveEntidadesInRequest(request);
//	                return mapping.findForward(LOGIN_PAGE);
//	            } catch (LocalgisDBException e) {
//	                log.error("Error al obtener las entidades", e);
//	                return mapping.findForward(ERROR_PAGE);
//	            }
//	        } else {
//	            /*
//	             * Obtenemos el manager de localgis
//	             */
//	     //       LocalgisUtilsManager localgisUtilsManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisUtilsManager();
//	     //       boolean isValidUser = localgisUtilsManager.isValidUserIncidencias(formBean.getUsername(), formBean.getPassword(), LOGIN_ROL, formBean.getIdEntidad());
//	     //       isValidUser = true;
//	     //       if (isValidUser) {
//	        	WsUsersLocator wsUserLocator = new WsUsersLocator();
//	        	WsUsersSoap wsUser = wsUserLocator.getwsUsersSoap();
//	        	String resultado = wsUser.verificacionUsuario(formBean.getUsername(), formBean.getPassword());
//
//	            if (resultado.indexOf(CODIGO_VALIDO) != -1) {
//	                log.debug("Autenticacion correcta del usuario [" + formBean.getUsername() + "] en la entidad [" + formBean.getIdEntidad() + "] con rol ["+LOGIN_ROL+"]");
//	                request.getSession().setAttribute(LoginFilter.LOGIN_ATTRIBUTE, formBean.getUsername());
//	                request.getSession().setAttribute("idEntidad", formBean.getIdEntidad());
//	                return mapping.findForward(SUCCESS_PAGE);
//	                //return new ActionForward("/private/selectMap.do?idEntidad="+formBean.getIdEntidad());
//	            } else {
//	                log.error("Error al autenticar al usuario [" + formBean.getUsername() + "] en la entidad [" + formBean.getIdEntidad() + "] con rol ["+LOGIN_ROL+"]");
//	                try {
//	                    saveEntidadesInRequest(request);
//	                    return mapping.findForward(INVALID_LOGIN_PAGE);
//	                } catch (LocalgisDBException e) {
//	                    log.error("Error al obtener las entidades", e);
//	                    return mapping.findForward(ERROR_PAGE);
//	                }
//	            }
//	        }
//        }
//    }
//  
//    private void saveEntidadesInRequest(HttpServletRequest request) throws Exception {
//        /*
//         * Obtenemos el manager de localgis
//         */
//        LocalgisMapsConfigurationManager localgisMapsConfigurationManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
//
//        List entidades = localgisMapsConfigurationManager.getEntidadesSupramunicipales();
//        request.setAttribute("entidades", entidades);
//            
//    }
//}
