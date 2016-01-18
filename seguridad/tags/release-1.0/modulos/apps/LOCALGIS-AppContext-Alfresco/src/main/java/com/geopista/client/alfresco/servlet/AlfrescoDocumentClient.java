package com.geopista.client.alfresco.servlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.zip.GZIPInputStream;

import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.alfresco.webservice.types.Node;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;

import com.geopista.app.AppContext;
import com.geopista.client.alfresco.AlfrescoConstants;
import com.geopista.security.SecurityManager;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACQuery;
import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 * @ClassComments Cliente del servlet de Alfresco
 */
public class AlfrescoDocumentClient implements IAlfrescoDocumentClient {

	/**
	 * Logger
	 */
	private static final Log logger = LogFactory
			.getLog(AlfrescoDocumentClient.class);

	/**
	 * Variables
	 */
	private static String sUrl = null;

	/**
	 * Constructor
	 * @param sUrl: Url del Servlet de Alfresco en LocalGIS
	 */
	public AlfrescoDocumentClient(String sUrl) {
		this.sUrl = sUrl;
	}

    /**
     * Solicita el directorio padre junto con su árbol de directorios hijos
     * @param rootNode: Directorio padre
     * @throws Exception
     * @return DefaultMutableTreeNode: Directorio padre junto con su árbol de directorios hijos
     */
	public DefaultMutableTreeNode getTreeDirectories(
			DefaultMutableTreeNode rootNode) throws Exception {
		DefaultMutableTreeNode cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(AlfrescoConstants.ACTION_GET_TREE_DIRECTORIES);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(AlfrescoConstants.KEY_TREE_NODE, rootNode);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (DefaultMutableTreeNode) readObject(ois);
		return cRet;
	}
	
	 /**
     * Solicita el nodo padre de un nodo referenciado por la clave unívoca
     * @param alfrescoKey: Clave unívoca de un nodo Alfresco
     * @throws Exception
     * @return Node: Nodo padre del nodo
     */
	public Node getParentNode(
			AlfrescoKey alfrescoKey) throws Exception {
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(AlfrescoConstants.KEY_ALFRESCOKEY, alfrescoKey);		
		return getNode(AlfrescoConstants.ACTION_GET_PARENT_NODE,
				params);
	}
	
    /**
     * Solicita la lista de nodos padre de un nodo referenciado por la clave unívoca
     * @param alfrescoKey: Clave unívoca de un nodo Alfresco
     * @throws Exception
     * @return ArrayList<Node>: Lista de nodos padre del nodo
     */
	public ArrayList<Node> getParentNodes(
			AlfrescoKey alfrescoKey) throws Exception {
		ArrayList<Node> cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(AlfrescoConstants.ACTION_GET_PARENT_NODES);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(AlfrescoConstants.KEY_ALFRESCOKEY, alfrescoKey);		
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (ArrayList<Node>) readObject(ois);
		return cRet;
	}

    /**
     * Solicita el modelo de tabla con los documentos contenidos en un directorio padre
     * @param parentKey: Clave unívoca de Alfresco del nodo padre
     * @param elements: Elementos LocalGIS seleccionados
     * @throws Exception
     * @return DefaultMutableTreeNode: Modelo de tabla con los documentos contenidos en un directorio padre
     */
	public DefaultTableModel getChildFiles(AlfrescoKey parentKey, Object [] elements)
			throws Exception {
		DefaultTableModel cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(AlfrescoConstants.ACTION_GET_CHILD_FILES);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(AlfrescoConstants.KEY_ALFRESCOKEY, parentKey);
		params.put(AlfrescoConstants.KEY_ELEMENT, elements);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (DefaultTableModel) readObject(ois);
		return cRet;
	}
	
    /**
     * Inicializa la ruta relativa de directorios y su acceso, y devuelve el nodo padre
	 * @param idMunicipality: Identificador de municipio
	 * @param appName: Nombre del tipo de aplicación
	 * @throws Exception
     * @return Node: Nodo padre de la ruta relativa de directorios
     */
	public Node initializeRelativeDirectoryPathAndAccess(String idMunicipality, String appName)
			throws Exception {
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(AlfrescoConstants.KEY_ID_MUNICIPALITY, idMunicipality);
		params.put(AlfrescoConstants.KEY_APP, appName);		
		return getNode(AlfrescoConstants.ACTION_INITIALIZE_RELATIVE_DIRECTORY_PATH_AND_ACCESS,
				params);
	}
	
