/*----------------    FILE HEADER  ------------------------------------------

This file is part of deegree.
Copyright (C) 2001 by:
EXSE, Department of Geography, University of Bonn
http://www.giub.uni-bonn.de/exse/
lat/lon Fitzke/Fretter/Poth GbR
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
lat/lon Fitzke/Fretter/Poth GbR
Meckenheimer Allee 176
53115 Bonn
Germany
E-Mail: poth@lat-lon.de

Jens Fitzke
Department of Geography
University of Bonn
Meckenheimer Allee 166
53115 Bonn
Germany
E-Mail: jens.fitzke@uni-bonn.de

                 
 ---------------------------------------------------------------------------*/
package org.deegree_impl.services.wfs;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.event.EventListenerList;

import org.deegree.services.Handler;
import org.deegree.services.OGCWebServiceEvent;
import org.deegree.services.OGCWebServiceRequest;
import org.deegree.services.wfs.DataStore;
import org.deegree.services.wfs.DataStoreException;
import org.deegree.services.wfs.configuration.DatastoreConfiguration;
import org.deegree.services.wfs.configuration.FeatureType;
import org.deegree.services.wfs.protocol.WFSDelete;
import org.deegree.services.wfs.protocol.WFSDescribeFeatureTypeRequest;
import org.deegree.services.wfs.protocol.WFSGetFeatureRequest;
import org.deegree.services.wfs.protocol.WFSGetFeatureWithLockRequest;
import org.deegree.services.wfs.protocol.WFSInsert;
import org.deegree.services.wfs.protocol.WFSLock;
import org.deegree.services.wfs.protocol.WFSLockFeatureRequest;
import org.deegree.services.wfs.protocol.WFSOperation;
import org.deegree.services.wfs.protocol.WFSQuery;
import org.deegree.services.wfs.protocol.WFSTransactionRequest;
import org.deegree.services.wfs.protocol.WFSUpdate;
import org.deegree_impl.io.DBConnectionPool;
import org.deegree_impl.services.wfs.configuration.ConfigurationFactory;


/**
 * the class implements the methods of the <tt>DataStore</tt> interface thats
 * implementation is common to all data stores and some additional methods that
 * will be used if the data store is controlled using observer/event handling
 * mechanism.
 *
 * <p>------------------------------------------------------------------------</p>
 * @author Andreas Poth
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:32 $
 * <p>
 */
abstract public class AbstractDataStore implements DataStore, Handler {
    protected DBConnectionPool pool = null;
    protected DatastoreConfiguration config = null;
    private EventListenerList listenerList = new EventListenerList();
    private List featureTypes = null;

    /**
     * Creates a new AbstractDataStore object.
     *
     * @param datastoreConfigSource 
     *
     * @throws DataStoreException 
     */
    protected AbstractDataStore( URL datastoreConfigSource ) throws DataStoreException {
        try {
            // get an instnace of the database connection pool
            pool = DBConnectionPool.getInstance();

            // get configuration of the datastore
            Reader reader = new InputStreamReader( datastoreConfigSource.openStream() );
            config = ConfigurationFactory.createDatastoreConfiguration( reader );
            reader.close();

            // write feature type names accessable through the data store to an
            // ArrayList for faster access
            FeatureType[] ft = config.getFeatureTypes();
            featureTypes = Collections.synchronizedList( new ArrayList( ft.length ) );

            for ( int i = 0; i < ft.length; i++ ) {
                featureTypes.add( ft[i].getName().trim() );
            }
        } catch ( Exception e ) {   
            e.printStackTrace();
            throw new DataStoreException( e );
        }
    }

    /**
     * returns the configuration of the datastore
     */
    public DatastoreConfiguration getConfiguration() {
        return config;
    }

    /**
     *
     *
     * @return 
     */
    public DBConnectionPool getConnectionPool() {
        return pool;
    }

    /**
     * registers a feature type to the data store.
     */
    public void registerFeatureType( String featureType ) {
        if ( !featureTypes.contains( featureType ) ) {
            featureTypes.add( featureType.trim() );            
        } 
    }

    /**
     * the inverse operation to <tt>registerFeatureType</tt>
     * @see registerFeatureType
     */
    public void removeFeatureType( String featureType ) {
        featureTypes.remove( featureType );
    }

    /**
     * returns true if the submitted feature type is known by the data store
     */
    public boolean isKnownFeatureType( String featureType ) {
        return featureTypes.contains( featureType );
    }

