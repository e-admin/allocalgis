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
package org.deegree_impl.services.wfs.mapinfo;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureCollection;
import org.deegree.model.feature.FeatureTypeProperty;
import org.deegree.model.geometry.GM_Object;
import org.deegree.services.OGCWebServiceException;
import org.deegree.services.OGCWebServiceRequest;
import org.deegree.services.OGCWebServiceResponse;
import org.deegree.services.wfs.DataStoreException;
import org.deegree.services.wfs.WFSConstants;
import org.deegree.services.wfs.configuration.FeatureType;
import org.deegree.services.wfs.protocol.WFSDescribeFeatureTypeRequest;
import org.deegree.services.wfs.protocol.WFSGetFeatureRequest;
import org.deegree.services.wfs.protocol.WFSGetFeatureWithLockRequest;
import org.deegree.services.wfs.protocol.WFSLockFeatureRequest;
import org.deegree.services.wfs.protocol.WFSQuery;
import org.deegree.services.wfs.protocol.WFSTransactionRequest;
import org.deegree.tools.ParameterList;
import org.deegree_impl.io.mapinfo.MapInfoDataSource;
import org.deegree_impl.model.cs.Adapters;
import org.deegree_impl.model.cs.ConvenienceCSFactory;
import org.deegree_impl.model.cs.CoordinateSystem;
import org.deegree_impl.model.feature.FeatureFactory;
import org.deegree_impl.model.geometry.GM_Object_Impl;
import org.deegree_impl.services.OGCWebServiceException_Impl;
import org.deegree_impl.services.wfs.AbstractDataStore;
import org.deegree_impl.services.wfs.AbstractDescribeFeatureType;
import org.deegree_impl.services.wfs.AbstractGetFeature;
import org.deegree_impl.services.wfs.WFSMainLoop;
import org.deegree_impl.services.wfs.protocol.WFSProtocolFactory;
import org.deegree_impl.tools.Debug;
import org.deegree_impl.tools.ParameterList_Impl;
import org.opengis.cs.CS_CoordinateSystem;
import org.w3c.dom.Document;

/**
 * The class provides reading and writing access to ESRI shapefiles.
 * The access is capsulated within the query and transaction mechanism
 * described at the OGC WFS specifications.<p></p>
 * The data store uses a cache for keeping features once read from a
 * shapefile in memory to provide a much faster access to them. The cache
 * can be configured through the configuration XML-document that also contains
 * the names and locations of the shapes handled by an instance of the
 * <tt>MapInfoDataStore</tt>.
 * <p>
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:35 $
 */
public class MapInfoDataStore extends AbstractDataStore {    
    
    public MapInfoDataStore(URL config) throws DataStoreException {    
        super( config );
    }
     
    /**
     * returns the describtion of one or more feature types
     *
     * @param request conainting the list of feature types that should be described
     */
    public void describeFeatureType(WFSDescribeFeatureTypeRequest request) {
        Thread thread = new CDescribeFeatureType( this, request );
        thread.start();
    }
    
    /**
     * returns the features that matches the submitted request
     * 
     * @param request containing the request for zero, one or more features. The request,
     *				 may contains a filter that describes the request more detailed
     */
    public void getFeature(WFSGetFeatureRequest request) {
        Thread thread = new CGetFeature( this, request );
        thread.start();
    }
    
    /**
     * same as <tt>getFeature(..)</tt> but locking the feature during processing.
     * @see #getFeature
     *
     * @param request containing the request for zero, one or more features.
     * 				 The request, may contains a filter that describes the
     *				 request more detailed.
     */
    public void getFeatureWithLock(WFSGetFeatureWithLockRequest request) {
        Thread thread = new CGetFeatureWithLock( this, request );
        thread.start();
    }
    
    /**
     * performs a transaction against the data store. This could be an update,
     * an insert or a delete of one or more features.
     *
     * @param request containing the transaction instruction(s)
     */
    public void transaction(WFSTransactionRequest request) {
        Thread thread = new CTransaction( this, request );
        thread.start();
    }
    
