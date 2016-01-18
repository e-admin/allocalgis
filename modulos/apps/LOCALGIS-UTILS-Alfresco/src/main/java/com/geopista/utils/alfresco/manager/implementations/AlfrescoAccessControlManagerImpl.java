/**
 * AlfrescoAccessControlManagerImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.utils.alfresco.manager.implementations;

import java.rmi.RemoteException;

import org.alfresco.webservice.accesscontrol.ACE;
import org.alfresco.webservice.accesscontrol.ACL;
import org.alfresco.webservice.accesscontrol.AccessControlFault;
import org.alfresco.webservice.accesscontrol.AccessStatus;
import org.alfresco.webservice.accesscontrol.AuthorityFilter;
import org.alfresco.webservice.accesscontrol.GetPermissionsResult;
import org.alfresco.webservice.accesscontrol.HasPermissionsResult;
import org.alfresco.webservice.accesscontrol.NewAuthority;
import org.alfresco.webservice.accesscontrol.SiblingAuthorityFilter;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;

import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;
import com.geopista.utils.alfresco.manager.interfaces.AlfrescoAccessControlManager;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 * @ClassComments Clase que gestiona el control de acceso en Alfresco (los métodos requieren autenticación)
 */
public class AlfrescoAccessControlManagerImpl implements AlfrescoAccessControlManager{

	/**
     * Variables
     */
	private static AlfrescoAccessControlManagerImpl instance = new AlfrescoAccessControlManagerImpl();
		   
	/**
     * Constructor
     */
	protected AlfrescoAccessControlManagerImpl() {
	}
	
	/**
     * Solicita la instancia única de la clase (Singleton)
     * @return AlfrescoAccessControlManagerImpl
     */
	public static AlfrescoAccessControlManagerImpl getInstance(){
		return instance;
	}

