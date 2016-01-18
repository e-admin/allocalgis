/*----------------    FILE HEADER  ------------------------------------------

This file has been provided to deegree by
Emanuele Tajariol e.tajariol@libero.it
 
 
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
package org.deegree_impl.services.wfs.bna;

import java.util.*;

import org.deegree.gml.*;
import org.deegree.services.wfs.*;
import org.deegree.tools.*;

import org.deegree_impl.gml.*;
import org.deegree_impl.tools.*;

import org.w3c.dom.*;


/**
 * Implements the DataStoreOutputFormat interface to format the result of a
 * data accessing class returned within the values of a HashMap as XML
 *
 * <p>-----------------------------------------------------------------------</p>
 *
 * @author Emanuele Tajariol
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:32 $
 * <p>
 */
public class BNADataStoreOutputXML extends BNADataStoreOutputGML implements DataStoreOutputFormat {
    /**
     * formats the data store at the values of the HashMap into
     * one single data structure.
     */
    public Object format( HashMap map, ParameterList paramList ) throws Exception {
        Debug.debugMethodBegin( this, "format" );

        // create the feature collection that will contain all requested
        // features (table rows)
        GMLFeatureCollection fc = (GMLFeatureCollection)super.format( map, paramList );
        Document doc = ( (GMLFeatureCollection_Impl)fc ).getAsElement().getOwnerDocument();

        Debug.debugMethodEnd();

        return doc;
    }
}