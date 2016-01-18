package com.geopista.client.alfresco.utils.interfaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.alfresco.webservice.types.Node;

import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.DocumentClient;
import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 */
public interface LocalgisIntegrationManager {
	
	/**
	 * Metodos abstractos
	 */
	public abstract boolean associateAlfrescoDocument(String idMunicipality, String appName, DocumentBean document, DocumentClient documentClient, Object [] features) throws Exception;
	public abstract boolean associateAlfrescoDocument(String idMunicipality, String appName, String fileName, DocumentBean document, DocumentClient documentClient) throws Exception;
	public abstract boolean associateAlfrescoInventarioDocument(String idMunicipality, String appName, DocumentBean document, DocumentClient documentClient, Object key) throws Exception;
	public abstract boolean associateAlfrescoInventarioDocument(String idMunicipality, String appName, String fileName, DocumentBean document, DocumentClient documentClient) throws Exception;
	public abstract String addAlfrescoDocument(String parentPath, File file, String fileName) throws Exception;
	public abstract String addAlfrescoDocument(String parentPath, File file) throws Exception;
	public abstract String updateLocalGISDocument(String parentPath, String uuid, File file) throws IOException;
	public abstract boolean updateUserPassword(String userName, String newPassword) throws Exception;	
	public abstract boolean downloadAssociateDocument(String uuid, String localFilePath) throws Exception;
	
}
