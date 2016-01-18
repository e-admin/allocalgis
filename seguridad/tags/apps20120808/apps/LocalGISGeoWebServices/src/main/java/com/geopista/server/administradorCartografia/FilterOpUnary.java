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
