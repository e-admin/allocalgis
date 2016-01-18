package com.geopista.security;

import java.security.Principal;
import java.security.acl.NotOwnerException;
import java.security.acl.AclEntry;
import java.security.acl.Permission;
import java.security.acl.LastOwnerException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.io.Serializable;



/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 28-may-2004
 * Time: 11:56:02
 */
public class GeopistaAcl implements java.security.acl.Acl, Serializable{
    private GeopistaPrincipal owner;
    private String aclName;
    private Hashtable allowedUsersTable = new Hashtable();
    
    private String aclDescripcion;

    public GeopistaAcl() {
    }
    public GeopistaAcl(GeopistaPrincipal ownerPrincipal, String sAclName)
    {
        owner=ownerPrincipal;
        aclName=sAclName;
    }
    public GeopistaAcl(Principal ownerPrincipal, String sAclName)
    {
        if (ownerPrincipal!=null)
        owner=new GeopistaPrincipal(ownerPrincipal.getName()) ;
        aclName=sAclName;
    }
    
    public GeopistaAcl(Principal ownerPrincipal, String sAclName,String aclDescripcion)
    {
        if (ownerPrincipal!=null)
        owner=new GeopistaPrincipal(ownerPrincipal.getName()) ;
        aclName=sAclName;
        this.setAclDescripcion(aclDescripcion);
    }

    public GeopistaPrincipal getOwner() {
        return owner;
    }

    public String getAclName() {
        return aclName;
    }

    public Hashtable getAllowedUsersTable() {
        return allowedUsersTable;
    }

    public void setOwner(GeopistaPrincipal owner) {
        this.owner = owner;
    }

    public void setAclName(String aclName) {
        this.aclName = aclName;
    }

    public void setAllowedUsersTable(Hashtable allowedUsersTable) {
        this.allowedUsersTable = allowedUsersTable;
    }

    public void setName(Principal caller, String name) throws NotOwnerException {

        if ((owner==null)||(!owner.equals(caller))) throw new NotOwnerException();
        aclName=name;
    }

    public String getName() {
        return aclName;
    }

    public boolean addEntry(Principal caller, AclEntry entry) throws NotOwnerException {
        if ((owner==null)||(!owner.equals(caller))) throw new NotOwnerException();
        Principal key = entry.getPrincipal();
        if (allowedUsersTable.get(key) != null)
	        return false;
        allowedUsersTable.put(key, entry);
	    return true;
    }


    public boolean removeEntry(Principal caller, AclEntry entry) throws NotOwnerException {
        if (!isOwner(caller))
	        throw new NotOwnerException();
        Object key = entry.getPrincipal();
        Object o = allowedUsersTable.remove(key);
	    return (o != null);
    }

    public Enumeration getPermissions(Principal user) {
        if (!( user instanceof GeopistaPrincipal))
        {
            user=new GeopistaPrincipal(user.getName());
        }
        AclEntry aclEntry=(AclEntry)allowedUsersTable.get(user);
        if (aclEntry==null) return null;
        return aclEntry.permissions();
   }
    public Enumeration getPermissions() {
            AclEntry aclEntry=(AclEntry)allowedUsersTable.get(owner);
            if (aclEntry==null) return null;
            return aclEntry.permissions();
       }


    public Enumeration entries() {
        return allowedUsersTable.elements();
    }

    public boolean checkPermission(Principal principal, Permission permission) {
        Enumeration permSet = getPermissions(principal);
        if (permSet ==null) return false;
	    while (permSet.hasMoreElements()) {
	        Permission p = (Permission) permSet.nextElement();
	        if (p.equals(permission))
	            return true;
    	}
	    return false;
    }
      public boolean checkPermission(Permission permission) {
        Enumeration permSet = getPermissions(owner);
        if (permSet!=null)
        {
	        while (permSet.hasMoreElements()) {
	             Permission p = (Permission) permSet.nextElement();
	             if (p.equals(permission))
	                return true;
    	    }
        }

	    return false;
    }

    public boolean addOwner(Principal caller, Principal newOwner) throws NotOwnerException {
        return false;
    }

    public boolean deleteOwner(Principal caller, Principal owner) throws NotOwnerException, LastOwnerException {
        return false;
    }

    public boolean isOwner(Principal caller) {
        return !((owner==null)||(!owner.equals(caller)));
    }
	public void setAclDescripcion(String aclDescripcion) {
		this.aclDescripcion = aclDescripcion;
	}
	public String getAclDescripcion() {
		return aclDescripcion;
	}
}