    /**
     * performs the locking/unlocking of one or more features.
     *
     * @param request the features that should be (un)locked
     */
    public void lockFeature(WFSLockFeatureRequest request) {
        Thread thread = new CLockFeature( this, request );
        thread.start();
    }
    
    //////////////////////////////////////////////////////////////////////////
    //                           inner classes 								//
    //////////////////////////////////////////////////////////////////////////
    
    /**
     * inner interface defining the processing of a DescribeFeatureType request
     */
    private class CDescribeFeatureType extends AbstractDescribeFeatureType {
        
        public CDescribeFeatureType(MapInfoDataStore parent, OGCWebServiceRequest request) {
            super( parent, request );
        }
        
        /**
         * creates a xml schema definition of the submitted feature type on the fly
         */
        protected Document createSchema(String featureType) throws Exception {
            throw new Exception( "At the moment createSchema is a not supported " +
            "function." );
        }
        
    }
    
    /**
     * inner interface defining the processing of a getFeature request
     */
    private class CGetFeature extends AbstractGetFeature {
        
        public CGetFeature(MapInfoDataStore parent, OGCWebServiceRequest request) {
            super( parent, request );
        }
        
        protected OGCWebServiceResponse[] performRequest(OGCWebServiceRequest request) {
            Debug.debugMethodBegin( this, "performRequest" );
    
            HashMap map = new HashMap();
            OGCWebServiceResponse[] response = null;
            String[] affectedFeatureTypes = null;
            WFSGetFeatureRequest req = (WFSGetFeatureRequest)request;
            try {
                
                WFSQuery[] queries = req.getQuery();
                affectedFeatureTypes = getAffectedFeatureTypes( queries );
                int startPosition = req.getStartPosition();
                int maxFeatures = req.getMaxFeatures();
                // get describtions for each feature type that's handled
                // by this data store
                for (int i = 0; i < queries.length; i++) {
                    if ( isKnownFeatureType( queries[i].getTypeName() ) ) {
                        
                        FeatureType ft = config.getFeatureType( queries[i].getTypeName() );

						// perform query and return result as table model
                        Feature[] features = getFeatures ( queries[i], startPosition, maxFeatures );                        
                        
                        ParameterList pl = new ParameterList_Impl();
                        pl.addParameter( WFSConstants.FEATURES, features );
                        pl.addParameter( WFSConstants.FEATURETYPE, ft );
                        pl.addParameter( WFSConstants.CRS, ft.getCRS() );
                        // transform table model to GMLFeatureCollection
                        // and put it onto a HashMap associated with its
                        // table (feature type) name.
                        map.put( queries[i].getTypeName(), pl );
                        
                    }
                }
                
                // create response
                response = createResponse( map, affectedFeatureTypes );
                
            } catch(Exception e) {
                Debug.debugException( e, null );
                OGCWebServiceException exce =
                new OGCWebServiceException_Impl( "CGetFeature: performRequest", e.toString() );
                
                response = new OGCWebServiceResponse[1];
                response[0] = 
                	WFSProtocolFactory.createWFSGetFeatureResponse(request, affectedFeatureTypes,
                                                        exce, null);
            }
            
            Debug.debugMethodEnd();
            return response;
        }              
		
