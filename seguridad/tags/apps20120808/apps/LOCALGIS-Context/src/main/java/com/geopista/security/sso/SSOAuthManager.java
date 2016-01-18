package com.geopista.security.sso;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.Principal;
import java.security.acl.Group;

import javax.security.auth.login.LoginException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Unmarshaller;

import com.geopista.app.AppContext;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.Sesion;
import com.geopista.security.config.SecurityPropertiesStore;
import com.geopista.security.sso.global.SSOConstants;
import com.geopista.security.sso.protocol.control.SesionBean;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.security.GeopistaPrincipal;
import com.geopista.security.SecurityManager;

public class SSOAuthManager {
	
	private static final Log logger = LogFactory.getLog(SSOAuthManager.class);
		
	private static String sUrl;
	
	private static String app=null;

    /**
     * Gestion de la autenticación con SSO  
     *
     */
    public static boolean ssoAuthManager(String profile){
    	boolean result = false;
    	
    	if (profile==null)
    		profile=app;
    	else
    		app=profile;
    	
 		try { 			 			
	    	if(Boolean.valueOf(AppContext.getApplicationContext().getString(SSOConstants.SSO_AUTH_ACTIVE))){
	 			String url = AppContext.getApplicationContext().getString(SSOConstants.SSO_SERVER_URL);			
	 			sUrl = SecurityManager.getUrl();	
	 			SecurityManager.setUrl(url);
		    	String SSOIdSesion = AppContext.getApplicationContext().getUserPreference(SSOConstants.SSO_ID_SESION,"",false);  
		    	if(!SSOIdSesion.equals("")){
		    		AppContext.getApplicationContext().setPartialLogged(true);
		    		SecurityManager.login(AppContext.getApplicationContext().getString(SSOConstants.SSO_USERNAME),SecurityPropertiesStore.getSecurityPropertiesStore().getString(SSOConstants.SSO_PASSWORD),profile);
		    		AppContext.getApplicationContext().setPartialLogged(false);
		    		Sesion SSOSesion = getUnaSesion(url, SSOIdSesion);	
		    		SecurityManager.logout();
		    		if(SSOSesion.getIdSesion()!=null && SSOSesion.getIdSesion().equals(SSOIdSesion)){			    			
		    			logger.info("ssoAuthManager() - Existe Sesion");
			    		SecurityManager sm = new SecurityManager();
			    		SSOSesion.setIdApp(profile);
			    		sm.setSession(SSOSesion);	
			    		sm.setSession(SSOSesion.getIdSesion());
			    		sm.setIdApp(profile);
			    		sm.setUrlNS(sUrl);	
			    		//sm.setUrlNS(url);
			    		sm.userPrincipal = (GeopistaPrincipal)SSOSesion.getUserPrincipal();
			    		sm.logged = true; 
			    		sm.connected = true;
			    		SecurityManager.setSm(sm);	
			    		if(AppContext.getApplicationContext()!=null)
			    			AppContext.getApplicationContext().getBlackboard().put(AppContext.SESION_KEY,SSOSesion);
			    		result = true;
			    		saveSSOAppSesion(url, SSOIdSesion);
		    		}else clearRegistrySesion(); 
		    	}else 
		    		logger.info("ssoAuthManager() - No Existe Sesion");
	        } 	    	
		} catch (LoginException e) {
			logger.info("ssoAuthManager() - ERROR: " + e.getMessage());
		} catch (Exception e) {
			logger.info("ssoAuthManager() - ERROR: " + e.getMessage());
			try {
				SecurityManager.logout();
			} catch (Exception e1) {
				logger.info("ssoAuthManager() - ERROR: " + e1.getMessage());
			}
		}
		return result;
    }
    
    public static void clearRegistrySesion(){
    	AppContext.getApplicationContext().setUserPreference(SSOConstants.SSO_ID_SESION, ""); 
    } 

    public static void saveSSORegistry(){
  		if (isSSOActive()){
  			AppContext.getApplicationContext()
  					.setUserPreference(
  							SSOConstants.SSO_ID_SESION,
  							SecurityManager.getIdSesion());	
  			logger.debug("GUARDADO REGISTRO: "
  				+ SecurityManager.getIdSesion());
  		}
    }
    
    public static boolean isSSOActive(){
    	return Boolean.valueOf(AppContext.getApplicationContext().getString(SSOConstants.SSO_AUTH_ACTIVE));
    }
    
	 public static Sesion getUnaSesion(String url, String sIdSesion) throws Exception
     {
          try {
              StringReader sr=EnviarSeguro.enviarPlano(url+SSOConstants.SERVLET_GETONESESSION,sIdSesion);
              SesionBean sbSesion=(SesionBean)Unmarshaller.unmarshal(SesionBean.class,sr);          
           	  Principal sUserPrincipal = new GeopistaPrincipal(sbSesion.getsName(),sbSesion.getIdSesion());             
           	  Group sGroup = null;
              Sesion  sSesion = new Sesion(sbSesion.getIdSesion(), sbSesion.getIdApp(), sUserPrincipal, sGroup);
              sSesion.setIdEntidad(sbSesion.getIdEntidad());
              sSesion.setIdMunicipio(sbSesion.getIdMunicipio());
              sSesion.setAlMunicipios(sbSesion.getAlMunicipios());
              return sSesion;
        }
        catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception al obtener las sesiones: " + sw.toString());
            throw ex;
        }
     }
	 
	 public static boolean saveSSOAppSesion(String url, String sIdSesion) throws Exception
     {
          try {
              StringReader sr=EnviarSeguro.enviarPlano(url + SSOConstants.SERVLET_SAVESSOAPPSESSION,sIdSesion);
              CResultadoOperacion cResultadoOperacion=(CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class,sr);              
              return cResultadoOperacion.getResultado();
        }
        catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception al obtener las sesiones: " + sw.toString());
            throw ex;
        }
     }
    
}