/*----------------    FILE HEADER  ------------------------------------------

This file has been provided to deegree by
Emanuele Tajariol e.tajariol@libero.it
 
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
package org.deegree_impl.services.wfs.bna;

import java.net.URL;

import java.util.*;

import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureTypeProperty;
import org.deegree.model.geometry.*;
import org.deegree.services.*;
import org.deegree.services.wfs.DataStoreException;
import org.deegree.services.wfs.WFSConstants;
import org.deegree.services.wfs.configuration.FeatureType;
import org.deegree.services.wfs.filterencoding.Filter;
import org.deegree.services.wfs.filterencoding.Operation;
import org.deegree.services.wfs.protocol.*;
import org.deegree.tools.ParameterList;

import org.deegree_impl.io.bnaapi.*;
import org.deegree_impl.model.cs.*;
import org.deegree_impl.model.feature.FeatureFactory;
import org.deegree_impl.model.geometry.GM_SurfaceInterpolation_Impl;
import org.deegree_impl.model.geometry.GeometryFactory;
import org.deegree_impl.services.OGCWebServiceException_Impl;
import org.deegree_impl.services.wfs.*;
import org.deegree_impl.services.wfs.filterencoding.*;
import org.deegree_impl.services.wfs.protocol.WFSProtocolFactory;
import org.deegree_impl.tools.*;

import org.opengis.cs.CS_CoordinateSystem;

import org.w3c.dom.Document;


/**
 * The class provides reading access to BNA files.
 * The access is capsulated withing the query and transaction mechanism
 * described at the OGC WFS specifications.<p></p>
 * The data store uses a cache for keeping the entire featureset once read from a
 * BNA file in memory to provide a much fast access to them.
 * <p>---------------------------------------------------------------------</p>
 * This source was created by adapting ShapeDataStore.java
 * <p>---------------------------------------------------------------------</p>
 *
 * @author Emanuele Tajariol
 * @version $Id: BNADataStore.java,v 1.1 2009/07/09 07:25:32 miriamperez Exp $
 */
public class BNADataStore extends AbstractDataStore {
    //	static
    //	{
    //		Debug.level = Debug.ALL;
    //	}
    private Cache_Impl cache = null;

    /**
     * Creates a new BNADataStore object.
     *
     * @param configUrl 
     *
     * @throws DataStoreException 
     */
    public BNADataStore( URL configUrl ) throws DataStoreException {
        super( configUrl );

        // The cache will store whole dataset found in BNAfiles.
        // It's a good idea to keep the items number low.
        cache = new Cache_Impl( 10, 2 * 60 * 1000 );
    }

    //////////////////////////////////////////////////////////////////////////
    //                           DataStore APIs     								//
    //////////////////////////////////////////////////////////////////////////

    /**
     * returns the describtion of one or more feature types
     *
     * @param request conainting the list of feature types that should be described
     */
    public void describeFeatureType( WFSDescribeFeatureTypeRequest request ) {
        Thread thread = new CDescribeFeatureType( this, request );
        thread.start();
    }

    /**
     * returns the features that matches the submitted request
     *
     * @param request containing the request for zero, one or more features. The request,
     *                 may contains a filter that describes the request more detailed
     */
    public void getFeature( WFSGetFeatureRequest request ) {
        Thread thread = new CGetFeature( this, request );
        thread.start();
    }

    /**
     * same as <tt>getFeature(..)</tt> but locking the feature during processing.
     * @see #getFeature
     *
     * @param request containing the request for zero, one or more features.
     *                  The request, may contains a filter that describes the
     *                 request more detailed.
     */
    public void getFeatureWithLock( WFSGetFeatureWithLockRequest request ) {
        Thread thread = new CGetFeatureWithLock( this, request );
        thread.start();
    }

    /**
     * performs a transaction against the data store. This could be an update,
     * an insert or a delete of one or more features.
     *
     * @param request containing the transaction instruction(s)
     */
    public void transaction( WFSTransactionRequest request ) {
        Thread thread = new CTransaction( this, request );
        thread.start();
    }

    /**
     * performs the locking/unlocking of one or more features.
     *
     * @param request the features that should be (un)locked
     */
    public void lockFeature( WFSLockFeatureRequest request ) {
        Thread thread = new CLockFeature( this, request );
        thread.start();
    }

    //////////////////////////////////////////////////////////////////////////
    //                           inner classes 								      //
    //////////////////////////////////////////////////////////////////////////

