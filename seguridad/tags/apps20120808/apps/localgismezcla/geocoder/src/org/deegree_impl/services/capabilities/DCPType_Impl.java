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

                 
 ---------------------------------------------------------------------------*/
package org.deegree_impl.services.capabilities;

import org.deegree.xml.Marshallable;
import org.deegree.services.capabilities.*;


/**
 * Available Distributed Computing Platforms (DCPs) are
 * listed here. At present, only HTTP is defined.
 *
 * <p>-----------------------------------------------------</p>
 *
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */
public class DCPType_Impl implements DCPType, Marshallable {
    private Protocol protocol = null;

    /**
    * default constructor
    */
    public DCPType_Impl() {
    }

    /**
    * constructor initializing the class with the protocol
    */
    public DCPType_Impl( Protocol protocol ) {
        setProtocol( protocol );
    }

    /**
    * returns the protocol of the available Distributed Computing Platforms (DCPs)
    */
    public Protocol getProtocol() {
        return protocol;
    }

    /**
    * sets the protocol of the available Distributed Computing Platforms (DCPs)
    */
    public void setProtocol( Protocol protocol ) {
        this.protocol = protocol;
    }

    /**
     * Returns an XML representation of this object.
     */
    public String exportAsXML () {
        StringBuffer sb = new StringBuffer ();

        sb.append ("<DCPType>")
          .append (((Marshallable) protocol).exportAsXML ())
          .append ("</DCPType>");

        return sb.toString ();
    }      
}