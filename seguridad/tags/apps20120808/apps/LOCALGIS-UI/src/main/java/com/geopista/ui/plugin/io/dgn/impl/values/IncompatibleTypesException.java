package com.geopista.ui.plugin.io.dgn.impl.values;

/**
 * Lanzado cuando la operación especificada no está definida para los tipos de
 * los operandos sobre los que se quiso operar
 *
 * @author Fernando González Cortés
 */
public class IncompatibleTypesException extends SemanticException {
    /**
     * Creates a new IncompatibleTypesException object.
     */
    public IncompatibleTypesException() {
        super();
    }

    /**
     * Creates a new IncompatibleTypesException object.
     *
     * @param arg0
     */
    public IncompatibleTypesException(String arg0) {
        super(arg0);
    }

    /**
     * Creates a new IncompatibleTypesException object.
     *
     * @param arg0
     */
    public IncompatibleTypesException(Throwable arg0) {
        super(arg0);
    }

    /**
     * Creates a new IncompatibleTypesException object.
     *
     * @param arg0
     * @param arg1
     */
    public IncompatibleTypesException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
