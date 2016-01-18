/**
 * AlfrescoAuthenticationManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.utils.alfresco.manager.interfaces;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.alfresco.webservice.util.AuthenticationDetails;
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
