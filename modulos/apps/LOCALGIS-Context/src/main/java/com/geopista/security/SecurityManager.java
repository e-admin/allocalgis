/**
 * SecurityManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.security;

import java.io.Serializable;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.net.ssl.KeyManagerFactory;
import javax.security.auth.login.LoginException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Unmarshaller;

import com.geopista.app.AppConstants;
import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.administrador.MunicipalityOperations;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.EncriptarPassword;
import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.security.dnie.utils.CertificateUtils;
import com.geopista.util.config.UserPreferenceStore;

public class SecurityManager implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	

	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory.getLog(SecurityManager.class);

	protected String sUrl=null;
   	protected String idApp = null;
    protected String idMunicipio = null;
    protected String idEntidad = null;
	protected String idSesion = null;
	protected String userIntentos = null;
	protected int intentos_reiterados = 0;
	protected int intentos = AppConstants.INTENTOS_MINIMOS;
    public  GeopistaPrincipal userPrincipal=null;
    public  boolean connected=false;
    public  boolean logged=false;
    public  boolean previouslyLogged=false;
    public  static final String HeartBeat="TODO_OK";
    public  TestConnection testConnection=null;

	private ISesion iSesion;
    private  static SecurityManager sm =null;

    public static boolean initHeartBeat=true;
    
    /**
     * Devuelve la clase estatica del security manager
     */

    public  SecurityManager (){
        if (sm==null) sm=this;
    }

    /**
     * Devuelve la clase estatica del security manager
     */

    public static SecurityManager getSm(){
        return sm;
    }
    
    /**
     * pone el valor
     */

    public static void setSm(SecurityManager instanciaSm){
       sm=instanciaSm;
    }
    /**
     * Inicializa la clase para utilizarla de forma estatica
     * @return una instancia de SecurityManager
     */
    public static SecurityManager initSM(){
           if (sm!=null) return sm;
           new SecurityManager();
           return sm;
    }
    public static void setsUrl(String sNewUrl) {
        initSM().setsUrlNS(sNewUrl);
     }

    public void setsUrlNS  (String sNewUrl) {
        sUrl = sNewUrl;
     }
    

    public static void setUrl(String sNewUrl) {
    	initSM().setUrlNS(sNewUrl);
    }

    public void setUrlNS  (String sNewUrl) {
    	sUrl = sNewUrl;
    }

    public static String getIdApp() {
		return initSM().getIdAppNS();
	}

    public static String getUserIntentos() {
		return initSM().userIntentos;
	}
    
    public static int getIntentos() {
		return initSM().intentos;
	}
    
    public static void setIntentos(int intentos){
    	initSM().setIntentosNS(intentos);
    }
    
    public int getIntentosNS(){
    	return intentos;
    }
    
    public void setIntentosNS(int intentos){
        this.intentos = intentos;
    }
    
    public  String getIdAppNS(){
        return idApp;
    }
    
    public void setIdApp(String idApp){
    	this.idApp=idApp;
    }

    public static GeopistaPrincipal getUserPrincipal() {
        return initSM().getUserPrincipalNS();
    }
    public GeopistaPrincipal getUserPrincipalNS() {
        return userPrincipal;
    }
    public static void setLogged(boolean loggedTemp){
    	initSM().setLoggedNS(loggedTemp);
    }
    public  void setLoggedNS(boolean loggedTemp) {
		logged=loggedTemp;
	}

    public static void setIdSesion(String idSesionTemp){
    	initSM().setIdSesionNS(idSesionTemp);
    }
    public  void setIdSesionNS(String idSesionTemp) {
		idSesion=idSesionTemp;
	}

    public static String getIdSesion() {
        return initSM().getIdSesionNS();
    }

	public  String getIdSesionNS() {
		return idSesion;
	}
    public static GeopistaPrincipal getPrincipal()
    {
        return initSM().getPrincipalNS();
    }

    public GeopistaPrincipal getPrincipalNS()
    {
        return userPrincipal;
    }
    

    public static boolean seleccionarMunicipio(String sUsuario, String sPassword, String sIdApp, String sIdMunicipio) throws Exception{
        return initSM().seleccionarMunicipioNS(sUsuario, sPassword, sIdApp,sIdMunicipio);
    }
    
    
    public  boolean seleccionarMunicipioNS(String sUsuario, String sPassword, String sIdApp, String sIdMunicipio) throws Exception{
		String url = AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_SERVER_URL);
		try{
			setIdMunicipio(sIdMunicipio);
			AppContext.setIdMunicipio(Integer.parseInt(sIdMunicipio));
	        CResultadoOperacion resultadoOperacion;
	        try {
	            StringReader sr = EnviarSeguro.enviar(url + WebAppConstants.GEOPISTA_WEBAPP_NAME + ServletConstants.SELECTMUNICIPALITY_SERVLET_NAME, "", sUsuario, sPassword);
	   		    resultadoOperacion= (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
	            //logger.debug("Resultado: "+ sr);
	            //System.out.println("Resultado:"+resultadoOperacion.getResultado());
		    } catch (Exception e) {
		    	throw e;
		    }
	
			if (!resultadoOperacion.getResultado()) {
				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
	            throw new LoginException(resultadoOperacion.getResultado()+" "+resultadoOperacion.getDescripcion());
			} else
	        {
	            idSesion=resultadoOperacion.getDescripcion();
	            userPrincipal= new GeopistaPrincipal(sUsuario,idSesion);
	            logged=true;
//	            Vector vecResultado = resultadoOperacion.getVector();
//	            idMunicipio = (String)vecResultado.elementAt(0);
//	            MunicipalityOperations municipalityOperations = new MunicipalityOperations();
//	            List listMunicipios = municipalityOperations.construirListaMunicipios((List)vecResultado.elementAt(1));
//	            if (listMunicipios!=null && listMunicipios.size()>0)
//	            	idMunicipio=new Integer(((com.geopista.app.catastro.model.beans.Municipio)listMunicipios.iterator().next()).getId()).toString();
//	    		if (AppContext.getApplicationContext().getBlackboard()!=null){
//	                ISesion iSesion = new Sesion();
//	                iSesion.setIdEntidad(idEntidad);
//	                iSesion.setIdMunicipio(idMunicipio);
//	                iSesion.setAlMunicipios(listMunicipios);
//	                AppContext.getApplicationContext().getBlackboard().put(AppContext.SESION_KEY,iSesion);
//	                //Para pruebas de carga Multi-Thread lo incluimos tambien en el Security Manager
//	                this.setSession(iSesion);
//	            }
	            
	        }
			return true;
	
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			throw ex;
	
		}
	}
		
    

    public static boolean seleccionarEntidad(String sUsuario, String sPassword, String sIdApp, String sIdEntidad) throws Exception{
        return initSM().seleccionarEntidadNS(sUsuario, sPassword, sIdApp,sIdEntidad);
    }
    public  boolean seleccionarEntidadNS(String sUsuario, String sPassword, String sIdApp, String sIdEntidad) throws Exception{
		String url = AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_SERVER_URL);
		try{
			setIdEntidad(sIdEntidad);
	        CResultadoOperacion resultadoOperacion;
	        try {
	            StringReader sr = EnviarSeguro.enviar(url + WebAppConstants.GEOPISTA_WEBAPP_NAME + ServletConstants.SELECTENTITY_SERVLET_NAME, "", sUsuario, sPassword);
	   		    resultadoOperacion= (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
	            //logger.debug("Resultado: "+ sr);
	            //System.out.println("Resultado:"+resultadoOperacion.getResultado());
		    } catch (Exception e) {
		    	throw e;
		    }
	
			if (!resultadoOperacion.getResultado()) {
				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
	            throw new LoginException(resultadoOperacion.getResultado()+" "+resultadoOperacion.getDescripcion());
			} else
	        {
	            idSesion=resultadoOperacion.getDescripcion();
	            userPrincipal= new GeopistaPrincipal(sUsuario,idSesion);
	            logged=true;
	            Vector vecResultado = resultadoOperacion.getVector();
	            idEntidad = (String)vecResultado.elementAt(0);
	            MunicipalityOperations municipalityOperations = new MunicipalityOperations();
	            List listMunicipios = municipalityOperations.construirListaMunicipios((List)vecResultado.elementAt(1));
	            if (listMunicipios!=null && listMunicipios.size()>0)
	            	idMunicipio=new Integer(((com.geopista.app.catastro.model.beans.Municipio)listMunicipios.iterator().next()).getId()).toString();
	    		if (AppContext.getApplicationContext().getBlackboard()!=null){
	                ISesion iSesion = new Sesion();
	                iSesion.setIdEntidad(idEntidad);
	                iSesion.setIdMunicipio(idMunicipio);
	                iSesion.setAlMunicipios(listMunicipios);
	                AppContext.getApplicationContext().getBlackboard().put(UserPreferenceConstants.SESION_KEY,iSesion);
	                //Para pruebas de carga Multi-Thread lo incluimos tambien en el Security Manager
	                this.setSession(iSesion);
	            }
	            
	        }
			return true;
	
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			throw ex;
	
		}
	}
    public static boolean login(String sUsuario, String sPassword, String sIdApp) throws Exception{
        return initSM().loginNS(sUsuario, sPassword, sIdApp);
    }
    
    //---NUEVO--->
    public static boolean loginCertificate(String sIdApp, X509Certificate certificate, KeyManagerFactory kmf, String certificateUrl) throws Exception{
    	return initSM().loginCertificateNS(sIdApp, certificate, kmf, certificateUrl);
    }
    
	public boolean loginCertificateNS(String sIdApp, X509Certificate certificate, KeyManagerFactory kmf, String certificateUrl) throws Exception{
		try {
	
			String sUsuario = CertificateUtils.getNIFfromSubjectDN(certificate.getSubjectDN().getName());
			
			logger.info("INICIO SecurityManager URL: " + certificateUrl);			
			logger.info("INICIO SecurityManager Certificate: " + certificate.getSubjectDN().getName());
            if (idSesion!=null)
            {
                logger.warn("El usuario ya ha hecho login con anterioridad");
                throw new LoginException("El usuario ya ha hecho login con anterioridad");
            }
            idApp = sIdApp;
          /// idEntidad=new Integer(iIdEntidad).toString();
            CResultadoOperacion resultadoOperacion;
            try {
            	StringReader sr=null;
            	//Esto sirve para hacer pruebas de carga con multiples hilos dentro
            	//de un mismo equipo sin utilizar variables globales tipo AppContext.
            	//----NUEVO---->
//            	if(SSOAuthManager.isSSOActive()){
//            		sr = EnviarSeguro.enviarCertificado(certificateUrl+sUrlLogin, "", certificate, kmf);
//            	}
//            	//---FIN NUEVO-->
//            	else	
            		sr = EnviarSeguro.enviarCertificado(certificateUrl+ServletConstants.LOGIN_SERVLET_NAME, "", certificate, kmf);
       		    resultadoOperacion = (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
		    } catch (Exception e) {
		    	userIntentos = sUsuario;
		    	logger.error(e);
		    	throw e;
		    }

			if (!resultadoOperacion.getResultado()) {
				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
                throw new LoginException(resultadoOperacion.getResultado()+resultadoOperacion.getDescripcion());
			} else
            {
				//Obtenemos el identificador de sesion que nos devuelve el login.
                idSesion=resultadoOperacion.getDescripcion();            
                
                userPrincipal= new GeopistaPrincipal(sUsuario,idSesion);
                logged=true;
                try{
	                Vector vecResultado = resultadoOperacion.getVector();
	                idEntidad = (String)vecResultado.elementAt(1);
	                //idMunicipio = (String)vecResultado.elementAt(1);
	                MunicipalityOperations municipalityOperations = new MunicipalityOperations();
	                List listMunicipios =construirListaMunicipios((List)vecResultado.elementAt(2));
	                	                
	                //List listMunicipios = municipalityOperations.construirListaMunicipios((List)vecResultado.elementAt(2));
	                if (idEntidad.equals("0")){
	                    List listEntidades = municipalityOperations.construirListaEntidades((List)vecResultado.elementAt(3));
		                AppContext.getApplicationContext().getBlackboard().put(UserPreferenceConstants.ENTIDADES,listEntidades);
	                }
	                if (listMunicipios!=null && listMunicipios.size()>0)
		            	idMunicipio=new Integer(((com.geopista.app.catastro.model.beans.Municipio)listMunicipios.iterator().next()).getId()).toString();
	              
	                String sPasswordRecuperada=null;
	                try{
	                	sPasswordRecuperada=(String)vecResultado.elementAt(4);
	                }
	                catch (Exception e){
	                }
	                //Almacenamos la sesion en el contexto.
	                if (AppContext.getApplicationContext().getBlackboard()!=null){
		                ISesion iSesion = new Sesion();
		                iSesion.setIdEntidad(idEntidad);
		                iSesion.setIdMunicipio(idMunicipio);
		                iSesion.setIdCurrentEntidad(String.valueOf(AppContext.getIdEntidad()));
		                iSesion.setIdCurrentMunicipio(String.valueOf(AppContext.getIdMunicipio()));
		                iSesion.setAlMunicipios(listMunicipios);
		                      		               
		                AppContext.getApplicationContext().getBlackboard().put(UserPreferenceConstants.SESION_KEY,iSesion);
		                //Para pruebas de carga Multi-Thread lo incluimos tambien en el Security Manager
		                
		                UserPreferenceStore.setUserPreference(UserPreferenceConstants.LOCALGIS_LOGIN_LAST_USERNAME,sUsuario);
		                UserPreferenceStore.setUserPreference("LAST_LOGIN",sUsuario);
		                if (sPasswordRecuperada!=null)
		                	UserPreferenceStore.setUserPreference("LAST_PASS", sPasswordRecuperada);
		                else 
		                	UserPreferenceStore.setUserPreference("LAST_PASS", "");
						
		                this.setSession(iSesion);
	                }
	                else{
	                	logger.info("Aplicacion Context es nulo");
	                }
                }catch(Exception ex){
                	logger.error("Error al obtener los datos del login", ex);
                	idSesion=null;
                	logged=false;
                	throw ex;
                }
                
            }
			return true;

		} catch (Exception ex) {
			logger.error("Exception al hacer el login: " +ex );
			throw ex;

		}
	}
    
    //---FIN NUEVO-->


	public  boolean loginNS(String sUsuario, String sPassword, String sIdApp) throws Exception{
		return loginNS(sUsuario,sPassword,sIdApp,false);
	}
    
	/**
	 * El valor de test es para realizar pruebas de carga del sistema.
	 * @param sUsuario
	 * @param sPassword
	 * @param sIdApp
	 * @param test
	 * @return
	 * @throws Exception
	 */
	public  boolean loginNS(String sUsuario, String sPassword, String sIdApp,boolean test) throws Exception{
		try {
            if (sUrl==null) return false;
            if (idSesion!=null)
            {
                logger.warn("El usuario ya ha hecho login con anterioridad");
                throw new LoginException("El usuario ya ha hecho login con anterioridad");
            }
            idApp = sIdApp;
          /// idEntidad=new Integer(iIdEntidad).toString();
            CResultadoOperacion resultadoOperacion;
            try {
            	StringReader sr=null;
            	//Esto sirve para hacer pruebas de carga con multiples hilos dentro
            	//de un mismo equipo sin utilizar variables globales tipo AppContext.
            	//----NUEVO---->
            	//logger.warn("PASO1");
//            	if(SSOAuthManager.isSSOActive()){
//                	//logger.warn("PASO2");
//            		sr = EnviarSeguro.enviar(sUrl+sUrlLogin, "", sUsuario, sPassword);
//            	}
//            	//---FIN NUEVO-->
//            	else 
            	if (test){
                	//logger.warn("PASO3");
            		sr = EnviarSeguro.enviar(sUrl+ServletConstants.LOGIN_SERVLET_NAME, "", sUsuario, sPassword,this);
            	}
            	else	
            		sr = EnviarSeguro.enviar(sUrl+ServletConstants.LOGIN_SERVLET_NAME, "", sUsuario, sPassword);
       		    resultadoOperacion= (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
		    } catch (Exception e) {

		    	userIntentos = sUsuario;
		    	StringReader result= EnviarSeguro.enviarPlano(sUrl+ServletConstants.CSERVLETINTENTOS_SERVLET_NAME,"");
		    	resultadoOperacion= (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, result);
		    	Vector vecResult = resultadoOperacion.getVector();
		    	if ((Integer)vecResult.elementAt(0) != null)
		    		intentos = (Integer)vecResult.elementAt(0);
		    	Boolean bloqueado = false;
				if ((Boolean)vecResult.elementAt(1) != null)
		    		bloqueado  = (Boolean)vecResult.elementAt(1);
				if ((Integer)vecResult.elementAt(2) != null)
		    		intentos_reiterados = (Integer)vecResult.elementAt(2);
				Boolean usuarioNoExiste = false; 
				if ((Boolean)vecResult.elementAt(3) != null)
		    		usuarioNoExiste = (Boolean)vecResult.elementAt(3);
                AppContext.getApplicationContext().getBlackboard().put("intentos",intentos);
                AppContext.getApplicationContext().getBlackboard().put("bloqueado",((Boolean) bloqueado).toString());
                AppContext.getApplicationContext().getBlackboard().put("intentos_reiterados",intentos_reiterados);
                AppContext.getApplicationContext().getBlackboard().put("usuarioNoExiste",((Boolean) usuarioNoExiste).toString());
                throw e;
		    }

			if (!resultadoOperacion.getResultado()) {
				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
                throw new LoginException(resultadoOperacion.getResultado()+resultadoOperacion.getDescripcion());
			} else
            {
				AppContext.getApplicationContext().getBlackboard().put("usuarioNoExiste",((Boolean) false).toString());
				//Obtenemos el identificador de sesion que nos devuelve el login.
                idSesion=resultadoOperacion.getDescripcion();
                userPrincipal= new GeopistaPrincipal(sUsuario,idSesion);
                logged=true;
                try{
	                Vector vecResultado = resultadoOperacion.getVector();
	                idEntidad = (String)vecResultado.elementAt(1);
	                //idMunicipio = (String)vecResultado.elementAt(1);
	                MunicipalityOperations municipalityOperations = new MunicipalityOperations();
	                List listMunicipios =construirListaMunicipios((List)vecResultado.elementAt(2));
	                	                
	                //List listMunicipios = municipalityOperations.construirListaMunicipios((List)vecResultado.elementAt(2));
	                if (idEntidad.equals("0")){
	                    List listEntidades = municipalityOperations.construirListaEntidades((List)vecResultado.elementAt(3));
		                AppContext.getApplicationContext().getBlackboard().put(UserPreferenceConstants.ENTIDADES,listEntidades);
	                }
	                if (listMunicipios!=null && listMunicipios.size()>0)
		            	idMunicipio=new Integer(((com.geopista.app.catastro.model.beans.Municipio)listMunicipios.iterator().next()).getId()).toString();
	              
	                
	                String sPasswordRecuperada=null;
	                try{
	                	sPasswordRecuperada=(String)vecResultado.elementAt(4);
	                }
	                catch (Exception e){
	                }
	                
	                //Almacenamos la sesion en el contexto.
	                if (AppContext.getApplicationContext().getBlackboard()!=null){
		                ISesion iSesion = new Sesion();
		                iSesion.setIdEntidad(idEntidad);
		                iSesion.setIdCurrentEntidad(String.valueOf(AppContext.getIdEntidad()));
		                iSesion.setIdCurrentMunicipio(String.valueOf(AppContext.getIdMunicipio()));
		                iSesion.setIdMunicipio(idMunicipio);
		                iSesion.setAlMunicipios(listMunicipios);
		                      		                
		                //Verificacion de no mezcla de informacion
		                if (test){
			                logger.error("Valores obtenidos:"+sUsuario+" - "+idMunicipio);
			                if (idMunicipio!=null && !sUsuario.contains(idMunicipio)){
			                	logger.error("!!!!Posible fallo de autenticacion:"+sUsuario+"-"+idMunicipio);
			                }
		                }
		                AppContext.getApplicationContext().getBlackboard().put(UserPreferenceConstants.SESION_KEY,iSesion);
		                //Para pruebas de carga Multi-Thread lo incluimos tambien en el Security Manager
		                
		                UserPreferenceStore.setUserPreference(UserPreferenceConstants.LOCALGIS_LOGIN_LAST_USERNAME,sUsuario);
		                UserPreferenceStore.setUserPreference("LAST_LOGIN",sUsuario);
		                if (sPasswordRecuperada!=null)
		                	UserPreferenceStore.setUserPreference("LAST_PASS", sPasswordRecuperada);
		                else 
		                	UserPreferenceStore.setUserPreference("LAST_PASS", "");

		                
		                this.setSession(iSesion);
	                }
	                else{
	                	logger.info("Aplicacion Context es nulo");
	                }
                }catch(Exception ex){
                	logger.error("Error al obtener los datos del login", ex);
                	idSesion=null;
                	logged=false;
                	throw ex;
                }
                
            }
			return true;

		} catch (Exception ex) {
			logger.error("Exception al hacer el login: " +ex );
			throw ex;

		}
	}

	

	/**
     * A partir de una lista con Object[] en cada posición, creo un lista de clases Municipio
     */
    public List construirListaMunicipios (List lista){
    	List listFinal = new ArrayList();
    	int n = lista.size();
    	//System.out.println("tamaño:"+n);
    	for (int i=0;i<n;i++){
    		//System.out.println("Municipio:"+i);
    		Object[] elemento = (Object[])lista.get(i);
    		//System.out.println("campo1:"+elemento[0]);
    		//System.out.println("campo1:"+elemento[1]);
    		//System.out.println("campo1:"+elemento[2]);
    		listFinal.add(new com.geopista.app.catastro.model.beans.Municipio((String)elemento[0],(String)elemento[1],(String)elemento[2]));
    	}
    	return listFinal;
    }
    public static void relogin() throws Exception{
        initSM().reloginNS();
    }

    public void reloginNS() throws Exception{
		String lastUserName=UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_LOGIN_LAST_USERNAME,null,false); //$NON-NLS-1$
		String lastPassword=UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_LOGIN_LAST_PASSWORD,null,false); //$NON-NLS-1$
		//String defaultProfile=AppContext.getApplicationContext().getDefaultProfile();
        System.out.println("Login:"+lastUserName);
        System.out.println("Password:"+lastPassword);
        EncriptarPassword  ep=new EncriptarPassword(lastPassword);
		String passwordDesencriptada = ep.desencriptar();
		login(lastUserName, passwordDesencriptada, idApp);

	}
    public static boolean logout() throws Exception {
        return initSM().logoutNS();
    }

    public boolean logoutNS() throws Exception{
    	return logoutNS(false);
    }
    
    public boolean logoutNS(boolean test) throws Exception
    {
          try
          {
            if (sUrl==null) return false;
            CResultadoOperacion resultadoOperacion=null;
            if (test)
            	resultadoOperacion=EnviarSeguro.enviar(sUrl+ServletConstants.LOGOUT_SERVLET_NAME,"",this);
            else
            	resultadoOperacion=EnviarSeguro.enviar(sUrl+ServletConstants.LOGOUT_SERVLET_NAME,"");

                if (!resultadoOperacion.getResultado()) {
                    logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
                    logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
                    throw new GeneralSecurityException("Error al ejecutar logout"+resultadoOperacion.getDescripcion());
                }
                idSesion=null;
                logged=false;
                return true;
          }catch(GeneralSecurityException e)
          {
             throw e;
          }
          catch (Exception e)
          {
              throw e;
          }
    }
    
    
    /*public static boolean invalidateSession() throws Exception {
        return initSM().invalidateSessionNS();
    }

    public boolean invalidateSessionNS() throws Exception{
    	return invalidateSessionNS(false);
    }
    
    public boolean invalidateSessionNS(boolean test) throws Exception
    {
          try
          {
            String url = AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + WebAppConstants.PRINCIPAL_WEBAPP_NAME;	
            CResultadoOperacion resultadoOperacion=null;
            if (test)
            	resultadoOperacion=EnviarSeguro.enviar(url+ServletConstants.INVALIDATE_SESSION_SERVLET_NAME,"",this);
            else
            	resultadoOperacion=EnviarSeguro.enviar(url+ServletConstants.INVALIDATE_SESSION_SERVLET_NAME,"");

                if (!resultadoOperacion.getResultado()) {
                    logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
                    logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
                    throw new GeneralSecurityException("Error al ejecutar logout"+resultadoOperacion.getDescripcion());
                }
                idSesion=null;
                logged=false;
                return true;
          }catch(GeneralSecurityException e)
          {
             throw e;
          }
          catch (Exception e)
          {
              throw e;
          }
    }*/

   public static GeopistaAcl getPerfil(String sNombreAcl) throws java.security.acl.AclNotFoundException,Exception {
        return  initSM().getPerfilNS(sNombreAcl);
   }

    public  GeopistaAcl getPerfilNS(String sNombreAcl) throws java.security.acl.AclNotFoundException,Exception
    {
        try {
               //System.out.println("Pidiendo perfil");
                if (sUrl==null) return null;
               CResultadoOperacion rResultado=EnviarSeguro.enviar(sUrl+ServletConstants.GETPROFILE_SERVLET_NAME,sNombreAcl);
                if (rResultado.getResultado())
                {
                    GeopistaAcl geopistaAcl = new GeopistaAcl(userPrincipal,sNombreAcl);
                    GeopistaAclEntry entry= new GeopistaAclEntry(userPrincipal);
                    if (rResultado.getVector()!=null)
                    {
                        for (Enumeration e=rResultado.getVector().elements();e.hasMoreElements();)
                        {
                            GeopistaPermission auxPermiso= new GeopistaPermission((String)e.nextElement());
                            entry.addPermission(auxPermiso);
                            //System.out.println("Añadiendo Permiso: "+auxPermiso.getName());
                        }
                    }
                    geopistaAcl.addEntry(userPrincipal,entry);
                    return geopistaAcl;
                }
                else
                {
                    logger.error("Error al solicitar el perfil:"+rResultado.getDescripcion());
                    throw new java.security.acl.AclNotFoundException();
                }
        }catch (Exception e)
        {
            throw e;
        }

    }
    public static void callHeartBeat(String extraInfo){
    	initSM().callHeartBeatNS(extraInfo);  
    }

    public static void callHeartBeat(){
        initSM().callHeartBeatNS(null);     
        
        //Si estamos logeados verificamos si seguimos autenticados en el servidor.
        //para en caso contrario verificar la conectividad
       //if (AppContext.getApplicationContext()!=null && AppContext.getApplicationContext().isOnlyLogged())
       	//initSM().callHeartBeatNS_Auth();
        
    }

    public void callHeartBeatNS(String extraInfo)
       {
    		//Vamos a realizar varios intentos por si es un problema transitorio
    	  boolean continuar=true;
    	    	  
    	  //Metiendo esta propiedad localgis.intentos.heartbeat en el registro nos permite hacer mas de X intentos
    	  //de hearbeat por si el primero no responde.
    	  int numIntentosConexion=3;
		try {
			String numIntentosRegistro = UserPreferenceStore.getUserPreference("localgis.intentos.heartbeat","3",true);
			  numIntentosConexion=Integer.parseInt(numIntentosRegistro);
		} catch (NumberFormatException e1) {
		}
    	  int numIntentos=0;
    	
    	  while (continuar){
    		
	           try {
	                   if (sUrl==null) return;
	                   StringReader result= EnviarSeguro.enviarPlano(sUrl+ServletConstants.HEARTBEAT_SERVLET_NAME,"");
	                   if (extraInfo!=null)
	                	   logger.debug("Enviando HeartBeat a URL:"+sUrl+ServletConstants.HEARTBEAT_SERVLET_NAME+" "+extraInfo);
	                   else
	                	   logger.debug("Enviando HeartBeat a URL:"+sUrl+ServletConstants.HEARTBEAT_SERVLET_NAME+ "Datos:");
	                   String sResultado="";
	                   int cr = result.read();
	                   while(cr!=-1)
	                   {    sResultado+=(char)cr;
	                        cr = result.read();
	                   }
	                   if ((sResultado!=null) && (sResultado.indexOf(HeartBeat)>=0))
	                   {
	                       connected=true;
	                       //logger.debug("heartbeat funcionando: "+sResultado);
	                       return;
	                   }
	                   else
	                   {
	                       connected=false;
	                       numIntentos++;
		                   if (numIntentos>numIntentosConexion)
		                	   logger.info("Error al solicitar el heartbeat: "+sResultado+ " Direccion: "+sUrl+" Intento:"+numIntentos);
	                   }
	           }catch (Exception e)
	           {
	               connected=false;
	               numIntentos++;
	               if (numIntentos>numIntentosConexion)
	            	   logger.info("Error de conexion al solicitar el HeartBeat:  Direccion: "+sUrl+" Intentos:"+numIntentos);
		       }
	           
	           try{
		    		Thread.sleep(500);
		    	}
		    	catch(Exception e){ };
		    	
		    	if (numIntentos>numIntentosConexion){
		    		return;
		    	}
    	  }
       }

    public static String getIdMunicipio() {
        return initSM().getIdMunicipioNS();
    }
    public String getIdMunicipioNS() {
        return idMunicipio;
    }
