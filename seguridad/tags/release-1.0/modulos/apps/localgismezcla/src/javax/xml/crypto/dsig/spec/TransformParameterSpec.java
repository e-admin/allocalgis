/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 */
/*
 * $Id: TransformParameterSpec.java,v 1.1 2009/07/03 12:31:55 miriamperez Exp $
 */
package javax.xml.crypto.dsig.spec;

import javax.xml.crypto.dsig.Transform;
import java.security.spec.AlgorithmParameterSpec;

/**
 * A specification of algorithm parameters for a {@link Transform} 
 * algorithm. The purpose of this interface is to group (and provide type 
 * safety for) all transform parameter specifications. All transform parameter 
 * specifications must implement this interface.
 *
 * @author Sean Mullan
 * @author JSR 105 Expert Group
 * @see Transform
 */
public interface TransformParameterSpec extends AlgorithmParameterSpec {}
