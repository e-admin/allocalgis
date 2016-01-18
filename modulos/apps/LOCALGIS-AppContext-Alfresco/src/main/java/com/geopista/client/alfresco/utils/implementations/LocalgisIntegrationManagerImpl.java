/**
 * LocalgisIntegrationManagerImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.client.alfresco.utils.implementations;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.util.ISO9075;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.client.alfresco.servlet.AlfrescoDocumentClient;
import com.geopista.client.alfresco.utils.interfaces.LocalgisIntegrationManager;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.cementerios.ElemCementerioBean;
import com.geopista.protocol.document.CementerioDocumentClient;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.DocumentClient;
import com.geopista.protocol.document.IDocumentClient;
import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 * @ClassComments Clase que gestiona la asociacion entre los documentos de Alfresco y los elementos de LocalGIS 
 */
public class LocalgisIntegrationManagerImpl implements LocalgisIntegrationManager {
	
	/**
	 * Logger
	 */
	 private static final Log logger = LogFactory.getLog(LocalgisIntegrationManagerImpl.class);
	
	/**
	 * Variables
	 */
	private AlfrescoDocumentClient alfrescoDocumentClient = null;
	
	/**
	 * Constructor
	 * @param url: Url del servidor LocalGIS
	 * @param idMunicipality: Identificador de un municipio
	 * @param appName: Nombre de la aplicación de documentación LocalGIS
	 */
	public LocalgisIntegrationManagerImpl(String url, String idMunicipality, String appName){
		alfrescoDocumentClient = new AlfrescoDocumentClient(url + WebAppConstants.ALFRESCO_WEBAPP_NAME + ServletConstants.ALFRESCO_DOCUMENT_SERVLET_NAME);
    	try {
			alfrescoDocumentClient.initializeRelativeDirectoryPathAndAccess(idMunicipality, appName);
		} catch (Exception e) {
			//System.out.println(e);
			logger.error("Excepcion al inicializar alfresco",e);
		}
	}
	
	
	/**
	 * Este metodo verifica si el alfresco esta activo a nivel cliente, abriendo una conexion
	 * contra el servidor para ver que es lo que está activado a ese nivel.
	 * @param url
	 * @param idMunicipality
	 * @param appName
	 * @return
	 */
	public static boolean verifyStatusAlfresco(String url, String idMunicipality, String appName){
    	try {
    		AlfrescoDocumentClient alfrescoDocumentClient = new AlfrescoDocumentClient(url + WebAppConstants.ALFRESCO_WEBAPP_NAME + ServletConstants.ALFRESCO_DOCUMENT_SERVLET_NAME);    		
    		String resultado=alfrescoDocumentClient.getAlfrescoProperty(AlfrescoManagerUtils.PROP_ALFRESCO_ACTIVE);
    		if ((resultado!=null) && resultado.equals("true"))
    			return true;
    		
		} catch (Exception e) {
			//System.out.println(e);
			logger.error("Excepcion al inicializar alfresco",e);
		}
    	return false;
	}
	
	
	/**
	 * Añade un documento a Alfresco (si no existe) y lo asocia al elemento en LocalGIS
	 * @param idMunicipality: Identificador de un municipio
	 * @param appName: Nombre de la aplicación de documentación LocalGIS
	 * @param document: Documento
	 * @param documentClient: Cliente del servlet de gestión documental LocaLGIS  
	 * @param array: Contenido del documento
	 * @throws Exception
	 * @return boolean: Resultado de la asociación
	 */	
	public boolean associateAlfrescoDocument(String idMunicipality, String appName, DocumentBean document, IDocumentClient documentClient, Object [] features) throws Exception{
		String parentPath = alfrescoDocumentClient.getAlfrescoProperty(AlfrescoManagerUtils.PROP_ROOT_PATH) + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(alfrescoDocumentClient.getMunicipalityName(idMunicipality)) + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(appName);
		Node node = alfrescoDocumentClient.getNode(new AlfrescoKey(parentPath + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(new File(document.getFileName()).getName()), AlfrescoKey.KEY_PATH));
		String uuid = null;
		File file = new File(document.getFileName());
		if(node==null)
			uuid = addAlfrescoDocument(parentPath, file);
		else			
			uuid = node.getReference().getUuid();		

		if(uuid != null){
			document.setId(uuid);
		    document.setContent(null);
		    document.setFileName(file.getAbsolutePath());
		    document.setFechaEntradaSistema(new Date());
		    document=documentClient.attachDocument(features, document);
			return true;
		}	
		file = null;
		return false;
	} 
		
