/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 */
/*
 * $Id: SignatureMethodParameterSpec.java,v 1.1 2009/07/03 12:31:55 miriamperez Exp $
 */
package javax.xml.crypto.dsig.spec;

import javax.xml.crypto.dsig.SignatureMethod;
import java.security.spec.AlgorithmParameterSpec;

/**
 * A specification of algorithm parameters for an XML {@link SignatureMethod} 
 * algorithm. The purpose of this interface is to group (and provide type 
 * safety for) all signature method parameter specifications. All signature
 * method parameter specifications must implement this interface.
 *
 * @author Sean Mullan
 * @author JSR 105 Expert Group
 * @see SignatureMethod
 */
public interface SignatureMethodParameterSpec extends AlgorithmParameterSpec {}
