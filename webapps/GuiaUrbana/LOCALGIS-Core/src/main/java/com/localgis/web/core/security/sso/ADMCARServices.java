/**
 * ADMCARServices.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.security.sso;

import java.io.IOException;
import java.io.StringReader;
import java.security.GeneralSecurityException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Unmarshaller;

import com.geopista.app.UserPreferenceConstants;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.security.sso.beans.SesionBean;

/*
 * @Author dcaaveiro
 */
public class ADMCARServices {	

	private static final Log logger = LogFactory.getLog(ADMCARServices.class);
	
	/**
	 * url: URL del servicio
	 * idSesion: Identificador de sesión
	 * username: Nombre de usuario que autenticará para acceder al servicio
	 * password: Contraseña del usuario
	 * Función encargada pedir y recomponer el SesionBean resultante de la petición al servicio getOneSession del administrador de cartografía
	 */
	public static SesionBean getSession(String url, String idSesion, String username, String password){
    	SesionBean sesionBean = new SesionBean(); 
    	try {        	   
              StringReader sr=enviar(url + WebAppConstants.PRINCIPAL_WEBAPP_NAME + ServletConstants.GETONESESSION_SERVLET_NAME,idSesion,username,password,"Administracion");
              sesionBean =(SesionBean)Unmarshaller.unmarshal(SesionBean.class,sr);       
        }
        catch (Exception ex) {
        	 logger.error(ex.getMessage());  
        }
        return sesionBean;
    }	  
	
	/**
	 * url: URL del servicio
	 * idSesion: Identificador de sesión
	 * username: Nombre de usuario que autenticará para acceder al servicio
	 * password: Contraseña del usuario
	 * idApp: Rol de acceso al servicio
	 * Función encargada de la peticion autenticada del servicio al administrador de cartografía
	 * devuelve un StringReader con la clase envoltorio de la información
	 */
    public static StringReader enviar(String url, String idSesion, String username, String password, String idApp){
		Credentials creds = new UsernamePasswordCredentials(username, password);		
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		org.apache.commons.httpclient.HttpClient client = new HttpClient(connectionManager);
		client.getHttpConnectionManager().getParams().setMaxConnectionsPerHost(new HostConfiguration(),3);
		client.getHttpConnectionManager().getParams().setMaxTotalConnections(3);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);		
		getProtocolSSL(client,url);		
		AuthScope auth=new AuthScope(null,-1, null);
		client.getState().setCredentials(auth, creds);				
		HttpMethod method = new PostMethod(url);
		((PostMethod) method).addParameter("IdApp", idApp);		
		((PostMethod) method).addParameter("mensajeXML", idSesion);		
		((PostMethod) method).addParameter("userIntentos","0");
		method.setFollowRedirects(false);
		((PostMethod) method).setRequestContentLength(PostMethod.CONTENT_LENGTH_AUTO);		
		//execute the method
		String responseBody = null;
		try {
			client.executeMethod(method);
			responseBody = method.getResponseBodyAsString();
		} catch (HttpException he) {
		} catch (IOException ioe) {
		}
		Header[] requestHeaders = method.getRequestHeaders();
		
		int iStatusCode = method.getStatusCode();
		String sStatusLine=method.getStatusLine().toString();
		/*Header[] responseHeaders = method.getResponseHeaders();
		for (int i = 0; i < responseHeaders.length; i++) {
		    if (responseHeaders[i].getName().equals("Content-Encoding") &&
		        responseHeaders[i].getValue().equals("gzip"))
		    {
		
		        try
		        {
		            BufferedReader inbuf =new BufferedReader(new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(method.getResponseBody()))));
		            String line=null;
		            StringBuffer buf = new StringBuffer();
		            while ((line = inbuf.readLine()) != null) {
		                buf.append(line);
		             }
		            responseBody= buf.toString();
		
		        }catch(Exception e){        	
		        
		        }
		    }
		}*/
		if (iStatusCode==200)
		{
		    return new StringReader(responseBody);
		}
		else if (iStatusCode==401)
		{
			logger.error("ERROR EN LOS PERMISOS. CAUSAS: " +
                    "\n\t-Usuario no autenticado " +
                    "\n\t-Usuario no tiene permisos" +
                    "\n\t-Se ha perdido la sesión con el servidor" +
                    "\nReinicie la aplicación y vuelva a intentarlo");
			return null;
		}
		else return null;
    }
    
    /**
     * client: Cliente HTTP
     * url: URL del servicio
     * Función que obtiene el protocolo SSL
     */
	private static void getProtocolSSL(HttpClient client, String url){
		try {
			int port = Integer.parseInt(Configuration.getPropertyString(UserPreferenceConstants.LOCALGIS_SERVER_PORT));
			Protocol easyhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), port);	
			Protocol.registerProtocol("https", easyhttps);
		    client.getHostConfiguration().setHost(url, port, easyhttps);
		} catch (NumberFormatException e) {
			System.out.println(e);
		} catch (LocalgisConfigurationException e) {
			System.out.println(e);
		} catch (GeneralSecurityException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

    
}