	private boolean existsLocalGISAssociation(String name, Object [] features, IDocumentClient documentClient) throws Exception{
		Collection documents;
		for(Object feature : features){
			documents = ((DocumentClient)documentClient).getAttachedDocuments(feature);
			Iterator<DocumentBean> it = documents.iterator();
			while(it.hasNext()){				
				if(it.next().getFileName().equals(name))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Actualiza (versiona) un documento en Alfresco (si existe, si no lo añade) y lo vuelve a asociar al elemento de LocalGIS
	 * @param idMunicipality: Identificador de un municipio
	 * @param appName: Nombre de la aplicación de documentación LocalGIS
	 * @param document: Documento
	 * @param documentClient: Cliente del servlet de gestión documental LocaLGIS  
	 * @throws Exception
	 * @return boolean: Resultado de la actualización de un documento asociado
	 */	
	public boolean associateAlfrescoDocument(String idMunicipality, String appName, String fileName, DocumentBean document, IDocumentClient documentClient) throws Exception{
		String parentPath = alfrescoDocumentClient.getAlfrescoProperty(AlfrescoManagerUtils.PROP_ROOT_PATH) + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(alfrescoDocumentClient.getMunicipalityName(idMunicipality)) + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(appName);
		Node node = alfrescoDocumentClient.getNode(new AlfrescoKey(parentPath + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(fileName), AlfrescoKey.KEY_PATH));
		String uuid = null;
		File file = new File(document.getFileName());
		if(node==null)
			uuid = addAlfrescoDocument(parentPath, file, fileName);
		else
			uuid = updateLocalGISDocument(parentPath, node.getReference().getUuid(), file);
		
		if(uuid != null){
			if(!document.getId().equals(uuid))
				alfrescoDocumentClient.updateDocumentUuid(document.getId(), uuid);
	    	document.setId(uuid);
	    	document.setContent(null);
	        document.setFileName(fileName);
	        document.setFechaUltimaModificacion(new Date());
	    	documentClient.updateDocument(document, file);	
	    	return true;
	    }
    	file = null;
	    return false;		
	} 
	
	/**
	 * Añade un documento de inventario a Alfresco (si no existe) y lo asocia al elemento en LocalGIS
	 * @param idMunicipality: Identificador de un municipio
	 * @param appName: Nombre de la aplicación de documentación LocalGIS
	 * @param document: Documento
	 * @param documentClient: Cliente del servlet de gestión documental LocaLGIS  
	 * @param key: Contenido del documento
	 * @throws Exception
	 * @return boolean: Resultado de la asociación
	 */	
	public boolean associateAlfrescoInventarioDocument(String idMunicipality, String appName, DocumentBean document, IDocumentClient documentClient, Object key) throws Exception{
		String parentPath = alfrescoDocumentClient.getAlfrescoProperty(AlfrescoManagerUtils.PROP_ROOT_PATH) + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(alfrescoDocumentClient.getMunicipalityName(idMunicipality)) + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(appName);
		String fileName = new File(document.getFileName()).getName();
		Node node = alfrescoDocumentClient.getNode(new AlfrescoKey(parentPath + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(fileName), AlfrescoKey.KEY_PATH));
		String uuid = null;
		File file = new File(document.getFileName());
		if(node==null)
			uuid = addAlfrescoDocument(parentPath, file);
		else
			uuid = node.getReference().getUuid();				
		
		if(uuid != null){
			document.setId(uuid);			
		    document.setContent(null);
		    document.setFileName(file.getAbsolutePath());
		    document.setFechaEntradaSistema(new Date());
		    document = ((DocumentClient)documentClient).attachInventarioDocument(key, document);
			return true;
		}	
		file = null;
		return false;
	} 
	
	/**
	 * Actualiza (versiona) un documento de inventario en Alfresco (si existe, si no lo añade) y lo vuelve a asociar al elemento de LocalGIS
	 * @param idMunicipality: Identificador de un municipio
	 * @param appName: Nombre de la aplicación de documentación LocalGIS
	 * @param document: Documento
	 * @param documentClient: Cliente del servlet de gestión documental LocaLGIS  
	 * @throws Exception
	 * @return boolean: Resultado de la actualización de un documento asociado
	 */	
	public boolean associateAlfrescoInventarioDocument(String idMunicipality, String appName, String fileName, DocumentBean document, IDocumentClient documentClient) throws Exception{
		String parentPath = alfrescoDocumentClient.getAlfrescoProperty(AlfrescoManagerUtils.PROP_ROOT_PATH) + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(alfrescoDocumentClient.getMunicipalityName(idMunicipality)) + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(appName);
		Node node = alfrescoDocumentClient.getNode(new AlfrescoKey(parentPath + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(fileName), AlfrescoKey.KEY_PATH));
		String uuid = null;
		File file = new File(document.getFileName());
		if(node==null)
			uuid = addAlfrescoDocument(parentPath, file, fileName);
		else
			uuid = updateLocalGISDocument(parentPath, node.getReference().getUuid(), file);
		
		if(uuid != null){
			if(!document.getId().equals(uuid))
				alfrescoDocumentClient.updateDocumentUuid(document.getId(), uuid);
	    	document.setId(uuid);
	    	document.setContent(null);
	        document.setFileName(fileName);
	        document.setFechaUltimaModificacion(new Date());
	    	documentClient.updateDocument(document, file);	
	    	return true;
	    }
    	file = null;
	    return false;		
	} 
	
	/**
	 * Añade un documento de cementerio a Alfresco (si no existe) y lo asocia al elemento en LocalGIS
	 * @param idMunicipality: Identificador de un municipio
	 * @param appName: Nombre de la aplicación de documentación LocalGIS
	 * @param document: Documento
	 * @param documentClient: Cliente del servlet de gestión documental LocaLGIS  
	 * @param key: Contenido del documento
	 * @throws Exception
	 * @return boolean: Resultado de la asociación
	 */	
	public boolean associateAlfrescoCementerioDocument(String idMunicipality, String appName, DocumentBean document, IDocumentClient documentClient, ElemCementerioBean elemCementerioBean) throws Exception{
		String parentPath = alfrescoDocumentClient.getAlfrescoProperty(AlfrescoManagerUtils.PROP_ROOT_PATH) + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(alfrescoDocumentClient.getMunicipalityName(idMunicipality)) + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(appName);
		String fileName = new File(document.getFileName()).getName();
		Node node = alfrescoDocumentClient.getNode(new AlfrescoKey(parentPath + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(fileName), AlfrescoKey.KEY_PATH));
		String uuid = null;
		File file = new File(document.getFileName());
		if(node==null)
			uuid = addAlfrescoDocument(parentPath, file);
		else
			uuid = node.getReference().getUuid();				
		
		if(uuid != null){
			document.setId(uuid);			
		    document.setContent(null);
		    document.setFileName(file.getAbsolutePath());
		    document.setFechaEntradaSistema(new Date());
		    document = ((CementerioDocumentClient)documentClient).attachDocument(elemCementerioBean, document);
			return true;
		}	
		file = null;
		return false;
	} 
	
	/**
	 * Actualiza (versiona) un documento de cemenerio en Alfresco (si existe, si no lo añade) y lo vuelve a asociar al elemento de LocalGIS
	 * @param idMunicipality: Identificador de un municipio
	 * @param appName: Nombre de la aplicación de documentación LocalGIS
	 * @param document: Documento
	 * @param documentClient: Cliente del servlet de gestión documental LocaLGIS  
	 * @throws Exception
	 * @return boolean: Resultado de la actualización de un documento asociado
	 */	
	public boolean associateAlfrescoCementerioDocument(String idMunicipality, String appName, String fileName, DocumentBean document, IDocumentClient documentClient) throws Exception{
		String parentPath = alfrescoDocumentClient.getAlfrescoProperty(AlfrescoManagerUtils.PROP_ROOT_PATH) + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(alfrescoDocumentClient.getMunicipalityName(idMunicipality)) + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(appName);
		Node node = alfrescoDocumentClient.getNode(new AlfrescoKey(parentPath + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(fileName), AlfrescoKey.KEY_PATH));
		String uuid = null;
		File file = new File(document.getFileName());
		if(node==null)
			uuid = addAlfrescoDocument(parentPath, file, fileName);
		else
			uuid = updateLocalGISDocument(parentPath, node.getReference().getUuid(), file);
		
		if(uuid != null){
			if(!document.getId().equals(uuid))
				alfrescoDocumentClient.updateDocumentUuid(document.getId(), uuid);
	    	document.setId(uuid);
	    	document.setContent(null);
	        document.setFileName(fileName);
	        document.setFechaUltimaModificacion(new Date());
	    	documentClient.updateDocument(document, file);	
	    	return true;
	    }
    	file = null;
	    return false;		
	} 
	
	/**
	 * Añade un documento a Alfresco
	 * @param parentPath: Ruta del directorio padre
	 * @param file: Documento
	 * @param fileName: Nombre del documento
	 * @throws Exception
	 * @return String: Identificador unívoco del documento de Alfresco
	 */	
	public String addAlfrescoDocument(String parentPath, File file, String fileName) throws Exception{
		Node node = alfrescoDocumentClient.addFileFromParent(new AlfrescoKey(parentPath, AlfrescoKey.KEY_PATH), file, fileName);
		if(node != null)
			return node.getReference().getUuid();
		return null;		
	} 
	
	/**
	 * Añade un documento a Alfresco
	 * @param parentPath: Ruta del directorio padre
	 * @param file: Documento
	 * @throws Exception
	 * @return String: Identificador unívoco del documento de Alfresco
	 */	
	public String addAlfrescoDocument(String parentPath, File file) throws Exception{
		return addAlfrescoDocument(parentPath, file, file.getName());
	} 
	
	/**
	 * Actualiza (versiona) un documento de Alfresco
	 * @param parentPath: Ruta del directorio padre
	 * @param uuid: Identificador unívoco de un nodo de Alfresco
	 * @param file: Documento
	 * @throws Exception
	 * @return String: Identificador unívoco del documento de Alfresco (tiene que ser el mismo que el parámetro inicial)
	 */	
	public String updateLocalGISDocument(String parentPath, String uuid, File file) throws IOException{
		RandomAccessFile f = new RandomAccessFile(file.getAbsolutePath(), "r");
		byte[] content = new byte[(int)f.length()];
		f.read(content);
		try {
			Node node = alfrescoDocumentClient.addVersion(new AlfrescoKey(uuid,AlfrescoKey.KEY_UUID), content, "Actualización LocalGIS - (" + (new SimpleDateFormat("dd/MM/yyyy hh:mm")).format(new Date()) + ")");
			if(node != null)
				return node.getReference().getUuid();
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	} 
	
	/**
     * Cambia la contraseña de un usuario
     * @param userName: Nombre del usuario
     * @param newPassword: Nueva contraseña del usuario
	 * @throws Exception
	 * @return boolean: Resultado de la actualización de la contraseña de usuario
	 */	
	public boolean updateUserPassword(String userName, String newPassword) throws Exception{
		return alfrescoDocumentClient.setPassword(userName, newPassword);
	} 
	
	/**
	 * Mueve un nodo de Alfresco a un nuevo directorio
	 * @param uuid: Identificador unívoco de un nodo de Alfresco
	 * @param parentPath: Ruta del directorio padre nuevo
	 * @throws Exception
	 * @return boolean: Resultado del movimiento del nodo de Alfresco
	 */	
	public boolean moveNode(String newParentPath, String uuid) throws Exception{		
		return alfrescoDocumentClient.moveNode(new AlfrescoKey(newParentPath, AlfrescoKey.KEY_PATH), new AlfrescoKey(uuid, AlfrescoKey.KEY_UUID));		
	} 

	/**
	 * Descarga en local un documento de Alfresco
	 * @param uuid: Identificador unívoco de un nodo de Alfresco
	 * @param localFilePath: Ruta local donde se descargará el documento
	 * @throws Exception
	 * @return boolean: Resultado de la descarga del documento
	 */	
	public boolean downloadAssociateDocument(String uuid, String localFilePath) throws Exception{		
		File file = new File(localFilePath);
		String filePath = file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf(File.separator));
		return alfrescoDocumentClient.downloadFile(new AlfrescoKey(uuid, AlfrescoKey.KEY_UUID), filePath, new File(localFilePath).getName());		
	} 
	
}
