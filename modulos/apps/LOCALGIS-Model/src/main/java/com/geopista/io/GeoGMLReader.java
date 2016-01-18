/**
 * GeoGMLReader.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.io;


import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.io.GMLReader;
import com.vividsolutions.jump.io.IllegalParametersException;
import com.vividsolutions.jump.io.JUMPReader;



public class GeoGMLReader implements JUMPReader {
    /** Creates new GeoGMLReader */
    public GeoGMLReader() {
    }

    /**
     * Read a JML file - passes the work off to {@link GMLReader}.
     *
     *@param dp 'InputFile' or 'DefaultValue' for the input JML file
     */
    public FeatureCollection read(DriverProperties dp)
        throws IllegalParametersException, Exception {
        GeoReader gmlReader;
        String inputFname;

        inputFname = dp.getProperty("File");

        if (inputFname == null) {
            inputFname = dp.getProperty("DefaultValue");
        }

        if (inputFname == null) {
            throw new IllegalParametersException(
                "call to JMLReader.read() has DataProperties w/o a InputFile specified");
        }

        gmlReader = new GeoReader();

        return gmlReader.read(dp);
    }
}