    /***************************************************************************
     * inner interface defining the processing of a DescribeFeatureType request
     */
    private class CDescribeFeatureType extends AbstractDescribeFeatureType {
        /**
         * Creates a new CDescribeFeatureType object.
         *
         * @param parent 
         * @param request 
         */
        public CDescribeFeatureType( BNADataStore parent, OGCWebServiceRequest request ) {
            super( parent, request );
        }

        /**
         * creates a xml schema definition of the submitted feature type on the fly
         */
        protected Document createSchema( String featureType ) throws Exception {
            throw new Exception( "At the moment createSchema is a not supported " + "function." );
        }
    }


    /***************************************************************************
     * inner interface defining the processing of a getFeature request
     */
    private class CGetFeature extends AbstractGetFeature {
        /**
         * Creates a new CGetFeature object.
         *
         * @param parent 
         * @param request 
         */
        public CGetFeature( BNADataStore parent, OGCWebServiceRequest request ) {
            super( parent, request );
        }

        /**
         *
         *
         * @param request 
         *
         * @return 
         */
        protected OGCWebServiceResponse[] performRequest( OGCWebServiceRequest request ) {
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
                //System.out.println("=********** REQUEST **********=");
                //Filter xfilter =	req.getFilter();
                //if(xfilter == null)
                //{
                //	System.out.println("Filter: NONE");
                //}
                //else if (xfilter instanceof FeatureFilter)
                //{
                //	FeatureFilter ffilt = (FeatureFilter)xfilter;
                //	for (int i = 0; i < ffilt.getFeatureIds().size(); i++)
                //	{
                //		FeatureId fid = ((FeatureId)ffilt.getFeatureIds().get(i));
                //		System.out.println("FeatureFilter: "+ fid.extractFeatureTypeName() +"="+fid.extractId());
                //	}
                //}
                //else if(xfilter instanceof ComplexFilter)
                //{
                //	ComplexFilter filt = (ComplexFilter)xfilter;
                //	Operation op = filt.getOperation();
                //	System.out.println("ComplexFilter: "+ op.toXML(" "));
                //}
                //else
                //{
                //	System.out.println("UNK filter: "+xfilter.toXML(" "));
                //}
                for ( int i = 0; i < queries.length; i++ ) {
                    //System.out.println("========== QUERY " + i + "================");
                    //System.out.println("Filter: " + (queries[i].getFilter()==null? "NONE": "\n"+queries[i].getFilter().toXML("  ").toString()));
                    //System.out.println("HANDLE:   " 	+ queries[i].getHandle());
                    //System.out.println("TYPENAME: " 	+ queries[i].getTypeName());
                    //String pnames[] = queries[i].getPropertyNames();
                    //for (int j = 0; j < pnames.length; j++)
                    //	System.out.println(" --- PRNAME: " + pnames[j]);
                    //System.out.println("---------------------------------------------");
                    if ( isKnownFeatureType( queries[i].getTypeName() ) ) {
                        String typeName = queries[i].getTypeName();
                        FeatureType ft = config.getFeatureType( typeName );

                        // create one filter-object that contains the
                        // operations from both filters (if two filters are given)
                        Filter filter = null;
                        Filter filter1 = req.getFilter();
                        Filter filter2 = queries[i].getFilter();

                        if ( ( filter1 != null ) && ( filter2 != null ) ) {
                            filter = new ComplexFilter( (ComplexFilter)filter1, 
                                                        (ComplexFilter)filter2, 
                                                        OperationDefines.AND );
                        } else if ( filter1 != null ) {
                            filter = filter1;
                        } else if ( filter2 != null ) {
                            filter = filter2;
                        }

                        // perform query and return result as table model
                        Feature[] features = getFeatures( typeName, filter, startPosition, 
                                                          maxFeatures );

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
            } catch ( Exception e ) {
                Debug.debugException( e, null );

                OGCWebServiceException exce = new OGCWebServiceException_Impl( 
                                                      "CGetFeature: performRequest", e.toString() );
                response = new OGCWebServiceResponse[1];
                response[0] = WFSProtocolFactory.createWFSGetFeatureResponse( request, 
                												   affectedFeatureTypes, 
                                                                   exce, null );
            }

            Debug.debugMethodEnd();

            return response;
        }

        /**
         * Traverses the <tt>Filter</tt>-tree and returns the first
         * BBOX-Operation that is found and a <tt>Filter</tt> that
         * is equal to the given one minus the BBOX-Operation.
         * <p>
         * @param operation search starts here
         * @return [0]: <tt>GM_Envelope</tt> (BBOX), [1]: <tt>Filter</tt>
         * @throws Exception
         */
        private Object[] extractFirstBBOX( ComplexFilter filter ) throws Exception {
            Debug.debugMethodBegin( this, "extractFirstBBOX" );

            // [0]: GM_Envelope, [1]: Filter
            Object[] objects = new Object[2];
            objects[1] = filter;

            // sanity check (Filter empty)
            if ( filter == null ) {
                return objects;
            }

            // used as LIFO-queue
            Stack operations = new Stack();
            operations.push( filter.getOperation() );

            while ( !operations.isEmpty() ) {
                // get the first element of the queue
                Operation operation = (Operation)operations.pop();

                switch ( operation.getOperatorId() ) {
                    case OperationDefines.BBOX:
                    {
                        // found BBOX
                        objects[0] = ( (SpatialOperation)operation ).getBoundingBox();
                        break;
                    }
                    case OperationDefines.AND:
                    {
                        ArrayList arguments = ( (LogicalOperation)operation ).getArguments();

                        for ( int i = 0; i < arguments.size(); i++ ) {
                            operations.push( arguments.get( i ) );

                            if ( ( (Operation)arguments.get( i ) ).getOperatorId() == OperationDefines.BBOX ) {
                                // remove the BBOX-Operation from the AND-tree
                                System.out.println( "Removing: " + i );
                                arguments.remove( i );
                                break;
                            }
                        }

                        break;
                    }
                }

                // BBOX found?
                if ( objects[0] != null ) {
                    break;
                }
            }

            // special case: Filter contains only the BBOX-Operation
            if ( filter.getOperation().getOperatorId() == OperationDefines.BBOX ) {
                objects[1] = null;
            }

            Debug.debugMethodEnd();
            return objects;
        }

        /**
         * reads features from a bna file
         */
        private Feature[] getFeatures( String typeName, Filter filter, int startPosition, 
                                       int maxFeatures ) throws Exception {
            Debug.debugMethodBegin( this, "getFeatures" );

            // extract possible BBOX-Operation from Filter
            GM_Envelope bbox = null;

            if ( filter instanceof ComplexFilter ) {
                Object[] objects = extractFirstBBOX( (ComplexFilter)filter );
                bbox = (GM_Envelope)objects[0];
                filter = (Filter)objects[1];
            }

            //System.out.println("BNA::getFeatures");
            //System.out.println("queries: "+query);
            //System.out.println("CONFIG:  "+config);
            //System.out.println("REQ:     "+startPosition+" -> #"+maxFeatures);
            //System.out.println("FEATURETYPES:");
            //
            //FeatureType[] fts = config.getFeatureTypes();
            //for (int i = 0; i < fts.length; i++)
            //{
            //	System.out.println("=============== FT "+i);
            //	System.out.println(fts[i]);
            //	System.out.println("MASTERTABLE : "	+ fts[i].getMasterTable());
            //	System.out.println("MT.FILE     : "	+ fts[i].getMasterTable().getName());
            //	System.out.println("MT.ID       : "	+ fts[i].getMasterTable().getIdField());
            //	System.out.println("MT.TARGET   : "	+ fts[i].getMasterTable().getTargetName());
            //	GeoFieldIdentifier[] gfis = fts[i].getMasterTable().getGeoFieldIdentifier();
            //	if(gfis!=null)
            //		for (int j = 0; j < gfis.length; j++)
            //			System.out.println("   GFID "+j+": "+gfis[j]);
            //}
            // INIT
            FeatureType ft = config.getFeatureType( typeName );

            //System.out.println(" -- FILTER "+filter);
            //System.out.println(" -- FT "+ft);
            //System.out.println(" -- MT "+ft.getMasterTable());
            //System.out.println(" -- MTname "+ft.getMasterTable().getName());
            // initialize file access
            String target = ft.getMasterTable().getTargetName();

            BNAFeatureSet bnafs = (BNAFeatureSet)cache.get( target );

            if ( bnafs == null ) {
                String filename = ft.getMasterTable().getName();
                System.out.print( "Loading BNA file " + filename + "..." );

                BNAFile bnafile = new BNAFile( filename );
                bnafs = bnafile.getFeatureSet();
                System.out.println( "loaded." );
                cache.push( target, bnafs );
            }

            // get coordinate system associated to the feature type
            CS_CoordinateSystem crs = getCRS( ft );

            // pack geometries
            if ( startPosition < 0 ) {
                startPosition = 0;
            }

            if ( startPosition >= bnafs.size() ) {
                return new Feature[0];
            }

            if ( ( maxFeatures < 0 ) || ( maxFeatures >= bnafs.size() ) ) {
                maxFeatures = bnafs.size();
            }

            ArrayList retval = new ArrayList();

            System.out.println( "Requested " + startPosition + "--(" + maxFeatures + 
                                ")-->x features - Showing the first 10..." );

            if ( bbox != null ) {
                System.out.println( "FILTERING BY BBOX " + bbox );
            }

            int skippedPos = 0; // needed to check when we get to the startPosition-th element

            for ( int i = 0; ( i < bnafs.size() ) && ( retval.size() < maxFeatures ); i++ ) {
                BNAFeature bnafeature = bnafs.getFeature( i );
                Feature feature = createFeature( bnafeature, crs );

                // FXIME: maybe we don't need to split the filter in bbox+world. Filtering could be applied on original filters only.
                // Apply bbox filter
                if ( ( bbox != null ) && 
                         !feature.getDefaultGeometryProperty().getEnvelope().intersects( bbox ) ) {
                    //System.out.println(""+feature.getId()+" BBOXed out");
                    continue;
                }

                // Apply other filters
                if ( ( filter != null ) && !filter.evaluate( feature ) ) {
                    System.out.println( "" + feature.getId() + " Filtered out" );
                    continue;
                }

                if ( skippedPos++ < startPosition ) {
                    continue;
                }

                if ( retval.size() < 10 ) {
                    System.out.println( "FEATURE SELECTED: " + feature.getId() );
                }

                retval.add( feature );
            }

            System.out.println( retval.size() + " features selected." );

            Feature[] features = (Feature[])retval.toArray( new Feature[retval.size()] );

            Debug.debugMethodEnd();

            return features;
        }

        /**
         *
         *
         * @param ft 
         *
         * @return 
         */
        private CS_CoordinateSystem getCRS( FeatureType ft ) {
            try {
                String crsName = ft.getCRS();
                ConvenienceCSFactory csFactory = ConvenienceCSFactory.getInstance();
                CoordinateSystem cs = csFactory.getCSByName( crsName );
                CS_CoordinateSystem crs = Adapters.getDefault().export( cs );
                return crs;
            } catch ( Exception e ) {
                System.out.println( e );
                e.printStackTrace();
                return null;
            }
        }

        /**
         * Creates a Feature from a BNAFeature
         */
        private Feature createFeature( BNAFeature bnafeature, CS_CoordinateSystem crs )
                               throws GM_Exception {
            FeatureFactory factory = new FeatureFactory();
            int size = bnafeature.getHeaderSize();

            // We need as many props as the field headers + the geometry property
            ArrayList ftpv = new ArrayList( size + 1 );
            ArrayList prov = new ArrayList( size + 1 );

            // Add the field data
            for ( int i = 0; i < size; i++ ) {
                prov.add( bnafeature.getHeader( i ) );
                ftpv.add( factory.createFeatureTypeProperty( "FIELD" + i, "java.lang.String", true ) );
            }

            // Add the geometry data
            prov.add( getGMObject( bnafeature, crs ) );
            ftpv.add( factory.createFeatureTypeProperty( "GEOM", 
                                                         "org.deegree.model.geometry.GM_Object", 
                                                         true ) );

            // Pack all together into a new Feature
            org.deegree.model.feature.FeatureType retft = factory.createFeatureType( null, null, 
                                                                                     "ETJETJETJ", // ETJ FIXME
                                                                                     (FeatureTypeProperty[])ftpv.toArray( 
                                                                                               new FeatureTypeProperty[ftpv.size()] ) );

            Feature ret = factory.createFeature( bnafeature.getHeads(), retft, prov.toArray() );

            return ret;
        }

        /**
         * Builds a GM_Object from a BNAFeature.
         * If the BNAFeature if composed by more than one geometry,
         * the corresponding aggregate GM_Object will be built.
         */
        private GM_Object getGMObject( BNAFeature bnafeature, CS_CoordinateSystem crs )
                               throws GM_Exception {
            GM_Object ret = null;
            GeometryFactory gf = new GeometryFactory();

            for ( int i = 0, size = bnafeature.size(); i < size; i++ ) {
                BNAGeometry bnageom = bnafeature.getGeometry( i );

                if ( bnageom.isPoint() ) ///// POINT
                {
                    GM_Point gmo = gf.createGM_Point( bnageom.getPoint( 0 ), crs );

                    if ( ret == null ) {
                        ret = gmo;
                    } else if ( ret instanceof GM_Point ) {
                        ret = gf.createGM_MultiPoint( new GM_Point[] { (GM_Point)ret } );
                        ( (GM_MultiPoint)ret ).addPoint( gmo );
                    } else if ( ret instanceof GM_MultiPoint ) {
                        ( (GM_MultiPoint)ret ).addPoint( gmo );
                    } else {
                        throw new GM_Exception( "BNA: Bad GM aggregation in feature " + 
                                                bnafeature.getHeads() );
                    }
                }
                else if ( bnageom.isLine() || bnageom.isPoly() ) ///// LINE & POLY // TODO: have to convert Polys on their own.
                {
                    GM_Curve gmo = gf.createGM_Curve( bnageom.getPoints(), crs );

                    if ( ret == null ) {
                        ret = gmo;
                    } else if ( ret instanceof GM_Curve ) {
                        ret = gf.createGM_MultiCurve( new GM_Curve[] { (GM_Curve)ret } );
                        ( (GM_MultiCurve)ret ).addCurve( gmo );
                    } else if ( ret instanceof GM_MultiCurve ) {
                        ( (GM_MultiCurve)ret ).addCurve( gmo );
                    } else {
                        throw new GM_Exception( "BNA: Bad GM aggregation in feature " + 
                                                bnafeature.getHeads() );
                    }
                }
                else if ( bnageom.isSurface() ) ///// SURFACE
                {
                    if ( bnageom.size() == 2 ) // workaround for some bad-conceived maps

                    {
                        bnageom.addPoint( bnageom.getPoint( 1 ) );
                    }

                    GM_Surface gmo = gf.createGM_Surface( bnageom.getPoints(), 
                                                          (GM_Position[][])null, 
                                                          new GM_SurfaceInterpolation_Impl( 
                                                                  GM_SurfaceInterpolation.NONE ), 
                                                          crs );

                    if ( ret == null ) {
                        ret = gmo;
                    } else if ( ret instanceof GM_Surface ) {
                        ret = gf.createGM_MultiSurface( new GM_Surface[] { (GM_Surface)ret } );
                        ( (GM_MultiSurface)ret ).addSurface( gmo );
                    } else if ( ret instanceof GM_MultiSurface ) {
                        ( (GM_MultiSurface)ret ).addSurface( gmo );
                    } else {
                        throw new GM_Exception( "BNA: Bad GM aggregation in feature " + 
                                                bnafeature.getHeads() );
                    }
                } else
                {
                    throw new GM_Exception( "BNA: Bad GM conversion in feature " + 
                                            bnafeature.getHeads() );
                }
            }

            return ret;
        }
    }


