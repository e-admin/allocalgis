package com.geopista.utils.alfresco.manager.interfaces;

import java.rmi.RemoteException;

import org.alfresco.webservice.authoring.AuthoringFault;
import org.alfresco.webservice.authoring.CheckinResult;
import org.alfresco.webservice.authoring.VersionResult;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Version;
import org.alfresco.webservice.types.VersionHistory;
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
public interface AlfrescoAuthoringManager {
	
	/**
	 * Metodos abstractos
	 */
	public abstract VersionHistory getVersionHistory(AlfrescoKey key) throws AuthoringFault, RemoteException;
	public abstract Version [] getAllVersions(AlfrescoKey key) throws AuthoringFault, RemoteException;
	public abstract Version getVersionsFromIdVersion(AlfrescoKey key, String idVersion) throws AuthoringFault, RemoteException;
	public abstract String checkOut(AlfrescoKey key) throws AuthoringFault, RemoteException;
	public abstract CheckinResult checkIn(AlfrescoKey key, String comments) throws AuthoringFault, RemoteException;	

}
