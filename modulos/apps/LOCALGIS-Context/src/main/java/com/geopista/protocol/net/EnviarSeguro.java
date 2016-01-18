/**
 * EnviarSeguro.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.net;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.exolab.castor.xml.Unmarshaller;

import com.geopista.app.AppConstants;
import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.contaminantes.Inspeccion;
import com.geopista.security.SecurityManager;
import com.geopista.security.dnie.utils.CertificateUtils;
import com.geopista.util.ApplicationContext;
import com.geopista.util.CancelListener;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 05-may-2004
 * Time: 11:17:49
 * To change this template use Options | File Templates.
 */
public class EnviarSeguro {
    public static final String mensajeXML="mensajeXML";
    public static final String idMunicipio="idMunicipio";
    public static final String idEntidad="idEntidad";
    public static final String IdApp="IdApp";
    public static final String userIntentos="userIntentos";
    
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EnviarSeguro.class);

    public static CResultadoOperacion enviar(String sUrl, String sMensaje) throws Exception{
    	SecurityManager sm=null;
    	return enviar(sUrl,sMensaje,sm);
    }
    
    public static CResultadoOperacion enviar(String sUrl, String sMensaje,SecurityManager sm) throws Exception{
        try
        {
		    StringReader sr = enviar(sUrl, sMensaje, null, null,sm);
             try {
			    return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	return new CResultadoOperacion(false, "Error al hacer el casting:" + sr);
		    }
        }catch (Exception e)
        {
            java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	//e.printStackTrace(pw);
            logger.error("Se ha producido un Excepcion al enviar los datos: "+sw.toString());
            throw (e);
        }
	}
    public static StringReader enviarPlano(String sUrl, String sMensaje) throws Exception{
          return enviar(sUrl, sMensaje, null, null);
    }


    public static StringReader enviarActividadContaminante(String sUrl, String sMensaje, Vector vInspecciones) throws Exception{
          return enviarActividadContaminante(sUrl, sMensaje, null, null, vInspecciones);
    }


    public static StringReader enviarPlanoMultiPart(String sUrl, String sMensaje) throws Exception{
        return enviarPlanoMultiPart(sUrl, sMensaje, null);
    }
    public static StringReader enviarPlanoMultiPart(String sUrl, String sMensaje,TaskMonitor taskMonitor) throws Exception{
          return enviarMultiPart(sUrl, sMensaje, null, null,taskMonitor);
    }

    public static StringReader enviarMultiPart(String sUrl, String sMensaje, String sUserName, String sPassword)
    throws Exception{
    	return enviarMultiPart(sUrl,  sMensaje,  sUserName,  sPassword,null);
    }
    public static StringReader enviarMultiPart(String sUrl, String sMensaje, String sUserName, String sPassword,TaskMonitor taskMonitor)
    throws Exception{
    	return enviarMultiPart(null,  sUrl,  sMensaje,  sUserName,  sPassword,taskMonitor);
    }
    
    public static StringReader enviarMultiPart(HttpClient client, String sUrl, String sMensaje, String sUserName, String sPassword)
    throws Exception{
    	return enviarMultiPart(null,  sUrl,  sMensaje,  sUserName,  sPassword,null);
    }
    
    
    public static StringReader enviarMultiPart(HttpClient client, String sUrl, String sMensaje, String sUserName, String sPassword,TaskMonitor taskMonitor)
            throws Exception{
    	
    	if (client==null)
    	{
    		Credentials creds = null;
    		if (sUserName != null && sPassword != null)
    			creds = new UsernamePasswordCredentials(sUserName, sPassword);
            else {
    			if (SecurityManager.getIdSesion() == null)
                {
    				logger.debug("Usuario no autenticado");
                    creds = new UsernamePasswordCredentials("GUEST", "");
                  }
                else
                   creds = new UsernamePasswordCredentials(SecurityManager.getIdSesion(), SecurityManager.getIdSesion());
    		}
    		//create a singular HttpClient object
    		//client = new HttpClient();
    		client = AppContext.getHttpClient();

    		//establish a connection within 5 seconds
    		//client.setConnectionTimeout(5000);
    		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
    		getProtocolSSL(client, sUrl);

    		//set the default credentials
    		if (creds != null) {
    			client.getState().setCredentials(null, null, creds);
    		}


    	}
    	
		
		/*HttpMethod method = null;

		//create a method object
		method = new PostMethod(sUrl);
        if (sMensaje!=null)
        {
            ((PostMethod) method).addParameter(mensajeXML, sMensaje);
        }
        if (SecurityManager.getIdMunicipio()!=null)
            ((PostMethod) method).addParameter(idMunicipio, SecurityManager.getIdMunicipio());

        if (SecurityManager.getIdApp()!=null)
			((PostMethod) method).addParameter(IdApp, SecurityManager.getIdApp());
		method.setFollowRedirects(true);
        ((PostMethod) method).setRequestContentLength(PostMethod.CONTENT_LENGTH_AUTO); */
        /* -- MultipartPostMethod -- */
    	//org.apache.commons.httpclient.methods.PostMethod method1 = new PostMethod(sUrl);
        final org.apache.commons.httpclient.methods.MultipartPostMethod method= new org.apache.commons.httpclient.methods.MultipartPostMethod(sUrl);

        if (com.geopista.security.SecurityManager.getIdEntidad()!=null)
        {
             method.addParameter(idEntidad, SecurityManager.getIdEntidad());
        }
        if (com.geopista.security.SecurityManager.getIdMunicipio()!=null)
        {
             method.addParameter(idMunicipio, SecurityManager.getIdMunicipio());
        }
        if (SecurityManager.getIdApp()!=null)
        {
			 method.addParameter(IdApp, SecurityManager.getIdApp());
        }
        if (sMensaje!=null)
        {
            //method.addParameter(mensajeXML, sMensaje);
            method.addPart(new org.apache.commons.httpclient.methods.multipart.StringPart(mensajeXML, sMensaje, "ISO-8859-1"));
        }

        method.setFollowRedirects(false);
        method.setHttp11(true);

        //execute the method
		String responseBody = null;
		try {
			
			if (taskMonitor!=null){
				((TaskMonitorDialog)taskMonitor).addCancelListener(new CancelListener() {
					public void doCancel(){    
						logger.info("Cancelando proceso de carga");
						method.abort();
					}
				});
			}
			
			client.executeMethod(method);
			responseBody = method.getResponseBodyAsString();			
			//responseBody=org.apache.commons.io.IOUtils.toString(method.getResponseBodyAsStream()); 
		} catch (HttpException he) {
            logger.debug("Http error connecting to '" + sUrl + "'");
			throw new Exception("Http error connecting to '" + sUrl + "'" + he.getMessage());
		} catch (IOException ioe) {
            logger.debug("Unable to connect to '" + sUrl + "'");
			throw new Exception("Unable to connect to '" + sUrl + "'" + ioe.getMessage());
		}
		logger.debug("Request Path: " + method.getPath());
		logger.debug("Request Query: " + method.getQueryString());
		Header[] requestHeaders = method.getRequestHeaders();
		for (int i = 0; i < requestHeaders.length; i++) {
            	logger.debug(requestHeaders[i]);
		}

		logger.debug("*** Response ***");
		logger.debug("Status Line: " + method.getStatusLine());
        int iStatusCode = method.getStatusCode();
        String sStatusLine=method.getStatusLine().toString();
		Header[] responseHeaders = method.getResponseHeaders();
		for (int i = 0; i < responseHeaders.length; i++) {
			logger.debug(responseHeaders[i]);
            if (responseHeaders[i].getName().equals("Content-Encoding") &&
                responseHeaders[i].getValue().equals("gzip"))
            {

                    try
                    {
                       // logger.info("El contenido es gzip");
                        BufferedReader inbuf =new BufferedReader(new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(method.getResponseBody()))));
                        //logger.info("Gzip correcto");
                        String line=null;
                        StringBuffer buf = new StringBuffer();
                        while ((line = inbuf.readLine()) != null) {
                            buf.append(line);
                         }
                        responseBody= buf.toString();
                        //logger.info("Fin conversion del contenido");

                    }catch(Exception e)
                    {
                        e.printStackTrace();
                        java.io.StringWriter sw=new java.io.StringWriter();
		                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	            e.printStackTrace(pw);
                        logger.error("Error al descomprimir:"+sw.toString());
                    }
            }
		}

		//write out the response body
		logger.debug("*** Response Body ***");
		logger.debug(responseBody);
     	//clean up the connection resources
