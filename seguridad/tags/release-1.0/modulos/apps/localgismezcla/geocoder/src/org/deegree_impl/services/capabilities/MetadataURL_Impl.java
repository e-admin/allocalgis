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

import java.net.*;

import org.deegree.xml.XMLTools;
import org.deegree.xml.Marshallable;
import org.deegree.services.capabilities.*;

import org.deegree_impl.tools.NetWorker;
import org.deegree_impl.ogcbasic.BaseURL_Impl;


/**
 * A WFS/WMS/WCS should use one or more <MetadataURL> elements to offer detailed,
 * standardized metadata about the data underneath a particular layer. The type
 * attribute indicates the standard to which the metadata complies. Two types are
 * defined at present: the value 'TC211' refers to [ISO 19115]; the value 'FGDC'
 * refers to [FGDC-STD-001-1988]. The MetadataURL element shall not be used to
 * reference metadata in a non-standardized metadata format
 * <p>----------------------------------------------------------------------</p>
 *
 * @author <a href="mailto:k.lupp@web.de>Katharina Lupp</a>
 * @author <a href="mailto:mschneider@lat-lon.de>Markus Schneider</a>
 * @version $Revision: 1.1 $
 */
public class MetadataURL_Impl extends BaseURL_Impl implements
    MetadataURL, Marshallable {

    private String type = null;

    /**
     * constructor initializing the class with the <MetadataURL>
     */
    public MetadataURL_Impl( String type, String format, URL onlineResource ) {
        super( format, onlineResource );
        setType( type );
    }

    /**
     * returns the type attribute indicating the standard to which the metadata
     * complies.
     */
    public String getType() {
        return type;
    }

    /**
     * sets the type attribute indicating the standard to which the metadata
     * complies.
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     *
     *
     * @return 
     */
    public String toString() {
        String ret = null;
        ret = "type = " + type + "\n";
        return ret;
    }

    /**
     * Returns an XML representation of this object.
     */
    public String exportAsXML () {
        StringBuffer sb = new StringBuffer ();

        sb.append ("<MetadataURL type=\"")
          .append (type).append ("\">")
          .append ("<Format>").append (XMLTools.validateCDATA (getFormat ()))
          .append ("</Format>")
          .append ("<OnlineResource ")
          .append ("xmlns:xlink=\"http://www.w3.org/1999/xlink\" ")
          .append ("xlink:type=\"simple\" xlink:href=\"")
          .append (NetWorker.url2String (getOnlineResource ()))
          .append ("\"/>")
          .append ("</MetadataURL>");

        return sb.toString ();
    }    
}