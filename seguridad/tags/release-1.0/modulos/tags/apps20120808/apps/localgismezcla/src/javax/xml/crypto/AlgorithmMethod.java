/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 */
/*
 * $Id: AlgorithmMethod.java,v 1.1 2009/07/03 12:31:45 miriamperez Exp $
 */
package javax.xml.crypto;

import java.security.spec.AlgorithmParameterSpec;

/**
 * An abstract representation of an algorithm defined in the XML Security 
 * specifications. Subclasses represent specific types of XML security
 * algorithms, such as a {@link javax.xml.crypto.dsig.Transform}.
 *
 * @author Sean Mullan
 * @author JSR 105 Expert Group
 */ 
public interface AlgorithmMethod {

    /**
     * Returns the algorithm URI of this <code>AlgorithmMethod</code>.
     *
     * @return the algorithm URI of this <code>AlgorithmMethod</code>
     */
    String getAlgorithm();

    /**
     * Returns the algorithm parameters of this <code>AlgorithmMethod</code>.
     *
     * @return the algorithm parameters of this <code>AlgorithmMethod</code>.
     *    Returns <code>null</code> if this <code>AlgorithmMethod</code> does
     *    not require parameters and they are not specified.
     */
    AlgorithmParameterSpec getParameterSpec();
}
