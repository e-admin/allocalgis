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
import org.deegree.services.wfs.filterencoding.Filter;
import org.deegree.services.wfs.filterencoding.FilterConstructionException;

/**
 * Abstract superclass representing <Filter> elements (as defined in the Filter DTD). 
 * A <Filter> element either consists of (one or more) FeatureId-elements or
 * one operation-element. This is reflected in the two implementations FeatureFilter
 * and ComplexFilter.
 * @author Markus Schneider
 * @version 06.08.2002
 */
public abstract class AbstractFilter implements Filter {

    /**
     * Given a DOM-fragment, a corresponding Filter-object is built. This method
     * recursively calls other buildFromDOM () - methods to validate the structure
     * of the DOM-fragment.
     * @throws FilterConstructionException if the structure of the DOM-fragment is invalid
     */
    public static Filter buildFromDOM (Element element) throws FilterConstructionException {        
        Filter filter = null;

        // check if root element's name equals 'filter'
        if (!element.getLocalName().equals ("Filter"))
            throw new FilterConstructionException ("Name of element does not equal 'Filter'!"); 
        
        // determine type of Filter (FeatureFilter / ComplexFilter)        
        Element firstElement = null;
        NodeList children = element.getChildNodes ();        
        for (int i = 0; i < children.getLength (); i++) {
            if (children.item (i).getNodeType () == Node.ELEMENT_NODE) {
                firstElement = (Element) children.item (i);
            }
        }
        if (firstElement == null) throw new FilterConstructionException ("Filter Node is empty!");
      
        if (firstElement.getLocalName().equals ("FeatureId")) {
            // must be a FeatureFilter
            FeatureFilter fFilter = new FeatureFilter ();
            children = element.getChildNodes ();
            for (int i = 0; i < children.getLength (); i++) {
                if (children.item(i).getNodeType () == Node.ELEMENT_NODE) {
                    Element fid = (Element) children.item (i);
                    if (!fid.getLocalName().equals ("FeatureId"))
                        throw new FilterConstructionException ("Unexpected Element encountered: " + fid.getLocalName());
                    fFilter.addFeatureId (FeatureId.buildFromDOM (fid));
                }
            }
            filter = fFilter;
        }
        else {
            // must be a ComplexFilter
            children = element.getChildNodes ();
            boolean justOne = false;
            for (int i = 0; i < children.getLength (); i++) {
                if (children.item(i).getNodeType () == Node.ELEMENT_NODE) {
                    Element operator = (Element) children.item (i);
                    if (justOne)
                        throw new FilterConstructionException ("Unexpected element encountered: " + operator.getLocalName());
                    ComplexFilter cFilter = new ComplexFilter (AbstractOperation.buildFromDOM (operator));
                    filter = cFilter;
                    justOne = true;
                }
            }
        }
        return filter;
    }

    /** Produces an indented XML representation of this object. */
    public abstract StringBuffer toXML ();
}