    /**
     * Solicita un array con los resultados de la existencia de los permisos presentados sobre el elemento solicitado
     * @param key: Objeto con la clave unívoca de referencia a un elemento de Alfresco
     * @param permissions: Array con nombres de permisos
     * @throws AccessControlFault
     * @throws RemoteException
     * @return HasPermissionsResult []: Array con los resultado de comprobación de acceso
     */
	//CAMBIAR
	public boolean hasPermission(AlfrescoKey key, ACE ace) throws AccessControlFault, RemoteException{
		ACL [] acl = WebServiceFactory.getAccessControlService().getACLs(AlfrescoManagerUtils.getPredicate(key), ace);
		boolean result = false;
		if(acl != null && acl.length>0){			
			for(int i=0; i<acl.length; i++){
				if(acl[i].getAces() != null && acl[i].getAces().length>0){
					for(int j=0; j<acl[i].getAces().length; j++){
						if(acl[i].getAces(j).getAuthority().equals(ace.getAuthority()) && acl[i].getAces(j).getAccessStatus().equals(ace.getAccessStatus()) && acl[i].getAces(j).getPermission().equals(ace.getPermission())){
							result = true;
							break;
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
     * Solicita un array con los resultados de la existencia de los permisos presentados sobre el elemento solicitado
     * @param key: Objeto con la clave unívoca de referencia a un elemento de Alfresco
     * @param permissions: Array con nombres de permisos
     * @throws AccessControlFault
     * @throws RemoteException
     * @return HasPermissionsResult []: Array con los resultado de comprobación de acceso
     */
	//CAMBIAR
	public boolean hasPermission(AlfrescoKey key, String userName, AccessStatus accessStatus, String [] permission) throws AccessControlFault, RemoteException{
		ACE ace = null;
		for(int i=0; i<permission.length; i++){
			ace = new ACE();
			ace.setAuthority(userName);
			ace.setAccessStatus(accessStatus);
			ace.setPermission(permission[i]);
			if(!hasPermission(key, ace))
				return false;
		}
		return true;
	}
	
	
	/**
     * Solicita un array con los resultados de la existencia de los permisos presentados sobre el elemento solicitado
     * @param key: Objeto con la clave unívoca de referencia a un elemento de Alfresco
     * @param permissions: Array con nombres de permisos
     * @throws AccessControlFault
     * @throws RemoteException
     * @return HasPermissionsResult []: Array con los resultado de comprobación de acceso
     */
	//CAMBIAR
	public boolean hasPermission(AlfrescoKey key, String userName, String [] permission) throws AccessControlFault, RemoteException{
		return hasPermission(key, userName, AccessStatus.acepted, permission);
	}
	
	/**
     * Solicita un array con los resultados de la existencia de los permisos presentados sobre el elemento solicitado
     * @param key: Objeto con la clave unívoca de referencia a un elemento de Alfresco
     * @param permissions: Array con nombres de permisos
     * @throws AccessControlFault
     * @throws RemoteException
     * @return HasPermissionsResult []: Array con los resultado de comprobación de acceso
     */
	//CAMBIAR
	public boolean hasPermission(AlfrescoKey key, String userName, String permission) throws AccessControlFault, RemoteException{
		return hasPermission(key, userName, new String [] {permission});
	}

	/**
     * Solicita un array con los resultados de la existencia de los permisos presentados sobre el elemento solicitado
     * @param key: Objeto con la clave unívoca de referencia a un elemento de Alfresco
     * @param permissions: Array con nombres de permisos
     * @throws AccessControlFault
     * @throws RemoteException
     * @return HasPermissionsResult []: Array con los resultado de comprobación de acceso
     */
	//CAMBIAR
	public boolean hasAllPermissions(AlfrescoKey key, String userName) throws AccessControlFault, RemoteException{
		return hasPermission(key, userName, new String [] {ACE_PERMISSION_COLLABORATOR, ACE_PERMISSION_CONSUMER, ACE_PERMISSION_CONTRIBUTOR, ACE_PERMISSION_COORDINATOR, ACE_PERMISSION_EDITOR});
	}
	
    /**
     * Solicita un array con los resultados de la existencia de los permisos presentados sobre el elemento solicitado
     * @param key: Objeto con la clave unívoca de referencia a un elemento de Alfresco
     * @param permissions: Array con nombres de permisos
     * @throws AccessControlFault
     * @throws RemoteException
     * @return HasPermissionsResult []: Array con los resultado de comprobación de acceso
     */
	public HasPermissionsResult [] hasPermission(AlfrescoKey key, String [] permissions) throws AccessControlFault, RemoteException{
		return WebServiceFactory.getAccessControlService().hasPermissions(AlfrescoManagerUtils.getPredicate(key), permissions);
	}
	
    /**
     * Comprueba si un se tiene un permiso concreto sobre un elemento de Alfresco
     * @param key: Objeto con la clave unívoca de referencia a un elemento de Alfresco
     * @param permission: Nombre de un permiso
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado de la comprobación de acceso
     */
	public boolean hasPermission(AlfrescoKey key, String permission) throws AccessControlFault, RemoteException{
		HasPermissionsResult [] hasPermissionsResult = hasPermission(key, new String []{permission});
		if(hasPermissionsResult != null && hasPermissionsResult.length > 0)
			return true;
		return false;	
	}
	
    /**
     * Comprueba si se tiene permiso de administrador sobre un elemento de Alfresco
     * @param key: Objeto con la clave unívoca de referencia a un elemento de Alfresco
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado de la comprobación de acceso
     */
	public boolean hasAllPermissions(AlfrescoKey key) throws AccessControlFault, RemoteException{
		return hasPermission(key, ACE_PERMISSION_ADMINISTRATOR);	
	}
	
    /**
     * Crea una autoridad (Grupo o Usuario)
     * @param name: Nombre de la autoridad a crear
     * @param type: Tipo de autoridad a crear (TYPE_GROUP o TYPE_USER)
     * @throws AccessControlFault
     * @throws RemoteException
     * @return String []: Array con las autoridades creadas 
     */
	public String [] addAuthority(String name, String type) throws AccessControlFault, RemoteException{
		return WebServiceFactory.getAccessControlService().createAuthorities(null, new NewAuthority[]{new NewAuthority(type, name)});
	}
	
    /**
     * Crea un grupo
     * @param groupName: Nombre del grupo a crear
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado de la creación del grupo
     */
	public boolean addGroup(String groupName) throws AccessControlFault, RemoteException{
		String [] authority = addAuthority(groupName, TYPE_GROUP);
		if(authority != null && authority.length > 0)
			return true;
		return false;
	}
	
    /**
     * Añade usuarios a un grupo
     * @param groupName: Nombre del grupo
     * @param userName: Array con los nombres de los usuarios a añadir
     * @throws AccessControlFault
     * @throws RemoteException
     * @return String []: Array con los usuarios añadidos al grupo
     */
	public String [] addUsersToGroup(String groupName, String [] userName) throws AccessControlFault, RemoteException{
		return WebServiceFactory.getAccessControlService().addChildAuthorities(Constants.GROUP_PREFIX + groupName, userName);
	}
	
    /**
     * Añade un usuario a un grupo
     * @param groupName: Nombre del grupo
     * @param userName: Nombre del usuario a añadir
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado de añadir un usuario a un grupo
     */
	public boolean addUserToGroup(String groupName, String userName) throws AccessControlFault, RemoteException{
		String [] authorities = addUsersToGroup(groupName, new String[]{userName});
		if(authorities != null && authorities.length > 0)
			return true;
		return false;
	}
	
    /**
     * Añade permisos a un nodo de Alfresco
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param aces: Array de permisos a añadir
     * @throws AccessControlFault
     * @throws RemoteException
     * @return ACL []: Array con los ACLs de las referencias modificadas
     */
	public ACL [] addAccess(AlfrescoKey key, ACE [] aces) throws AccessControlFault, RemoteException{	
		return WebServiceFactory.getAccessControlService().addACEs(AlfrescoManagerUtils.getPredicate(key), aces);
	}
	
    /**
     * Añade un permiso a un nodo de Alfresco
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param ace: Permiso a añadir
     * @throws AccessControlFault
     * @throws RemoteException
     * @return ACL []: Array con el listado de acceso de las referencias modificadas
     */
	public ACL [] addAccess(AlfrescoKey key, ACE ace) throws AccessControlFault, RemoteException{	
		return addAccess(key, new ACE [] {ace});
	}
	
    /**
     * Añade un permiso a un nodo de Alfresco
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param name: Nombre del permiso a añadir
     * @param accessStatus: Estado de acceso del permiso a añadir
     * @param permission: Tipos de permisos a añadir
     * @throws AccessControlFault
     * @throws RemoteException
     * @return ACL []: Array con el listado de acceso de las referencias modificadas
     */
	public boolean addAccess(AlfrescoKey key, String name, AccessStatus accessStatus, String [] permissions) throws AccessControlFault, RemoteException{	
		ACE [] aces = new ACE[permissions.length];
		for(int i=0; i<permissions.length; i++){
			aces[i] = new ACE();
			aces[i].setAuthority(name);
			aces[i].setAccessStatus(accessStatus);
			aces[i].setPermission(permissions[i]);
		}
		ACL [] acls = addAccess(key, aces);
		if(acls != null && acls.length > 0)
			return true;
		return false;
	}
	
    /**
     * Añade un permiso de acceso a un nodo de Alfresco
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param name: Nombre del permiso a añadir
     * @param permission: Tipo de permiso a añadir
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado de añadir el permiso de acceso
     */
	public boolean addAccess(AlfrescoKey key, String name, String permission) throws AccessControlFault, RemoteException{	
		return addAccess(key, name, AccessStatus.acepted, new String [] {permission});
	}
	
    /**
     * Añade permisos de acceso total (administrador)
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param name: Nombre del permiso a añadir
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado de añadir los permisos de acceso total
     */
	public boolean addAllAccess(AlfrescoKey key, String name) throws AccessControlFault, RemoteException{			
		return addAccess(key, name, AccessStatus.acepted, new String [] {ACE_PERMISSION_COLLABORATOR, ACE_PERMISSION_CONSUMER, ACE_PERMISSION_CONTRIBUTOR, ACE_PERMISSION_COORDINATOR, ACE_PERMISSION_EDITOR});
	}
	
    /**
     * Añade un permiso a un grupo
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param groupName: Nombre del grupo
     * @param permission: Tipo de permiso a añadir
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado de añadir el permiso al grupo
     */
	public boolean addAccessToGroup(AlfrescoKey key, String groupName, String permission) throws AccessControlFault, RemoteException{	
		return addAccess(key, Constants.GROUP_PREFIX + groupName.toUpperCase(), permission);
	}
	
    /**
     * Añade todos los permisos (administrador) a un grupo
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param groupName: Nombre del grupo
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado de añadir todos los permisos a un grupo
     */
	public boolean addAllAccessToGroup(AlfrescoKey key, String groupName) throws AccessControlFault, RemoteException{	
		return addAllAccess(key, Constants.GROUP_PREFIX + groupName.toUpperCase());
	}
	
    /**
     * Añade un permiso a un usuario
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param userName: Nombre del usuario
     * @param permission: Tipo de permiso a añadir
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado de añadir el permiso al usuario
     */
	public boolean addAccessToUser(AlfrescoKey key, String userName, String permission) throws AccessControlFault, RemoteException{	
		return addAccess(key, userName, permission);
	}
	
    /**
     * Añade todos los permisos (administrador) a un usuario
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param userName: Nombre del usuario
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado de añadir todos los permisos a un usuario
     */
	public boolean addAllAccessToUser(AlfrescoKey key, String userName) throws AccessControlFault, RemoteException{	
		return addAllAccess(key, userName);
	}
	
    /**
     * Deniega un permiso de acceso a un nodo de Alfresco
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param name: Nombre del permiso a añadir
     * @param permission: Tipo de permiso a denegar
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado de denegar el permiso de acceso
     */
	public boolean denyAccess(AlfrescoKey key, String name, String [] permissions) throws AccessControlFault, RemoteException{	
		return addAccess(key, name, AccessStatus.declined, permissions);
	}
	
    /**
     * Deniega permisos de acceso total (administrador)
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param name: Nombre del permiso a denegar
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado de denegar los permisos de acceso total
     */
	public boolean denyAllAccess(AlfrescoKey key, String name) throws AccessControlFault, RemoteException{	
		return addAccess(key, name, AccessStatus.declined, new String [] {ACE_PERMISSION_COLLABORATOR, ACE_PERMISSION_CONSUMER, ACE_PERMISSION_CONTRIBUTOR, ACE_PERMISSION_COORDINATOR, ACE_PERMISSION_EDITOR});
	}
	
    /**
     * Deniega un permiso a un grupo
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param groupName: Nombre del grupo
     * @param permission: Tipo de permiso a denegar
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado de denegar el permiso al grupo
     */
	public boolean denyAccessToGroup(AlfrescoKey key, String groupName, String permission) throws AccessControlFault, RemoteException{	
		return denyAccess(key, Constants.GROUP_PREFIX + groupName, new String [] {permission});
	}
	
    /**
     * Deniega todos los permisos (administrador) a un grupo
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param groupName: Nombre del grupo
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado de denegar todos los permisos a un grupo
     */
	public boolean denyAllAccessToGroup(AlfrescoKey key, String groupName) throws AccessControlFault, RemoteException{	
		return denyAllAccess(key, Constants.GROUP_PREFIX + groupName);
	}
	
    /**
     * Deniega un permiso a un usuario
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param userName: Nombre del usuario
     * @param permission: Tipo de permiso a denegar
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado de denegar el permiso al usuario
     */
	public boolean denyAccessToUser(AlfrescoKey key, String userName, String permission) throws AccessControlFault, RemoteException{	
		return denyAccess(key, userName, new String [] {permission});
	}
	
    /**
     * Deniega todos los permisos (administrador) a un usuario
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param userName: Nombre del usuario
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado de denegar todos los permisos a un usuario
     */
	public boolean denyAllAccessToUser(AlfrescoKey key, String userName) throws AccessControlFault, RemoteException{	
		return denyAllAccess(key, userName);
	}
		
    /**
     * Modifica el permiso de herencia de un nodo
     * @param key: Clave unívoca de un nodo de Alfresco
     * @param inheritPermission: Valor de activación del permiso de herencia
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado de la modificación de la herencia de un nodo
     */
	public boolean setInheritPermission(AlfrescoKey key, boolean inheritPermission) throws AccessControlFault, RemoteException{
		ACL [] acl = WebServiceFactory.getAccessControlService().setInheritPermission(AlfrescoManagerUtils.getPredicate(key), inheritPermission);
		if(acl != null)
			return true;
		return false;
	}
	
    /**
     * Comprueba la existencia de un grupo
     * @param groupName: Nombre del grupo
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado de la comprobación de la existencia de un grupo
     */
	public boolean existsGroup(String groupName) throws AccessControlFault, RemoteException{
		AuthorityFilter authorityFilter = new AuthorityFilter(TYPE_GROUP, true);
		String [] allAuthorities = WebServiceFactory.getAccessControlService().getAllAuthorities(authorityFilter);
		return AlfrescoManagerUtils.isInArray(allAuthorities, Constants.GROUP_PREFIX + groupName);
	}
	
    /**
     * Comprueba la pertenecia de un usuario a un grupo
     * @param groupName: Nombre del grupo
     * @param userName: Nombre del usuario
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado de la comprobación de la pertenencia de un usuario a un grupo
     */
	public boolean existsUserInGroup(String groupName, String userName) throws AccessControlFault, RemoteException{
		SiblingAuthorityFilter siblingAuthorityFilter = new SiblingAuthorityFilter(TYPE_USER, true);		
		String [] childAuthorities = WebServiceFactory.getAccessControlService().getChildAuthorities(Constants.GROUP_PREFIX + groupName, siblingAuthorityFilter);
		return AlfrescoManagerUtils.isInArray(childAuthorities, userName);
	}
	
    /**
     * Solicita un array con las autoridades por tipo
     * @param type: Tipo de autoridad
     * @throws AccessControlFault
     * @throws RemoteException
     * @return String []: Array con los nombres de autoridades por tipo
     */
	public String [] getAllAuthoritiesFromType(String type) throws AccessControlFault, RemoteException{
		return WebServiceFactory.getAccessControlService().getAllAuthorities(new AuthorityFilter(type, true));
	}	
	
	/**
     * Solicita un array con todos los grupos
     * @throws AccessControlFault
     * @throws RemoteException
     * @return String []: Array con los nombres de los grupos
     */
	public String [] getAllGroups() throws AccessControlFault, RemoteException{
		return getAllAuthoritiesFromType(TYPE_GROUP);
	}	
	
	/**
     * Solicita un array con todos los usuarios
     * @throws AccessControlFault
     * @throws RemoteException
     * @return String []: Array con los nombres de los usuarios
     */
	public String [] getAllUsers() throws AccessControlFault, RemoteException{
		return getAllAuthoritiesFromType(TYPE_USER);
	}

    /**
     * Solicita un array con los resultados de la existencia de los permisos presentados sobre el elemento solicitado
     * @param key: Objeto con la clave unívoca de referencia a un elemento de Alfresco
     * @param permissions: Array con nombres de permisos
     * @throws AccessControlFault
     * @throws RemoteException
     * @return HasPermissionsResult []: Array con los resultado de comprobación de acceso
     */
	public GetPermissionsResult [] getPermissions(AlfrescoKey key) throws AccessControlFault, RemoteException{
		return WebServiceFactory.getAccessControlService().getPermissions(AlfrescoManagerUtils.getPredicate(key));
	}
	
    /**
     * Borra grupos
     * @param groupNames: Array con los nombres de los grupos a borrar
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado del borrado de los grupos
     */
	public boolean deleteGroups(String [] groupNames) throws AccessControlFault, RemoteException{
		WebServiceFactory.getAccessControlService().deleteAuthorities(groupNames);
		return true;
	}
	
    /**
     * Borra un grupo
     * @param groupName: Nombre del grupo a borrar
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado del borrado del grupo
     */
	public boolean deleteGroup(String groupName) throws AccessControlFault, RemoteException{
		deleteGroups(new String []{groupName});
		return true;
	}	
	
    /**
     * Borra usuarios de un grupo
     * @param groupName: Nombre del grupo
     * @param userNames: Array con los Nombres de los usuarios a borrar del grupo
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado del borrado de los usuarios del grupo
     */
	public boolean deleteUsersFromGroup(String groupName, String [] userNames) throws AccessControlFault, RemoteException{
		 WebServiceFactory.getAccessControlService().removeChildAuthorities(groupName, userNames);
		 return true;
	}
	
    /**
     * Borra un usuario de un grupo
     * @param groupName: Nombre del grupo
     * @param userName: Nombre del usuario a borrar del grupo
     * @throws AccessControlFault
     * @throws RemoteException
     * @return boolean: Resultado del borrado del usuario del grupo
     */
	public boolean deleteUserFromGroup(String groupName, String userName) throws AccessControlFault, RemoteException{
		deleteUsersFromGroup(groupName, new String[]{userName});
		return true;
	}

	
}
