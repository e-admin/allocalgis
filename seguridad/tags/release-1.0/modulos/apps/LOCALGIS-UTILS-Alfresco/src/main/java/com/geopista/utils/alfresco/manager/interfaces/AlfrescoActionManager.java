package com.geopista.utils.alfresco.manager.interfaces;

import java.rmi.RemoteException;

import org.alfresco.webservice.accesscontrol.ACE;
import org.alfresco.webservice.accesscontrol.ACL;
import org.alfresco.webservice.accesscontrol.AccessControlFault;
import org.alfresco.webservice.accesscontrol.AccessStatus;
import org.alfresco.webservice.accesscontrol.AuthorityFilter;
import org.alfresco.webservice.accesscontrol.GetPermissionsResult;
import org.alfresco.webservice.accesscontrol.HasPermissionsResult;
import org.alfresco.webservice.accesscontrol.NewAuthority;
import org.alfresco.webservice.accesscontrol.SiblingAuthorityFilter;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;

import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 */
public interface AlfrescoActionManager {
	
	/**
	 * Constantes
	 */
	public static final String RULETYPE_UPDATE = "UPDATE";
	public static final String RULETYPE_INBOUND = "INBOUND";
	public static final String RULETYPE_OUTBOUND = "OUTBOUND";
	
	/**
	 * Métodos abstractos
	 */
	
	//public HasPermissionsResult [] hasPermission(AlfrescoKey key, String [] permissions) throws AccessControlFault, RemoteException;
	
}