    /**
     * returns true if the handler is interested in a event
     */
    public boolean isInterested( OGCWebServiceEvent event ) {
        if ( event.getType() == OGCWebServiceEvent.RESPONSE ) {
            return false;
        } else {
            OGCWebServiceRequest request = event.getRequest();

            if ( request instanceof WFSDescribeFeatureTypeRequest ) {
                String[] typeNames = ( (WFSDescribeFeatureTypeRequest)request ).getTypeNames();

                for ( int i = 0; i < typeNames.length; i++ ) {
                    if ( isKnownFeatureType( typeNames[i] ) ) {
                        return true;
                    }
                }
            } else if ( request instanceof WFSGetFeatureWithLockRequest || 
                            request instanceof WFSGetFeatureRequest ) {
                WFSQuery[] queries = ( (WFSGetFeatureRequest)request ).getQuery();

                for ( int i = 0; i < queries.length; i++ ) {
                    if ( isKnownFeatureType( queries[i].getTypeName() ) ) {
                        return true;
                    }
                }
            } else if ( request instanceof WFSLockFeatureRequest ) {
                WFSLock[] locks = ( (WFSLockFeatureRequest)request ).getLocks();

                for ( int i = 0; i < locks.length; i++ ) {
                    if ( isKnownFeatureType( locks[i].getTypeName() ) ) {
                        return true;
                    }
                }
            } else if ( request instanceof WFSTransactionRequest ) {
                WFSOperation[] operations = ( (WFSTransactionRequest)request ).getOperations();

                // handle operations defined by the request                            
                for ( int i = 0; i < operations.length; i++ ) {
                    if ( operations[i] instanceof WFSInsert ) {
                        String[] ft = ( (WFSInsert)operations[i] ).getFeatureTypes();

                        for ( int j = 0; j < ft.length; j++ ) {
                            if ( isKnownFeatureType( ft[j] ) ) {
                                return true;
                            }
                        }
                    } else if ( operations[i] instanceof WFSUpdate ) {
                        WFSUpdate update = (WFSUpdate)operations[i];

                        if ( isKnownFeatureType( update.getTypeName() ) ) {
                            return true;
                        }
                    } else if ( operations[i] instanceof WFSDelete ) {
                        WFSDelete delete = (WFSDelete)operations[i];

                        if ( isKnownFeatureType( delete.getTypeName() ) ) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }
    }

    /**
     * registers a Handler so this Handler is able to act as a proxy
     * to the registered handler
     */
    public void registerHandler( Handler handler ) {
        listenerList.add( Handler.class, handler );
    }

    /**
     * @see registerHandler
     */
    public void removeHandler( Handler handler ) {
        listenerList.remove( Handler.class, handler );
    }

    /**
     * implements the <tt>handleRequest(..)</tt> method of the
     * <tt>WFSRequestListener</tt> interface. The method will be call
     * by the Dispatcher if a request against the WFS has been
     * recieved.
     */
    public void handleRequest( OGCWebServiceEvent event ) {
        OGCWebServiceRequest request = event.getRequest();

        if ( request instanceof WFSDescribeFeatureTypeRequest ) {
            describeFeatureType( (WFSDescribeFeatureTypeRequest)request );
        } else if ( request instanceof WFSGetFeatureWithLockRequest ) {
            getFeatureWithLock( (WFSGetFeatureWithLockRequest)request );
        } else if ( request instanceof WFSGetFeatureRequest ) {
            getFeature( (WFSGetFeatureRequest)request );
        } else if ( request instanceof WFSLockFeatureRequest ) {
            lockFeature( (WFSLockFeatureRequest)request );
        } else if ( request instanceof WFSTransactionRequest ) {
            transaction( (WFSTransactionRequest)request );
        }
    }

    /**
     *
     *
     * @param event 
     */
    public void handleResponse( OGCWebServiceEvent event ) {
        throw new NoSuchMethodError( "DataStore doesn't implement this method" );
    }

    /**
     * notifies the registered <tt>WFSResponseListener</tt> that
     * the peromance of request has been finished and submits the
     * result.
     */
    protected void fireResponse( OGCWebServiceEvent event ) {

        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        // Process the listeners last to first, notifying
        // those that are interested in this event     
        for ( int i = listeners.length - 2; i >= 0; i -= 2 ) {   
            if ( ( (Handler)listeners[i + 1] ).isInterested( event ) ) {      
                ( (Handler)listeners[i + 1] ).handleResponse( event );
            }
        }
    }
}