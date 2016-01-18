/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 */
/*
 * $Id: KeySelectorResult.java,v 1.1 2009/07/03 12:31:45 miriamperez Exp $
 */
package javax.xml.crypto;

import java.security.Key;

/**
 * The result returned by the {@link KeySelector#select KeySelector.select} 
 * method.
 * <p>
 * At a minimum, a <code>KeySelectorResult</code> contains the <code>Key</code>
 * selected by the <code>KeySelector</code>. Implementations of this interface
 * may add methods to return implementation or algorithm specific information,
 * such as a chain of certificates or debugging information.
 *
 * @author Sean Mullan
 * @author JSR 105 Expert Group
 * @see KeySelector
 */
public interface KeySelectorResult {

    /**
     * Returns the selected key.
     *
     * @return the selected key, or <code>null</code> if none can be found
     */
    Key getKey();
}
