/**
 * AlfrescoContentManagerImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.utils.alfresco.manager.implementations;

import java.rmi.RemoteException;

import org.alfresco.webservice.content.Content;
import org.alfresco.webservice.content.ContentFault;
import org.alfresco.webservice.types.ContentFormat;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;

import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;
import com.geopista.utils.alfresco.manager.interfaces.AlfrescoContentManager;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 * @ClassComments Clase que gestiona los documentos contenidos en Alfresco (los métodos requieren autenticación)
 */
public class AlfrescoContentManagerImpl implements AlfrescoContentManager {
	
	/**
     * Variables
     */
	private static AlfrescoContentManagerImpl instance = new AlfrescoContentManagerImpl();
	   
	/**
     * Constructor
     */
	protected AlfrescoContentManagerImpl() {
	}
	
	/**
     * Solicita la instancia única de la clase (Singleton)
     * @return AlfrescoContentManagerImpl
     */
	public static AlfrescoContentManagerImpl getInstance(){
		return instance;
	}
	
	/**
	 * Añade contenido a un documento
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @param content: Array de bytes con el contenido del documento
	 * @param content: Formato del documento
	 * @throws ContentFault
	 * @throws RemoteException
	 * @return Content: Contenido
	 */
	public Content addContentToFile(AlfrescoKey key, byte [] content, String format) throws ContentFault, RemoteException{
		return addContentToFile(key, content, new ContentFormat(Constants.MIMETYPE_TEXT_PLAIN, format));
	}
	
	/**
	 * Añade contenido a un documento
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @param content: Array de bytes con el contenido del documento
	 * @param contentFormat: Formato de contenido
	 * @throws ContentFault
	 * @throws RemoteException
	 * @return Content: Contenido
	 */
	public Content addContentToFile(AlfrescoKey key, byte [] content, ContentFormat contentFormat) throws ContentFault, RemoteException{
		return addContentToFile(AlfrescoManagerUtils.getReference(key), content, contentFormat);
	}
	
	/**
	 * Añade contenido a un documento
	 * @param reference: Referencia del nodo de Alfresco
	 * @param content: Array de bytes con el contenido del documento
	 * @param contentFormat: Formato de contenido
	 * @throws ContentFault
	 * @throws RemoteException
	 * @return Content: Contenido del documento añadido
	 */
	public Content addContentToFile(Reference reference, byte [] content, ContentFormat contentFormat) throws ContentFault, RemoteException{
		return WebServiceFactory.getContentService().write(reference, Constants.PROP_CONTENT, content, contentFormat);
	}
	
	/**
	 * Solicita el contenido de un documento
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @throws ContentFault
	 * @throws RemoteException
	 * @return Content: Contenido
	 */
	public Content getFileContent(AlfrescoKey key) throws ContentFault, RemoteException{
		Content[] readResult = WebServiceFactory.getContentService().read(AlfrescoManagerUtils.getPredicate(key), Constants.PROP_CONTENT);
		if(readResult != null && readResult.length >0)
			return readResult[0];
		return null;
	}
	
}
