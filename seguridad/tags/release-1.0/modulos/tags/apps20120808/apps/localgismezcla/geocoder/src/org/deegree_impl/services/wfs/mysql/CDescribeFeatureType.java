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

import org.deegree.model.feature.FeatureTypeProperty;
import org.deegree.model.table.Table;
import org.deegree.services.OGCWebServiceRequest;
import org.deegree.services.wfs.configuration.Connection;
import org.deegree.services.wfs.configuration.DatastoreConfiguration;
import org.deegree_impl.io.DBConnectionPool;
import org.deegree_impl.io.MySQLDBAccess;
import org.deegree_impl.model.feature.FeatureFactory;
import org.deegree_impl.services.wfs.AbstractDescribeFeatureType;
import org.deegree_impl.services.wfs.FeatureTypeToSchema;
import org.deegree_impl.tools.Debug;
import org.deegree_impl.tools.StringExtend;
import org.w3c.dom.Document;

/**
 * class defining the processing of a DescribeFeatureType request
 */
class CDescribeFeatureType extends AbstractDescribeFeatureType {
    
    private DatastoreConfiguration config     = null;
    private DBConnectionPool pool             = null;    
    
    public CDescribeFeatureType(MySQLDataStore parent, OGCWebServiceRequest request) {
        super( parent, request );
        config = parent.getConfiguration();
        pool = parent.getConnectionPool();
    }
    
    /**
     * creates a xml schema definition of the submitted feature type on the fly
     */
    protected Document createSchema(String featureType) throws Exception {
        Debug.debugMethodBegin( this, "createSchema" );
        
        Table table = null;
        
        Connection connect = config.getConnection();
        // get jdbc connection and access object for postgis
        java.sql.Connection con = null;
        try {
            con = pool.acuireConnection( connect.getDriver(), connect.getLogon(),
            connect.getUser(), connect.getPassword() );
            MySQLDBAccess osa = new MySQLDBAccess( con );
            
            // get describtions for the submitted feature type
            String query = "select column_name, data_type, nullable from " +
            "sys.all_tab_columns where OWNER = '" +
            getOwner(featureType) + "' and TABLE_NAME = '" +
            getTableName(featureType) + "'";
            table = osa.performTableQuery( query );
            
            pool.releaseConnection( con, connect.getDriver(), connect.getLogon(),
            connect.getUser(), connect.getPassword() );
        } catch(Exception ex) {
            pool.releaseConnection( con, connect.getDriver(), connect.getLogon(),
            connect.getUser(), connect.getPassword() );
            throw new Exception( ex.toString() );
        }
        
        Document doc = tableToSchema( featureType, table );
        
        Debug.debugMethodEnd();
        
        return doc;
        
    }
    
    /**
     * returns the owner (first part) of the feature type name
     */
    private String getOwner(String featureType) {
        return StringExtend.toArray( featureType, ".", false )[0];
    }
    
    /**
     * returns the table name (second part) of the feature type name
     */
    private String getTableName(String featureType) {
        return StringExtend.toArray( featureType, ".", false )[1];
    }
    
    private Document tableToSchema(String featureType, Table table)
    throws Exception {
        Debug.debugMethodBegin( this, "createResponse" );
        
        boolean nullable = false;
        org.deegree.model.feature.FeatureType[] ft =
        new org.deegree.model.feature.FeatureType[1];
        String[] ftNames = new String[] { featureType };
        
        // create an array od FeatureTypeProperties -> for each row
        // of the table, one array field
        FeatureTypeProperty[] ftp = new FeatureTypeProperty[table.getRowCount()];
        
        FeatureFactory factory = new FeatureFactory();
        // create FeatureTypeProperty for each row
        for (int i = 0; i < table.getRowCount(); i++) {
            if (table.getValueAt(i,2).toString().equalsIgnoreCase("false") ) {
                nullable = false;
            } else {
                nullable = true;
            }
            String type = getType( table.getValueAt(i,1).toString() );
            ftp[i] = factory.createFeatureTypeProperty(
            table.getValueAt(i,0).toString(),
            type, nullable );
        }
        // create feature type with the properties taken from the table
        ft[0] = factory.createFeatureType( null, null, featureType, ftp );
        
        // transform the OGC feature types to GML feature type description(s)
        FeatureTypeToSchema ftts = new FeatureTypeToSchema();
        Document[] doc = ftts.createFeatureTypeSchema( ft );
        
        Debug.debugMethodEnd();
        return doc[0];
        
    }
    
    private String getType(String oType) {
        if (oType.equalsIgnoreCase("Number")) {
            return "java.lang.Double";
        }
        else
            if (oType.equalsIgnoreCase("Varchar2")) {
                return "java.lang.String";
            }
            else
                if (oType.equalsIgnoreCase("Varchar")) {
                    return "java.lang.String";
                }
                else
                    if (oType.equalsIgnoreCase("SDO_Geometry")) {
                        return "org.deegree.model.geometry.GM_Object";
                    }
        
        return "java.lang.Object";
        
    }
    
}


