package com.geopista.io;


/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
*/

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

