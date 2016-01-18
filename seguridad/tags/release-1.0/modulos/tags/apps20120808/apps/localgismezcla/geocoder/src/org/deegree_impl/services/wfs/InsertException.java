/*
 * InsertException.java
 *
 * Created on 22. November 2002, 02:26
 */

package org.deegree_impl.services.wfs;

/**
 * Thrown when a semantic error occurs while building an InsertTree.
 * @author  Markus Schneider
 */
public class InsertException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>InsertException</code> without detail message.
     */
    public InsertException() {
    }  
    
    /**
     * Constructs an instance of <code>InsertException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public InsertException(String msg) {
        super(msg);
    }
}