//    public  static void setIdMunicipio(String sIdMunicipio) {
//        initSM().setIdMunicipioNS(sIdMunicipio);
//    }
//    
//    public  void setIdMunicipioNS(String sIdMunicipio) {
//        idMunicipio=sIdMunicipio;
//    }
    
    public static String getIdEntidad() {
        return initSM().getIdEntidadNS();
    }
    public  String getIdEntidadNS() {
        return idEntidad;
    }
    
    private static boolean canceled=false;
    public static void setCanceled(boolean canceled2){
    	canceled=canceled2;
    }
    
    public static boolean isCanceled(){
    	return canceled;
    }
    
    public  static void setIdEntidad(String sIdEntidad) {
        initSM().setIdEntidadNS(sIdEntidad);
    }
       
    public  void setIdEntidadNS(String sIdEntidad) {
    	idEntidad=sIdEntidad;
    }
    
    public static void setIdMunicipio(String sIdMunicipio) {
    	initSM().setIdMunicipioNS(sIdMunicipio);
    }
    
    public  void setIdMunicipioNS(String sIdMunicipio) {
    	idMunicipio=sIdMunicipio;
    }
    
    public static boolean isOnlyLogged()
    {
    	/*if ((initSM().idEntidad!=null) && (initSM().idMunicipio!=null) && initSM().isLoggedNS())
    		return true;
    	else
    		return false;*/	      		    		
        return initSM().isLoggedNS();
    }

    public static boolean isLogged()
    {

    	//CASOS ESPECIALES
    	//Cuando el usuario no pertenece a la entidad 0 el identidad viene vacio por lo que siempre apareceria
    	//como que no esta autenticado. Controlamos este caso.
    	if ((initSM().idEntidad==null) && (initSM().idMunicipio!=null) && initSM().isLoggedNS()){
    		return true;
    	}
    	if (((initSM().idEntidad!=null) && (initSM().idEntidad.equals("0"))) && (initSM().idMunicipio==null) && initSM().isLoggedNS()){
    		return true;
    	}
    	else if ((initSM().idEntidad!=null) && (initSM().idMunicipio!=null) && initSM().isLoggedNS())
	    	return true;
	    else
	    	return false;
    		    		
        //return initSM().isLoggedNS();
    }

    public  boolean isLoggedNS()
    {
    	//Algunas veces se ha detetado que el AppContest.USER_LOGIN va vacio
    	if (logged==true){
    		if ((AppContext.getApplicationContext().getString(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME))==null)    		
			if (com.geopista.security.SecurityManager.getPrincipal()!=null)
				UserPreferenceStore.setUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME, com.geopista.security.SecurityManager.getPrincipal().getName());
    	}
        return logged;
    }
     public static  void unLogged()
    {
        initSM().unLoggedNS();
    }
    public  void unLoggedNS()
    {
         idSesion=null;
         //Si previamente estaba logueado
         if (logged==true)
        	 previouslyLogged=true;
         logged=false;
    }
    
    public static  boolean isPreviouslyLogged()
    {
        return initSM().isPreviouslyLoggedNS();
    }
    public  boolean isPreviouslyLoggedNS()
    {
         return previouslyLogged;
         
         
    }

    public static boolean isConnected()
    {
        return initSM().isConnectedNS();
    }
    public  boolean isConnectedNS() {
        return connected;
    }
    
    public static boolean isEntidadSelected()
    {
    	   
    	if ((initSM().idEntidad!=null))
    		return true;
    	else
    		return false;
    		    		
        //return initSM().isLoggedNS();
    }


    public static TestConnection getObjetoTestConnection()
    {
         return initSM().getTestConnectionNS();
    }
     public  TestConnection getTestConnectionNS() {
        return testConnection;
    }
    public static TestConnection getTestConnection()
    {
         return initSM().initTestConnectionNS();
    }
    public  TestConnection initTestConnectionNS() {
        if (testConnection==null)
    		testConnection=new TestConnection();
        return testConnection;
    }
     public static void setHeartBeatTime(long timeToSleep)
    {
        initSM().setHeartBeatTimeNS(timeToSleep);
    }
    public  void setHeartBeatTimeNS(long timeToSleep)
    {
         initTestConnectionNS().setTimeToSleep(timeToSleep);
    }
    
    public static String getUrl()
    {
        return initSM().getUrlNS();
    }
    public  String getUrlNS()
    {
        return sUrl;
    }

    public static boolean isInitHeartBeat() {
        return initSM().isInitHeartBeatNS();
    }
    public boolean isInitHeartBeatNS() {
        return initHeartBeat;
    }

    public static void setInitHeartBeat(boolean initHeartBeat) {
    	initSM().setInitHeartBeatNS(initHeartBeat);
    }
    
    public void setInitHeartBeatNS(boolean initHeartBeat) {
    	this.initHeartBeat=initHeartBeat;
    }
    
    public void setSession(String idSesion) {
		this.idSesion=idSesion;
		
	}
	public void setSession(ISesion iSesion) {
		this.iSesion=iSesion;		
	}
	
	public ISesion getSession(){
		return iSesion;
	}

    
}

