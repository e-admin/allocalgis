/**
 * ListaGroupPermisos.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.administrador.usuarios;

import java.util.Enumeration;
import java.util.Hashtable;

import com.geopista.protocol.administrador.ListaPermisos;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 03-jun-2004
 * Time: 11:23:38
 */
public class ListaGroupPermisos{
    private Hashtable hGroupPermisos;
    private String idAcl;
    public  ListaGroupPermisos(String sIdAcl)
    {
          this.hGroupPermisos = new Hashtable();
          idAcl=sIdAcl;
    }
    public void add(GroupPermisos groupPermisos) {
        this.hGroupPermisos.put(groupPermisos.getIdPerm(),groupPermisos);
    }


    public Hashtable gethGroupPermisos() {
        return hGroupPermisos;
    }

    public void inicializaGroupPermisos(ListaPermisos listaPermisos)
    {
            for (Enumeration e=hGroupPermisos.elements();e.hasMoreElements();)
            {
                GroupPermisos auxGroupPermiso=(GroupPermisos) e.nextElement();
                auxGroupPermiso.inicializaGroupPermisos(listaPermisos,idAcl);
            }
    }
}
