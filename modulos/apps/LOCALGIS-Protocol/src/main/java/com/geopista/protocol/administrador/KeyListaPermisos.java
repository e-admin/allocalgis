/**
 * KeyListaPermisos.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.administrador;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 23-nov-2005
 * Time: 12:44:41
 * To change this template use File | Settings | File Templates.
 */

/** Incidencia [308]. Distintos acl pueden tener los mismos permisos.
 * Clave para la ListaPermisos */

public class KeyListaPermisos implements Serializable {
    private String idperm;
    private String idacl;

    public KeyListaPermisos(){

    }
    
    public KeyListaPermisos(String idperm, String idacl){
        this.idperm= idperm;
        this.idacl= idacl;

    }

    public String getIdperm(){
        return idperm;
    }

    public void setIdperm(String idperm){
        this.idperm= idperm;
    }

    public String getIdacl(){
        return idacl;
    }

    public void setIdacl(String idacl){
        this.idacl= idacl;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyListaPermisos)) return false;

        final KeyListaPermisos keyListaPermisos = (KeyListaPermisos) o;

        if (idacl != null ? !idacl.equals(keyListaPermisos.idacl) : keyListaPermisos.idacl != null) return false;
        if (idperm != null ? !idperm.equals(keyListaPermisos.idperm) : keyListaPermisos.idperm != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (idperm != null ? idperm.hashCode() : 0);
        result = 29 * result + (idacl != null ? idacl.hashCode() : 0);
        return result;
    }

}
