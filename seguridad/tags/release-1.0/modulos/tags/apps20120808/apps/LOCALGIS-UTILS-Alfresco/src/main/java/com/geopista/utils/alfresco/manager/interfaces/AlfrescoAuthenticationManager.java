package com.geopista.utils.alfresco.manager.interfaces;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.alfresco.webservice.util.AuthenticationDetails;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.apache.axis.EngineConfiguration;

import com.geopista.utils.alfresco.manager.beans.AlfrescoCredential;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 */
public interface AlfrescoAuthenticationManager {
	
	/**
	 * Metodos abstractos
	 */
	public abstract void login(AlfrescoCredential alfrescoCedential) throws RemoteException, ServiceException;
	public abstract void logout();	
	public abstract String getTicket();
	public abstract AuthenticationDetails getAuthenticationDetails();
	public abstract EngineConfiguration getEngineConfiguration();	

}
