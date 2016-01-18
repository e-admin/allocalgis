/**
 * AlfrescoRepositoryManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.utils.alfresco.manager.interfaces;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.types.Node;

import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 */
public interface AlfrescoRepositoryManager {
	
	/**
	 * Metodos abstractos
	 */
	public abstract Node [] getAllNodes(AlfrescoKey key) throws RepositoryFault, RemoteException;
	public abstract Node getNode(AlfrescoKey key) throws RepositoryFault, RemoteException;
	public abstract boolean existsNodeFromName(AlfrescoKey parentKey, String name) throws RepositoryFault, RemoteException;
	public abstract QueryResult getChildNodes(AlfrescoKey key) throws RepositoryFault, RemoteException;
	public abstract QueryResult getAllParentNode(AlfrescoKey key) throws RepositoryFault, RemoteException;
	public abstract Node getParentNode(AlfrescoKey key) throws RepositoryFault, RemoteException;
	public abstract ArrayList<Node> getParentNodes(AlfrescoKey key) throws RepositoryFault, RemoteException;
	public abstract Node addFromParent(AlfrescoKey parentKey, String childName, String type) throws RepositoryFault, RemoteException;
	public abstract Node addDirectoryFromParent(AlfrescoKey parentKey, String directoryName) throws RepositoryFault, RemoteException;
	public abstract Node addFileFromParent(AlfrescoKey parentKey, String directoryName) throws RepositoryFault, RemoteException;
	public abstract void setVersionable(AlfrescoKey key) throws RepositoryFault, RemoteException;
	public abstract boolean moveNode(AlfrescoKey newParentKey, AlfrescoKey key) throws RepositoryFault, RemoteException;
	public abstract boolean renameNode(AlfrescoKey key, String newName) throws RepositoryFault, RemoteException;
	public abstract boolean removeNode(AlfrescoKey key) throws RepositoryFault, RemoteException;
	
}

