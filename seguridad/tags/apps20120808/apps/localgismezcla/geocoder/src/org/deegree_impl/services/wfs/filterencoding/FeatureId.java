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
package org.deegree_impl.services.wfs.filterencoding;

import org.w3c.dom.*;
import org.deegree.services.wfs.filterencoding.*;

/**
 * Encapsulates the information of a <FeatureId> element as defined in the
 * FeatureId DTD.
 * The <FeatureId> element is used to encode the unique identifier for 
 * any feature instance. Within a filter expression, the <FeatureId> 
 * is used as a reference to a particular feature instance.
 * 
 * @author Markus Schneider
 * @version 06.08.2002
 */
public class FeatureId {
	
    /** The FeatureId's value. */
    private String value;

    /** Constructs a new FeatureId. */
    public FeatureId (String value) {
        this.value = value;
    }

    /**
     * Given a DOM-fragment, a corresponding Expression-object is built. This method
     * recursively calls other buildFromDOM () - methods to validate the structure
     * of the DOM-fragment.
     * @throws FilterConstructionException if the structure of the DOM-fragment is invalid
     */  
    public static FeatureId buildFromDOM (Element element) throws FilterConstructionException {        
        Filter filter = null;

        // check if root element's name equals 'FeatureId'
        if (!element.getLocalName().toLowerCase ().equals ("featureid"))
            throw new FilterConstructionException ("Name of element does not equal 'FeatureId'!");

        // determine the value of the FeatureId
        String fid = element.getAttribute ("fid");
        if (fid == null) throw new FilterConstructionException ("<FeatureId> requires 'fid'-attribute!");
                
        return new FeatureId (fid);
    }
 	
   /**
    * Returns the feature id. A feature id is built from it's feature type
    * name and it's id separated by a ".". e.g. Road.A565
    */	
    public String getValue () {
        return value;
    }
	
   /**
    * @see #getValue
    */	
    public void setValue (String value) {
        this.value = value;
    }

    /** Produces an indented XML representation of this object. */
    public StringBuffer toXML () {
        StringBuffer sb = new StringBuffer ();
        sb.append ("<ogc:FeatureId fid=\"").append (value).append ("\"/>");
        return sb;
    }
}
