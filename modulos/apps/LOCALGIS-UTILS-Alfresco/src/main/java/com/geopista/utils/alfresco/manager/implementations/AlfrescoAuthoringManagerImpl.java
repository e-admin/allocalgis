/**
 * AlfrescoAuthoringManagerImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.utils.alfresco.manager.implementations;

import java.rmi.RemoteException;

import org.alfresco.webservice.authoring.AuthoringFault;
import org.alfresco.webservice.authoring.CheckinResult;
import org.alfresco.webservice.authoring.CheckoutResult;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Version;
import org.alfresco.webservice.types.VersionHistory;
import org.alfresco.webservice.util.Utils;
import org.alfresco.webservice.util.WebServiceFactory;

import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;
import com.geopista.utils.alfresco.manager.interfaces.AlfrescoAuthoringManager;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 * @ClassComments Clase que gestiona las versiones de docuemntos en Alfresco (los métodos requieren autenticación)
 */
public class AlfrescoAuthoringManagerImpl implements AlfrescoAuthoringManager {
	
	/**
     * Variables
     */
	private static AlfrescoAuthoringManagerImpl instance = new AlfrescoAuthoringManagerImpl();
	   
	/**
     * Constructor
     */
	protected AlfrescoAuthoringManagerImpl() {
	}
	
	/**
     * Solicita la instancia única de la clase (Singleton)
     * @return AlfrescoAuthoringManagerImpl
     */
	public static AlfrescoAuthoringManagerImpl getInstance(){
		return instance;
	}
	
	/**
	 * Solicita un objeto histórico versión de un documento de Alfresco
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @throws AuthoringFault
	 * @throws RemoteException
	 * @return VersionHistory: Objeto histórico de versión de un documento
	 */
	public VersionHistory getVersionHistory(AlfrescoKey key) throws AuthoringFault, RemoteException{			
		return WebServiceFactory.getAuthoringService().getVersionHistory(AlfrescoManagerUtils.getReference(key));
	}
	
	/**
	 * Solicita un array con las versiones de un documento de Alfresco
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @throws AuthoringFault
	 * @throws RemoteException
	 * @return Version []: Array de versiones de un documento
	 */
	public Version [] getAllVersions(AlfrescoKey key) throws AuthoringFault, RemoteException{		
		VersionHistory versionHistory = getVersionHistory(key);
		if(versionHistory != null)
			return versionHistory.getVersions();
		return null;
	}
		
	/**
	 * Solicita una versión específica de un documento
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @param idVersion: Identificador de versión
	 * @throws AuthoringFault
	 * @throws RemoteException
	 * @return Version: Versión de un documento
	 */
	public Version getVersionsFromIdVersion(AlfrescoKey key, String idVersion) throws AuthoringFault, RemoteException{			
		Version[] versions = getAllVersions(key);
		if(versions != null){				
			for(Version nodeVersion : versions) {
				if(nodeVersion.getLabel().equals(idVersion)) {
					return nodeVersion;
				}
			}
		}		
		return null;
	}	
	
	/**
	 * Cambia un documento a modo edición y crea una copia de trabajo del mismo
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @throws AuthoringFault
	 * @throws RemoteException
	 * @return String: Uuid del nodo de la copia de trabajo
	 */
	public String checkOut(AlfrescoKey key) throws AuthoringFault, RemoteException{
		CheckoutResult checkOutResult = WebServiceFactory.getAuthoringService().checkout(AlfrescoManagerUtils.getPredicate(key), null);
		if(checkOutResult.getWorkingCopies()!= null && checkOutResult.getWorkingCopies().length>0)
			return (checkOutResult.getWorkingCopies()[0]).getUuid();	
		return null;	
	}	
	
	/**
	 * Sustituye o versiona (dependiendo si el documento es versionable) el documento incial en modo edición por la copia de trabajo
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @param comments: Comentarios
	 * @throws AuthoringFault
	 * @throws RemoteException
	 * @return CheckinResult: Resultado de la asignación
	 */
	public CheckinResult checkIn(AlfrescoKey key, String comments) throws AuthoringFault, RemoteException{
		return WebServiceFactory.getAuthoringService().checkin(AlfrescoManagerUtils.getPredicate(key), new NamedValue[]{Utils.createNamedValue("description", comments)}, false);
	}	
	
	//authoringService.cancelCheckout(new Predicate(new Reference[]{defaultRef},storeRef,null));
}
