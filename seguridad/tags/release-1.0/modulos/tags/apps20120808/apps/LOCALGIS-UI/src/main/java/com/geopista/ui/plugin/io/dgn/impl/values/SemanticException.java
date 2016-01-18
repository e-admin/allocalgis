package com.geopista.ui.plugin.io.dgn.impl.values;

/**
 * Clase base de las excepciones semánticas
 *
 * @author Fernando González Cortés
 */
public class SemanticException extends Exception {
    /**
     * Creates a new SemanticException object.
     */
    public SemanticException() {
        super();
    }

    /**
     * Creates a new SemanticException object.
     *
     * @param arg0
     */
    public SemanticException(String arg0) {
        super(arg0);
    }

    /**
     * Creates a new SemanticException object.
     *
     * @param arg0
     */
    public SemanticException(Throwable arg0) {
        super(arg0);
    }

    /**
     * Creates a new SemanticException object.
     *
     * @param arg0
     * @param arg1
     */
    public SemanticException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
