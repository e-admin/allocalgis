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
package org.deegree_impl.services.wfs.configuration;

import org.deegree.services.wfs.configuration.GeoFieldIdentifier;


/**
 *
 *
 * <p>---------------------------------------------------------------------</p>
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:32 $
 */
public class GeoFieldIdentifier_Impl implements GeoFieldIdentifier {
    private String datastoreFieldBaseName = null;
    private int dimension = 0;

    /**
     * Creates a new GeoFieldIdentifier_Impl object.
     *
     * @param datastoreFieldBaseName 
     * @param dimension 
     */
    GeoFieldIdentifier_Impl( String datastoreFieldBaseName, int dimension ) {
        setDatastoreFieldBaseName( datastoreFieldBaseName );
        setDimension( dimension );
    }

    /**
     * returns the root of the geo field name(s). example: if a POINTDB is
     * used where the x- and y-value are stored in columns named col_x and
     * col_y the base name will be 'col'.
     */
    public String getDatastoreFieldBaseName() {
        return datastoreFieldBaseName;
    }

    /**
     * @see getDatastoreFieldBaseName
     */
    public void setDatastoreFieldBaseName( String datastoreFieldBaseName ) {
        this.datastoreFieldBaseName = datastoreFieldBaseName;
    }

    /**
     * returns the dimension of the geomtries stored. supported are two-
     * and three-dimensional geometries.
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * @see getDimension
     */
    public void setDimension( int dimension ) {
        this.dimension = dimension;
    }

    /**
     *
     *
     * @return 
     */
    public String toString() {
        return datastoreFieldBaseName;
    }
}