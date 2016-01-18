/**
 * FilterOpBinary.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.util.List;

/** Nodo binario de un arbol de clausula WHERE correspondiente a una operacion AND u OR */
public class FilterOpBinary implements FilterNode{

    private static final String OP_OR=" OR ";
    private static final String OP_AND=" AND ";
    private String op;

    public FilterOpBinary(){

    }
    private FilterOpBinary(String op, FilterNode fnLeft, FilterNode fnRight){
        this.op=op;
        this.setLeft(fnLeft);
        this.setRight(fnRight);
    }

    public void values(List l) {
        left.values(l);
        right.values(l);
    }

    private FilterNode right;
    private FilterNode left;

    public int getType() {
        return NODETYPE_OPERATOR_BINARY;
    }

    public String toSQL() {
        return "("+ this.left.toSQL()+op+this.right.toSQL()+")";
    }

    public FilterNode getRight() {
        return right;
    }

    public void setRight(FilterNode right) {
        this.right = right;
    }

    public FilterNode getLeft() {
        return left;
    }

    public void setLeft(FilterNode left) {
        this.left = left;
    }

    /** Instancia un nuevo nodo AND con nodos derecho e izquierdo */
    public static FilterNode and(FilterNode fnLeft,FilterNode fnRight){
        return new FilterOpBinary(OP_AND,fnLeft,fnRight);
    }

    /** Instancia un nuevo nodo OR con nodos derecho e izquierdo */
    public static FilterNode or(FilterNode fnLeft,FilterNode fnRight){
        return new FilterOpBinary(OP_OR,fnLeft,fnRight);
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }
}
