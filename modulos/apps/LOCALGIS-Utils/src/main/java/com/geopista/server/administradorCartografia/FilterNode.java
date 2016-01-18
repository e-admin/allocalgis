/**
 * FilterNode.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.io.Serializable;
import java.util.List;

/** Nodo del arbol sintactico para construir filtros WHERE en el Administrador de
 * Cartografia.
 */
public interface FilterNode extends Serializable{
    public static final int NODETYPE_OPERATOR_UNARY=1;
    public static final int NODETYPE_OPERATOR_BINARY=2;
    public static final int NODETYPE_LEAF=3;

    public int getType();
    public String toSQL();
    public void  values(List l);
}
