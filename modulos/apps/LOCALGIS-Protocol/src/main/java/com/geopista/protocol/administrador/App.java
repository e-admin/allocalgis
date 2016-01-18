/**
 * App.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.administrador;


/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 16-mar-2012
 * Time: 11:13:04
 */
public class App implements java.io.Serializable{
    String id;
    String nombre;
    ListaAcl acls;

    public App(){
    	acls= new ListaAcl();
    }
    public App(String sId,String sNombre)
    {
        id=sId;
        nombre=sNombre;
        acls=new ListaAcl();
    }
    public App(String sId,String sNombre,  ListaAcl listaAcls)
    {
        id=sId;
        nombre=sNombre;
        acls=listaAcls;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public void setPermisos(ListaAcl listaAcls) {
        this.acls = listaAcls;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public ListaAcl getAcls() {
        return acls;
    }

    
    public void addAcls(Acl auxAcl)
    {
        if (auxAcl==null) return;
        acls.add(auxAcl);
    }
}


