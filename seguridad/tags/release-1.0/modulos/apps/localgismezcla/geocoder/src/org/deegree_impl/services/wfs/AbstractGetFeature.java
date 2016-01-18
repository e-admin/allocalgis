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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.deegree.services.OGCWebServiceException;
import org.deegree.services.OGCWebServiceRequest;
import org.deegree.services.OGCWebServiceResponse;
import org.deegree.services.wfs.DataStoreOutputFormat;
import org.deegree.services.wfs.configuration.DatastoreConfiguration;
import org.deegree.services.wfs.configuration.FeatureType;
import org.deegree.services.wfs.configuration.OutputFormat;
import org.deegree.services.wfs.protocol.WFSGetFeatureRequest;
import org.deegree.services.wfs.protocol.WFSQuery;
import org.deegree.tools.ParameterList;
import org.deegree_impl.services.OGCWebServiceException_Impl;
import org.deegree_impl.services.wfs.protocol.WFSProtocolFactory;
import org.deegree_impl.tools.Debug;


/**
 * class defining the basic processing of a getFeature request
 * <p>-------------------------------------------------------------------------</p>
 *
 * @author Andreas Poth
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:32 $
 * <p>
 */
abstract public class AbstractGetFeature extends WFSMainLoop {
    
    protected DatastoreConfiguration config  = null;
    
    protected AbstractGetFeature(AbstractDataStore parent,
                                OGCWebServiceRequest request) {
        super( parent, request);
        this.config = parent.getConfiguration();
    }
        
    /**
     * return the names of the feature types affected by a <tt>Query</tt>
     */
    protected String[] getAffectedFeatureTypes(WFSQuery[] queries) {
        ArrayList lst = new ArrayList();
        for (int i = 0; i < queries.length; i++) {
            if ( parent.isKnownFeatureType( queries[i].getTypeName() ) ) {
                lst.add( queries[i].getTypeName() );
            }
        }
        return (String[])lst.toArray( new String[lst.size()] );
    }
    
    /**
     * the method returns a comma-seperated list of fields, that
     * are affected by a <tt>WFSQuery</tt> against the subbmitted table.
     * If all fields of the submitted table shall be selected to
     * method just returns "*".<p></p>
     * example:<p>
     * "table1.ID,table1.NAME,table1.VALUE"</p>
     */
    protected String getAffectedFields(String table, FeatureType ft, WFSQuery query)
                                                                    throws Exception {
        Debug.debugMethodBegin( this, "getAffectedFields" );

        String s = null;
        StringBuffer sb = new StringBuffer(1000);

        String[] props = query.getPropertyNames();    

        if ( props == null || props.length == 0) {
            HashMap map = ft.getMappings();
            if ( map == null || map.size() == 0 ) {
                sb.append( " * " );
            } else {
                // map the database field names to the property names
                Iterator iterator = map.keySet().iterator();
                while ( iterator.hasNext() ) {
                    String prop = (String)iterator.next();
                    String[] field = (String[])map.get( prop );
                    s = field[0];
                    String tabName = getTableName( ft, s );        
                    String[] alias = ft.getAlias( prop );
                    if ( table.equals( tabName ) && alias != null &&
                         !(s.equals(alias[0])) ) {
                        sb.append( s + " AS \"" + alias[0] + "\"," );
                    } else {
                        if ( table.equals( tabName ) ) {
                            sb.append(  s  + "," );
                        }
                    }
                }
            }           
        } else {            

            for (int i = 0; i < props.length; i++) {         
                if ( props[i].equals( "COUNT(*)" ) ) {
                    // if a count statement shall performed the result will be
                    // returned in a column named 'COUNTCOUNT'
                    sb.append( props[i] + " AS COUNTCOUNT," );
                } else {
                   
                    String[] s_ = ft.getDatastoreField( props[i] );
                    if ( s_ == null ) {
                        String ex = "Field/property '" + props[i] + "' is not known " +
                                    "by the datastore!";
                        throw new Exception( ex );
                    }
                    s = s_[0];
                    String tabName = getTableName( ft, s );
                    String[] alias = ft.getAlias( props[i] );
                    if ( table.equals( tabName ) && alias != null &&
                    	 !(s.equals(alias[0])) ) {
                        sb.append( s + " AS \"" + alias[0] + "\"," );
                    } else {
                        if ( table.equalsIgnoreCase( tabName ) ) {
                            sb.append( s + "," );
                        }
                    }
                }
            }            
        }

        s = sb.toString();

        if ( s.length() > 0 ) {
            s = s.substring( 0, s.length()-1);
        } else {
            throw new Exception("No valid target field for SQL statement selected" );
            //s = " * ";
        }
        
        Debug.debugMethodEnd();

        return s;
    }
    
    /**
     * extracts the table name from a property. it is assumed that the property
     * name is constructed like this: table.propertyname or schema.table.propertyname.
     * if no table is specified with a property name, the name of the feature
     * types master table will be returned.
     */
    private String getTableName(FeatureType ft, String prop)
    {
        String tab = ft.getMasterTable().getName();
        int pos = prop.lastIndexOf('.');
        if ( pos > 0 ) {
            tab = prop.substring(0,pos);
        } 
        
        if ( tab != null ) {
			return tab.toUpperCase();
        } else {
        	return null;
        }
        
    }
        
    /**
     * create a response object from the GMLFeatureCollections
     * store into the submitted HashMap
     */
    protected OGCWebServiceResponse[] createResponse(HashMap map, String[] affectedFeatureTypes)
                                                                            throws Exception {
        Debug.debugMethodBegin( this, "createResponse" ); 
        
        String outputFormat = ((WFSGetFeatureRequest)request).getOutputFormat();

        OGCWebServiceResponse[] responses = 
            new OGCWebServiceResponse[ affectedFeatureTypes.length ];
        for (int i = 0; i < affectedFeatureTypes.length; i++) {            
            FeatureType ft = config.getFeatureType( affectedFeatureTypes[i] );
            OutputFormat of = ft.getOutputFormat( outputFormat );
            if ( of == null ) {
                throw new Exception( "Outputformat: " + outputFormat + " is not known!" );
            }
            
            Class cl = Class.forName( of.getResponsibleClass() );
            DataStoreOutputFormat dsof = (DataStoreOutputFormat)cl.newInstance();
            
            ParameterList pl = of.getParameter(); 
            pl.addParameter( affectedFeatureTypes[i], dsof );            
            Object fc = dsof.format( map, pl );
  
            // create response object
            OGCWebServiceResponse response = null;
            try {
                String[] s = new String[] { affectedFeatureTypes[i] };
                responses[i] =
                	WFSProtocolFactory.createWFSGetFeatureResponse(request, s, null, fc);
            } catch(Exception e) {
                Debug.debugException( e, " - " );
                OGCWebServiceException exce =
                new OGCWebServiceException_Impl( e.toString(),
                "DBDataStore createResponse idx1" );
                response = WFSProtocolFactory.createWFSGetFeatureResponse(request, null, exce, null);
                Debug.debugMethodEnd();
            }
        }                
 
        Debug.debugMethodEnd();

        return responses;
    }
    
    
}
