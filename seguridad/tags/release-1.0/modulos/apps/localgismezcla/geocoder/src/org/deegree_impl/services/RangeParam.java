/*----------------    FILE HEADER  ------------------------------------------

This file is part of deegree.
Copyright (C) 2001 by:
EXSE, Department of Geography, University of Bonn
http://www.giub.uni-bonn.de/exse/
lat/lon Fitzke/Fretter/Poth GbR
http://www.lat-lon.de

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

Contact:

Andreas Poth
lat/lon Fitzke/Fretter/Poth GbR
Meckenheimer Allee 176
53115 Bonn
Germany
E-Mail: poth@lat-lon.de

Jens Fitzke
Department of Geography
University of Bonn
Meckenheimer Allee 166
53115 Bonn
Germany
E-Mail: jens.fitzke@uni-bonn.de

 copyright Emanuele Tajariol / itworks
 ---------------------------------------------------------------------------*/

package org.deegree_impl.services;

import org.deegree.tools.Parameter;


/**
 * A parsed request parameter concerning range values
 *

 * @version $Revision: 1.1 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author Created by Omnicore CodeGuide
 */
public abstract class RangeParam implements Parameter {
    protected Object _paramObj = null;
    protected String _paramString = null;

    /**
     * Creates a new RangeParam object.
     *
     * @param value 
     */
    protected RangeParam( String value ) {
        _paramString = value;
        _paramObj = parseValue( value );
    }

    /**
     *
     *
     * @return 
     */
    public Object getValue() {
        return _paramObj;
    }

    /**
     *
     *
     * @param param 
     *
     * @return 
     */
    protected abstract Object parseValue( String param );

    /**
     *
     *
     * @return 
     */
    public String getParamString() {
        return _paramString;
    }

    /**
     *
     *
     * @return 
     */
    public String toString() {
        return "RangeParam[" + _paramString + "]";
    }
}