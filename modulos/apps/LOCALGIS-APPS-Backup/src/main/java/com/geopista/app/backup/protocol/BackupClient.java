/**
 * BackupClient.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.backup.protocol;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;

import com.geopista.app.AppContext;
import com.geopista.security.SecurityManager;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACQuery;


/**
 * Clase que implementa las operaciones que se pueden llevar a cabo en BD desde las aplicaciones cliente
 */

public class BackupClient {
	private static final Log logger= LogFactory.getLog(BackupClient.class);

	public static final String	mensajeXML	= "mensajeXML";
	public static final String	idMunicipio	= "idMunicipio";
	public static final String	IdApp		= "IdApp";

	public static final int SET_PWD_USR_BACKUP = 0;
	
	public static final String USUARIO = "usuario";
	public static final String PASSWORD = "password";

	private String sUrl=null;
	private SecurityManager sm=null;

	/**
	 * Inicializa la conexion al servidor
	 * @param sUrl del servidor
	 */
	public BackupClient(String sUrl){
		this.sm=SecurityManager.getSm();
		this.sUrl=sUrl;
	}

	public BackupClient(String sUrl, SecurityManager sm){
		this.sm=sm;
		this.sUrl=sUrl;
	}

	private Object readObject(ObjectInputStream ois) throws IOException,ClassNotFoundException,ACException{
		Object oRet=null;
		oRet=ois.readObject();
		if (oRet instanceof ACException){
			throw (ACException)oRet;
		}
		return oRet;
	}

	public InputStream enviar(String sUrl, String sMensaje) throws Exception{
		return enviar(sUrl, sMensaje, null, null, null);
	}

	public InputStream enviar(String sUrl, String sMensaje, Vector files) throws Exception{
		return enviar(sUrl, sMensaje, null, null, files);
	}

	private  InputStream enviar(String sUrl, String sMensaje, String sUserName, String sPassword, Vector files) throws Exception{
		Credentials creds = null;
		if (sUserName != null && sPassword != null)
			creds = new UsernamePasswordCredentials(sUserName, sPassword);
		else {
			if (sm.getIdSesionNS() == null){
				if (logger.isDebugEnabled()){
					logger.debug("enviar(String, String, String, String) - Usuario no autienticado");
				}
				creds = new UsernamePasswordCredentials("GUEST", "");
			}else
				creds = new UsernamePasswordCredentials(sm.getIdSesionNS(), sm.getIdSesionNS());
		}
		//create a singular HttpClient object
		org.apache.commons.httpclient.HttpClient client= AppContext.getHttpClient();

		//establish a connection within 5 seconds
		//client.setConnectionTimeout(5000);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

		//set the default credentials
		if (creds != null) {
			client.getState().setCredentials(null, null, creds);
		}

		/* -- MultipartPostMethod -- */
		org.apache.commons.httpclient.methods.MultipartPostMethod method= new org.apache.commons.httpclient.methods.MultipartPostMethod(sUrl);

		if (sm.getIdMunicipioNS()!=null){
			method.addParameter(idMunicipio, sm.getIdMunicipioNS());
		}
		if (SecurityManager.getIdApp()!=null){
			method.addParameter(IdApp, sm.getIdAppNS());
		}
		if (sMensaje!=null){
			//method.addParameter(mensajeXML, sMensaje);
			method.addPart(new org.apache.commons.httpclient.methods.multipart.StringPart(mensajeXML, sMensaje, "ISO-8859-1"));
		}

		/** Necesario para la aplicacion de Inventario. En la aplicacion se pasa un vector files a insertar o modificar */
		if (files != null){
			for (int i=0; i<files.size(); i++){
				File file= (File)files.get(i);
				/** Debido a que el nombre del fichero puede contener acentos. */
				try{method.addParameter(URLEncoder.encode(i+"FILE"+file.getName(), "ISO-8859-1"), file);}catch(java.io.FileNotFoundException e){/** Fichero vacio */}
			}
		}

		method.setFollowRedirects(false);

		//execute the method
		byte[] responseBody = null;
		try {
			client.executeMethod(method);
			responseBody = method.getResponseBody();
		} catch (HttpException he) {
			throw new Exception("Http error connecting to '" + sUrl + "'" + he.getMessage());
		} catch (IOException ioe) {
			logger.error("enviar(String, String, String, String) - Unable to connect to '" + sUrl + "'", ioe);
			throw new Exception("Unable to connect to '" + sUrl + "'" + ioe.getMessage());
		}
		if (logger.isDebugEnabled()){
			logger.debug("enviar(String, String, String, String) - Request Path: "+ method.getPath());
		}
		if (logger.isDebugEnabled()){
			logger.debug("enviar(String, String, String, String) - Request Query: "+ method.getQueryString());
		}
		Header[] requestHeaders = method.getRequestHeaders();
		for (int i = 0; i < requestHeaders.length; i++) {
			if (logger.isDebugEnabled()){
				logger.debug("enviar(String, String, String, String)" + requestHeaders[i]);
			}
		}

		//write out the response headers
		if (logger.isDebugEnabled()){
			logger.debug("enviar(String, String, String, String) - *** Response ***");
		}
		if (logger.isDebugEnabled()){
			logger.debug("enviar(String, String, String, String) - Status Line: " + method.getStatusLine());
		}
		int iStatusCode = method.getStatusCode();
		String sStatusLine=method.getStatusLine().toString();
		Header[] responseHeaders = method.getResponseHeaders();
		for (int i = 0; i < responseHeaders.length; i++) {
			if (logger.isDebugEnabled()){
				logger.debug("enviar(String, String, String, String)" + responseHeaders[i]);
			}
		}

		//clean up the connection resources
		method.releaseConnection();
		method.recycle();
		if (iStatusCode==200){
			return new GZIPInputStream(new ByteArrayInputStream(responseBody));
		}
		else
			throw new Exception(sStatusLine);
	}


	/**
	 * Creamos un rol en la BBDD
	 * @throws Exception
	 */
	public void actualizarPwdUsuarioBdD(String loginNewUsr, String passwordNewUsr) throws Exception
	{
		ACQuery query= new ACQuery();
		Hashtable params= new Hashtable();
		query.setAction(SET_PWD_USR_BACKUP);
		params.put(USUARIO, loginNewUsr);
		params.put(PASSWORD, passwordNewUsr);
		query.setParams(params);
		StringWriter swQuery= new StringWriter();
		ByteArrayOutputStream baos= new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = enviar(this.sUrl, swQuery.toString());
		ObjectInputStream ois= new ObjectInputStream(in);
		try {
			readObject(ois);
		} catch(OptionalDataException ode) {
			if (ode.eof!=true)
				logger.error("crearUsuario()" + ode.getMessage());
		} catch (EOFException ee) {
		}
		finally {
			try{ois.close();}catch(Exception e){};
		}
	}
}
