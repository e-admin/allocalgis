/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 */
/*
 * $Id: XMLStructure.java,v 1.1 2009/07/03 12:31:45 miriamperez Exp $
 */
package javax.xml.crypto;

/**
 * A representation of an XML structure from any namespace. The purpose of 
 * this interface is to group (and provide type safety for) all 
 * representations of XML structures.
 *
 * @author Sean Mullan
 * @author JSR 105 Expert Group
 */
public interface XMLStructure {

    /**
     * Indicates whether a specified feature is supported.
     *
     * @param feature the feature name (as an absolute URI)
     * @return <code>true</code> if the specified feature is supported,
     *    <code>false</code> otherwise
     * @throws NullPointerException if <code>feature</code> is <code>null</code>
     */
    boolean isFeatureSupported(String feature);
}
