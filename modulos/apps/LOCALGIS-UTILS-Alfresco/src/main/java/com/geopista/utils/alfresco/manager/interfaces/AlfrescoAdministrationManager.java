/**
 * AlfrescoAdministrationManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.utils.alfresco.manager.interfaces;

import java.rmi.RemoteException;

import org.alfresco.webservice.administration.AdministrationFault;
import org.alfresco.webservice.administration.NewUserDetails;
import org.alfresco.webservice.administration.UserDetails;
import org.alfresco.webservice.types.NamedValue;

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
