/**
 * ListaUsuarios.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.administrador;

import java.io.Serializable;
import java.util.Hashtable;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 02-jun-2004
 * Time: 12:49:35
 */
public class ListaUsuarios implements Serializable{
    private Hashtable hUsuarios;
    public  ListaUsuarios()
    {
          this.hUsuarios = new Hashtable();
    }
    public void add(Usuario  usuario) {
        this.hUsuarios.put(usuario.getId(),usuario);
    }

    public Usuario get(String sIdUsuario)
    {
        return (Usuario)this.hUsuarios.get(sIdUsuario);
    }
    public void remove(Usuario usuarioEliminado)
    {
        this.hUsuarios.remove(usuarioEliminado.getId());
    }

    public Hashtable gethUsuarios() {
        return hUsuarios;
    }

    public void sethUsuarios(Hashtable hUsuarios) {
        this.hUsuarios = hUsuarios;
    }
    public void addPermiso(String sIdUsuario, Permiso permiso)
    {
        Usuario auxUsuario=(Usuario)hUsuarios.get(sIdUsuario);
        if (auxUsuario==null) return;
        auxUsuario.addPermiso(permiso);
    }
    public void addGrupo(String sIdUsuario, String sIdGrupo)
    {
        Usuario auxUsuario=(Usuario)hUsuarios.get(sIdUsuario);
        if (auxUsuario==null) return;
        auxUsuario.addGrupo(sIdGrupo);
    }
}

