/**
 * GeopistaAclEntry.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.security;

import java.io.Serializable;
import java.security.Principal;
import java.security.acl.Permission;
import java.util.Enumeration;
import java.util.Hashtable;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 28-may-2004
 * Time: 11:56:15
 */
public class GeopistaAclEntry implements java.security.acl.AclEntry, Serializable{
    GeopistaPrincipal geopistaUser;
    private Hashtable geoPermisos;

    public GeopistaAclEntry() {
    }

    public GeopistaAclEntry(GeopistaPrincipal theUser) {
        geopistaUser = theUser;
    }

    public GeopistaAclEntry(Principal theUser) {
        if (theUser==null) return;
        geopistaUser = new GeopistaPrincipal(theUser.getName());
    }
    public GeopistaPrincipal getGeopistaUser() {
        return geopistaUser;
    }

    public Hashtable getGeoPermisos() {
        return geoPermisos;
     }

    public void setGeopistaUser(GeopistaPrincipal geopistaUser) {
        this.geopistaUser = geopistaUser;
    }

    public void setGeoPermisos(Hashtable geoPermisos) {
        this.geoPermisos = geoPermisos;
    }

    public boolean setPrincipal(Principal user) {
        if (user==null) return false;
        geopistaUser=(GeopistaPrincipal)user;
        return true;
    }

    public Principal getPrincipal() {
        return (GeopistaPrincipal)geopistaUser;
    }

    public void setNegativePermissions() {

    }

    public boolean isNegative() {
        return false;
    }

    public boolean addPermission(Permission permission) {
        if(permission==null)return false;
        if (! (permission instanceof GeopistaPermission))
                   return false;
        if (geoPermisos==null) geoPermisos=new Hashtable();
        geoPermisos.put(((GeopistaPermission)permission).getName(),(GeopistaPermission)permission);
        return true;
    }

    public boolean removePermission(Permission permission) {
        if (permission==null) return false;
        if (! (permission instanceof GeopistaPermission))
                   return false;
        if (geoPermisos==null)return false;
        geoPermisos.remove(((GeopistaPermission)permission).getName());
        return true;
    }

    public boolean checkPermission(Permission permission) {
        if (! (permission instanceof GeopistaPermission))
                           return false;
        if (geoPermisos==null)return false;
        return (geoPermisos.get(((GeopistaPermission)permission).getName())!=null);
    }

    public Enumeration permissions() {
        if (geoPermisos==null)return null;
        return (geoPermisos.elements());
    }

    public Object clone() {
        return null;
    }
}
