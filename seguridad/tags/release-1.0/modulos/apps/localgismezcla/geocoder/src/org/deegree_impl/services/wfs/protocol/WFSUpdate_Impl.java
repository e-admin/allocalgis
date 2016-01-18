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
package org.deegree_impl.services.wfs.protocol;

import java.util.HashMap;
import org.deegree.services.wfs.protocol.WFSUpdate;
import org.deegree.services.wfs.filterencoding.Filter;

/**
 * Incarnation of a wfs:Update-element.
 * <p>
 * @author <a href="mailto:mschneider@lat-lon.de>Markus Schneider</a>
 * @version $Revision: 1.1 $
 */
public class WFSUpdate_Impl implements WFSUpdate {

    private String typeName;
    private HashMap properties;
    private Filter filter;

    /**
     * Create a new instance of <tt>WFSUpdate_Impl</tt>.
     * <p>
     * @param typeName value of the typeName-Attribute (FeatureType-name)
     * @param properties name/value-pairs of fields to be changed
     * @param filter wfs:Filter-Expression to be applied
     */
    protected WFSUpdate_Impl (String typeName, HashMap properties,
                              Filter filter) {
        this.typeName = typeName;
        this.properties = properties;
        this.filter = filter;
    }
    
    /**
     * The typeName attribute is used to indicate the name of the feature type 
     * be updated.
     * <p>
     * @return the typeName attribute's value
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Name/value-pairs that indicate which attributes are to be changed.
     * <p>
     * @return keys are names, values describe the value to be set
     */
    public HashMap getProperties () {
        return properties;
    }
    
    /**
     * A filter specification describes a set of features to operate upon. 
     * The format of the filter is defined in the OGC Filter Encoding 
     * Specification. Optional. No default. Prerequisite: TYPENAME
     * <p>
     * @return the filter expression to be applied or null
     */
    public Filter getFilter() {
        return filter;
    }
    
    public String toString() {
        return "WFSUpdate: " + typeName+ "\n" + properties + "\n" + filter.toXML();
    }
}