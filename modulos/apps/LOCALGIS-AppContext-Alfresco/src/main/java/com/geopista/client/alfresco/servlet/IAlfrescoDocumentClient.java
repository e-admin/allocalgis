/**
 * IAlfrescoDocumentClient.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.client.alfresco.servlet;

import java.io.File;
import java.util.Hashtable;

import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.alfresco.webservice.types.Node;

import com.geopista.utils.alfresco.beans.DocumentBean;
import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 */
public interface IAlfrescoDocumentClient {

	/**
	 * Constantes
	 */
    public static final String	MENSAJE_XML	= "mensajeXML";
     
    /**
     * Metodos abstractos
     */
    public abstract DefaultMutableTreeNode getTreeDirectories(DefaultMutableTreeNode rootNode) throws Exception;     
    public abstract DefaultTableModel getChildFiles(AlfrescoKey parentKey, Object [] elements) throws Exception;     
    public abstract Node initializeRelativeDirectoryPathAndAccess(String idMunicipality, String appName) throws Exception;
    public abstract Boolean setPassword(String userName, String newPassword) throws Exception;
    public abstract Boolean moveNode(AlfrescoKey newParentKey, AlfrescoKey key) throws Exception;
    public abstract Boolean renameNode(AlfrescoKey key, String newName) throws Exception;
    public abstract Boolean removeNode(AlfrescoKey key) throws Exception;
    public abstract Boolean updateDocumentUuid(String oldUuid, String newUuid) throws Exception;
//    public abstract Boolean addGroup(String groupName) throws Exception;     
//    public abstract Boolean addUserToGroup(String groupName, String userName) throws Exception;
//    public abstract Boolean addAccessToGroup(AlfrescoKey key, String groupName, String accessRole) throws Exception;
    public abstract DocumentBean returnFile(AlfrescoKey key) throws Exception;
    public abstract Node addVersion(AlfrescoKey key, byte[] content, String comments) throws Exception;
    public abstract Boolean downloadFile(AlfrescoKey key, String path, String name) throws Exception;
    public abstract Node addFileFromParent(AlfrescoKey key, File file, String fileName) throws Exception;         
    public abstract Node addDirectoryFromParent(AlfrescoKey key, String directoryName) throws Exception;
    public abstract Node getNode(AlfrescoKey key) throws Exception;
    public abstract Node getNode(int action, Hashtable<Integer, Object> params) throws Exception;
    public abstract String getMunicipalityName(String idMunicipality) throws Exception;
}
