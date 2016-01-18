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
