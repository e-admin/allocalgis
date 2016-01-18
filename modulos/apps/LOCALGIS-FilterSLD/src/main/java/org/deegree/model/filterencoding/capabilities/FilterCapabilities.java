/**
 * FilterCapabilities.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
// $HeadURL:
// /cvsroot/deegree/src/org/deegree/ogcwebservices/getcapabilities/Contents.java,v
// 1.1 2004/06/23 11:55:40 mschneider Exp $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2006 by:
 EXSE, Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
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
 lat/lon GmbH
 Aennchenstr. 19
 53115 Bonn
 Germany
 E-Mail: poth@lat-lon.de

 Prof. Dr. Klaus Greve
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: greve@giub.uni-bonn.de

 
 ---------------------------------------------------------------------------*/
package org.deegree.model.filterencoding.capabilities;

/**
 * FilterCapabilitiesBean used to represent <code>Filter<code> expressions according to the
 * 1.0.0 as well as the 1.1.1 <code>Filter Encoding Implementation Specification</code>.
 * 
 * 
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe</a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * 
 * @author last edited by: $Author: satec $
 * 
 * @version 2.0, $Revision: 1.1 $, $Date: 2011/09/19 13:47:32 $
 * 
 * @since 2.0
 */
public class FilterCapabilities {

    public static final String VERSION_100 = "1.0.0";

    public static final String VERSION_110 = "1.1.0";

    private ScalarCapabilities scalarCapabilities;

    private SpatialCapabilities spatialCapabilities;

    private IdCapabilities idCapabilities;

    private String version;

    /**
     * Constructs a new <code>FilterCapabilities</code> -instance with the given parameters. Used
     * for filter expressions according to the 1.0.0 specification that don't have an
     * <code>Id_Capabilities</code> section.
     * 
     * @param scalarCapabilities
     * @param spatialCapabilities
     */
    public FilterCapabilities( ScalarCapabilities scalarCapabilities, SpatialCapabilities spatialCapabilities ) {
        this.scalarCapabilities = scalarCapabilities;
        this.spatialCapabilities = spatialCapabilities;
        this.version = VERSION_100;
    }

    /**
     * Constructs a new <code>FilterCapabilities</code> -instance with the given parameters. Used
     * for filter expressions according to the 1.1.0 specification that have an
     * <code>Id_Capabilities</code> section.
     * 
     * @param scalarCapabilities
     * @param spatialCapabilities
     * @param idCapabilities
     */
    public FilterCapabilities( ScalarCapabilities scalarCapabilities, SpatialCapabilities spatialCapabilities,
                               IdCapabilities idCapabilities ) {
        this.scalarCapabilities = scalarCapabilities;
        this.spatialCapabilities = spatialCapabilities;
        this.idCapabilities = idCapabilities;
        this.version = VERSION_110;
    }

    /**
     * @return scalarCapabilities
     * 
     */
    public ScalarCapabilities getScalarCapabilities() {
        return scalarCapabilities;
    }

    /**
     * @return spatialCapabilities
     * 
     */
    public SpatialCapabilities getSpatialCapabilities() {
        return spatialCapabilities;
    }

    /**
     * @param capabilities
     * 
     */
    public void setScalarCapabilities( ScalarCapabilities capabilities ) {
        scalarCapabilities = capabilities;
    }

    /**
     * @param capabilities
     * 
     */
    public void setSpatialCapabilities( SpatialCapabilities capabilities ) {
        spatialCapabilities = capabilities;
    }

    /**
     * @return Returns the idCapabilities.
     */
    public IdCapabilities getIdCapabilities() {
        return idCapabilities;
    }

    /**
     * @param idCapabilities
     *            The idCapabilities to set.
     */
    public void setIdCapabilities( IdCapabilities idCapabilities ) {
        this.idCapabilities = idCapabilities;
    }

    /**
     * @return Returns the version.
     */
    public String getVersion() {
        return version;
    }
}
