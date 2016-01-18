/**
 * SSOAuthManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.security.sso;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.security.acl.Group;

import javax.security.auth.login.LoginException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Unmarshaller;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.utilidades.PasswordManager;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.security.GeopistaPrincipal;
import com.geopista.security.SecurityManager;
import com.geopista.security.sso.global.SSOConstants;
import com.geopista.security.sso.protocol.control.SesionBean;
import com.geopista.util.config.UserPreferenceStore;
import com.sun.identity.wss.security.WSSConstants;

public class SSOAuthManager {
	private static final Log logger = LogFactory.getLog(SSOAuthManager.class);

	private static final boolean SSO_ACTIVE = true;

	private static String sUrl;

	private static String app = null;
	


	/**
	 * Gestion de la autenticación con SSO
	 * 
	 */
	public static boolean ssoAuthManager(String profile) {
		boolean result = false;

		logger.info("Entrando en el metodo de autenticacion para SSO");
		if (profile == null)
			profile = app;
		else
			app = profile;

		try {
			String url = AppContext.getApplicationContext().getString(
					UserPreferenceConstants.LOCALGIS_SERVER_URL)
					+ WebAppConstants.PRINCIPAL_WEBAPP_NAME;
			sUrl = SecurityManager.getUrl();
			SecurityManager.setUrl(url);
			String SSOIdSesion = UserPreferenceStore.getUserPreference(
					UserPreferenceConstants.SSO_SESSION_ID, "", false);
			if (!SSOIdSesion.equals("")) {
				//Almacenar datos de registro anterior antes de hacer conexion ficticia con SSO para mantener informacion real de conexion
				String lastUser = UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_LOGIN_LAST_USERNAME, null, false);
				String lastPassword = UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_LOGIN_LAST_PASSWORD, null, false);
				
				AppContext.getApplicationContext().setPartialLogged(true);
				logger.info("Autenticando como " + SSOConstants.SSO_USERNAME);
				SecurityManager.login(SSOConstants.SSO_USERNAME,
						SSOConstants.SSO_PASSWORD, profile);
				AppContext.getApplicationContext().setPartialLogged(false);

				// Verificamos si la Sesion almacenada en el registro realmente
				// existe o no
				Sesion SSOSesion = getUnaSesion(url, SSOIdSesion);

				// Desconctamos la sesion del SSOADMIN (No desconectamos la
				// buena realmente solo la que internamente hemos creado)
				SecurityManager.logout();
				
				//Restaurar valores del registro originales antes de login con SSO para mantener informacion correcta
				UserPreferenceStore.setUserPreference(UserPreferenceConstants.LOCALGIS_LOGIN_LAST_USERNAME, lastUser);
				UserPreferenceStore.setUserPreference(UserPreferenceConstants.LOCALGIS_LOGIN_LAST_PASSWORD, lastPassword);
				
				// Ahora verificamos si la sesion obtenida del servidor
				// corresponde a la que hemos recuperado
				// en cuyo caso es que previamente hemos abierta la sesion desde
				// otra aplicacion
				if (SSOSesion.getIdSesion() != null
						&& SSOSesion.getIdSesion().equals(SSOIdSesion)) {
					logger.info("Existe Sesion. Usuario autenticado:"
							+ (GeopistaPrincipal) SSOSesion.getUserPrincipal());
					SecurityManager sm = new SecurityManager();
					SSOSesion.setIdApp(profile);
					sm.setSession(SSOSesion);
					sm.setSession(SSOSesion.getIdSesion());
					sm.setIdApp(profile);
					sm.setUrlNS(sUrl);
					// sm.setUrlNS(url);
					sm.userPrincipal = (GeopistaPrincipal) SSOSesion
							.getUserPrincipal();
					sm.logged = true;
					sm.connected = true;
					SecurityManager.setSm(sm);
					if (AppContext.getApplicationContext() != null)
						AppContext
								.getApplicationContext()
								.getBlackboard()
								.put(UserPreferenceConstants.SESION_KEY,
										SSOSesion);
					result = true;
					saveSSOAppSesion(url, SSOIdSesion);
				} else {
					// Si la sesion no existe en el servidor, la eliminamos del
					// registro.
					logger.info("La sesion almacenada en el registro ya no existe en el servidor:"
							+ SSOIdSesion);
					clearRegistrySesion();
				}
			} else {
				logger.info("No Existe Sesion en el registro del sistema");
			}
		} catch (LoginException e) {
			logger.info("ssoAuthManager() - ERROR: " + e);
		} catch (Exception e) {
			logger.info("ssoAuthManager() - ERROR: " + e);
			try {
				SecurityManager.logout();
			} catch (Exception e1) {
				logger.info("ssoAuthManager() - ERROR: " + e1);
			}
		}
		return result;
	}

	
	
    
	/**
	 * Verificamos si la sesion almacenada en el servidor es todavia valida Si
	 * los datos que nos envian estan vacios, significa que o bien la sesion ha
	 * caducado o bien el servidor se ha caido y se ha vuelto a levantar.
	 * 
	 * @return
	 */
	public static boolean isSessionValid() {
		boolean continuar = true;

		
  	  //Metiendo esta propiedad localgis.intentos.heartbeat en el registro nos permite hacer mas de X intentos
  	  //de hearbeat por si el primero no responde.
  	  int numIntentosConexion=3;
		try {
			String numIntentosRegistro = UserPreferenceStore.getUserPreference("localgis.intentos.heartbeat","3",true);
			  numIntentosConexion=Integer.parseInt(numIntentosRegistro);
		} catch (NumberFormatException e1) {
		}
		int numIntentos = 0;

		while (continuar) {
			try {
				String SSOIdSesion = UserPreferenceStore.getUserPreference(
						UserPreferenceConstants.SSO_SESSION_ID, "", false);
				String url = AppContext.getApplicationContext().getString(
						UserPreferenceConstants.LOCALGIS_SERVER_URL)
						+ WebAppConstants.PRINCIPAL_WEBAPP_NAME;
				Sesion SSOSesion = getUnaSesion(url, SSOIdSesion, false);

				if (SSOSesion.getIdSesion() != null
						&& SSOSesion.getIdSesion().equals(SSOIdSesion)) {
					return true;
				} else {
					if (numIntentos > numIntentosConexion)
						logger.error("Error al verificar si la sesion es valida: "
								+ SSOIdSesion
								+ " tras "
								+ numIntentos
								+ " intentos");
					numIntentos++;
				}
			} catch (Exception e) {
				numIntentos++;
				if (numIntentos > numIntentosConexion)
					logger.error(
							"Excepcion al verificar si la sesion es valida tras "
									+ numIntentos + " intentos", e);
			}

			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
			;

			if (numIntentos > numIntentosConexion) {
				return false;
			}
		}
		return true;
	}

	public static void clearRegistrySesion() {
		UserPreferenceStore.setUserPreference(
				UserPreferenceConstants.SSO_SESSION_ID, "");
	}

	public static void saveSSORegistry() {
		if (isSSOActive()) {
			UserPreferenceStore.setUserPreference(
					UserPreferenceConstants.SSO_SESSION_ID,
					SecurityManager.getIdSesion());
			logger.info("Guardando identificador de sesion en registro: "
					+ SecurityManager.getIdSesion());
		}
	}

	public static boolean isSSOActive() {
		return SSO_ACTIVE;
	}

	public static Sesion getUnaSesion(String url, String sIdSesion)
			throws Exception {
		return getUnaSesion(url, sIdSesion, true);
	}

	public static Sesion getUnaSesion(String url, String sIdSesion,
			boolean mostrarExcepcion) throws Exception {
		try {
			StringReader sr = EnviarSeguro.enviarPlano(url
					+ ServletConstants.GETONESESSION_SERVLET_NAME, sIdSesion);
			SesionBean sbSesion = (SesionBean) Unmarshaller.unmarshal(
					SesionBean.class, sr);
			Principal sUserPrincipal = new GeopistaPrincipal(
					sbSesion.getsName(), sbSesion.getIdSesion());
			Group sGroup = null;
			Sesion sSesion = new Sesion(sbSesion.getIdSesion(),
					sbSesion.getIdApp(), sUserPrincipal, sGroup);
			sSesion.setIdEntidad(sbSesion.getIdEntidad());
			sSesion.setIdMunicipio(sbSesion.getIdMunicipio());
			sSesion.setAlMunicipios(sbSesion.getAlMunicipios());
			return sSesion;
		} catch (Exception ex) {
			if (mostrarExcepcion) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
				logger.error("Exception al obtener las sesiones: "
						+ sw.toString());
			}
			logger.error("Error al verificar la disponibilidad de la sesion:"
					+ sIdSesion);
			throw ex;
		}
	}

	public static void invalidateSession() throws Exception {
		try {
			String url = AppContext.getApplicationContext().getString(
					UserPreferenceConstants.LOCALGIS_SERVER_URL)
					+ WebAppConstants.PRINCIPAL_WEBAPP_NAME;

			StringReader sr = EnviarSeguro.enviarPlano(url
					+ ServletConstants.INVALIDATE_SESSION_SERVLET_NAME, null);
			CResultadoOperacion resultadoOperacion = (CResultadoOperacion) Unmarshaller
					.unmarshal(CResultadoOperacion.class, sr);

			if (!resultadoOperacion.getResultado()) {
				logger.warn("resultadoOperacion.getResultado(): "
						+ resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): "
						+ resultadoOperacion.getDescripcion());
				throw new GeneralSecurityException("Error al ejecutar logout"
						+ resultadoOperacion.getDescripcion());
			}

		} catch (Exception ex) {
			throw ex;
		}
	}

	public static boolean saveSSOAppSesion(String url, String sIdSesion)
			throws Exception {
		try {
			StringReader sr = EnviarSeguro.enviarPlano(url
					+ ServletConstants.SAVESSOAPPSESSION_SERVLET_NAME,
					sIdSesion);
			CResultadoOperacion cResultadoOperacion = (CResultadoOperacion) Unmarshaller
					.unmarshal(CResultadoOperacion.class, sr);
			return cResultadoOperacion.getResultado();
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception al obtener las sesiones: " + sw.toString());
			throw ex;
		}
	}


	public static void logout()
    {
        try
        {
            if (SecurityManager.isLogged())
                SecurityManager.logout();
        } catch (Exception e) //TODO: revise extra-wide exception handle
        {
            logger.error("logout()", e);
        }
        
        UserPreferenceStore.setUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME, null);
    }
}
