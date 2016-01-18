/**
 * ListaEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.inventario;

import java.io.Serializable;
import java.util.Hashtable;


public class ListaEIEL implements Serializable{
	
    private Hashtable hEIEL;
    
    public  ListaEIEL()
    {
          this.hEIEL = new Hashtable();
    }
    public void add(InventarioEIELBean  eiel) {
        this.hEIEL.put(eiel.getUnionClaveEIEL(), eiel);
    }

    public InventarioEIELBean get(String union_clave_eiel)
    {
        return (InventarioEIELBean)this.hEIEL.get(union_clave_eiel);
    }
    public void remove(InventarioEIELBean eiel)
    {
        this.hEIEL.remove(eiel.getUnionClaveEIEL());
    }

    public Hashtable gethEIEL() {
        return hEIEL;
    }

    public void sethEIEL(Hashtable hEIEL) {
        this.hEIEL = hEIEL;
    }
}

