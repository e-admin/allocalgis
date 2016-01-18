package com.geopista.server.administradorCartografia;

import java.util.List;
import java.io.Serializable;

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