    /**
     * Cambia la contraseña de un usuario
     * @param userName: Nombre del usuario
     * @param newPassword: Nueva contraseña del usuario
	 * @throws Exception
     * @return Boolean: Resultado de la actualización de la contraseña de usuario
     */
	public Boolean setPassword(String userName, String newPassword) throws Exception {
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(AlfrescoConstants.KEY_USER, userName);
		params.put(AlfrescoConstants.KEY_PASS, newPassword);
		return getBooleanResult(AlfrescoConstants.ACTION_SET_PASSWORD, params);
	}
	
	/**
	 * Mueve un nodo de Alfresco a un nuevo directorio
	 * @param newParentKey: Clave unívoca del nuevo directorio padre
	 * @param key: Clave unívoca del nodo de Alfresco a mover
	 * @throws Exception
	 * @return Boolean: Resultado del movimiento del nodo Alfresco
	 */
	public Boolean moveNode(AlfrescoKey newParentKey, AlfrescoKey key) throws Exception {
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(AlfrescoConstants.KEY_PARENT_ALFRESCOKEY, newParentKey);
		params.put(AlfrescoConstants.KEY_ALFRESCOKEY, key);
		return getBooleanResult(AlfrescoConstants.ACTION_MOVE_NODE, params);
	}
	
	/**
	 * Renombra un nodo
	 * @param key: Clave unívoca del nodo
	 * @param newName: Nuevo nombre del nodo de Alfresco
	 * @throws Exception
	 * @return Boolean: Resultado del renombre del nodo Alfresco
	 */
	public Boolean renameNode(AlfrescoKey key, String newName) throws Exception {
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(AlfrescoConstants.KEY_ALFRESCOKEY, key);
		params.put(AlfrescoConstants.KEY_NAME, newName);
		return getBooleanResult(AlfrescoConstants.ACTION_RENAME_NODE, params);
	}
	
	/**
	 * Actualiza el identificador de un documento 
	 * @param oldUuid: Clave unívoca del documento
	 * @param newUuid: Nueva clave unívoca del documento
	 * @throws Exception
	 * @return Boolean: Resultado de la actualización del identificador del documento
	 */
	public Boolean updateDocumentUuid(String oldUuid, String newUuid) throws Exception {
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(AlfrescoConstants.KEY_OLD_UUID, oldUuid);
		params.put(AlfrescoConstants.KEY_NEW_UUID, newUuid);
		return getBooleanResult(AlfrescoConstants.ACTION_UPDATE_DOCUMENT_UUID, params);
	}

    /**
     * Crea una versión de un documento
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @param content: Array de bytes con el contenido del documento
	 * @param comments: Formato del documento
	 * @throws Exception
     * @return Node: Nodo de la versión del documento 
     */
	public Node addVersion(AlfrescoKey key, byte [] content, String comments)
			throws Exception {
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(AlfrescoConstants.KEY_ALFRESCOKEY, key);
		params.put(AlfrescoConstants.KEY_BYTE_CONTENT, content);
		params.put(AlfrescoConstants.KEY_COMMENTS, comments);
		return getNode(AlfrescoConstants.ACTION_ADD_VERSION, params);
	}

