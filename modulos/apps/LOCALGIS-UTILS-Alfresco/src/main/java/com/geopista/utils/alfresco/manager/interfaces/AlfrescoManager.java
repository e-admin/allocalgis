/**
 * AlfrescoManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.utils.alfresco.manager.interfaces;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.io.FileInputStream;
import java.util.ArrayList;

import javax.xml.rpc.ServiceException;

import org.alfresco.webservice.accesscontrol.GetPermissionsResult;
import org.alfresco.webservice.administration.UserDetails;
import org.alfresco.webservice.content.ContentFault;
import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.types.Version;

import com.geopista.utils.alfresco.beans.DocumentBean;
import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 */
public interface AlfrescoManager {
	
	/**
	 * Metodos abstractos
	 */
	public abstract void setAdminCredential();
	public abstract void setUserCredential(String user, String pass);
	public abstract boolean existsNode(AlfrescoKey key) throws RemoteException, ServiceException;
	public abstract boolean existsNodeFromName(AlfrescoKey parentKey, String name) throws RemoteException, ServiceException;
	public abstract Node [] getAllNodes(AlfrescoKey key) throws RepositoryFault, RemoteException, ServiceException;
	public abstract Node getNode(AlfrescoKey key) throws RemoteException, ServiceException;
	public abstract Node getParentNode(AlfrescoKey key) throws RemoteException, ServiceException;
	public abstract ArrayList<Node> getParentNodes(AlfrescoKey key) throws RemoteException, ServiceException;
	public abstract QueryResult getChildNodes(AlfrescoKey key) throws RemoteException, ServiceException;
	public abstract Version [] getVersions(AlfrescoKey key) throws RemoteException, ServiceException;
	public abstract Version getVersionsFromIdVersion(AlfrescoKey key, String idVersion) throws RemoteException, ServiceException;
	public abstract Node addVersion(AlfrescoKey key, byte [] content, String comments) throws RemoteException, ServiceException;
	public abstract Node addDirectoryFromParent(AlfrescoKey parentKey, String directoryName) throws RepositoryFault, RemoteException, ServiceException;
	public abstract Node addFileFromParent(AlfrescoKey parentKey, DocumentBean file, String fileName) throws RepositoryFault, ContentFault, RemoteException, IOException, ServiceException;
	public abstract Node addFileFromParent(AlfrescoKey parentKey, File file, String fileName) throws RepositoryFault, ContentFault, RemoteException, IOException, ServiceException;
	public abstract boolean moveNode(AlfrescoKey newParentKey, AlfrescoKey key) throws RepositoryFault, RemoteException, ServiceException;
	public abstract boolean renameNode(AlfrescoKey key, String newName) throws RepositoryFault, RemoteException, ServiceException;
	public abstract boolean removeNode(AlfrescoKey key) throws RepositoryFault, RemoteException, ServiceException;
	public abstract boolean downloadFile(AlfrescoKey key, String localPath) throws ContentFault, RemoteException, ServiceException, IOException;
	public abstract UserDetails addUser(String userName, String password) throws RemoteException, ServiceException;
	public abstract UserDetails addUser(String userName, String password, NamedValue [] properties) throws RemoteException, ServiceException;
	public abstract UserDetails getUser(String userName) throws RemoteException, ServiceException;
	public abstract UserDetails setUser(String userName, NamedValue [] properties) throws RemoteException, ServiceException;
	public abstract boolean setPassword(String userName, String newPassword) throws RemoteException, ServiceException;
	public abstract boolean deleteUser(String userName) throws RemoteException, ServiceException;
	public abstract boolean addGroup(String groupName) throws RemoteException, ServiceException;
	public abstract boolean addUserToGroup(String groupName, String userName) throws RemoteException, ServiceException;
	public abstract boolean addAllAccessToUser(AlfrescoKey key, String userName) throws RemoteException, ServiceException;
	public abstract boolean addAccessToUser(AlfrescoKey key, String userName, String permission) throws RemoteException, ServiceException;
	public abstract boolean addAllAccessToGroup(AlfrescoKey key, String groupName) throws RemoteException, ServiceException;
	public abstract boolean addAccessToGroup(AlfrescoKey key, String groupName, String permission) throws RemoteException, ServiceException;
	public boolean setInheritPermission(AlfrescoKey key, boolean inheritPermission) throws RemoteException, ServiceException;
	public abstract GetPermissionsResult [] getPermissions(AlfrescoKey key) throws RemoteException, ServiceException;

}
