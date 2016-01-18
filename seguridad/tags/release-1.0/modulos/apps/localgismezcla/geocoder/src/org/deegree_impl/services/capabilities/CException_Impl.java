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

import java.util.*;

import org.deegree.xml.XMLTools;
import org.deegree.xml.Marshallable;

import org.deegree.services.capabilities.*;


/**
 *
 * <p>----------------------------------------------------------------------</p>
 *
 * @author <a href="mailto: k.lupp@web.de">Katharina Lupp</a>
 * @author <a href="mailto:mschneider@lat-lon.de>Markus Schneider</a>
 * @version $Revision: 1.1 $
 */
public class CException_Impl implements CException, Marshallable {
    private ArrayList formats = null;

    /**
     * constructor initializing the class with the formats array
     */
    public CException_Impl( String[] formats ) {
        this.formats = new ArrayList();
        setFormats( formats );
    }

    /**
     * returns the possible formats of the exceptions
     */
    public String[] getFormats() {
        return (String[])formats.toArray( new String[formats.size()] );
    }

    /**
     * Adds a format to the <tt>CException</tt>'s formats (if it is not defined
     * yet).
     * <p>
     * @param format the name of the format to add
     */
    public void addFormat (String format) {
        if (!formats.contains(format)) {
            formats.add (format);
        }
    }    
    
    /**
     * sets the possible formats of the exceptions
     */
    public void setFormats( String[] formats ) {
        for ( int i = 0; i < formats.length; i++ ) {
            addFormat( formats[i] );
        }
    }

    /**
     * Returns an XML representation of this object.
     */
    public String exportAsXML() {
        StringBuffer sb = new StringBuffer ();

        sb.append ("<Exception>");

        Iterator it = formats.iterator ();
        while (it.hasNext ()) {
            sb.append ("<Format>")
              .append (XMLTools.validateCDATA ((String) it.next ()))
              .append ("</Format>");
        }

        sb.append ("</Exception>");

        return sb.toString ();
    }           
}