    /**
     * Método genérico para las solicitudes del cliente que devuelvan un Boolean
	 * @param action: Accion solicitada
	 * @param params: Hashtable con las claves de parámetro y sus valores 
	 * @throws Exception
     * @return Boolean: Resultado de la operación solicitada por el cliente
     */
	private Boolean getBooleanResult(int action,
			Hashtable<Integer, Object> params) throws Exception {
		Boolean cRet = false;
		ACQuery query = new ACQuery();
		query.setAction(action);
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
     * Descarga un documento
	 * @param key: Clave unívoca de un nodo padre de Alfresco
	 * @param path: Ruta local de descarga
	 * @param name: Nombre del documento en el sistema local
	 * @throws Exception
     * @return Boolean: Resultado de la descarga
     */
	public Boolean downloadFile(AlfrescoKey key, String path, String name)
			throws Exception {
		FileInputStream inputStream  = returnFile(key);
		FileOutputStream outputStream = new FileOutputStream(new File(path
				+ File.separator + name));
		byte[] buf = new byte[1024];
		int len;
		while ((len = inputStream.read(buf)) > 0) {
			outputStream.write(buf, 0, len);
		}
		inputStream.close();
		outputStream.flush();
		outputStream.close();
		return true;
	}
	
    /**
     * Solicita un documento al servidor
	 * @param key: Clave unívoca de un nodo padre de Alfresco
	 * @throws Exception
     * @return FileInputStream: Contenido del documento solicitado
     */
	public FileInputStream returnFile(AlfrescoKey key)
	throws Exception {
		ACQuery query = new ACQuery();
		query.setAction(AlfrescoConstants.ACTION_RETURN_FILE);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(AlfrescoConstants.KEY_ALFRESCOKEY, key);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		return new FileInputStream(
				(File) readObject(ois));
	}

    /**
     * Añade un documento a un directorio
	 * @param parentKey: Clave unívoca de un nodo padre de Alfresco
	 * @param file: Documento
	 * @param fileName: Nombre del documento
	 * @throws Exception
     * @return Node: Nodo del documento añadido
     */
	public Node addFileFromParent(AlfrescoKey parentKey, File file, String fileName) throws Exception {
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(AlfrescoConstants.KEY_ALFRESCOKEY, parentKey);
		params.put(AlfrescoConstants.KEY_FILE, file);
		params.put(AlfrescoConstants.KEY_NAME, fileName);
		return getNode(AlfrescoConstants.ACTION_ADD_FILE_FROM_PARENT, params);
	}	
	
    /**
     * Añade un documento a un directorio
	 * @param parentKey: Clave unívoca de un nodo padre de Alfresco
	 * @param file: Documento
	 * @throws Exception
     * @return Node: Nodo del documento añadido
     */
	public Node addFileFromParent(AlfrescoKey parentKey, File file) throws Exception {
		return addFileFromParent(parentKey, file, file.getName());
	}
  
    /**
     * Añade un directorio a un nodo padre
	 * @param parentKey: Clave unívoca de un nodo padre de Alfresco
	 * @param directoryName: Nombre del directorio hijo a crear
	 * @throws Exception
     * @return Node: Directorio añadido
     */
	public Node addDirectoryFromParent(AlfrescoKey parentKey, String directoryName)
			throws Exception {
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(AlfrescoConstants.KEY_ALFRESCOKEY, parentKey);
		params.put(AlfrescoConstants.KEY_DIRECTORY, directoryName);
		return getNode(AlfrescoConstants.ACTION_ADD_DIRECTORY_FROM_PARENT,
				params);
	}

	/**
	 * Solicita el nodo referenciado por la clave unívoca
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @throws Exception
	 * @return Node: Nodo de alfresco
	 */
	public Node getNode(AlfrescoKey key) throws Exception {
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(AlfrescoConstants.KEY_ALFRESCOKEY, key);
		return getNode(AlfrescoConstants.ACTION_GET_NODE, params);
	}

    /**
     * Método genérico para las solicitudes del cliente que devuelvan un nodo de Alfresco
	 * @param action: Accion solicitada
	 * @param params: Hashtable con las claves de parámetro y sus valores 
	 * @throws Exception
     * @return Node: Nodo de Alfresco
     */
	public Node getNode(int action, Hashtable<Integer, Object> params)
			throws Exception {
		Node cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(action);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = send(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		cRet = (Node) readObject(ois);		
		return cRet;
	}

	/**
     * Recupera el nombre de un municipio
     * @param idMunicipality: Identificador de municipio
	 * @throws Exception
     * @return String: Nombre del municipio
     */
	public String getMunicipalityName(String idMunicipality) throws Exception {
		String cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(AlfrescoConstants.ACTION_GET_MUNICPALITY_NAME);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(AlfrescoConstants.KEY_ID_MUNICIPALITY, idMunicipality);
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
