/**
 * AlfrescoManagerImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.utils.alfresco.manager.implementations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.xml.rpc.ServiceException;

import org.alfresco.webservice.accesscontrol.GetPermissionsResult;
import org.alfresco.webservice.administration.UserDetails;
import org.alfresco.webservice.content.Content;
import org.alfresco.webservice.content.ContentFault;
import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.types.ContentFormat;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.types.Version;
import org.alfresco.webservice.util.ContentUtils;


import com.geopista.utils.alfresco.beans.DocumentBean;
import com.geopista.utils.alfresco.manager.beans.AlfrescoCredential;
import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;
import com.geopista.utils.alfresco.manager.interfaces.AlfrescoManager;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 * @ClassComments Clase que gestiona las operaciones solicitadas a los gestores de Alfresco
 */
public class AlfrescoManagerImpl implements AlfrescoManager {

	/**
     * Variables
     */
	private static AlfrescoManagerImpl instance = new AlfrescoManagerImpl();
	private AlfrescoCredential alfrescoCredential = null;  	
	
	private org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(AlfrescoManagerImpl.class);
	
	/**
     * Constructor
     */
	protected AlfrescoManagerImpl() {
		setAccessControlCredential();
	}
	
	/**
     * Solicita la instancia única de la clase (Singleton)
     * @return AlfrescoManagerImpl
     */
	public static AlfrescoManagerImpl getInstance(){
		return instance;
	}
	
	/**
	 * Asigna las credenciales de usuario administrador 
	 */
	private void setAccessControlCredential() {		
		String url = AlfrescoManagerUtils.getUrlProperty();			
		alfrescoCredential = new AlfrescoCredential(url);
		setAdminCredential();
	}
	
	/**
	 * Asigna las credenciales de un administrador
	 */
	public void setAdminCredential(){	
		String user = AlfrescoManagerUtils.getAdminNameProperty();
		String pass = AlfrescoManagerUtils.getAdminPassProperty();	
		alfrescoCredential.setUser(user);
		alfrescoCredential.setPass(pass);
	}
	
	/**
	 * Asigna las credenciales de un usuario
	 * @param user: Usuario
	 * @param pass: Contraseña
	 */
	public void setUserCredential(String user, String pass){
		alfrescoCredential.setUser(user);
		alfrescoCredential.setPass(pass);
	}

