/**
 * FilterOpUnary.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.util.List;

/** Nodo unario (NOT) para la sintaxis de las clausulas WHERE */
public class FilterOpUnary implements FilterNode{

    private FilterNode node;

    private FilterOpUnary(FilterNode node){
        this.node=node;
    }

    private FilterOpUnary(){
        
    }
    public FilterNode getNode() {
        return node;
    }

    public void setNode(FilterNode node) {
        this.node = node;
    }

    public int getType() {
        return NODETYPE_OPERATOR_UNARY;
    }

    public void values(List l) {
        node.values(l);
    }

    public String toSQL() {
        return "NOT "+node.toSQL();
    }

    /** Instancia un nuevo nodo NOT a partir de otro nodo */
    public static FilterNode not(FilterNode node){
        return new FilterOpUnary(node);
    }
}
