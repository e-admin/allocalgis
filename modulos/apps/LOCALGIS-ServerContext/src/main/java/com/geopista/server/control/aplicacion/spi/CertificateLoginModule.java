/**
 * CertificateLoginModule.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.control.aplicacion.spi;

/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 26-abr-2004
 * Time: 19:11:14
 * To change this template use Options | File Templates.
 */

import java.io.IOException;
import java.security.Principal;
import java.security.acl.Group;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.eclipse.jetty.plus.jaas.JAASPrincipal;
import org.eclipse.jetty.plus.jaas.JAASRole;
import org.eclipse.jetty.plus.jaas.callback.ObjectCallback;
import org.eclipse.jetty.util.security.Credential;

import admcarApp.PasarelaAdmcar;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.control.ListaSesiones;
import com.geopista.security.CertificateCallback;
import com.geopista.security.dnie.CertificateManager;
import com.geopista.server.database.COperacionesControl;
import com.localgis.security.model.LocalgisJAASGroup;
import com.localgis.server.LocalgisSerlvetContextListener;
import com.localgis.server.SessionsContextShared;

/* ---------------------------------------------------- */

/**
 * JDBCLoginModule
 * <p>
 * JAAS LoginModule to retrieve user information from a database and
 * authenticate the user.
 * <p/>
 * <p>
 * <h4>Notes</h4>
 * <p>
 * This version uses plain old JDBC connections NOT Datasources.
 * <p/>
 * <p>
 * <h4>Usage</h4>
 * 
 * <pre>
 */
/*
 * </pre>
 * 
 * @see
 * 
 * @version 1.0 Tue Apr 15 2003
 * 
 * @author Jan Bartel (janb)
 */
public class CertificateLoginModule implements LoginModule {
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(CertificateLoginModule.class);

	private CallbackHandler callbackHandler = null;

	private boolean authState = false;
	private boolean commitState = false;

	private Subject subject = null;
	private Principal principal = null;
	private Credential credential = null;
	private Group roleGroup = null;

	private String userQuery;
	private String superUserQuery;
	private String rolesQuery;

	/* ------------------------------------------------ */
	/**
	 * Abort login
	 * 
	 * @return
	 * @throws LoginException
	 */
	public boolean abort() throws LoginException {
		principal = null;
		credential = null;
		roleGroup = null;

		if (authState && commitState)
			return true;
		else
			return false;
	}

	/* ------------------------------------------------ */
	/**
	 * Commit the authenticated user
	 * 
	 * @return
	 * @throws LoginException
	 */
	public boolean commit() throws LoginException {
		if (!authState) {
			principal = null;
			credential = null;
			roleGroup = null;
			commitState = false;
			return authState;
		}

		subject.getPrincipals().add(principal);
		// System.out.println(principal.getName());
		subject.getPrivateCredentials().add(credential);
		subject.getPrincipals().add(roleGroup);
		commitState = true;
		logger.debug("Commit todo OK");
		return true;
	}

	/* ------------------------------------------------ */
	/**
	 * Authenticate the user.
	 * 
	 * @return
	 * @throws LoginException
	 */
	public boolean login() throws LoginException {
		if (callbackHandler == null)
			throw new LoginException("No callback handler");

		Callback[] callbacks = new Callback[3];
		callbacks[0] = new NameCallback("Enter user name");		
        callbacks[1] = new CertificateCallback();
        callbacks[2] = new ObjectCallback();

		try {
			// if (true) return true;
			callbackHandler.handle(callbacks);			
			String webUserName = ((NameCallback) callbacks[0]).getName();
			Object webCredential = ((CertificateCallback) callbacks[1]).getCertificate();
			
			 if (webUserName == null) {
			 authState = false;
			 return authState;
			 }
			
			 PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(LocalgisSerlvetContextListener.getServletContext(), "UserSessions");    
	    		  
			 //primero miro si el webUserName es un identificador de sesion
			 if (PasarelaAdmcar.listaSesiones.existeSesion(webUserName)) {
				 //logger.info("Usuario existe en sesion:"+webUserName);
				 ISesion sesion =
				 PasarelaAdmcar.listaSesiones.getSesion(webUserName);
				 authState = true;
				 principal = sesion.getUserPrincipal();
				 roleGroup = sesion.getRoleGroup();
				 return authState;
			 }
			
			 if (webCredential == null) {
			 authState = false;
			 return authState;
			 }
			
			 //load user from database
			 loadUser(webUserName);
			
			 principal = new JAASPrincipal(webUserName);	
			
			 
			if(webCredential instanceof X509Certificate){ 
				X509Certificate clientCert = (X509Certificate) webCredential;
				CertificateManager certificateManager = new CertificateManager(clientCert);
				credential = Credential.getCredential(clientCert.getType());
				if(certificateManager.isValid()){				
					authState = true;
					return authState;					
				}else logger.error("Error en la validación");
			 }else logger.error("La credencial no es un certificado");			
			 if(!authState)
				 logger.info("Auntenticación negativa. Password incorrecta");
			 else
				 logger.info("Autenticación positiva. Password correcta");
			
			 return authState;
		} catch (IOException e) {
			throw new LoginException(e.toString());
		} catch (UnsupportedCallbackException e) {
			throw new LoginException(e.toString());
		} catch (SQLException e) {
			 throw new LoginException(e.toString());
		}

	}

