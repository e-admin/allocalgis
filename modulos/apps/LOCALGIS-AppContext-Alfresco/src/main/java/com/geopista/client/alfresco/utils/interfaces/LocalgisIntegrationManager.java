/**
 * LocalgisIntegrationManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.client.alfresco.utils.interfaces;

import java.io.File;
import java.io.IOException;

import com.geopista.protocol.cementerios.ElemCementerioBean;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.IDocumentClient;

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
	public abstract boolean associateAlfrescoDocument(String idMunicipality, String appName, DocumentBean document, IDocumentClient documentClient, Object [] features) throws Exception;
	public abstract boolean associateAlfrescoDocument(String idMunicipality, String appName, String fileName, DocumentBean document, IDocumentClient documentClient) throws Exception;
	public abstract boolean associateAlfrescoInventarioDocument(String idMunicipality, String appName, DocumentBean document, IDocumentClient documentClient, Object key) throws Exception;
	public abstract boolean associateAlfrescoInventarioDocument(String idMunicipality, String appName, String fileName, DocumentBean document, IDocumentClient documentClient) throws Exception;
	public abstract boolean associateAlfrescoCementerioDocument(String idMunicipality, String appName, DocumentBean document, IDocumentClient documentClient, ElemCementerioBean elemCementerioBean) throws Exception;
	public abstract boolean associateAlfrescoCementerioDocument(String idMunicipality, String appName, String fileName, DocumentBean document, IDocumentClient documentClient) throws Exception;
	public abstract String addAlfrescoDocument(String parentPath, File file, String fileName) throws Exception;
	public abstract String addAlfrescoDocument(String parentPath, File file) throws Exception;
	public abstract String updateLocalGISDocument(String parentPath, String uuid, File file) throws IOException;
	public abstract boolean updateUserPassword(String userName, String newPassword) throws Exception;	
	public abstract boolean downloadAssociateDocument(String uuid, String localFilePath) throws Exception;
	
}
