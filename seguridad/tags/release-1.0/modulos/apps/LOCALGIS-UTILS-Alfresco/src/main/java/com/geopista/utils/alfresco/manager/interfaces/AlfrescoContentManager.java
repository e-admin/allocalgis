package com.geopista.utils.alfresco.manager.interfaces;

import java.rmi.RemoteException;

import org.alfresco.webservice.content.Content;
import org.alfresco.webservice.content.ContentFault;
import org.alfresco.webservice.types.ContentFormat;
import org.alfresco.webservice.types.Reference;

import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 */
public interface AlfrescoContentManager {
	
	/**
	 * Metodos abstractos
	 */
	public abstract Content addContentToFile(AlfrescoKey key, byte [] content, String format) throws ContentFault, RemoteException;
	public abstract Content addContentToFile(AlfrescoKey key, byte [] content, ContentFormat contentFormat) throws ContentFault, RemoteException;
	public abstract Content addContentToFile(Reference reference, byte [] content, ContentFormat contentFormat) throws ContentFault, RemoteException;
	public abstract Content getFileContent(AlfrescoKey key) throws ContentFault, RemoteException;

}
