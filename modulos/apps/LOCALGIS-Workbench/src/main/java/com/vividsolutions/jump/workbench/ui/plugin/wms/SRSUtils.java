/**
 * SRSUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.plugin.wms;

import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.impl.PredefinedCoordinateSystems;


public class SRSUtils {

    //
    // If the coordinate system string has the form "EPSG:someNumber" then see
    // if we can get that number and create a more human readable string.
    //
    public static String getName( String srsCode ) {
        final String epsg = "EPSG:";
        String stringToShow = srsCode;

        if ( srsCode.startsWith( epsg ) ) {
            String intPart = srsCode.substring( 5, srsCode.length() );

            try {
                int epsgCode = Integer.parseInt( intPart );
                CoordinateSystem cs = PredefinedCoordinateSystems.getCoordinateSystem( epsgCode );

                if ( cs != null ) {
                    stringToShow = cs.getName();
                }
            } catch ( Exception ignored ){
                // do nothing
            }
        }

        return stringToShow;
    }
}