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
		WebServiceFactory.setEndpointAddress(alfrescoCedential.getUrl());
		AuthenticationUtils.startSession(alfrescoCedential.getUser(), alfrescoCedential.getPass());	
		logger.info("Session Started: \n	Server: " + alfrescoCedential.getUrl() + "\n	User: " + alfrescoCedential.getUser());
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
