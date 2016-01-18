/**
 * AppsClient.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.centralizadorsso.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;


import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;

import com.geopista.app.AppContext;
import com.geopista.app.centralizadorsso.beans.LocalGISApp;
import com.geopista.security.SecurityManager;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;

public class AppsClient {
	
	/**
	 * Logger
	 */
	private static final Log logger = LogFactory
			.getLog(AppsClient.class);

	/**
	 * Constantes
	 */
    public static final String	MENSAJE_XML	= "mensajeXML";
    
	/**
	 * Variables
	 */
	private static String sUrl = null;

	/**
	 * Constructor
	 * @param sUrl: Url del Servlet de Apps en LocalGIS
	 */
	public AppsClient(String sUrl) {
		this.sUrl = sUrl;
	}
	
    /**
     * Recupera las aplicaciones del sistema
     * @throws Exception
     * @return HashMap<String, LocalGISApp>: Mapa con los objetos LocalGISApp
     */
	public HashMap<String, LocalGISApp> getAllApps() throws Exception {
		HashMap<String, LocalGISApp> cRet = null;
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (HashMap<String, LocalGISApp>) readObject(ois);
		return cRet;
	}
	/**
     * Envía la solicitud de operación del cliente
	 * @param sUrl: url del servlet del servidor
	 * @param sMensaje: Mensaje con los parámetros
	 * @throws Exception
     * @return InputStream: Resultado de la solicitud
     */
	public static InputStream send(String sUrl, String sMensaje)
			throws Exception {
		return send(sUrl, sMensaje, null, null);
	}

    /**
     * Envía la solicitud de operación del cliente
	 * @param sUrl: url del servlet del servidor
	 * @param sMensaje: Mensaje con los parámetros
	 * @param sUserName: Nombre de usuario LocalGIS
	 * @param sPassword: Contraseña de usuario LocalGIS
	 * @throws Exception
     * @return InputStream: Resultado de la solicitud
     */
	private static InputStream send(String sUrl, String sMensaje,
			String sUserName, String sPassword) throws Exception {
		Credentials creds = null;
		if (sUserName != null && sPassword != null)
			creds = new UsernamePasswordCredentials(sUserName, sPassword);
		else {
			if (com.geopista.security.SecurityManager.getIdSesion() == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("enviar(String, String, String, String) - Usuario no autienticado");
				}
				creds = new UsernamePasswordCredentials("GUEST", "");
			} else
				creds = new UsernamePasswordCredentials(
						com.geopista.security.SecurityManager.getIdSesion(),
						SecurityManager.getIdSesion());
		}
		// create a singular HttpClient object
		org.apache.commons.httpclient.HttpClient client = AppContext
				.getHttpClient();

		// establish a connection within 5 seconds
		client.setConnectionTimeout(5000);

		// set the default credentials
		if (creds != null) {
			client.getState().setCredentials(null, null, creds);
		}

		/* -- MultipartPostMethod -- */
		org.apache.commons.httpclient.methods.MultipartPostMethod method = new org.apache.commons.httpclient.methods.MultipartPostMethod(
				sUrl);

		if (sMensaje != null) {
			// method.addParameter(mensajeXML, sMensaje);
			method.addPart(new org.apache.commons.httpclient.methods.multipart.StringPart(
					MENSAJE_XML, sMensaje, "ISO-8859-1"));
		}

		method.setFollowRedirects(false);

		// execute the method
		byte[] responseBody = null;
		try {
			client.executeMethod(method);
			responseBody = method.getResponseBody();
		} catch (HttpException he) {
			throw new Exception("Http error connecting to '" + sUrl + "'"
					+ he.getMessage());
		} catch (IOException ioe) {
			logger.error(
					"enviar(String, String, String, String) - Unable to connect to '"
							+ sUrl + "'", ioe);
			throw new Exception("Unable to connect to '" + sUrl + "'"
					+ ioe.getMessage());
		}
		if (logger.isDebugEnabled()) {
			logger.debug("enviar(String, String, String, String) - Request Path: "
					+ method.getPath());
		}
		if (logger.isDebugEnabled()) {
			logger.debug("enviar(String, String, String, String) - Request Query: "
					+ method.getQueryString());
		}
		Header[] requestHeaders = method.getRequestHeaders();
		for (int i = 0; i < requestHeaders.length; i++) {
			if (logger.isDebugEnabled()) {
				logger.debug("enviar(String, String, String, String)"
						+ requestHeaders[i]);
			}
		}

		// write out the response headers
		if (logger.isDebugEnabled()) {
			logger.debug("enviar(String, String, String, String) - *** Response ***");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("enviar(String, String, String, String) - Status Line: "
					+ method.getStatusLine());
		}
		int iStatusCode = method.getStatusCode();
		String sStatusLine = method.getStatusLine().toString();
		Header[] responseHeaders = method.getResponseHeaders();
		for (int i = 0; i < responseHeaders.length; i++) {
			if (logger.isDebugEnabled()) {
				logger.debug("enviar(String, String, String, String)"
						+ responseHeaders[i]);
			}
		}

		// clean up the connection resources
		method.releaseConnection();
		method.recycle();
		if (iStatusCode == 200) {
			//return new ByteArrayInputStream(responseBody);
			return new GZIPInputStream(new ByteArrayInputStream(responseBody));
		} else
			throw new Exception(sStatusLine);
	}

    /**
     * Lee un resultado en cadena de bytes y lo traduce a un objeto
	 * @param ois: Cadena de bytes
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws ACException
     * @return Object: Object resultante
     */
	private static Object readObject(ObjectInputStream ois) throws IOException,
			ClassNotFoundException, ACException {
		Object oRet = null;
		oRet = ois.readObject();
		if (oRet instanceof ACException) {
			throw (ACException) oRet;
		}

		return oRet;
	}

}
