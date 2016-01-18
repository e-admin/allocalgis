/**
 * DGNReader.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 14-jun-2004
 */
package com.geopista.ui.plugin.io.dgn;

import java.util.Iterator;

import com.geopista.ui.plugin.io.dgn.impl.DGNReader_Impl;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.io.IllegalParametersException;

/**
 * @author Enxenio, SL
 */
public class DGNReader {
	private DGNReader_Impl _reader; 

	public DGNReader() {
    	_reader = null;
    }

   public void read(DriverProperties dp) throws Exception {
       String dgnFileName = dp.getProperty("File");

       if (dgnFileName == null) {
           dgnFileName = dp.getProperty("DefaultValue");
       }

       if (dgnFileName == null) {
           throw new IllegalParametersException("no File property specified");
       }

       GeometryFactory factory = new GeometryFactory();
       _reader = new DGNReader_Impl(dgnFileName, factory);
       _reader.readElements();
    }
   
   public Iterator getFeatureTypesIterator() {
   		Iterator result = null;
   		if (_reader != null) {
   			result = _reader.getFeatureTypesIterator();
   		}
   		return result;
   }
   
   public String getFeatureCollectionName(FeatureCollection fc) {
		String result = null;
   		if (_reader != null) {
   			result = _reader.getFeatureCollectionName(fc);
   		}
   		return result;
   }
}