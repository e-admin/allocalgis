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
package org.deegree_impl.services.wfs.mysql;

import java.util.HashMap;

import org.deegree.model.table.Table;
import org.deegree.services.OGCWebServiceException;
import org.deegree.services.OGCWebServiceRequest;
import org.deegree.services.OGCWebServiceResponse;
import org.deegree.services.wfs.WFSConstants;
import org.deegree.services.wfs.configuration.Connection;
import org.deegree.services.wfs.configuration.FeatureType;
import org.deegree.services.wfs.configuration.GeoFieldIdentifier;
import org.deegree.services.wfs.filterencoding.Filter;
import org.deegree.services.wfs.protocol.WFSGetFeatureRequest;
import org.deegree.services.wfs.protocol.WFSQuery;
import org.deegree.tools.ParameterList;
import org.deegree_impl.io.MySQLDBAccess;
import org.deegree_impl.services.OGCWebServiceException_Impl;
import org.deegree_impl.services.wfs.DBGetFeature;
import org.deegree_impl.services.wfs.filterencoding.AbstractSQLBuilder;
import org.deegree_impl.services.wfs.filterencoding.MySQLSQLBuilder;
import org.deegree_impl.services.wfs.protocol.WFSProtocolFactory;
import org.deegree_impl.tools.Debug;
import org.deegree_impl.tools.ParameterList_Impl;

/**
 * class implementing the processing of a getFeature request
 */
class CGetFeature extends DBGetFeature {
    
    public CGetFeature(MySQLDataStore parent, OGCWebServiceRequest request) {
        super( parent, request );
    }
    
    protected OGCWebServiceResponse[] performRequest(OGCWebServiceRequest request) {
        Debug.debugMethodBegin( this, "performRequest" );
        
        WFSQuery[] queries = null;            
        String[] affectedFeatureTypes = null;            
        OGCWebServiceResponse[] response = null;            
        
        java.sql.Connection con = null;
        Connection connect = config.getConnection();
        try {
            HashMap map = new HashMap();
            WFSGetFeatureRequest req = (WFSGetFeatureRequest)request;
            
            // get jdbc connection and access object for postgis spatial
            con = pool.acuireConnection( connect.getDriver(), connect.getLogon(),
                                         connect.getUser(), connect.getPassword() );
            MySQLDBAccess osa = new MySQLDBAccess( con );
               
            queries = req.getQuery();
            affectedFeatureTypes = getAffectedFeatureTypes( queries );
            int startPosition = req.getStartPosition();
            int maxFeatures = req.getMaxFeatures();
            // get describtions for each feature type that's handled
            // by this data store
            for (int i = 0; i < queries.length; i++) {

                if ( parent.isKnownFeatureType( queries[i].getTypeName() ) ) {
                    
                    FeatureType ft = config.getFeatureType(queries[i].getTypeName());
                    
                    // create query string
                    // the useage of a global filter ( req.getFilter() ) is
                    // depreceated
                    String query = getQuery( req.getFilter(), queries[i] );
                    
                    GeoFieldIdentifier[] gfi = ft.getMasterTable().getGeoFieldIdentifier();
                    if ( gfi != null ) {
                        String[] geoCols = new String[gfi.length];
                        for (int k = 0; k < gfi.length; k++) {
                            String tmp = gfi[i].getDatastoreFieldBaseName().toUpperCase();
                            geoCols[k] = tmp;
                        }
                        osa.announceGeometryColumnNames( geoCols );
                    }

                    // perform query and return result as table model                   
                    Table tm = osa.performTableQuery( query, startPosition, maxFeatures );

                    tm.setTableName( ft.getMasterTable().getName() );
                    // solves the relation if exist
                    tm = getRelations( tm, ft, queries[i] );
                    
                    ParameterList pl = new ParameterList_Impl();
                    pl.addParameter( WFSConstants.CRS, ft.getCRS() );
                    pl.addParameter( WFSConstants.TABLE, tm );
                    pl.addParameter( WFSConstants.FEATURETYPE, ft );
                    
                    // transform table model to GMLFeatureCollection
                    // and put it onto a HashMap associated with its
                    // table (feature type) name.
                    map.put( queries[i].getTypeName(), pl );
                }
                reset();
            }
            
            // release database connection back to the connection pool
            pool.releaseConnection( con, connect.getDriver(), connect.getLogon(),
            connect.getUser(), connect.getPassword() );
            
            // create response
            response = createResponse( map, affectedFeatureTypes );
            
        } catch(Exception e) {
            Debug.debugException( e, null );
            try {
                // release database connection back to the connection pool
                pool.releaseConnection( con, connect.getDriver(), connect.getLogon(),
                connect.getUser(), connect.getPassword() );
            } catch(Exception eex) {}

            OGCWebServiceException exce =
                new OGCWebServiceException_Impl( "CGetFeature: performRequest",
                                                 e.getMessage() );            
            response = new OGCWebServiceResponse[1];
            response[0] = 
            	WFSProtocolFactory.createWFSGetFeatureResponse(request, affectedFeatureTypes, 
                                                    exce, null);
        }
        
        Debug.debugMethodEnd();
        return response;
    }
        
    private String getQuery( Filter filter, WFSQuery query) throws Exception {
        Debug.debugMethodBegin( this, "getQuery" );
        
        FeatureType ft = config.getFeatureType(query.getTypeName());
        
        // add properties (columns) to select
        StringBuffer query_s = new StringBuffer("SELECT ");
        String s = getAffectedFields( ft.getMasterTable().getName(), ft, query );         
        query_s.append( s );
        
        // transform the query conditions coded as Filters
        // to sql statements. The first filter is global and
        // has to be performed on each feature type. The second
        // filter is feature type depending and will be added
        // to the global filter conditions.
        AbstractSQLBuilder sqlBuilder = new MySQLSQLBuilder( ft  );
        String filter_s1 = null;
        if ( filter != null ) {
            filter_s1 = sqlBuilder.filter2SQL( filter );
        }
        String filter_s2 = null;
        if ( query.getFilter() != null ) {
            filter_s2 = sqlBuilder.filter2SQL( query.getFilter() );
        }
        
        if ( filter_s1 != null ) {
            query_s.append( filter_s1 + " ");
        }
        if ( filter_s1 != null && filter_s2 != null) {
            query_s.append( " AND " + filter_s2);
        }
        else
            if ( filter_s2 != null ) {
                query_s.append( filter_s2 );
            }
        if ( filter_s1 == null && filter_s2 == null ) {
            query_s.append( " FROM " + ft.getMasterTable().getName() );
        }
        
        Debug.debugObject("database query: ", query_s );
        
        Debug.debugMethodEnd();
        
        return query_s.toString();
    }
    
}