    /***************************************************************************
     * inner interface defining the processing of a transaction request
     */
    private class CTransaction extends WFSMainLoop {
        /**
         * Creates a new CTransaction object.
         *
         * @param parent 
         * @param request 
         */
        public CTransaction( BNADataStore parent, OGCWebServiceRequest request ) {
            super( parent, request );
        }

        /**
         *
         *
         * @param request 
         *
         * @return 
         */
        protected OGCWebServiceResponse[] performRequest( OGCWebServiceRequest request ) {
            return null;
        }
    }


    /***************************************************************************
     * inner interface defining the processing of a lockFeature request
     */
    private class CLockFeature extends WFSMainLoop {
        /**
         * Creates a new CLockFeature object.
         *
         * @param parent 
         * @param request 
         */
        public CLockFeature( BNADataStore parent, OGCWebServiceRequest request ) {
            super( parent, request );
        }

        /**
         *
         *
         * @param request 
         *
         * @return 
         */
        protected OGCWebServiceResponse[] performRequest( OGCWebServiceRequest request ) {
            return null;
        }
    }


    /***************************************************************************
     * class defining the processing of a getFeature request
     */
    class CGetFeatureWithLock extends WFSMainLoop {
        /**
         * Creates a new CGetFeatureWithLock object.
         *
         * @param parent 
         * @param request 
         */
        public CGetFeatureWithLock( BNADataStore parent, OGCWebServiceRequest request ) {
            super( parent, request );
        }

        /**
         *
         *
         * @param request 
         *
         * @return 
         */
        protected OGCWebServiceResponse[] performRequest( OGCWebServiceRequest request ) {
            return null;
        }
    }
}