	/**
	 * Comprueba la existencia de un nodo
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @throws RemoteException
	 * @throws ServiceException
	 * @return boolean: Existencia del nodo
	 */
	public boolean existsNode(AlfrescoKey key) throws RemoteException, ServiceException {
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);
			if(AlfrescoRepositoryManagerImpl.getInstance().getNode(key) != null)
				return true;		    
		}
		catch(RepositoryFault e){
			logger.error(e);
		}
		finally{
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}	
		return false;
	}
	
	/**
	 * Comprueba la existencia de un nodo por el nombre
	 * @param parentKey: Clave unívoca del directorio padre
	 * @param name: Nombre del nodo hijo
	 * @throws RemoteException
	 * @throws ServiceException
	 * @return boolean: Existencia del nodo por el nombre
	 */
	public boolean existsNodeFromName(AlfrescoKey parentKey, String name) throws RemoteException, ServiceException {
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);
			return AlfrescoRepositoryManagerImpl.getInstance().existsNodeFromName(parentKey, name);		    
		}
		catch(RepositoryFault e){
			logger.error(e);
		}
		finally{
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}	
		return false;
	}
		
	/**
	 * Solicita un array con los nodos referenciados por la clave unívoca
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @throws RepositoryFault
	 * @throws RemoteException
	 * @throws ServiceException
	 * @return Node []: Array de nodos de alfresco
	 */
	public Node [] getAllNodes(AlfrescoKey key) throws RepositoryFault, RemoteException, ServiceException{
		try{
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);
			return AlfrescoRepositoryManagerImpl.getInstance().getAllNodes(key);
		}
		finally{
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}
	
	/**
	 * Solicita el nodo referenciado por la clave unívoca
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @throws RemoteException
	 * @throws ServiceException
	 * @return Node: Nodo de alfresco
	 */
	public Node getNode(AlfrescoKey key) throws RemoteException, ServiceException{
		try{
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);			
			return AlfrescoRepositoryManagerImpl.getInstance().getNode(key);
		}
		finally{
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}	
	
	/**
	 * Solicita el nodo padre del nodo referenciado por la clave unívoca
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @throws RemoteException
	 * @throws ServiceException
	 * @return Node: Nodo de alfresco
	 */
	public Node getParentNode(AlfrescoKey key) throws RemoteException, ServiceException{
		try{
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);			
			return AlfrescoRepositoryManagerImpl.getInstance().getParentNode(key);
		}
		finally{
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}	
	
	/**
	 * Solicita los nodos padre del nodo referenciado por la clave unívoca
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @throws RemoteException
	 * @throws ServiceException
	 * @return Node: Nodo de alfresco
	 */
	public ArrayList<Node> getParentNodes(AlfrescoKey key) throws RemoteException, ServiceException{
		try{
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);			
			return AlfrescoRepositoryManagerImpl.getInstance().getParentNodes(key);
		}
		finally{
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}	
	
	/**
	 * Solicita un resultado de consulta con los nodos hijos de un directorio
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @throws RemoteException
	 * @throws ServiceException
	 * @return QueryResult: QueryResult con los nodos hijos de un directorio
	 */
	public QueryResult getChildNodes(AlfrescoKey key) throws RemoteException, ServiceException {
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);
			return AlfrescoRepositoryManagerImpl.getInstance().getChildNodes(key);
		}
        finally{
        	//AlfrescoAuthenticationManagerImpl.getInstance().logout();
        }
	}
	
	/**
	 * Solicita un array con las versiones de un documento de Alfresco
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @throws RemoteException
	 * @throws ServiceException
	 * @return Version []: Array de versiones de un documento
	 */
	public Version [] getVersions(AlfrescoKey key) throws RemoteException, ServiceException{			
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);				
			return AlfrescoAuthoringManagerImpl.getInstance().getAllVersions(key);
		} 
	    finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}	

	/**
	 * Solicita una versión específica de un documento
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @param idVersion: Identificador de versión
	 * @throws RemoteException
	 * @throws ServiceException
	 * @return Version: Versión de un documento
	 */
	public Version getVersionsFromIdVersion(AlfrescoKey key, String idVersion) throws RemoteException, ServiceException{
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);				
			return AlfrescoAuthoringManagerImpl.getInstance().getVersionsFromIdVersion(key, idVersion);
		} 
	    finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}		
	}	
	
	/**
	 * Crea una versión de un documento
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @param content: Array de bytes con el contenido del documento
	 * @param comments: Comentarios
	 * @throws RemoteException
	 * @throws ServiceException
	 * @return Node: Nodo que corresponde a la nueva versión del documento
	 */
	public Node addVersion(AlfrescoKey key, byte [] content, String comments) throws RemoteException, ServiceException{
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);
			if(AlfrescoRepositoryManagerImpl.getInstance().getNode(key)!=null){
				String uuid = AlfrescoAuthoringManagerImpl.getInstance().checkOut(key);
				if(uuid!=null){
					AlfrescoKey versionKey = new AlfrescoKey(uuid, AlfrescoKey.KEY_UUID);
					AlfrescoContentManagerImpl.getInstance().addContentToFile(versionKey, content, "UTF-8");
					AlfrescoAuthoringManagerImpl.getInstance().checkIn(versionKey, comments);	
					return getNode(key);
				}
			}
			return null;	
		} 
	    finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}	
	}	
		
	/**
	 * Añade un directorio a un nodo padre
	 * @param parentKey: Clave unívoca de un nodo padre de Alfresco
	 * @param directoryName: Nombre del directorio hijo a crear
	 * @throws RepositoryFault
	 * @throws RemoteException
	 * @throws ServiceException
	 * @return Node: Directorio añadido
	 */
	public Node addDirectoryFromParent(AlfrescoKey parentKey, String directoryName) throws RepositoryFault, RemoteException, ServiceException{			
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);				
			return AlfrescoRepositoryManagerImpl.getInstance().addDirectoryFromParent(parentKey, directoryName);
		} 
	    finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}
	
	/**
	 * Añade un documento a un nodo padre
	 * @param parentKey: Clave unívoca de un nodo padre de Alfresco
	 * @param file: Documento a añadir
	 * @param fileName: Nombre del documento
	 * @throws RepositoryFault
	 * @throws ContentFault
	 * @throws RemoteException
	 * @throws IOException
	 * @throws ServiceException
	 * @return Node: Documento añadido
	 */
	public Node addFileFromParent(AlfrescoKey parentKey, DocumentBean file, String fileName) throws RepositoryFault, ContentFault, RemoteException, IOException, ServiceException{			
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);
			if(!AlfrescoRepositoryManagerImpl.getInstance().existsNodeFromName(parentKey, fileName)){
				Node node = AlfrescoRepositoryManagerImpl.getInstance().addFileFromParent(parentKey, fileName);
				AlfrescoContentManagerImpl.getInstance().addContentToFile(node.getReference(), file.getContent(), new ContentFormat(file.getMimeType(), "UTF-8"));					
				AlfrescoRepositoryManagerImpl.getInstance().setVersionable(new AlfrescoKey(node.getReference().getUuid(), AlfrescoKey.KEY_UUID));		
				return node;	
			}
			return null;
		} 
		finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}
	
		/**
	 * Añade un documento a un nodo padre
	 * @param parentKey: Clave unívoca de un nodo padre de Alfresco
	 * @param file: Documento a añadir
	 * @param fileName: Nombre del documento
	 * @throws RepositoryFault
	 * @throws ContentFault
	 * @throws RemoteException
	 * @throws IOException
	 * @throws ServiceException
	 * @return Node: Documento añadido
	 */
	public Node addFileFromParent(AlfrescoKey parentKey, File file, String fileName) throws RepositoryFault, ContentFault, RemoteException, IOException, ServiceException{			
		try {
		/*
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);
			if(!AlfrescoRepositoryManagerImpl.getInstance().existsNodeFromName(parentKey, fileName)){
				Node node = AlfrescoRepositoryManagerImpl.getInstance().addFileFromParent(parentKey, fileName);
				byte[] contentString = IOUtils.toByteArray(file);
				String mimeType = URLConnection.guessContentTypeFromStream(file);
				AlfrescoContentManagerImpl.getInstance().addContentToFile(node.getReference(), contentString, new ContentFormat(mimeType, "UTF-8"));					
				AlfrescoRepositoryManagerImpl.getInstance().setVersionable(new AlfrescoKey(node.getReference().getUuid(), AlfrescoKey.KEY_UUID));		
				return node;	
			}
			*/
			return null;
		} 
		finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}
	
	/**
	 * Mueve un nodo de Alfresco a un nuevo directorio
	 * @param newParentKey: Clave unívoca del nuevo directorio padre
	 * @param key: Clave unívoca del nodo de Alfresco a mover
	 * @throws RepositoryFault
	 * @throws RemoteException
	 * @throws ServiceException
	 * @return boolean: Resultado del movimiento del nodo Alfresco
	 */
	public boolean moveNode(AlfrescoKey newParentKey, AlfrescoKey key) throws RepositoryFault, RemoteException, ServiceException{			
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);				
			return AlfrescoRepositoryManagerImpl.getInstance().moveNode(newParentKey, key);
		} 
	    finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}	
	
	/**
	 * Renombra un nodo
	 * @param key: Clave unívoca del nodo
	 * @param newName: Nuevo nombre del nodo de Alfresco
	 * @throws RepositoryFault
	 * @throws RemoteException
	 * @throws ServiceException
	 * @return boolean: Resultado del renombrado del nodo Alfresco
	 */
	public boolean renameNode(AlfrescoKey key, String newName) throws RepositoryFault, RemoteException, ServiceException{			
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);				
			return AlfrescoRepositoryManagerImpl.getInstance().renameNode(key, newName);
		} 
	    finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}	
	
	/**
	 * Elimina un nodo
	 * @param key: Clave unívoca del nodo
	 * @throws RepositoryFault
	 * @throws RemoteException
	 * @throws ServiceException
	 * @return boolean: Resultado de la eliminación del nodo Alfresco
	 */
	public boolean removeNode(AlfrescoKey key) throws RepositoryFault, RemoteException, ServiceException{			
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);				
			return AlfrescoRepositoryManagerImpl.getInstance().removeNode(key);
		} 
	    finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}	
	
	/**
	 * Descarga un documento de Alfresco
	 * @param key: Clave unívoca de un nodo padre de Alfresco
	 * @param localPath: Ruta local de descarga
	 * @throws ServiceException
	 * @throws IOException
	 * @return bolean: Resultado de la descarga
	 */
	public boolean downloadFile(AlfrescoKey key, String localPath) throws ServiceException, IOException{
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);	
			Content content = AlfrescoContentManagerImpl.getInstance().getFileContent(key);				
			File file = new File(localPath);
			file.getParentFile().mkdirs();
			file.createNewFile();	
			FileOutputStream fos = new FileOutputStream(file);
			ContentUtils.copy(ContentUtils.getContentAsInputStream(content), fos);		
			return true;
		} 
		finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}
		
	/**
	 * Crea un usuario
	 * @param userName: Nombre del usuario a crear
	 * @param password: Contraseña del usuario a crear
	 * @throws RemoteException
	 * @throws ServiceException
	 * @return UserDetails: Detalles del usuario creado
	 */
	public UserDetails addUser(String userName, String password) throws RemoteException, ServiceException{
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);	
			if(AlfrescoAdministrationManagerImpl.getInstance().existsUser(userName))
				return AlfrescoAdministrationManagerImpl.getInstance().getUser(userName);	
			else
				return AlfrescoAdministrationManagerImpl.getInstance().addUser(userName, password);
		}
		finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}
	
	/**
	 * Crea un usuario
	 * @param userName: Nombre del usuario a crear
	 * @param password: Contraseña del usuario a crear
	 * @param properties: Array de propiedades del usuario a crear
	 * @throws RemoteException
	 * @throws ServiceException
	 * @return UserDetails: Detalles del usuario creado
	 */
	public UserDetails addUser(String userName, String password, NamedValue [] properties) throws RemoteException, ServiceException{
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);	
			if(AlfrescoAdministrationManagerImpl.getInstance().existsUser(userName))			
				return AlfrescoAdministrationManagerImpl.getInstance().getUser(userName);	
			else
				return AlfrescoAdministrationManagerImpl.getInstance().addUser(userName, password, properties);
		}
		finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}
	
	/**
	 * Solicita los detalles del usuario por su nombre
	 * @param userName: Nombre del usuario
	 * @throws RemoteException
	 * @throws ServiceException
	 * @return UserDetails: Detalles del usuario
	 */
	public UserDetails getUser(String userName) throws RemoteException, ServiceException{
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);	
			return AlfrescoAdministrationManagerImpl.getInstance().getUser(userName);		
		}
		finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}
	
	/**
	 * Actualiza las propiedades de un usuario
	 * @param userName: Nombre del usuario
	 * @param properties: Array de propiedades a actualizar
	 * @throws RemoteException
	 * @throws ServiceException
	 * @return UserDetails: Detalles del usuario
	 */
	public UserDetails setUser(String userName, NamedValue [] properties) throws RemoteException, ServiceException{
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);	
			return AlfrescoAdministrationManagerImpl.getInstance().setUser(userName, properties);		
		}
		finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}
	
	/**
	 * Cambia la contraseña de un usuario
	 * @param userName: Nombre del usuario
	 * @param newPassword: Nueva contraseña del usuario
	 * @throws RemoteException
	 * @throws ServiceException
	 * @return boolean: Resultado del cambio de contraseña
	 */
	public boolean setPassword(String userName, String newPassword) throws RemoteException, ServiceException{
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);	
			return AlfrescoAdministrationManagerImpl.getInstance().setPassword(userName, newPassword);	
		}
		finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}
	
	/**
	 * Borra usuario
	 * @param userNames: Nombre del usuario a borrar
	 * @throws RemoteException
	 * @throws ServiceException
	 * @return boolean: Resultado del borrado del usuario
	 */
	public boolean deleteUser(String userName) throws RemoteException, ServiceException{
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);	
			return AlfrescoAdministrationManagerImpl.getInstance().deleteUser(userName);	
		}
		finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}
	
    /**
     * Crea un grupo
     * @param groupName: Nombre del grupo a crear
     * @throws RemoteException
     * @throws ServiceException
     * @return boolean: Resultado de la creación del grupo
     */
	public boolean addGroup(String groupName) throws RemoteException, ServiceException{
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);	
			if(!AlfrescoAccessControlManagerImpl.getInstance().existsGroup(groupName))
				return AlfrescoAccessControlManagerImpl.getInstance().addGroup(groupName);
			return true;
		}
		finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}	
	
    /**
     * Añade un usuario a un grupo
     * @param groupName: Nombre del grupo
     * @param userName: Nombre del usuario a añadir
     * @throws RemoteException
     * @throws ServiceException
     * @return boolean: Resultado de añadir un usuario a un grupo
     */
	public boolean addUserToGroup(String groupName, String userName) throws RemoteException, ServiceException{
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);	
			//if(!AlfrescoAccessControlManagerImpl.getInstance().existsUserInGroup(groupName, userName))
			AlfrescoAccessControlManagerImpl.getInstance().addUserToGroup(groupName, userName);	
			return true;
		}
		finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}
	
	   /**
     * Añade todos los permisos a un usuario
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param groupName: Nombre del usuario
     * @throws RemoteException
     * @throws ServiceException
     * @return boolean: Resultado de añadir todos los permisos a un usuario
     */
	public boolean addAllAccessToUser(AlfrescoKey key, String userName) throws RemoteException, ServiceException{
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);
			if(!AlfrescoAccessControlManagerImpl.getInstance().hasAllPermissions(key, userName))
				return AlfrescoAccessControlManagerImpl.getInstance().addAllAccessToUser(key, userName);
			return AlfrescoAccessControlManagerImpl.getInstance().hasAllPermissions(key, userName);
			//AlfrescoAccessControlManagerImpl.getInstance().addAllAccessToUser(key, groupName);
			//return true;
		}
		finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}	
	
    /**
     * Añade un permiso a un usuario
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param groupName: Nombre del usuario
     * @param permission: Tipo de permiso a añadir
     * @throws RemoteException
     * @throws ServiceException
     * @return boolean: Resultado de añadir el permiso al usuario
     */
	public boolean addAccessToUser(AlfrescoKey key, String userName, String permission) throws RemoteException, ServiceException{
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);	
			if(!AlfrescoAccessControlManagerImpl.getInstance().hasPermission(key, userName, permission))				
				return AlfrescoAccessControlManagerImpl.getInstance().addAccessToUser(key, userName, permission);
			//return AlfrescoAccessControlManagerImpl.getInstance().hasPermission(key, userName, permission);
			return true;
		}
		finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}	
	
    /**
     * Añade todos los permisos a un grupo
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param groupName: Nombre del grupo
     * @throws RemoteException
     * @throws ServiceException
     * @return boolean: Resultado de añadir todos los permisos a un grupo
     */
	public boolean addAllAccessToGroup(AlfrescoKey key, String groupName) throws RemoteException, ServiceException{
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);
//			if(!AlfrescoAccessControlManagerImpl.getInstance().hasAllPermissions(key))
//				return AlfrescoAccessControlManagerImpl.getInstance().addAllAccessToGroup(key, groupName);
//			return AlfrescoAccessControlManagerImpl.getInstance().hasAllPermissions(key);
			AlfrescoAccessControlManagerImpl.getInstance().addAllAccessToGroup(key, groupName);
			return true;
		}
		finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}	
	
    /**
     * Añade un permiso a un grupo
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param groupName: Nombre del grupo
     * @param permission: Tipo de permiso a añadir
     * @throws RemoteException
     * @throws ServiceException
     * @return boolean: Resultado de añadir el permiso al grupo
     */
	public boolean addAccessToGroup(AlfrescoKey key, String groupName, String permission) throws RemoteException, ServiceException{
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);	
//			if(!AlfrescoAccessControlManagerImpl.getInstance().hasPermission(key, permission))				
//				return AlfrescoAccessControlManagerImpl.getInstance().addAccessToGroup(key, groupName, permission);
//			return AlfrescoAccessControlManagerImpl.getInstance().hasPermission(key, permission);
			AlfrescoAccessControlManagerImpl.getInstance().addAccessToGroup(key, groupName, permission);
			return true;
		}
		finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}	
	
    /**
     * Modifica el permiso de herencia de un nodo
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param inheritPermission: Valor de activación del permiso de herencia
     * @throws RemoteException
     * @throws ServiceException      
     * @return boolean: Resultado de la modificación de la herencia de un nodo
     */
	public boolean setInheritPermission(AlfrescoKey key, boolean inheritPermission) throws RemoteException, ServiceException{
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);	
			return AlfrescoAccessControlManagerImpl.getInstance().setInheritPermission(key, inheritPermission);		
		}
		finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}
	
	
    /**
     * Solicita un array con los resultados de la existencia de los permisos presentados sobre el elemento solicitado
     * @param key: Objeto con la clave unívoca de referencia a un elemento de Alfresco
     * @param permissions: Array con nombres de permisos
     * @throws RemoteException
     * @throws ServiceException
     * @return HasPermissionsResult []: Array con los resultado de comprobación de acceso
     */
	public GetPermissionsResult [] getPermissions(AlfrescoKey key) throws RemoteException, ServiceException{
		try {
			AlfrescoAuthenticationManagerImpl.getInstance().login(alfrescoCredential);	
			return AlfrescoAccessControlManagerImpl.getInstance().getPermissions(key);		
		}
		finally {				
			//AlfrescoAuthenticationManagerImpl.getInstance().logout();
		}
	}
			
}
