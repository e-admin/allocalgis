/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 */
/*
 * $Id: C14NMethodParameterSpec.java,v 1.1 2009/07/03 12:31:55 miriamperez Exp $
 */
package javax.xml.crypto.dsig.spec;

import javax.xml.crypto.dsig.CanonicalizationMethod;

/**
 * A specification of algorithm parameters for a {@link CanonicalizationMethod}
 * Algorithm. The purpose of this interface is to group (and provide type 
 * safety for) all canonicalization (C14N) parameter specifications. All 
 * canonicalization parameter specifications must implement this interface.
 *
 * @author Sean Mullan
 * @author JSR 105 Expert Group
 * @see CanonicalizationMethod
 */
public interface C14NMethodParameterSpec extends TransformParameterSpec {}
