/**
 * GeoGMLWriter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.io;

import java.util.Collection;
import java.util.Iterator;

import com.geopista.feature.GeopistaFeature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.io.GMLWriter;
import com.vividsolutions.jump.io.IllegalParametersException;
import com.vividsolutions.jump.io.JUMPWriter;


public class GeoGMLWriter implements JUMPWriter {
    /** Creates new GeoGMLWriter */
    public GeoGMLWriter() {
    }

    /**
     *  Writes the feature collection to the specified file in JML format.
     * @param featureCollection features to write
     * @param dp 'OutputFile' or 'DefaultValue' to specify what file to write.
     */
    public void write(FeatureCollection featureCollection, DriverProperties dp)
        throws IllegalParametersException, Exception {
        GMLWriter gmlWriter;
        String outputFname;

        outputFname = dp.getProperty("File");

        if (outputFname == null) {
            outputFname = dp.getProperty("DefaultValue");
        }

        if (outputFname == null) {
            throw new IllegalParametersException(
                "call to JMLWriter.write() has DataProperties w/o a OutputFile specified");
        }

        gmlWriter = new GMLWriter();

        //desbloqueamos las features para poder grabar todos los attributos
        Collection lockedFeatures = featureCollection.getFeatures();
        Iterator lockedFeaturesIter = lockedFeatures.iterator();
        while (lockedFeaturesIter.hasNext())
        {
          GeopistaFeature actualFeature = (GeopistaFeature) lockedFeaturesIter.next();
          actualFeature.setLockedFeature(false);
        }

        gmlWriter.write(featureCollection, dp);
        
        //bloqueamos las features para poder aplicar tipos de acceso
        lockedFeatures = featureCollection.getFeatures();
        lockedFeaturesIter = lockedFeatures.iterator();
        while (lockedFeaturesIter.hasNext())
        {
          GeopistaFeature actualFeature = (GeopistaFeature) lockedFeaturesIter.next();
          actualFeature.setLockedFeature(true);
        }
    }
}