		/**
		 * Collects the <tt>Feature</tt>s from the shape file that match the
		 * given <tt>Filter</tt>.
		 * <p>
		 * To make use of the indexing of the shapefile, the <tt>Filter</tt>
		 * is examined and if a BBOX-Operation is found it is used on the
		 * index of the shapefile.
		 * <p>
		 * @param query query object to limit the amount of returned features
		 * @param startPosition
		 * @param maxFeatures
		 * @return
		 * @throws Exception
		 */
		private Feature [] getFeatures (WFSQuery query, int startPosition, 
                                        int maxFeatures) throws Exception {

			Debug.debugMethodBegin( this, "getFeatures" );

			// initialize shape file access
			FeatureType ft = config.getFeatureType ( query.getTypeName() );
            URL url = new URL( ft.getMasterTable().getName() );
        
            MapInfoDataSource mif = new MapInfoDataSource( url );
            
            FeatureFactory fac = new FeatureFactory();
            FeatureCollection collection = fac.createFeatureCollection( "ID", 100 );
            mif.getFeatures( collection, query );
            
			// get coordinate system associated to the feature type
			String crsName = ft.getCRS ();
			CS_CoordinateSystem crs = null;
			try {
				ConvenienceCSFactory csFactory 	= ConvenienceCSFactory.getInstance();
				CoordinateSystem cs = csFactory.getCSByName( crsName ); 
				crs = Adapters.getDefault().export( cs );
			} catch(Exception e) {
				System.out.println(e);
			}

			// check parameters for sanity
			if (startPosition < 0) startPosition = 0;            
			if (maxFeatures < 0 || maxFeatures >= collection.getSize() ) {
				maxFeatures = collection.getSize();
			}

			ArrayList features = new ArrayList( maxFeatures );
 
			// collect features that match the filter
			String tn = ft.getMasterTable().getTargetName();    

			for (int i = startPosition; i < maxFeatures; i++) {
                Feature feature = collection.getFeature( i );
                if (feature.getGeometryProperties().length > 0 ) {
                    GM_Object geo = feature.getGeometryProperties()[0];
                    ((GM_Object_Impl)geo).setCoordinateSystem( crs );
                }
                features.add (feature);				
			}            			
            
			Debug.debugMethodEnd();

			// create and return feature array
			return (Feature []) features.toArray(new Feature [features.size ()]);
		}
                
        private org.deegree.model.feature.FeatureType createFeatureType(String name, String[] names, String[] types) {
            org.deegree_impl.model.feature.FeatureFactory factory = 
                new org.deegree_impl.model.feature.FeatureFactory();
            // create feature type
            FeatureTypeProperty[] ftp = new FeatureTypeProperty[names.length+1];
            for (int j = 0; j < names.length; j++) {
                if ( types[j].equalsIgnoreCase("N") ) {
                    types[j] = "java.lang.Double";
                }
                else
                    if ( types[j].equalsIgnoreCase("C") ) {
                        types[j] = "java.lang.String";
                    }
                ftp[j] = factory.createFeatureTypeProperty( names[j], types[j], true );
            }
            ftp[ftp.length-1] = 
                factory.createFeatureTypeProperty( "GEOM", "de.deegree.model.geometry.GM_Object", 
                                                   true );
            
            return factory.createFeatureType( null, null, name, ftp );
            
        }
        
    }
    
    /**
     * inner interface defining the processing of a transaction request
     */
    private class CTransaction extends WFSMainLoop {
        
        public CTransaction(MapInfoDataStore parent, OGCWebServiceRequest request) {
            super( parent, request );
        }
        
        protected OGCWebServiceResponse[] performRequest(OGCWebServiceRequest request) {
            return null;
        }
        
    }
    
    /**
     * inner interface defining the processing of a lockFeature request
     */
    private class CLockFeature extends WFSMainLoop {
        
        public CLockFeature(MapInfoDataStore parent, OGCWebServiceRequest request) {
            super( parent, request );
        }
        
        protected OGCWebServiceResponse[] performRequest(OGCWebServiceRequest request) {
            return null;
        }
        
    }
    
    /**
     * class defining the processing of a getFeature request
     */
    class CGetFeatureWithLock extends WFSMainLoop  {
        
        public CGetFeatureWithLock(MapInfoDataStore parent, OGCWebServiceRequest request) {
            super( parent, request );
        }
        
        protected OGCWebServiceResponse[] performRequest(OGCWebServiceRequest request) {
            return null;
        }
        
    }
}
