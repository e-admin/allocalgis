/**
 * SigmClient.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.client.sigm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;

import com.geopista.app.AppContext;
import com.geopista.app.sigm.SigmConstants;
import com.geopista.security.SecurityManager;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACQuery;
import com.geopista.server.administradorCartografia.AuthenticationException;
import com.geopista.server.administradorCartografia.Const;
import com.localgis.web.core.model.Procedure;
import com.localgis.web.core.model.ProcedureDefaults;
import com.localgis.web.core.model.ProcedureProperty;
import com.localgis.web.core.model.ProcedurePropertyKey;
import com.localgis.web.gwfst.ws.sigm.beans.PropertyAndName;
import com.localgis.web.gwfst.ws.sigm.beans.PropertyAndValue;
import com.vividsolutions.jump.task.TaskMonitor;

public class SigmClient implements ISigmClient {
	
	/**
	 * Logger
	 */
	private static final Log logger = LogFactory
			.getLog(SigmClient.class);

	/**
	 * Variables
	 */
	private static String sUrl = null;

	/**
	 * Constructor
	 * @param sUrl: Url del Servlet de SiGM en LocalGIS
	 */
	public SigmClient(String sUrl) {
		this.sUrl = sUrl;
	}
	
    /**
     * Recupera un array de PropertyAndValue con la información
     * @param idEntidad: Identificador de entidad de LocalGIS
     * @param nombreCapa: Identificador de la capa de LocalGIS
     * @param idFeature: Identificador de expediente de SiGM
     * @throws Exception
     * @return PropertyAndValue []: Array de PropertyAndValue con las propiedades de un trámite en SiGM y su valor
     */
	public PropertyAndValue [] getInfoAll(
			Integer idEntidad, String nombreCapa, String idFeature) throws Exception {
		PropertyAndValue [] cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_GET_INFO_ALL);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(SigmConstants.KEY_ENTITY_ID, idEntidad);
		params.put(SigmConstants.KEY_LAYER_NAME, nombreCapa);
		params.put(SigmConstants.KEY_FEATURE_ID, idFeature);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (PropertyAndValue []) readObject(ois);
		return cRet;
	}

    /**
     * Recupera un array de cadenas con los números de expedientes resultantes de la búsqueda
     * @param idEntidad: Identificador de entidad de SiGM
     * @param featureType: Tipo de trámite de SiGM
     * @param searchPropertyAndValues: Array de PropertyAndValue con las propiedades y sus valores de búsqueda
     * @throws Exception
     * @return String []: Array de cadenas con los números de expedientes resultantes de la búsqueda
     */
	public String [] getSearchAll(Integer idEntidad, String featureType, PropertyAndValue [] searchPropertyAndValues) throws Exception {
		String [] cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_GET_INFO_ALL);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(SigmConstants.KEY_ENTITY_ID, idEntidad);
		params.put(SigmConstants.KEY_FEATURE_TYPE, featureType);
		params.put(SigmConstants.KEY_SEARCH_PROPERTY_AND_VALUE, searchPropertyAndValues);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (String []) readObject(ois);
		return cRet;
	}
	
    /**
     * Recupera un valor de una propiedad de un expediente concreto
     * @param idEntidad: Identificador de entidad de SiGM
     * @param nombreEntidad: Nombre de trámite de SiGM
     * @param idFeature: Identificador de expediente de SiGM
     * @param property: Propiedad cuyo valro se desea recuperar
     * @throws Exception
     * @return String: Array de cadenas con los números de expedientes resultantes de la búsqueda
     */
	public String getInfoByPrimaryKey(
			Integer idEntidad, String nombreEntidad, String idFeature, String property) throws Exception {
		String cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_GET_INFO_ALL);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(SigmConstants.KEY_ENTITY_ID, idEntidad);
		params.put(SigmConstants.KEY_LAYER_NAME, nombreEntidad);
		params.put(SigmConstants.KEY_FEATURE_ID, idFeature);
		params.put(SigmConstants.KEY_PROPERTY, property);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (String) readObject(ois);
		return cRet;
	}
	
	/**
	 * 
	 */
	public List<Procedure> getAllProcedures() throws Exception{
		List<Procedure> cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_GET_ALL_PROCEDURES);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (List<Procedure>) readObject(ois);
		return cRet;
	}
	
	/**
	 * 
	 */
	public Procedure getProcedure(String procedureId) throws Exception{
		Procedure cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_GET_ALL_PROCEDURES);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		List<Procedure> procedures = (List<Procedure>) readObject(ois);
		Iterator<Procedure> it = procedures.iterator();
		while(it.hasNext()){
			Procedure procedure = it.next();
			if(procedure.getId().equals(procedureId)){
				return procedure;
			}
		}
		return null;
	}
	
	/**
	 * 
	 */
	public Boolean insertProcedure(Procedure procedure) throws Exception{
		Boolean cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_INSERT_PROCEDURE);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(SigmConstants.KEY_PROCEDURE, procedure);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (Boolean) readObject(ois);
		return cRet;
	}
	
	/**
	 * 
	 */
	public Boolean updateProcedure(Procedure procedure) throws Exception{
		Boolean cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_UPDATE_PROCEDURE);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(SigmConstants.KEY_PROCEDURE, procedure);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (Boolean) readObject(ois);
		return cRet;
	}
	
	/**
	 * 
	 */
	public Boolean deleteProcedure(String procedureId) throws Exception{
		Boolean cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_DELETE_PROCEDURE);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(SigmConstants.KEY_PROCEDURE_ID, procedureId);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (Boolean) readObject(ois);
		return cRet;
	}
	
	/**
	 * 
	 */
	public ProcedureDefaults getProcedureDefaults(String procedureId) throws Exception{
		ProcedureDefaults cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_GET_PROCEDURE_DEFAULTS);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(SigmConstants.KEY_PROCEDURE_ID, procedureId);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (ProcedureDefaults) readObject(ois);
		return cRet;
	}
	
	/**
	 * 
	 */
	public Boolean insertProcedureDefaults(ProcedureDefaults procedureDefaults) throws Exception{
		Boolean cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_INSERT_PROCEDURE_DEFAULTS);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(SigmConstants.KEY_PROCEDURE_DEFAULTS, procedureDefaults);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (Boolean) readObject(ois);
		return cRet;
	}
	
	/**
	 * 
	 */
	public Boolean updateProcedureDefaults(ProcedureDefaults procedureDefaults) throws Exception{
		Boolean cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_UPDATE_PROCEDURE_DEFAULTS);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(SigmConstants.KEY_PROCEDURE_DEFAULTS, procedureDefaults);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (Boolean) readObject(ois);
		return cRet;
	}
	
	/**
	 * 
	 */
	public Boolean deleteProcedureDefaults(String procedureId) throws Exception{
		Boolean cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_DELETE_PROCEDURE_DEFAULTS);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(SigmConstants.KEY_FEATURE_ID, procedureId);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (Boolean) readObject(ois);
		return cRet;
	}
	
	/**
	 * 
	 */
	public List<ProcedureProperty> getProcedureProperties(String procedureId) throws Exception{
		List<ProcedureProperty> cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_GET_PROCEDURE_PROPERTIES);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(SigmConstants.KEY_PROCEDURE_ID, procedureId);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (List<ProcedureProperty>) readObject(ois);
		return cRet;
	}
	
	/**
	 * 
	 */
	public HashMap<String, ProcedureProperty> getProcedurePropertiesMap(String procedureId) throws Exception{
		HashMap<String, ProcedureProperty> cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_GET_PROCEDURE_PROPERTIES_MAP);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(SigmConstants.KEY_PROCEDURE_ID, procedureId);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (HashMap<String, ProcedureProperty>) readObject(ois);
		return cRet;
	}
	
	/**
	 * 
	 */
	public Boolean insertProcedureProperty(ProcedureProperty procedureProperty) throws Exception{
		Boolean cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_INSERT_PROCEDURE_PROPERTY);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(SigmConstants.KEY_PROCEDURE_PROPERTY, procedureProperty);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (Boolean) readObject(ois);
		return cRet;
	}
	
	/**
	 * 
	 */
	public Boolean updateProcedureProperty(ProcedureProperty procedureProperty) throws Exception{
		Boolean cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_UPDATE_PROCEDURE_PROPERTY);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(SigmConstants.KEY_PROCEDURE_PROPERTY, procedureProperty);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (Boolean) readObject(ois);
		return cRet;
	}
	
	/**
	 * 
	 */
	public Boolean deleteProcedureProperty(ProcedurePropertyKey procedurePropertyId) throws Exception{
		Boolean cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_DELETE_PROCEDURE_PROPERTY);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(SigmConstants.KEY_PROCEDURE_PROPERTY_ID, procedurePropertyId);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (Boolean) readObject(ois);
		return cRet;
	}
	
	/**
	 * 
	 */
	public Boolean insertProcedureProperties(HashMap<String, ProcedureProperty> procedureProperties) throws Exception{
		Boolean cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_INSERT_PROCEDURE_PROPERTIES);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(SigmConstants.KEY_PROCEDURE_PROPERTIES_MAP, procedureProperties);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (Boolean) readObject(ois);
		return cRet;
	}
	
	/**
	 * 
	 */
	public Boolean updateProcedureProperties(String procedureId, HashMap<String, ProcedureProperty> procedureProperties) throws Exception{
		Boolean cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_UPDATE_PROCEDURE_PROPERTIES);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(SigmConstants.KEY_PROPERTY_ID, procedureId);
		params.put(SigmConstants.KEY_PROCEDURE_PROPERTIES_MAP, procedureProperties);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (Boolean) readObject(ois);
		return cRet;
	}
	
	/**
	 * 
	 */
	public Boolean deleteProcedureProperties(String procedureId) throws Exception{
		Boolean cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_DELETE_PROCEDURE_PROPERTIES);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(SigmConstants.KEY_PROCEDURE_ID, procedureId);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (Boolean) readObject(ois);
		return cRet;
	}
	
	/**
     * Recupera un array de PropertyAndName con los atributos de un trámite e SiGM
     * @param nombreCapa: Identificador de la capa de LocalGIS
     * @throws Exception
     * @return PropertyAndValue []: Array de PropertyAndName con los atributos de un trámite en SiGM y su nombre
     */
	public PropertyAndName [] getPropertyAndName(String nombreCapa) throws Exception {
		PropertyAndName [] cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(SigmConstants.ACTION_GET_PROPERTY_AND_NAME_KEY);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(SigmConstants.KEY_LAYER_NAME, nombreCapa);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (PropertyAndName []) readObject(ois);
		return cRet;
	}
		
	/**
     * Recupera un HashMap con los atributos de un trámite e SiGM
     * @param nombreCapa: Identificador de la capa de LocalGIS
     * @throws Exception
     * @return HashMap<String,String>: HashMap con los atributos de un trámite en SiGM y su nombre
     */
	public HashMap<String,String> getPropertyAndNameAsHashMap(String nombreCapa) throws Exception {
		HashMap<String,String> propertyAndNameAsHashMap = new HashMap<String,String>();
		PropertyAndName [] propertyAndNames = getPropertyAndName(nombreCapa);
		if(propertyAndNames != null){
			for(PropertyAndName propertyAndName : propertyAndNames){
				propertyAndNameAsHashMap.put(propertyAndName.getProperty(), propertyAndName.getName());
			}		
		}
		return propertyAndNameAsHashMap;
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
