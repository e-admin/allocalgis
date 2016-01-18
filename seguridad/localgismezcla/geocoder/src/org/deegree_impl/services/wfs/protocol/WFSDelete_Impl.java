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

import org.deegree.services.wfs.filterencoding.Filter;
import org.deegree.services.wfs.protocol.*;


/**
* 
*
* <p>--------------------------------------------------------</p>
*
* @author Katharina Lupp<a href="mailto:k.lupp@web.de>Katharina Lupp</a>
* @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
*/
class WFSDelete_Impl implements WFSDelete {
    private Filter filter = null;
    private String typeName = null;

    /**
    * constructor initializing the class with the <WFSDelete>
    */
    WFSDelete_Impl(Filter filter, String typeName) {
        setFilter(filter);
        setTypeName(typeName);
    }

    /**
     * A filter specification describes a set of features to operate upon. 
     * The format of the filter is defined in the OGC Filter Encoding 
     * Specification. Optional. No default. Prerequisite: TYPENAME    
     */
    public Filter getFilter() {
        return filter;
    }

    /**
    * sets the <filter>
    */
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    /**
     * The typeName attribute is used to indicate the name of the feature type 
     * or class to be deleted.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
    * sets the typeName
    */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    public String toString() {
        return "WFSDelete: " + typeName+ "\n" + filter.toXML();
    }
}