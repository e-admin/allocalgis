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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import org.deegree.services.OGCWebServiceException;
import org.deegree.services.OGCWebServiceRequest;
import org.deegree.services.OGCWebServiceResponse;
import org.deegree.services.wfs.WFSConstants;
import org.deegree.services.wfs.configuration.DatastoreConfiguration;
import org.deegree.services.wfs.configuration.OutputFormat;
import org.deegree.services.wfs.protocol.WFSDescribeFeatureTypeRequest;
import org.deegree.xml.XMLTools;
import org.deegree_impl.services.OGCWebServiceException_Impl;
import org.deegree_impl.services.wfs.protocol.WFSProtocolFactory;
import org.deegree_impl.tools.Debug;
import org.w3c.dom.Document;

/**
 * class defining the basic processing of a DescribFeatureType request
 * <p>-------------------------------------------------------------------------</p>
 *
 * @author Andreas Poth
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:32 $
 * <p>
 */
abstract public class AbstractDescribeFeatureType extends org.deegree_impl.services.wfs.WFSMainLoop {
    
    protected DatastoreConfiguration config  = null;
    
    public AbstractDescribeFeatureType(org.deegree_impl.services.wfs.AbstractDataStore parent,
    OGCWebServiceRequest request) {
        super( parent, request );
        config = parent.getConfiguration();
    }
    
    
    protected synchronized OGCWebServiceResponse[] performRequest(OGCWebServiceRequest request) {
        Debug.debugMethodBegin( this, "performRequest" );
        
        OGCWebServiceResponse[] response = new OGCWebServiceResponse[1];
        
        WFSDescribeFeatureTypeRequest df = (WFSDescribeFeatureTypeRequest)request;
        String[] types = df.getTypeNames();
        
        ArrayList list = new ArrayList();
        ArrayList afft = new ArrayList();
        
        try {
            for (int i = 0; i < types.length; i++) {
                if ( parent.isKnownFeatureType( types[i] ) ) {
                    afft.add( types[i] );
                    try {
                        org.deegree.services.wfs.configuration.FeatureType ft = 
                            config.getFeatureType( types[i] );
                        OutputFormat of = ft.getOutputFormat( WFSConstants.GML );
                        InputStream is = of.getSchemaLocation().openStream();
                        Reader reader = new InputStreamReader( is );
                        list.add( XMLTools.parse( reader ) );
                    } catch(Exception ee) {
                        ee.printStackTrace();
                        // if no schema can be found describing the feature
                        // type, try creating it on the fly
                        list.add( createSchema( types[i] ) );
                    }
                }
            }
            
            // get schemas
            Document[] schemas = new Document[list.size()];
            schemas = (Document[])list.toArray( schemas );
            Document schema = XMLTools.mergeSchemas( schemas );
            
            // get affected feature types
            String[] at = new String[ afft.size() ];
            at = (String[])afft.toArray( at );
            response[0] =
            	WFSProtocolFactory.createWFSDescribeFeatureTypeResponse( request, at, null, schema);
        } catch (Exception e) {
            Debug.debugException( e, null );
            OGCWebServiceException exce =
            new OGCWebServiceException_Impl( "CDescribeFeatureType: performRequest",
            e.toString() );
            response[0] = WFSProtocolFactory.createWFSGetFeatureResponse(request, types, exce, null);
        }
        
        Debug.debugMethodEnd();
        
        return response;
    }
    
    /**
     * creates a xml schema definition of the submitted feature type on the fly
     */
    abstract protected Document createSchema(String featureType) throws Exception;
    
}