	/* ------------------------------------------------ */
	/**
	 * Logout authenticated user
	 * 
	 * @return
	 * @throws LoginException
	 */
	public boolean logout() throws LoginException {
		subject.getPrincipals().remove(principal);
		subject.getPrivateCredentials().remove(credential);
		subject.getPrincipals().remove(roleGroup);
		return true;
	}

	/* ------------------------------------------------ */
	/**
	 * Init LoginModule. Called once by JAAS after new instance created. Tengo
	 * otra query para el caso del superusuario
	 * 
	 * @param subject
	 * @param callbackHandler
	 * @param sharedState
	 * @param options
	 */
	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map sharedState, Map options) {
		try {

			// get the user credential query out of the options
			String dbUserTable = (String) options.get("userTable");
			String dbEntityTable = (String) options.get("entityTable");
			String dbUserTableUserField = (String) options.get("userField");
			String dbUserTableUserIDField = (String) options.get("userIdField");
			String dbUserTableCredentialField = (String) options
					.get("credentialField");
			String dbUserTableEntityField = (String) options.get("entityField");
			String dbUserTableMunicipalityField = (String) options
					.get("municipalityField");

			userQuery = "select " + dbUserTableCredentialField + ", "
					+ dbUserTableUserIDField + ", " + dbUserTable + "."
					+ dbUserTableEntityField + ", " + dbEntityTable + "."
					+ dbUserTableMunicipalityField;
			userQuery += " from " + dbUserTable + "," + dbEntityTable;
			userQuery += " where upper(" + dbUserTableUserField + ")=? and "
					+ dbUserTable + "." + dbUserTableEntityField + "="
					+ dbEntityTable + "." + dbUserTableEntityField
					+ " and bloqueado = false";
			// userQuery +=
			// " and "+dbEntityTable+"."+dbUserTableMunicipalityField+"<>0";
			superUserQuery = "select " + dbUserTableCredentialField + ", "
					+ dbUserTableUserIDField + ", " + dbUserTable + "."
					+ dbUserTableEntityField + ", 0";
			superUserQuery += " from " + dbUserTable;
			superUserQuery += " where upper(" + dbUserTableUserField
					+ ")=? and bloqueado = false";

			// get the user roles query out of the options
			String dbUserRoleSelect = (String) options.get("userRoleSelect");
			rolesQuery = dbUserRoleSelect;

//			Code..debug("userQuery = " + userQuery);
//			Code.debug("rolesQuery = " + rolesQuery);

			this.subject = subject;
			this.callbackHandler = callbackHandler;
		} catch (Exception e) {
			throw new IllegalStateException(e.toString());
		}
	}

	/* ------------------------------------------------ */
	/**
	 * Load info from database
	 * 
	 * @param userName
	 *            user info to load
	 * @throws SQLException
	 */
	public void loadUser(String userName) throws SQLException {

		Vector vParametros = new Vector();

		String usuarioConsulta = userName;
		String cadenaTest = "_T_";
		if (userName.contains(cadenaTest))
			usuarioConsulta = userName.substring(0, userName.indexOf("_T_"));

		vParametros.add(0, usuarioConsulta.toUpperCase());

		logger.debug("Ejecutando el userQuery " + userQuery);
		CResultadoOperacion resultado = COperacionesControl.ejecutarQuery(
				userQuery, vParametros);
		if (!resultado.getResultado() || (resultado.getVector() == null)
				|| (resultado.getVector().size() == 0)) {
			resultado = COperacionesControl.ejecutarQuery(superUserQuery,
					vParametros);
			if (!resultado.getResultado() || (resultado.getVector() == null)
					|| (resultado.getVector().size() == 0)) {
				logger.info("El usuario " + userName + " (" + usuarioConsulta
						+ ") no existe en el sistema ");
				throw new IllegalStateException("El usuario " + userName
						+ " no existe en el sistema. \nRevise la password.");
			}
		}
		String sIdField = "";
		if (resultado.getVector().size() > 0) {
			Vector auxVector = (Vector) resultado.getVector().get(0);
			sIdField = (String) auxVector.get(1);
			// ASO elimina porque no es coherente
			/*
			 * String sIdEntidad = (String) auxVector.get(2); String
			 * sIdMunicipio = (String) auxVector.get(3);
			 * com.geopista.app.AppContext
			 * .setIdMunicipio(Integer.parseInt(sIdMunicipio));
			 * com.geopista.app.
			 * AppContext.setIdEntidad(Integer.parseInt(sIdEntidad));
			 */
			logger.debug("Usuario existe en el sistema " + userName);
		}

		vParametros = new Vector();
		vParametros.add(0, sIdField);
		vParametros.add(1, sIdField);
		vParametros.add(2, sIdField);

		resultado = COperacionesControl.ejecutarQuery(rolesQuery, vParametros);
		if (!resultado.getResultado() || (resultado.getVector() == null)) {
			logger.info("El usuario " + userName + " no tiene ningún permiso. "
					+ rolesQuery);
			throw new IllegalStateException("El usuario no tiene permisos");
		}

		roleGroup = new LocalgisJAASGroup(LocalgisJAASGroup.ROLES);
		//roleGroup = new JAASGroup(JAASGroup.ROLES);
		roleGroup.addMember(new JAASRole("Geopista.Common.Actions"));
		for (Enumeration e = resultado.getVector().elements(); e
				.hasMoreElements();) {
			String roleName = (String) e.nextElement();
			roleGroup.addMember(new JAASRole(roleName));
			logger.debug("Añadiendo permiso:" + roleName);
		}
	}

}
