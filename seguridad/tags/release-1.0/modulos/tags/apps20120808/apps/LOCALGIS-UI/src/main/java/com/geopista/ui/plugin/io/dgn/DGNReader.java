/*
 * Created on 14-jun-2004
 */
package com.geopista.ui.plugin.io.dgn;

import java.util.Collection;
import java.util.Iterator;

import com.geopista.ui.plugin.io.dgn.impl.DGNReader_Impl;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.io.GMLReader;
import com.vividsolutions.jump.io.IllegalParametersException;
import com.vividsolutions.jump.io.JUMPReader;

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