//		method.releaseConnection();
//		method.recycle();
        if (iStatusCode==200)
        {
	        return new StringReader(responseBody);
            /*try {
			    return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
		    } catch (Exception e) {
		    	return new CResultadoOperacion(false, "Error al hacer el casting:" + responseBody);
		    }*/
        }
        else if (iStatusCode==401)
        {
            SecurityManager.unLogged();
            throw new Exception("ERROR EN LOS PERMISOS. CAUSAS: " +
                                        "\n\t-Usuario no autenticado " +
                                        "\n\t-Usuario no tiene permisos" +
                                        "\n\t-Se ha perdido la sesión con el servidor" +
                                        "\nReinicie la aplicación y vuelva a intentarlo");
        }
        else
            throw new Exception("Url: " + sUrl + " - Status Line: " + sStatusLine);
	}


    public static StringReader enviar(String sUrl, String sMensaje, String sUserName, String sPassword) throws Exception{
    	SecurityManager sm=null;
    	return enviar(sUrl,sMensaje,sUserName,sPassword,sm);
    }
    

    public static StringReader enviar(String sUrl, String sMensaje, String sUserName, String sPassword,SecurityManager sm)
            throws Exception{
		Credentials creds = null;
		
		if((sUrl!=null) && (!sUrl.contains("HeartBeat")))
			if (sm!= null && sm.getIdSesionNS()!=null)
				logger.error("Informacion de Login en Sesion:"+sm.getIdSesionNS()+"->URL:"+sUrl);
			//else
				//logger.error("Informacion de Login sin Sesion:"+sUserName+"->URL:"+sUrl);
		
		if (sUserName != null && sPassword != null)
			creds = new UsernamePasswordCredentials(sUserName, sPassword);
        else {
        	boolean usuarioAutenticado=true;
        	if (sm!=null){
            	if (sm.getIdSesionNS()==null)
            		usuarioAutenticado=false;
        	}
        	else if (SecurityManager.getIdSesion() == null){
        		usuarioAutenticado=false;
        	}
        		
        	if (!usuarioAutenticado){
        		//System.out.println("Usuario no autenticado");
        		if((sUrl!=null) && (!sUrl.contains("HeartBeat")))
        			logger.error("Usuario no autenticado al conectar");
                creds = new UsernamePasswordCredentials("GUEST", "");
        		
        	}
        	else{	
        		
        		if (sm!=null){
        			//logger.error("Generando credenciales Caso1:"+sm.getIdSesionNS());
            		creds = new UsernamePasswordCredentials(sm.getIdSesionNS(), sm.getIdSesionNS());
        		}
            	else{
            		//logger.error("Generando credenciales Caso2:"+SecurityManager.getIdSesion());
            		creds = new UsernamePasswordCredentials(SecurityManager.getIdSesion(), SecurityManager.getIdSesion());
            	}
            }
		}
		//create a singular HttpClient object
		//org.apache.commons.httpclient.HttpClient client = new HttpClient();
		
		org.apache.commons.httpclient.HttpClient client;
		if (sm!=null)
			client = AppContext.getHttpClient(sm);
		else
			client = AppContext.getHttpClient();
		//establish a connection within 5 seconds
		//client.setConnectionTimeout(5000);
		String usuario=((UsernamePasswordCredentials)creds).getUserName();
		//if ((usuario!=null) && (!usuario.equalsIgnoreCase("GUEST"))) 
		//	System.out.println("VALOR DE CLIENT:"+client);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		
		getProtocolSSL(client,sUrl);

		//set the default credentials
		if (creds != null) {
			//client.getState().setCredentials(null, null, creds);
			AuthScope auth=new AuthScope(null,-1, null);
			client.getState().setCredentials(auth, creds);	
			//String usuario=((UsernamePasswordCredentials)creds).getUserName();
			//if ((usuario!=null) && (!usuario.equalsIgnoreCase("GUEST"))) 
			//	logger.info("USUARIO:"+usuario);
		}

		HttpMethod method = null;

		//create a method object
		method = new PostMethod(sUrl);
        if (sMensaje!=null)
        {
            ((PostMethod) method).addParameter(mensajeXML, sMensaje);
        }

        if (SecurityManager.getIdMunicipio()!=null){
        	String sMunicipio=null;
        	if (sm!=null)
        		sMunicipio=sm.getIdMunicipioNS();
        	else
        		sMunicipio= SecurityManager.getIdMunicipio();
        	if (sMunicipio!=null)
        		((PostMethod) method).addParameter(idMunicipio, sMunicipio);
        }
        if (SecurityManager.getIdEntidad()!=null){
        	String sEntidad=null;
        	if (sm!=null)
        		sEntidad=sm.getIdEntidadNS();
        	else
        		sEntidad= SecurityManager.getIdEntidad();
        	if (sEntidad!=null)
        		((PostMethod) method).addParameter(idEntidad, sEntidad);
        }
        
        if (SecurityManager.getIdApp()!=null){
        	String sApp=null;
        	if (sm!=null)
        		sApp=sm.getIdAppNS();
        	else
        		sApp= SecurityManager.getIdApp();
        	if (sApp!=null)
        		((PostMethod) method).addParameter(IdApp, sApp);
        }
        
        if (SecurityManager.getUserIntentos()!=null){
        	int sIntentos=0;
        	if (sm!=null)
        		sIntentos=sm.getIntentosNS();
        	else
        		sIntentos = SecurityManager.getIntentos();
        	if (sIntentos!=0)
        		//((PostMethod) method).addParameter(idEntidad, ((Integer) sIntentos).toString());
        		((PostMethod) method).addParameter(userIntentos, SecurityManager.getUserIntentos());
        }

		method.setFollowRedirects(false);
        ((PostMethod) method).setRequestContentLength(PostMethod.CONTENT_LENGTH_AUTO);

		//execute the method
		String responseBody = null;
		try {
			client.executeMethod(method);
			responseBody = method.getResponseBodyAsString();
			//responseBody=org.apache.commons.io.IOUtils.toString(method.getResponseBodyAsStream());
		} catch (HttpException he) {
            logger.debug("Http error connecting to '" + sUrl + "'");
			throw new Exception("Http error connecting to '" + sUrl + "'" + he.getMessage());
		} catch (IOException ioe) {
            logger.debug("Unable to connect to '" + sUrl + "'");
			throw new Exception("Unable to connect to '" + sUrl + "'" + ioe.getMessage());
		}
		logger.debug("Request Path: " + method.getPath());
		logger.debug("Request Query: " + method.getQueryString());
		Header[] requestHeaders = method.getRequestHeaders();
		for (int i = 0; i < requestHeaders.length; i++) {
            	logger.debug(requestHeaders[i]);
		}

		logger.debug("*** Response ***");
		logger.debug("Status Line: " + method.getStatusLine());
        int iStatusCode = method.getStatusCode();
        String sStatusLine=method.getStatusLine().toString();
		Header[] responseHeaders = method.getResponseHeaders();
		for (int i = 0; i < responseHeaders.length; i++) {
			logger.debug(responseHeaders[i]);
            if (responseHeaders[i].getName().equals("Content-Encoding") &&
                responseHeaders[i].getValue().equals("gzip"))
            {

                try
                {
                    logger.info("El contenido es gzip");
                    BufferedReader inbuf =new BufferedReader(new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(method.getResponseBody()))));
                    logger.info("Gzip correcto");
                    String line=null;
                    StringBuffer buf = new StringBuffer();
                    while ((line = inbuf.readLine()) != null) {
                        buf.append(line);
                     }
                    responseBody= buf.toString();
                    logger.info("Fin conversion del contenido");

                }catch(Exception e)
                {
                    e.printStackTrace();
                    java.io.StringWriter sw=new java.io.StringWriter();
	                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
    	            e.printStackTrace(pw);
                    logger.error("Error al descomprimir:"+sw.toString());
                }
            }
		}

		//write out the response body
		logger.debug("*** Response Body ***");
		logger.debug(responseBody);
     	//clean up the connection resources
		//method.releaseConnection();
		//method.recycle();
		client=null;//clean-up
		
        if (iStatusCode==200)
        {
	        return new StringReader(responseBody);
            /*try {
			    return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
		    } catch (Exception e) {
		    	return new CResultadoOperacion(false, "Error al hacer el casting:" + responseBody);
		    }*/
        }
        else if (iStatusCode==401)
        {
            SecurityManager.unLogged();
            throw new Exception("ERROR EN LOS PERMISOS. " +sUrl+
            							"\n\tCAUSAS: " +
                                        "\n\t-Usuario no autenticado " +
                                        "\n\t-Usuario no tiene permisos" +
                                        "\n\t-Se ha perdido la sesión con el servidor" +
                                        "\nReinicie la aplicación y vuelva a intentarlo");
        }
        else
            throw new Exception(sStatusLine);
	}
    
    /*public static CResultadoOperacion enviarNS(String sUrl, String sMensaje,SecurityManager sm) throws Exception{
        try
        {
		    StringReader sr = enviarNS(sUrl, sMensaje, null, null,sm);
             try {
			    return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
		    } catch (Exception e) {
		    	return new CResultadoOperacion(false, "Error al hacer el casting:" + sr);
		    }
        }catch (Exception e)
        {
            java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	e.printStackTrace(pw);
            logger.error("Se ha producido un Excepcion al enviar los datos: "+sw.toString());
            throw (e);
        }
	}*/
    
    /**
     * Metodo que envia una peticion sin utilizar los metodos estaticos de la funcion
     * SecurityManager.
     * @param sUrl
     * @param sMensaje
     * @param sUserName
     * @param sPassword
     * @param sm
     * @return
     * @throws Exception
     */
    /*public static StringReader enviarNS(String sUrl, String sMensaje, String sUserName, 
    						String sPassword,SecurityManager sm)
    throws Exception{
			Credentials creds = null;
			if (sUserName != null && sPassword != null)
				creds = new UsernamePasswordCredentials(sUserName, sPassword);
			else {
				if (sm.getIdSesionNS() == null)
			    {
					logger.debug("Usuario no autenticado");
			        creds = new UsernamePasswordCredentials("GUEST", "");
			      }
			    else
			       creds = new UsernamePasswordCredentials(sm.getIdSesionNS(), sm.getIdSesionNS());
			}
				
			org.apache.commons.httpclient.HttpClient client = AppContext.getNewHttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
			
			getProtocolSSL(client,sUrl);
			
			//set the default credentials
			if (creds != null) {
				AuthScope auth=new AuthScope(null,-1, null);
				client.getState().setCredentials(auth, creds);
			}
			
			HttpMethod method = null;
			
			//create a method object
			method = new PostMethod(sUrl);
			if (sMensaje!=null)
			{
			    ((PostMethod) method).addParameter(mensajeXML, sMensaje);
			}
			
			if (sm.getIdMunicipioNS()!=null){
				((PostMethod) method).addParameter(idMunicipio, sm.getIdMunicipioNS());
			}
			if (sm.getIdEntidadNS()!=null){
				((PostMethod) method).addParameter(idEntidad, sm.getIdEntidadNS());
			}
			if (sm.getIdAppNS()!=null)
				((PostMethod) method).addParameter(IdApp, sm.getIdAppNS());
			method.setFollowRedirects(false);
			((PostMethod) method).setRequestContentLength(PostMethod.CONTENT_LENGTH_AUTO);
			
			//execute the method
			String responseBody = null;
			try {
				client.executeMethod(method);
				responseBody = method.getResponseBodyAsString();
			} catch (HttpException he) {
			    logger.debug("Http error connecting to '" + sUrl + "'");
				throw new Exception("Http error connecting to '" + sUrl + "'" + he.getMessage());
			} catch (IOException ioe) {
			    logger.debug("Unable to connect to '" + sUrl + "'");
				throw new Exception("Unable to connect to '" + sUrl + "'" + ioe.getMessage());
			}
			logger.debug("Request Path: " + method.getPath());
			logger.debug("Request Query: " + method.getQueryString());
			Header[] requestHeaders = method.getRequestHeaders();
			for (int i = 0; i < requestHeaders.length; i++) {
			    	logger.debug(requestHeaders[i]);
			}
			
			logger.debug("*** Response ***");
			logger.debug("Status Line: " + method.getStatusLine());
			int iStatusCode = method.getStatusCode();
			String sStatusLine=method.getStatusLine().toString();
			Header[] responseHeaders = method.getResponseHeaders();
			for (int i = 0; i < responseHeaders.length; i++) {
				logger.debug(responseHeaders[i]);
			    if (responseHeaders[i].getName().equals("Content-Encoding") &&
			        responseHeaders[i].getValue().equals("gzip"))
			    {
			
		            try
		            {
		                logger.info("El contenido es gzip");
		                BufferedReader inbuf =new BufferedReader(new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(method.getResponseBody()))));
		                logger.info("Gzip correcto");
		                String line=null;
		                StringBuffer buf = new StringBuffer();
		                while ((line = inbuf.readLine()) != null) {
		                    buf.append(line);
		                 }
		                responseBody= buf.toString();
		                logger.info("Fin conversion del contenido");
		
		            }catch(Exception e)
		            {
		                e.printStackTrace();
		                java.io.StringWriter sw=new java.io.StringWriter();
		                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
			            e.printStackTrace(pw);
		                logger.error("Error al descomprimir:"+sw.toString());
		            }
			    }
			}
			
			//write out the response body
			logger.debug("*** Response Body ***");
			logger.debug(responseBody);
				//clean up the connection resources
			//method.releaseConnection();
			//method.recycle();
			client=null;//clean-up
			
			if (iStatusCode==200)
			{
			    return new StringReader(responseBody);
			    //try {
				//    return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
			    //} catch (Exception e) {
			    //	return new CResultadoOperacion(false, "Error al hacer el casting:" + responseBody);
			    //}
			}
			else if (iStatusCode==401)
			{
			    sm.unLoggedNS();
			    throw new Exception("ERROR EN LOS PERMISOS. CAUSAS: " +
			                                "\n\t-Usuario no autenticado " +
			                                "\n\t-Usuario no tiene permisos" +
			                                "\n\t-Se ha perdido la sesión con el servidor" +
			                                "\nReinicie la aplicación y vuelva a intentarlo");
			}
			else
			    throw new Exception(sStatusLine);
	}    
    */

    public static CResultadoOperacion enviar(String sUrl, String sMensaje, Vector anexos) throws Exception{
        try
        {
            StringReader sr = enviar(sUrl, sMensaje, null, null, anexos);
             try {
                return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
            } catch (Exception e) {
                return new CResultadoOperacion(false, "Error al hacer el casting:" + sr);
            }
        }catch (Exception e)
        {
            java.io.StringWriter sw=new java.io.StringWriter();
            java.io.PrintWriter pw=new java.io.PrintWriter(sw);
            //e.printStackTrace(pw);
            logger.error("Se ha producido una Excepcion al enviar los datos: "+sw.toString());
            throw (e);
        }
    }



    public static StringReader enviar(String sUrl, String sMensaje, String sUserName, String sPassword, Vector anexos)
            throws Exception{
		Credentials creds = null;
		if (sUserName != null && sPassword != null)
			creds = new UsernamePasswordCredentials(sUserName, sPassword);
        else {
			if (SecurityManager.getIdSesion() == null)
            {
				logger.debug("Usuario no autienticado");
                creds = new UsernamePasswordCredentials("GUEST", "");
              }
            else
               creds = new UsernamePasswordCredentials(SecurityManager.getIdSesion(), SecurityManager.getIdSesion());
		}
		//create a singular HttpClient object
		//org.apache.commons.httpclient.HttpClient client = new HttpClient();
		org.apache.commons.httpclient.HttpClient client = AppContext.getHttpClient();

		//establish a connection within 5 seconds
		//client.setConnectionTimeout(5000);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

		//set the default credentials
		if (creds != null) {
			client.getState().setCredentials(null, null, creds);
		}

        /* -- PostMethod--
		//create a method object
		method = new PostMethod(sUrl);
        if (SecurityManager.getIdMunicipio()!=null)
            ((PostMethod) method).addParameter(idMunicipio, SecurityManager.getIdMunicipio());
	    if (sMensaje!=null)
		    ((PostMethod) method).addParameter(mensajeXML, sMensaje);
        if (SecurityManager.getIdApp()!=null)
			((PostMethod) method).addParameter(IdApp, SecurityManager.getIdApp());
		method.setFollowRedirects(true);

        logger.info("sMensaje="+sMensaje);
        */

        /* -- MultipartPostMethod -- */
        MultipartPostMethod mPost = new MultipartPostMethod(sUrl);

        if (SecurityManager.getIdMunicipio()!=null){
             mPost.addParameter(idMunicipio, SecurityManager.getIdMunicipio());
        }
        if (SecurityManager.getIdEntidad()!=null){
            mPost.addParameter(idEntidad, SecurityManager.getIdEntidad());
        }
        if (SecurityManager.getIdApp()!=null){
			 mPost.addParameter(IdApp, SecurityManager.getIdApp());
        }
        if (sMensaje!=null){
             //mPost.addParameter(mensajeXML, sMensaje);
            mPost.addPart(new StringPart(mensajeXML, sMensaje, "ISO-8859-1"));
        }

        for (Enumeration e=anexos.elements(); e.hasMoreElements();){
            com.geopista.protocol.licencias.CAnexo anexo= (com.geopista.protocol.licencias.CAnexo)e.nextElement();
            if ((anexo.getPath()!= null) && (!anexo.getPath().trim().equalsIgnoreCase(""))){
                File f= new File(anexo.getPath());
                //mPost.addParameter(f.getName(), f);
                /** Debido a que el nombre del fichero puede contener acentos. */
                mPost.addParameter(URLEncoder.encode(f.getName(),"ISO-8859-1"), f);
            }
        }
        /**/

        mPost.setFollowRedirects(false);

		//execute the method
		String responseBody = null;
		try {
			int status= client.executeMethod(mPost);
            if (status == HttpStatus.SC_OK) {
               logger.debug("Upload OK, response=" + status);
             } else {
               logger.debug("Upload NOK, response=" + status);
             }

			responseBody = mPost.getResponseBodyAsString();
			//responseBody=org.apache.commons.io.IOUtils.toString(mPost.getResponseBodyAsStream());
		} catch (HttpException he) {
            logger.debug("Http error connecting to '" + sUrl + "'");
			throw new Exception("Http error connecting to '" + sUrl + "'" + he.getMessage());
		} catch (IOException ioe) {
            logger.debug("Unable to connect to '" + sUrl + "'");
			throw new Exception("Unable to connect to '" + sUrl + "'" + ioe.getMessage());
		}
		logger.debug("Request Path: " + mPost.getPath());
		logger.debug("Request Query: " + mPost.getQueryString());
		Header[] requestHeaders = mPost.getRequestHeaders();
		for (int i = 0; i < requestHeaders.length; i++) {
            	logger.debug(requestHeaders[i]);
		}

		logger.debug("*** Response ***");
		logger.debug("Status Line: " + mPost.getStatusLine());
        int iStatusCode = mPost.getStatusCode();
        String sStatusLine= mPost.getStatusLine().toString();
		Header[] responseHeaders = mPost.getResponseHeaders();
		for (int i = 0; i < responseHeaders.length; i++) {
			logger.debug(responseHeaders[i]);
            if (responseHeaders[i].getName().equals("Content-Encoding") &&
                responseHeaders[i].getValue().equals("gzip"))
            {

                    try
                    {
                        logger.info("El contenido es gzip");
                        BufferedReader inbuf =new BufferedReader(new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(((MultipartPostMethod)mPost).getResponseBody()))));
                        logger.info("Gzip correcto");
                        String line=null;
                        StringBuffer buf = new StringBuffer();
                        while ((line = inbuf.readLine()) != null) {
                            buf.append(line);
                         }
                        responseBody= buf.toString();
                        logger.info("Fin conversion del contenido");

                    }catch(Exception e)
                    {
                        e.printStackTrace();
                        java.io.StringWriter sw=new java.io.StringWriter();
		                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	            e.printStackTrace(pw);
                        logger.error("Error al descomprimir:"+sw.toString());
                    }
            }
		}

		//write out the response body
		logger.debug("*** Response Body ***");
		logger.debug(responseBody);
     	//clean up the connection resources
		mPost.releaseConnection();
		mPost.recycle();
        if (iStatusCode==200)
        {
	        return new StringReader(responseBody);
            /*try {
			    return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
		    } catch (Exception e) {
		    	return new CResultadoOperacion(false, "Error al hacer el casting:" + responseBody);
		    }*/
        }
        else if (iStatusCode==401)
        {
            SecurityManager.unLogged();
            throw new Exception("ERROR EN LOS PERMISOS. CAUSAS: " +
                                        "\n\t-Usuario no autenticado " +
                                        "\n\t-Usuario no tiene permisos" +
                                        "\n\t-Se ha perdido la sesión con el servidor" +
                                        "\nReinicie la aplicación y vuelva a intentarlo");
        }
        else
            throw new Exception(sStatusLine);
	}

    
    
 // ---NUEVO--->
	public static StringReader enviarCertificado(String sUrl, String sMensaje, X509Certificate certificate, KeyManagerFactory kmf)
			throws Exception {
		SecurityManager sm = null;
		//return enviarCertificado(sUrl, sMensaje, certificate, kmf, sm);
		return enviarCertificadoHttpClient(sUrl, sMensaje, certificate, kmf, sm);
	}

	
	
	public static StringReader enviarCertificado(String sUrl, String sMensaje, X509Certificate certificate, KeyManagerFactory kmf,
			SecurityManager sm) throws Exception {
		logger.debug("INICIO EnviarSeguro StringReader: " + sUrl);
		String usuario = CertificateUtils.getNIFfromSubjectDN(certificate.getSubjectDN().getName());
		//Credentials creds = new CertificateCredentials(usuario,certificate);

		SSLContext sslContext = SSLContext.getInstance("TLS");
		
		sslContext.init(kmf.getKeyManagers(), new TrustManager[] {new CertificateUtils.DefaultTrustManager()}, new SecureRandom());
		
		URL url = new URL(sUrl);
		
		HttpsURLConnection httpsConnection = (HttpsURLConnection)url.openConnection();
		httpsConnection.setRequestMethod("POST");
		httpsConnection.setDoInput(true);	
		httpsConnection.setDoOutput(true);	
		httpsConnection.setSSLSocketFactory(sslContext.getSocketFactory());
		httpsConnection.setHostnameVerifier(new HostnameVerifier()
		{
			@Override
			public boolean verify(String hostname, SSLSession session) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		httpsConnection.setConnectTimeout(5000);

		
		
		HttpsURLConnection.setFollowRedirects(true);
		httpsConnection.setInstanceFollowRedirects(true);
		httpsConnection.setAllowUserInteraction(true);
		httpsConnection.setUseCaches(false);
		httpsConnection.setDefaultUseCaches(false);
		
		String query = "";
		
		if (sMensaje != null) {
			query = mensajeXML + "=" + URLEncoder.encode(sMensaje);
		}
		
		if (SecurityManager.getIdMunicipio() != null) {
			String sMunicipio = null;
			if (sm != null)
				sMunicipio = sm.getIdMunicipioNS();
			else
				sMunicipio = SecurityManager.getIdMunicipio();
			if (sMunicipio != null){
				if(!query.equals("")) query += "&";
				query += idMunicipio + "=" + URLEncoder.encode(sMunicipio);
			}
		}
		
		if (SecurityManager.getIdEntidad() != null) {
			String sEntidad = null;
			if (sm != null)
				sEntidad = sm.getIdEntidadNS();
			else
				sEntidad = SecurityManager.getIdEntidad();
			if (sEntidad != null){
				if(!query.equals("")) query += "&";
				query += idEntidad + "=" + URLEncoder.encode(sEntidad);
			}
		}
		
		if (SecurityManager.getIdApp() != null) {
			String sApp = null;
			if (sm != null)
				sApp = sm.getIdAppNS();
			else
				sApp = SecurityManager.getIdApp();
			if (sApp != null){
				if(!query.equals("")) query += "&";
				query += IdApp + "=" + URLEncoder.encode(sApp);
			}
		}		
		

		
		//System.out.println("Fijando en sesion la cookie");
		//httpsConnection.setRequestProperty("Cookie","JSESSIONID=");
		//httpsConnection.setRequestProperty("Cookie","sessionId=");
				
		httpsConnection.setRequestProperty ("Content-Type", "application/x-www-form-urlencoded");
		httpsConnection.setRequestProperty("Content-Length", "" + query.length());
		//httpsConnection.setRequestProperty("Connection", "Close");
		//System.setProperty("http.keepAlive", "false");
		
		// execute the method
		String responseBody = "";
		try {
			httpsConnection.connect();
			
			DataOutputStream out = new DataOutputStream( httpsConnection.getOutputStream() );
			out.writeBytes (query);
			out.flush();
			out.close();		
			
			//client.executeMethod(method);
			//responseBody = method.getResponseBodyAsString();	
			BufferedReader in = new BufferedReader(new InputStreamReader(httpsConnection.getInputStream()));						 
			String linea = "";
			while ((linea = in.readLine()) != null) {
				responseBody += linea;
			}
			in.close();			
			//responseBody = httpsConnection.getResponseMessage();
			logger.debug(responseBody);
			// responseBody=org.apache.commons.io.IOUtils.toString(method.getResponseBodyAsStream());
		} catch (HttpException he) {
			logger.debug("Http error connecting to '" + sUrl + "'");
			throw new Exception("Http error connecting to '" + sUrl + "'"
					+ he.getMessage());
		} catch (IOException ioe) {
			logger.debug("Unable to connect to '" + sUrl + "'");
			throw new Exception("Unable to connect to '" + sUrl + "'"
					+ ioe.getMessage());
		}
		int iStatusCode = httpsConnection.getResponseCode();
		String sStatusLine = httpsConnection.getResponseMessage();
		logger.debug("*** Response Body ***");
		logger.debug(responseBody);
		//httpsConnection.disconnect();
		//httpsConnection.getInputStream().close();
		//httpsConnection.disconnect();
		httpsConnection = null;// clean-up

		if (iStatusCode == 200) {
			return new StringReader(responseBody);			
		} else if (iStatusCode == 401) {
			SecurityManager.unLogged();
			throw new Exception("ERROR EN LOS PERMISOS. CAUSAS: "
					+ "\n\t-Usuario no autenticado "
					+ "\n\t-Usuario no tiene permisos"
					+ "\n\t-Se ha perdido la sesión con el servidor"
					+ "\nReinicie la aplicación y vuelva a intentarlo");
		} else
			System.out.println("INICIO enviar URL: " + sUrl);
		throw new Exception(sStatusLine);
	}
	// ---FIN NUEVO--->

	public static StringReader enviarCertificadoHttpClient(String sUrl, String sMensaje, X509Certificate certificate, KeyManagerFactory kmf,
			SecurityManager sm) throws Exception {
		logger.debug("INICIO EnviarSeguro StringReader: " + sUrl);
		String usuario = CertificateUtils.getNIFfromSubjectDN(certificate.getSubjectDN().getName());
		//Credentials creds = new CertificateCredentials(usuario,certificate);

		SSLContext sslContext = SSLContext.getInstance("TLS");		
		sslContext.init(kmf.getKeyManagers(), new TrustManager[] {new CertificateUtils.DefaultTrustManager()}, new SecureRandom());
		
		//org.apache.commons.httpclient.HttpClient client = AppContext.getHttpClient();
		
		HttpClient client = new HttpClient();
        //establish a connection within 5 seconds
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

        
        SslContextedSecureProtocolSocketFactory secureProtocolSocketFactory =
        		   new SslContextedSecureProtocolSocketFactory(sslContext);
        secureProtocolSocketFactory.setHostnameVerification(false);
        
        
        //Registramos el listener para el protocolo. En lugar de hacerlo
        //para https que ya un manejador registrado lo realizamos para mhttps
        Protocol protocol=new Protocol("mhttps", (ProtocolSocketFactory)secureProtocolSocketFactory, 9443);
        Protocol.registerProtocol("mhttps", protocol);
        
        sUrl=sUrl.replace("https", "mhttps");
        PostMethod post = new PostMethod(sUrl);
        
        String query=getParametersQuery(sMensaje,sm); 
        
        //post.addRequestHeader("Content-type", "application/xml; charset=ISO-8859-1");
        //post.addRequestHeader("Content-Length", "" + query.length());
        post.setQueryString(query);       
        
				
		// execute the method
		String responseBody = "";
		int iStatusCode;
		try {
			//httpsConnection.connect();
			iStatusCode = client.executeMethod(post);
			
			if (iStatusCode == HttpStatus.SC_OK) {
                logger.debug("Upload OK, response=" + iStatusCode);
              } else {
                logger.debug("Upload NOK, response=" + iStatusCode);
              }
             responseBody = post.getResponseBodyAsString();
			
					
			//responseBody = httpsConnection.getResponseMessage();
			logger.debug(responseBody);
			// responseBody=org.apache.commons.io.IOUtils.toString(method.getResponseBodyAsStream());
		} catch (HttpException he) {
			logger.debug("Http error connecting to '" + sUrl + "'");
			throw new Exception("Http error connecting to '" + sUrl + "'"
					+ he.getMessage());
		} catch (IOException ioe) {
			logger.debug("Unable to connect to '" + sUrl + "'");
			throw new Exception("Unable to connect to '" + sUrl + "'"
					+ ioe.getMessage());
		}
		//int iStatusCode = httpsConnection.getResponseCode();
		//String sStatusLine = httpsConnection.getResponseMessage();
		logger.debug("*** Response Body ***");
		logger.debug(responseBody);
		//httpsConnection.disconnect();
		//httpsConnection.getInputStream().close();
		//httpsConnection.disconnect();
		//httpsConnection = null;// clean-up

		if (iStatusCode == 200) {
			return new StringReader(responseBody);			
		} else if (iStatusCode == 401) {
			SecurityManager.unLogged();
			throw new Exception("ERROR EN LOS PERMISOS. CAUSAS: "
					+ "\n\t-Usuario no autenticado "
					+ "\n\t-Usuario no tiene permisos"
					+ "\n\t-Se ha perdido la sesión con el servidor"
					+ "\nReinicie la aplicación y vuelva a intentarlo");
		} else
			System.out.println("INICIO enviar URL: " + sUrl);
		throw new Exception("");
	}
	
	private static String getParametersQuery(String sMensaje,SecurityManager sm) throws Exception{
		String query = "";
		
		if (sMensaje != null) {
			query = mensajeXML + "=" + URLEncoder.encode(sMensaje,"UTF-8");
		}
		
		if (SecurityManager.getIdMunicipio() != null) {
			String sMunicipio = null;
			if (sm != null)
				sMunicipio = sm.getIdMunicipioNS();
			else
				sMunicipio = SecurityManager.getIdMunicipio();
			if (sMunicipio != null){
				if(!query.equals("")) query += "&";
				query += idMunicipio + "=" + URLEncoder.encode(sMunicipio,"UTF-8");
			}
		}
		
		if (SecurityManager.getIdEntidad() != null) {
			String sEntidad = null;
			if (sm != null)
				sEntidad = sm.getIdEntidadNS();
			else
				sEntidad = SecurityManager.getIdEntidad();
			if (sEntidad != null){
				if(!query.equals("")) query += "&";
				query += idEntidad + "=" + URLEncoder.encode(sEntidad,"UTF-8");
			}
		}
		
		if (SecurityManager.getIdApp() != null) {
			String sApp = null;
			if (sm != null)
				sApp = sm.getIdAppNS();
			else
				sApp = SecurityManager.getIdApp();
			if (sApp != null){
				if(!query.equals("")) query += "&";
				query += IdApp + "=" + URLEncoder.encode(sApp,"UTF-8");
			}
		}		
		return query;
			
	}
	
    public static CResultadoOperacion enviarFichero(String sUrl, String sMensaje, File fichero)
                throws Exception
    {
            Credentials creds = null;
            if (SecurityManager.getIdSesion() == null)
            {
                logger.debug("Usuario no autienticado");
                creds = new UsernamePasswordCredentials("GUEST", "");
            }
            else
               creds = new UsernamePasswordCredentials(SecurityManager.getIdSesion(), SecurityManager.getIdSesion());
            //create a singular HttpClient object
            //org.apache.commons.httpclient.HttpClient client = new HttpClient();
            org.apache.commons.httpclient.HttpClient client = AppContext.getHttpClient();

            //establish a connection within 5 seconds
            //client.setConnectionTimeout(5000);
            client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

            //set the default credentials
            if (creds != null) {
                client.getState().setCredentials(null, null, creds);
            }
            /* -- MultipartPostMethod -- */
            MultipartPostMethod mPost = new MultipartPostMethod(sUrl);

            if (SecurityManager.getIdMunicipio()!=null){
                 mPost.addParameter(idMunicipio, SecurityManager.getIdMunicipio());
            }
            if (SecurityManager.getIdEntidad()!=null){
                mPost.addParameter(idEntidad, SecurityManager.getIdEntidad());
            }
            if (SecurityManager.getIdApp()!=null){
                 mPost.addParameter(IdApp, SecurityManager.getIdApp());
            }
            if (sMensaje!=null){

                 //mPost.addParameter(mensajeXML, sMensaje);
                mPost.addPart(new StringPart(mensajeXML, sMensaje, "ISO-8859-1"));
            }
            if (fichero!=null){
                //mPost.addParameter(fichero.getName(), fichero);
                /** Debido a que el nombre del fichero puede contener acentos. */
                mPost.addParameter(URLEncoder.encode(fichero.getName(),"ISO-8859-1"), fichero);
            }
            /**/
            mPost.setFollowRedirects(false);
            //execute the method
            String responseBody = null;
            try {
                int status= client.executeMethod(mPost);
                if (status == HttpStatus.SC_OK) {
                   logger.debug("Upload OK, response=" + status);
                 } else {
                   logger.debug("Upload NOK, response=" + status);
                 }
                responseBody = mPost.getResponseBodyAsString();
                //responseBody=org.apache.commons.io.IOUtils.toString(mPost.getResponseBodyAsStream());
            } catch (HttpException he) {
                logger.debug("Http error connecting to '" + sUrl + "'");
                throw new Exception("Http error connecting to '" + sUrl + "'" + he.getMessage());
            } catch (IOException ioe) {
                logger.debug("Unable to connect to '" + sUrl + "'");
                throw new Exception("Unable to connect to '" + sUrl + "'" + ioe.getMessage());
            }
            logger.debug("Request Path: " + mPost.getPath());
            logger.debug("Request Query: " + mPost.getQueryString());
            Header[] requestHeaders = mPost.getRequestHeaders();
            for (int i = 0; i < requestHeaders.length; i++) {
                    logger.debug(requestHeaders[i]);
            }

            logger.debug("*** Response ***");
            logger.debug("Status Line: " + mPost.getStatusLine());
            int iStatusCode = mPost.getStatusCode();
            String sStatusLine= mPost.getStatusLine().toString();
            Header[] responseHeaders = mPost.getResponseHeaders();
            for (int i = 0; i < responseHeaders.length; i++) {
                logger.debug(responseHeaders[i]);
                if (responseHeaders[i].getName().equals("Content-Encoding") &&
                    responseHeaders[i].getValue().equals("gzip"))
                {
                        try
                        {
                            logger.info("El contenido es gzip");
                            BufferedReader inbuf =new BufferedReader(new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(((MultipartPostMethod)mPost).getResponseBody()))));
                            logger.info("Gzip correcto");
                            String line=null;
                            StringBuffer buf = new StringBuffer();
                            while ((line = inbuf.readLine()) != null) {
                                buf.append(line);
                             }
                            responseBody= buf.toString();
                            logger.info("Fin conversion del contenido");
                        }catch(Exception e)
                        {
                            e.printStackTrace();
                            java.io.StringWriter sw=new java.io.StringWriter();
                            java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                            e.printStackTrace(pw);
                            logger.error("Error al descomprimir:"+sw.toString());
                        }
                }
            }
            //write out the response body
            logger.debug("*** Response Body ***");
            logger.debug(responseBody);
             //clean up the connection resources
//            mPost.releaseConnection();
//            mPost.recycle();
            if (iStatusCode==200)
            {
                 try {
                     return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, new StringReader(responseBody));
                } catch (Exception e) {
                     return new CResultadoOperacion(false, "Error al hacer el casting:" + responseBody);
                 }
            }
            else if (iStatusCode==401)
            {
                SecurityManager.unLogged();
                throw new Exception("ERROR EN LOS PERMISOS. CAUSAS: " +
                                            "\n\t-Usuario no autenticado " +
                                            "\n\t-Usuario no tiene permisos" +
                                            "\n\t-Se ha perdido la sesión con el servidor" +
                                            "\nReinicie la aplicación y vuelva a intentarlo");
            }
            else
                throw new Exception(sStatusLine);
        }


    public static StringReader enviarActividadContaminante(String sUrl, String sMensaje, String sUserName, String sPassword, Vector vInspecciones)
            throws Exception{
		Credentials creds = null;
		if (sUserName != null && sPassword != null)
			creds = new UsernamePasswordCredentials(sUserName, sPassword);
        else {
			if (SecurityManager.getIdSesion() == null)
            {
				logger.debug("Usuario no autienticado");
                creds = new UsernamePasswordCredentials("GUEST", "");
              }
            else
               creds = new UsernamePasswordCredentials(SecurityManager.getIdSesion(), SecurityManager.getIdSesion());
		}
		//create a singular HttpClient object
		//org.apache.commons.httpclient.HttpClient client = new HttpClient();
		org.apache.commons.httpclient.HttpClient client = AppContext.getHttpClient();
		//establish a connection within 5 seconds
		//client.setConnectionTimeout(5000);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

		//set the default credentials
		if (creds != null) {
			client.getState().setCredentials(null, null, creds);
		}

        /* -- PostMethod--
		//create a method object
		method = new PostMethod(sUrl);
        if (SecurityManager.getIdMunicipio()!=null)
            ((PostMethod) method).addParameter(idMunicipio, SecurityManager.getIdMunicipio());
	    if (sMensaje!=null)
		    ((PostMethod) method).addParameter(mensajeXML, sMensaje);
        if (SecurityManager.getIdApp()!=null)
			((PostMethod) method).addParameter(IdApp, SecurityManager.getIdApp());
		method.setFollowRedirects(true);

        logger.info("sMensaje="+sMensaje);
        */

        /* -- MultipartPostMethod -- */
        MultipartPostMethod mPost = new MultipartPostMethod(sUrl);

        if (SecurityManager.getIdMunicipio()!=null){
             mPost.addParameter(idMunicipio, SecurityManager.getIdMunicipio());
        }
        if (SecurityManager.getIdEntidad()!=null){
            mPost.addParameter(idEntidad, SecurityManager.getIdEntidad());
        }
        if (SecurityManager.getIdApp()!=null){
			 mPost.addParameter(IdApp, SecurityManager.getIdApp());
        }
        if (sMensaje!=null){
            mPost.addPart(new StringPart(mensajeXML, sMensaje, "ISO-8859-1"));
        }
        if (vInspecciones != null){
            for (Enumeration e= vInspecciones.elements(); e.hasMoreElements();){
                Inspeccion inspeccion= (Inspeccion)e.nextElement();
                Vector vAnexos= inspeccion.getAnexos();
                if (vAnexos != null){
                    for (Enumeration e1= vAnexos.elements(); e1.hasMoreElements();){
                    	com.geopista.protocol.contaminantes.CAnexo anexo= (com.geopista.protocol.contaminantes.CAnexo)e1.nextElement();
                        /** No enviamos los ficheros de los anexos marcados como borrados */
                        if (anexo.getEstado() != AppConstants.CMD_ANEXO_DELETED){
                            if ((anexo.getPath()!= null) && (!anexo.getPath().trim().equalsIgnoreCase(""))){
                                File f= new File(anexo.getPath());
                                /* Distintas inspecciones pueden tener el mismo anexo */
                                /* Anexos diferentes (distinto path) pueden tener el mismo nombre */
                                //mPost.addParameter(f.getPath(), f);
                                /** Debido a que el nombre del fichero puede contener acentos. */
                                mPost.addParameter(URLEncoder.encode(f.getPath(),"ISO-8859-1"), f);

                            }
                        }
                    }
                }
            }
        }
        /**/

        mPost.setFollowRedirects(false);

		//execute the method
		String responseBody = null;
		try {
			int status= client.executeMethod(mPost);
            if (status == HttpStatus.SC_OK) {
               logger.debug("Upload OK, response=" + status);
             } else {
               logger.debug("Upload NOK, response=" + status);
             }

			responseBody = mPost.getResponseBodyAsString();
			//responseBody=org.apache.commons.io.IOUtils.toString(mPost.getResponseBodyAsStream());
		} catch (HttpException he) {
            logger.debug("Http error connecting to '" + sUrl + "'");
			throw new Exception("Http error connecting to '" + sUrl + "'" + he.getMessage());
		} catch (IOException ioe) {
            logger.debug("Unable to connect to '" + sUrl + "'");
			throw new Exception("Unable to connect to '" + sUrl + "'" + ioe.getMessage());
		}
		logger.debug("Request Path: " + mPost.getPath());
		logger.debug("Request Query: " + mPost.getQueryString());
		Header[] requestHeaders = mPost.getRequestHeaders();
		for (int i = 0; i < requestHeaders.length; i++) {
            	logger.debug(requestHeaders[i]);
		}

		logger.debug("*** Response ***");
		logger.debug("Status Line: " + mPost.getStatusLine());
        int iStatusCode = mPost.getStatusCode();
        String sStatusLine= mPost.getStatusLine().toString();
		Header[] responseHeaders = mPost.getResponseHeaders();
		for (int i = 0; i < responseHeaders.length; i++) {
			logger.debug(responseHeaders[i]);
            if (responseHeaders[i].getName().equals("Content-Encoding") &&
                responseHeaders[i].getValue().equals("gzip"))
            {

                    try
                    {
                        logger.info("El contenido es gzip");
                        BufferedReader inbuf =new BufferedReader(new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(((MultipartPostMethod)mPost).getResponseBody()))));
                        logger.info("Gzip correcto");
                        String line=null;
                        StringBuffer buf = new StringBuffer();
                        while ((line = inbuf.readLine()) != null) {
                            buf.append(line);
                         }
                        responseBody= buf.toString();
                        logger.info("Fin conversion del contenido");

                    }catch(Exception e)
                    {
                        e.printStackTrace();
                        java.io.StringWriter sw=new java.io.StringWriter();
		                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	            e.printStackTrace(pw);
                        logger.error("Error al descomprimir:"+sw.toString());
                    }
            }
		}

		//write out the response body
		logger.debug("*** Response Body ***");
		logger.debug(responseBody);
     	//clean up the connection resources
		mPost.releaseConnection();
		mPost.recycle();
        if (iStatusCode==200)
        {
	        return new StringReader(responseBody);
            /*try {
			    return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
		    } catch (Exception e) {
		    	return new CResultadoOperacion(false, "Error al hacer el casting:" + responseBody);
		    }*/
        }
        else if (iStatusCode==401)
        {
            SecurityManager.unLogged();
            throw new Exception("ERROR EN LOS PERMISOS. CAUSAS: " +
                                        "\n\t-Usuario no autenticado " +
                                        "\n\t-Usuario no tiene permisos" +
                                        "\n\t-Se ha perdido la sesión con el servidor" +
                                        "\nReinicie la aplicación y vuelva a intentarlo");
        }
        else
            throw new Exception(sStatusLine);
	}

    private static void getProtocolSSL(HttpClient client, String sUrl) throws GeneralSecurityException, IOException{
    	ApplicationContext _app = null;
		if (AppContext.getApplicationContext() != null)
	    	_app = AppContext.getApplicationContext();
		else
			_app = new AppContext();
		int port = Integer.parseInt(_app.getString(UserPreferenceConstants.LOCALGIS_SERVER_PORT));
		Protocol easyhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), port);
		Protocol.registerProtocol("https", easyhttps);
	    client.getHostConfiguration().setHost(sUrl, port, easyhttps);
    }

}



