package com.geopista.utils.alfresco.manager.interfaces;

import java.rmi.RemoteException;

import org.alfresco.webservice.administration.AdministrationFault;
import org.alfresco.webservice.administration.NewUserDetails;
import org.alfresco.webservice.administration.UserDetails;
import org.alfresco.webservice.types.NamedValue;

import com.geopista.utils.alfresco.manager.implementations.AlfrescoAccessControlManagerImpl;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 */
public interface AlfrescoAdministrationManager {	
	
	/**
	 * Metodos abstractos
	 */
	public abstract UserDetails [] addUsers(NewUserDetails [] users) throws AdministrationFault, RemoteException;
	public abstract UserDetails addUser(NewUserDetails user) throws AdministrationFault, RemoteException;
	public abstract UserDetails addUser(String userName, String password, NamedValue [] properties) throws AdministrationFault, RemoteException;
	public abstract UserDetails addUser(String userName, String password) throws AdministrationFault, RemoteException;
	public abstract boolean existsUser(String userName) throws AdministrationFault, RemoteException;
	public abstract UserDetails getUser(String userName) throws AdministrationFault, RemoteException;
	public abstract UserDetails [] setUsers(UserDetails [] users) throws AdministrationFault, RemoteException;
	public abstract UserDetails setUser(UserDetails user) throws AdministrationFault, RemoteException;
	public abstract UserDetails setUser(String userName, NamedValue [] properties) throws AdministrationFault, RemoteException;
	public abstract boolean setPassword(String userName, String newPassword) throws AdministrationFault, RemoteException;
	public abstract boolean deleteUsers(String [] userNames) throws AdministrationFault, RemoteException;
	public abstract boolean deleteUser(String userName) throws AdministrationFault, RemoteException;	

}
