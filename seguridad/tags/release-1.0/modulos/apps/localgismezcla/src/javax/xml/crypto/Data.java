/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 */
/*
 * $Id: Data.java,v 1.1 2009/07/03 12:31:45 miriamperez Exp $
 */
package javax.xml.crypto;

import javax.xml.crypto.dsig.Transform;

/**
 * An abstract representation of the result of dereferencing a 
 * {@link URIReference} or the input/output of subsequent {@link Transform}s.
 * The primary purpose of this interface is to group and provide type safety
 * for all <code>Data</code> subtypes.
 *
 * @author Sean Mullan
 * @author JSR 105 Expert Group
 */
public interface Data { }
