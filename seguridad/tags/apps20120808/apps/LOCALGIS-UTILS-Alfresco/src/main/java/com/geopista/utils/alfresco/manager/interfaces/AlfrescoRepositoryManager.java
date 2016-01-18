package com.geopista.utils.alfresco.manager.interfaces;

import java.rmi.RemoteException;

import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.UpdateResult;
import org.alfresco.webservice.types.CML;
import org.alfresco.webservice.types.CMLAddAspect;
import org.alfresco.webservice.types.CMLCreate;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.types.ParentReference;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.Utils;
import org.alfresco.webservice.util.WebServiceFactory;

import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;

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
	public abstract QueryResult getParentNode(AlfrescoKey key) throws RepositoryFault, RemoteException;
	public abstract Node addFromParent(AlfrescoKey parentKey, String childName, String type) throws RepositoryFault, RemoteException;
	public abstract Node addDirectoryFromParent(AlfrescoKey parentKey, String directoryName) throws RepositoryFault, RemoteException;
	public abstract Node addFileFromParent(AlfrescoKey parentKey, String directoryName) throws RepositoryFault, RemoteException;
	public abstract void setVersionable(AlfrescoKey key) throws RepositoryFault, RemoteException;
	public abstract boolean moveNode(AlfrescoKey newParentKey, AlfrescoKey key) throws RepositoryFault, RemoteException;
	public abstract boolean renameNode(AlfrescoKey key, String newName) throws RepositoryFault, RemoteException;
	
}

