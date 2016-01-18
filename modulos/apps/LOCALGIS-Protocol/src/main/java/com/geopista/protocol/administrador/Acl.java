/**
 * Acl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.administrador;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 01-jun-2004
 * Time: 10:10:59
 */
public class Acl implements java.io.Serializable{
    String id;
    String nombre;
    String idApp;
    ListaPermisos permisos;

    public Acl(){
        permisos= new ListaPermisos();
    }
    public Acl(String sId,String sNombre)
    {
        id=sId;
        nombre=sNombre;
         permisos= new ListaPermisos();
    }
    public Acl(String sId,String sNombre,  ListaPermisos listaPermisos)
    {
        id=sId;
        nombre=sNombre;
        permisos=listaPermisos;
    }
    public Acl(String sId,String sNombre, String sIdApp)
    {
        id=sId;
        nombre=sNombre;
        idApp=sIdApp;
         permisos= new ListaPermisos();
    }
    public Acl(String sId,String sNombre, String sIdApp,  ListaPermisos listaPermisos)
    {
        id=sId;
        nombre=sNombre;
        idApp=sIdApp;
        permisos=listaPermisos;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdApp() {
		return idApp;
	}
	public void setIdApp(String idApp) {
		this.idApp = idApp;
	}
	
	public void setPermisos(ListaPermisos listaPermisos) {
        this.permisos = listaPermisos;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public ListaPermisos getPermisos() {
        return permisos;
    }

    
    public void addPermiso(Permiso auxPermiso)
    {
        if (auxPermiso==null) return;
        permisos.add(auxPermiso);
    }
}


