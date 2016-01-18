/**
 * AlfrescoAuthenticationManagerImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.utils.alfresco.manager.implementations;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.alfresco.webservice.util.AuthenticationDetails;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.axis.EngineConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.utils.alfresco.manager.beans.AlfrescoCredential;
import com.geopista.utils.alfresco.manager.interfaces.AlfrescoAuthenticationManager;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 * @ClassComments Clase que gestiona la autenticación en Alfresco (los métodos requieren autenticación)
 */
public class AlfrescoAuthenticationManagerImpl implements AlfrescoAuthenticationManager {
	
	/**
     * Variables
     */
	private static AlfrescoAuthenticationManagerImpl instance = new AlfrescoAuthenticationManagerImpl();	
	private static Log logger = LogFactory.getLog(AlfrescoAuthenticationManagerImpl.class);   
	
	/**
     * Constructor
     */
	protected AlfrescoAuthenticationManagerImpl() {
	}
	
	/**
     * Solicita la instancia única de la clase (Singleton)
     * @return AlfrescoAuthenticationManagerImpl
     */
	public static AlfrescoAuthenticationManagerImpl getInstance(){
		return instance;
	}

    /**
     * Autentica un usuario en Alfresco remotamente
     * @param alfrescoCedential: Objeto con las credenciales de autenticación
     * @throws RemoteException
     * @throws ServiceException
     */
	public void login(AlfrescoCredential alfrescoCedential) throws RemoteException, ServiceException {
		if((AuthenticationUtils.getAuthenticationDetails() != null && !alfrescoCedential.getUser().equals(AuthenticationUtils.getAuthenticationDetails().getUserName())) || (AlfrescoManagerUtils.getAuthenticationDetails() != null && !alfrescoCedential.getUser().equals(AlfrescoManagerUtils.getAuthenticationDetails().getUserName()))){
			AuthenticationUtils.startSession(alfrescoCedential.getUser(), alfrescoCedential.getPass());	
			AlfrescoManagerUtils.setAuthenticationDetails(AuthenticationUtils.getAuthenticationDetails());
			logger.info("Session Started: \n	Server: " + alfrescoCedential.getUrl() + "\n	User: " + alfrescoCedential.getUser());
		}
		
		if(AuthenticationUtils.getAuthenticationDetails() == null){			
			WebServiceFactory.setEndpointAddress(alfrescoCedential.getUrl());
			if(AlfrescoManagerUtils.getAuthenticationDetails() == null){
				AuthenticationUtils.startSession(alfrescoCedential.getUser(), alfrescoCedential.getPass());	
				AlfrescoManagerUtils.setAuthenticationDetails(AuthenticationUtils.getAuthenticationDetails());
				logger.info("Session Started: \n	Server: " + alfrescoCedential.getUrl() + "\n	User: " + alfrescoCedential.getUser());
			}
			else{
				AuthenticationUtils.setAuthenticationDetails(AlfrescoManagerUtils.getAuthenticationDetails());
				logger.info("Session Had Already Been Started: \n	Server: " + alfrescoCedential.getUrl() + "\n	User: " + alfrescoCedential.getUser());
			}
		}
	}
	
    /**
     * Finaliza la sesión abierta
     */
	public void logout(){
		AuthenticationUtils.endSession();
		logger.info("Session Finished");
	}
	
    /**
     * Solicita el Ticket de la sesión
     * @return String
     */
	public String getTicket(){
		return AuthenticationUtils.getTicket();
	}
	
    /**
     * Solicita los detalles de la autenticación
     * @return AuthenticationDetails
     */
	public AuthenticationDetails getAuthenticationDetails(){
		return AuthenticationUtils.getAuthenticationDetails();
	}
	
    /**
     * Solicita la confuguración
     * @return EngineConfiguration
     */
	public EngineConfiguration getEngineConfiguration(){
		return AuthenticationUtils.getEngineConfiguration();
	}	

}
