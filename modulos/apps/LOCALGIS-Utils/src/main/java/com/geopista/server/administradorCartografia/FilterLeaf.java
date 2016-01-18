/**
 * FilterLeaf.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.io.Serializable;
import java.util.List;

/** Nodo hoja para las expresiones del modelo de clausula WHERE correspondiente a
 * una igualdad. */
/**
 * Aso añade operaciones para menor, menorigual, mayor, mayorigual
 */
public class FilterLeaf implements FilterNode{

    private static final String OP_IGUAL=" = ";
    private static final String OP_MENOR=" < ";
    private static final String OP_MENORIGUAL=" <= ";
    private static final String OP_MAYOR=" < ";
    private static final String OP_MAYORIGUAL=" <= ";
    private String op;

    String lValue=null;
    Serializable rValue=null;

    public FilterLeaf(){

    }

    private FilterLeaf(String op,String lValue, Serializable rValue){
        this.op=op;
        this.lValue=lValue;
        this.rValue=rValue;
    }

    public int getType() {
        return NODETYPE_LEAF;
    }

    public String toSQL() {
        return lValue+op+"?";
    }

    public void values(List l) {
        l.add(rValue);
    }

    public String getLValue() {
        return lValue;
    }

    public void setLValue(String lValue) {
        this.lValue = lValue;
    }

    public Serializable getRValue() {
        return rValue;
    }

    public void setRValue(Serializable rValue) {
        this.rValue = rValue;
    }

    /** Instancia un nuevo nodo hoja con un par atributo/valor */
    public static FilterNode equal(String sL, Serializable sR){
        return new FilterLeaf(OP_IGUAL,sL,sR);
    }
    /** Instancia un nuevo nodo hoja con un par atributo/valor */
    public static FilterNode menor(String sL, Serializable sR){
        return new FilterLeaf(OP_MENOR,sL,sR);
    }
     /** Instancia un nuevo nodo hoja con un par atributo/valor */
    public static FilterNode menorIgual(String sL, Serializable sR){
        return new FilterLeaf(OP_MENORIGUAL,sL,sR);
    }
    /** Instancia un nuevo nodo hoja con un par atributo/valor */
    public static FilterNode mayor(String sL, Serializable sR){
        return new FilterLeaf(OP_MAYOR,sL,sR);
    }
    /** Instancia un nuevo nodo hoja con un par atributo/valor */
    public static FilterNode mayorIgual(String sL, Serializable sR){
        return new FilterLeaf(OP_MAYORIGUAL,sL,sR);
    }
}
