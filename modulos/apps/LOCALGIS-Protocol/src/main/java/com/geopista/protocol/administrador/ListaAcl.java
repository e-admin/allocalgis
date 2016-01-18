/**
 * ListaAcl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.administrador;

import java.util.Hashtable;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 01-jun-2004
 * Time: 10:13:59
 */
public class ListaAcl {
    private Hashtable hAcls;
    public  ListaAcl()
    {
          this.hAcls = new Hashtable();
    }
    public void add(Acl acl) {
        this.hAcls.put(acl.getId(),acl);
    }

    public Acl get(String sIdAcl)
    {
        return (Acl)this.hAcls.get(sIdAcl);
    }

    public Hashtable gethAcls() {
        return hAcls;
    }
}

