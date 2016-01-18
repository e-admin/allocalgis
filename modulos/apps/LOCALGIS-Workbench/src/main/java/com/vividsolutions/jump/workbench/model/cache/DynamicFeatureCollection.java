/**
 * DynamicFeatureCollection.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.model.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.SwingUtilities;

import com.geopista.app.AppContext;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.datastore.DataStoreConnection;
import com.vividsolutions.jump.datastore.DataStoreMetadata;
import com.vividsolutions.jump.datastore.FilterQuery;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.io.FeatureInputStream;
import com.vividsolutions.jump.util.ListWrapper;
import com.vividsolutions.jump.workbench.datastore.ConnectionDescriptor;
import com.vividsolutions.jump.workbench.datastore.ConnectionManager;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;

public class DynamicFeatureCollection implements FeatureCollection {
  private Integer featureLimit = null;

  private FilterQuery spatialQuery;

  private ConnectionManager connectionManager;
  private ConnectionDescriptor connectionDescriptor;

  public DynamicFeatureCollection(ConnectionDescriptor connectionDescriptor,
                                  ConnectionManager connectionManager, FilterQuery spatialQuery) {
    this.connectionManager = connectionManager;
    this.connectionDescriptor = connectionDescriptor;
    this.spatialQuery = spatialQuery;
  }

  public void setFeatureLimit(Integer featureLimit) {
    this.featureLimit = featureLimit;
  }

  private volatile Object currentQueryContext;

  private FeatureSchema schema = AddNewLayerPlugIn
                               .createBlankFeatureCollection().getFeatureSchema();

  public FeatureSchema getFeatureSchema() {
    return schema;
  }

  public List query(Envelope envelope) {
    final Object myQueryContext = new Object();
    currentQueryContext = myQueryContext;
    
    Envelope layerExtents = getEnvelope();
    if(layerExtents == null || layerExtents.isNull() || layerExtents.contains(envelope)){
    	spatialQuery.setFilterGeometry(AppContext.getApplicationContext().getGeometryFactory().toGeometry(envelope));
    }else{
    	// we are asking for too much data ...
    	spatialQuery.setFilterGeometry(AppContext.getApplicationContext().getGeometryFactory().toGeometry(layerExtents.intersection(envelope)));
    	
    }
    
    // Q: When do we close the stream? A: When a new stream is
    // requested. Implication: You cannot have two streams active from
    // the same DynamicFeatureCollection. But JUMP does not need this
    // capability. [Jon Aquino 2005-03-02]
    final FeatureInputStream myFeatureInputStream;
    try {
      myFeatureInputStream = connectionManager.getOpenConnection(
          connectionDescriptor).execute(spatialQuery);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    // Sometimes #execute takes a long time (e.g. SDE), and other calls to
    // #query may have occurred. [Jon Aquino 2005-03-15]
    if (myQueryContext != currentQueryContext) {
      return Collections.EMPTY_LIST;
    }
    schema = myFeatureInputStream.getFeatureSchema();
    return new ListWrapper() {
      public Collection getCollection() {
        // Implement #iterator only [Jon Aquino 2005-03-03]
        throw new UnsupportedOperationException();
      }

      public Iterator iterator() {
        return new Iterator() {
          private int featuresReturned = 0;

          private boolean featureInputStreamOpen = true;

          public void remove() {
            throw new UnsupportedOperationException();
          }

          public boolean hasNext() {
            try {
              if (featureLimit != null
                  && featuresReturned >= featureLimit
                  .intValue()) {
                closeFeatureInputStream();
                return false;
              }
              if (myQueryContext != currentQueryContext) {
                closeFeatureInputStream();
                return false;
              }
              // Explicitly check if the stream is closed;
              // otherwise #hasNext will throw a
              // NullPointerException.
              // [Jon Aquino 2005-03-03]
              if (!featureInputStreamOpen) {
                return false;
              }
              if (!myFeatureInputStream.hasNext()) {
                closeFeatureInputStream();
                return false;
              }
              return true;
            } catch (Exception e) {
            	e.printStackTrace();
              throw new RuntimeException(e);
            }
          }

          private void closeFeatureInputStream() throws Exception {
            myFeatureInputStream.close();
            featureInputStreamOpen = false;
          }

          public Object next() {
            assertNotInGUIThread();
            if (!hasNext()) {
              throw new NoSuchElementException();
            }
            try {
              featuresReturned++;
              return myFeatureInputStream.next();
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          }
        };
      }
    };
  }

  public void add(Feature feature) {
    throw new UnsupportedOperationException();
  }

  public void addAll(Collection features) {
    throw new UnsupportedOperationException();
  }

  public void removeAll(Collection features) {
    throw new UnsupportedOperationException();
  }

  public void remove(Feature feature) {
    throw new UnsupportedOperationException();
  }

  public void clear() {
    throw new UnsupportedOperationException();
  }

  public Collection remove(Envelope env) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see com.vividsolutions.jump.feature.FeatureCollection#getEnvelope()
   */
  public Envelope getEnvelope() {
    DataStoreConnection dsc = null;
    try {
      dsc = connectionManager.getOpenConnection(connectionDescriptor);
    } catch (Exception e1) {
      // ignore
      return new Envelope();
    }
    Envelope e = null;
    if(dsc != null){
      DataStoreMetadata dsm = dsc.getMetadata();
      if(dsm != null && spatialQuery != null)
        e = dsm.getExtents(spatialQuery.getDatasetName(), spatialQuery.getGeometryAttributeName());
    }
    return e;
  }

  public int size() {
    throw new UnsupportedOperationException();
  }

  public boolean isEmpty() {
    throw new UnsupportedOperationException();
  }

  public List getFeatures() {
    throw new UnsupportedOperationException();
  }

  public Iterator iterator() {
    throw new UnsupportedOperationException();
  }

  private void assertNotInGUIThread() {
    Assert.isTrue(!SwingUtilities.isEventDispatchThread(),
                  "This operation should be done outside of the GUI thread");
  }

@Override
public void add(Feature feature, boolean quietly) {
	// TODO Auto-generated method stub
	
}

@Override
public Iterator queryIterator(Envelope envelope) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void remove(Feature feature, boolean quietly) {
	// TODO Auto-generated method stub
	
}
}