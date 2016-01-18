/*
 * Created on 18-ene-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.io.datasource;

import java.util.ArrayList;
import java.util.Collection;

import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.feature.IndexedFeatureCollection;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.io.JUMPReader;
import com.vividsolutions.jump.io.JUMPWriter;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.task.TaskMonitor;


/**
 * @author jalopez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GeopistaServerDataSource extends DataSource
{

       
        public GeopistaServerDataSource() {
            
        }   

        public Connection getConnection() {
            return new GeopistaConnection(getDriverProperties());
                
        }

        protected DriverProperties getReaderDriverProperties() {
            return getDriverProperties();
        }

        protected DriverProperties getWriterDriverProperties() {
            return getDriverProperties();
        }

        private DriverProperties getDriverProperties() {
            DriverProperties properties = new DriverProperties();
            properties.putAll(getProperties());
            return properties;
        }

    }



