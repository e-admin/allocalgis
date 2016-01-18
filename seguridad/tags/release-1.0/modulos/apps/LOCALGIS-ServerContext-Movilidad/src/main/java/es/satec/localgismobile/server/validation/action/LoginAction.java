package es.satec.localgismobile.server.validation.action;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import es.satec.localgismobile.server.Global;
import es.satec.localgismobile.server.config.ConfigurationManager;
import es.satec.localgismobile.server.validation.delegate.ValidationDelegate;
import es.satec.localgismobile.server.validation.exceptions.IncorrectPasswordException;
import es.satec.localgismobile.server.validation.exceptions.PermissionDeniedException;
import es.satec.localgismobile.server.validation.vo.UserVO;
import es.satec.localgismobile.server.web.filters.AuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.geopista.security.SecurityManager;

public class LoginAction extends Action {

    private static final Logger logger = Logger.getLogger(LoginAction.class);
    //ASO 20-01-2010 parametro de configuración del servidor de cartografia
	
    private static String SERVER = ConfigurationManager.getApplicationProperty(Global.PROP_GEOPISTA_CON_SERVER);
	

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	//url de acceso => http://localhost:8082/localgismobile/login.do?j_username=nacho&j_password=nacho&device_id=pda
    	String user = request.getParameter("j_username");
    	String password = request.getParameter("j_password");
    	String deviceId = request.getParameter("device_id");
    	
    	logger.debug("LoginPdaAction user=" + user + ", password=" + password + ", device id=" + deviceId);

		response.setContentType("text/xml");
		
		String idPermisoAsString = ConfigurationManager.getApplicationProperty("permissions.idperm_login");
		String idAclAsString = ConfigurationManager.getApplicationProperty("permissions.idacl");
		int idPermiso = -1;
		int idAcl = -1;
		try {
			idPermiso = Integer.valueOf(idPermisoAsString).intValue();
			idAcl = Integer.valueOf(idAclAsString).intValue();
		} catch (Exception e) {
			logger.error("No se ha podido obtener el identificador de ACL o permiso");
			return null;
		}

		ValidationDelegate delegate = new ValidationDelegate(request.getSession(true).getServletContext());
		StringBuffer sb = null;
		try {
			UserVO usuario = delegate.login(user, password, idPermiso, idAcl);
	    	//ASO 20-01-2010 Hacemos login en el SecurityManager para la parte de los mapas
			//Creamos el securityManager
			SecurityManager sm= new SecurityManager();
			sm.setUrlNS(SERVER + "/Geopista");
			String defaultProfile = "Geopista";
			logger.info("SecurityManager url: " + sm.getUrlNS());
			if (usuario!=null && sm.loginNS(user, password, defaultProfile))
			{
				request.getSession().setAttribute(AuthenticationFilter.SM_ATTRIBUTE , sm);
				sb = new StringBuffer();
				sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
				sb.append("<loginOK>");
				sb.append("<userInfo>");
				sb.append("<userid>");
				sb.append(usuario.getId());
				sb.append("</userid>");
				sb.append("<username>");
				sb.append(usuario.getLogin());
				sb.append("</username>");
				sb.append("<name>");
				sb.append(usuario.getCompleteName());
				sb.append("</name>");
				sb.append("</userInfo>");
				sb.append("<permissions>");
				if (usuario.getPermissionsByLayer() != null) {
					Iterator it = usuario.getPermissionsByLayer().keySet().iterator();
					while (it.hasNext()) {
						String layer = (String) it.next();
						Collection perms = (Collection) usuario.getPermissionsByLayer().get(layer);
						
						sb.append("<layer name=\"" + layer + "\">");
						Iterator it2 = perms.iterator();
						while (it2.hasNext()) {
							sb.append("<idPerm>" + it2.next() + "</idPerm>");
						}
						sb.append("</layer>");
					}
				}
				sb.append("</permissions>");
				sb.append("</loginOK>");
		
				// Se mete el nombre de usuario en sesion
				request.getSession(true).setAttribute(AuthenticationFilter.LOGIN_ATTRIBUTE, usuario.getLogin());
			}
    	} catch (IncorrectPasswordException e) {
    		logger.error("Se ha producido un excepción al hacer el login- Password incorrecta");
			sb = new StringBuffer();

			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
			sb.append("<loginFailed>");
			sb.append("<incorrectpassword>true</incorrectpassword>");
			sb.append("</loginFailed>");
    	} catch (PermissionDeniedException e) {
    		logger.error("Se ha producido un excepción al hacer el login- Permisos denegados:");
    		
			sb = new StringBuffer();

			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
			sb.append("<loginFailed>");
			sb.append("<permissiondenied>true</permissiondenied>");
			sb.append("</loginFailed>");
    	} catch (Exception ex) {
    		logger.error("Se ha producido un excepción al hacer el login:",ex);
			sb = new StringBuffer();

			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
			sb.append("<loginFailed>");
			sb.append("<permissiondenied>true</permissiondenied>");
			sb.append("</loginFailed>");
    	}

    	if (sb!=null) {
    		PrintWriter out = response.getWriter();
    		out.println(sb.toString());
    	}
    	
		return null;
	}

}
