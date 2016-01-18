/**
 * GeopistaStandarReaderWriteFileDataSource.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.io.datasource;

import com.geopista.io.GeoGMLReader;
import com.geopista.io.GeoGMLWriter;
import com.vividsolutions.jump.io.JUMPReader;
import com.vividsolutions.jump.io.JUMPWriter;
import com.vividsolutions.jump.io.datasource.DelegatingCompressedFileHandler;
import com.vividsolutions.jump.io.datasource.StandardReaderWriterFileDataSource;

public class GeopistaStandarReaderWriteFileDataSource extends StandardReaderWriterFileDataSource
{
  public GeopistaStandarReaderWriteFileDataSource(
        JUMPReader reader,
            JUMPWriter writer,
            String[] extensions) {
            super(new DelegatingCompressedFileHandler(reader, toEndings(extensions)), writer, extensions);
            this.extensions = extensions;
    }

    public static class GeoGML extends GeopistaStandarReaderWriteFileDataSource {
        public GeoGML() {
            super(new GeoGMLReader(), new GeoGMLWriter(), new String[] { "gml" });
        }
    }

 
}