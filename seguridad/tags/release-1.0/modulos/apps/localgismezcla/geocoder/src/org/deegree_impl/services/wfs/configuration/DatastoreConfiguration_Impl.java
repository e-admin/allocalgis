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
package org.deegree_impl.services.wfs.configuration;

import java.io.StringReader;

import java.net.URL;

import java.sql.Types;

import java.util.HashMap;
import java.util.Iterator;

import org.deegree.services.wfs.configuration.*;
import org.deegree.tools.Parameter;
import org.deegree.tools.ParameterList;
import org.deegree.xml.XMLTools;

import org.w3c.dom.Document;


/**
 * the toplevel interface describing a deegree wfs datastore
 *
 * <p>---------------------------------------------------------------------</p>
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */
public class DatastoreConfiguration_Impl implements DatastoreConfiguration {
    private Connection connection = null;
    private HashMap featureTypes = null;
    private String name = null;
    private int type = -1;

    /**
     * Creates a new DatastoreConfiguration_Impl object.
     *
     * @param name 
     * @param type 
     * @param connection 
     * @param featureTypes 
     */
    DatastoreConfiguration_Impl( String name, int type, Connection connection, 
                                 FeatureType[] featureTypes ) {
        this.featureTypes = new HashMap();
        setName( name );
        setType( type );
        setConnection( connection );
        setFeatureTypes( featureTypes );
    }

    /**
    * returns the name of a datastore
    */
    public String getName() {
        return name;
    }

    /**
    * @see getName
    */
    public void setName( String name ) {
        this.name = name;
    }

    /**
    * returns the type of a datastore. at the moment four types
    * are known:
    * <ul>
    *    <li>ORACLESPATIAL
    *    <li>GMLDB
    *    <li>POINTDB
    *    <li>SHAPEFILES
    * </ul>
    */
    public int getType() {
        return type;
    }

    /**
    * @see getType
    */
    public void setType( int type ) {
        this.type = type;
    }

    /**
    * returns an object that describes the connection to a database
    * if datastore type equals ORACLESPATIAL, GMLDB or POINTDB
    */
    public Connection getConnection() {
        return connection;
    }

    /**
    * @see getConnection
    */
    public void setConnection( Connection connection ) {
        this.connection = connection;
    }

    /**
    * return describing objects for each feature type that is accessible
    * through a datastore.
    */
    public FeatureType[] getFeatureTypes() {
        FeatureType[] tmp = new FeatureType[featureTypes.size()];
        synchronized ( featureTypes ) {
            Iterator iterator = featureTypes.values().iterator();
            int i = 0;

            while ( iterator.hasNext() ) {
                tmp[i++] = (FeatureType)iterator.next();
            }
        }

        return tmp;
    }

    /** returns the feature description for a named feature type. if no feature
    * type is known with the submitted name <tt>null</tt> will be returned
    *
    */
    public FeatureType getFeatureType( String name ) {
        return (FeatureType)featureTypes.get( name );
    }

    /**
    * @see getFeatureTypes
    */
    public void setFeatureTypes( FeatureType[] featureTypes ) {
        this.featureTypes.clear();

        if ( featureTypes != null ) {
            for ( int i = 0; i < featureTypes.length; i++ ) {
                addFeatureType( featureTypes[i] );
            }
        }
    }

    /**
    * @see getFeatureTypes
    */
    public void addFeatureType( FeatureType featureType ) {
        featureTypes.put( featureType.getName(), featureType );
    }

    /**
    * exports the configuration as OGC WFS conform XML document
    */
    public Document exportAsXML() {
        StringBuffer sb = new StringBuffer( "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" );

        sb.append( "<DatastoreConfiguration name=\"" + getName() + "\" type=\"" );

        switch ( getType() ) {
            case DatastoreConfiguration.GMLDB:
                sb.append( "GMLDB\">" );
                break;
            case DatastoreConfiguration.ORACLESPATIAL:
                sb.append( "ORACLESPATIAL\">" );
                break;
            case DatastoreConfiguration.POINTDB:
                sb.append( "POINTDB\">" );
                break;
            case DatastoreConfiguration.SHAPEFILES:
                sb.append( "SHAPEFILES\">" );
                break;
        }

        if ( getType() != DatastoreConfiguration.SHAPEFILES ) {
            Connection con = getConnection();
            sb.append( "<Connection><driver>" + con.getDriver() + "</driver>" );
            sb.append( "<logon>" + con.getLogon() + "</logon>" );
            sb.append( "<user>" + con.getUser() + "</user>" );
            sb.append( "<password>" + con.getPassword() + "</password>" );

            if ( getType() == DatastoreConfiguration.ORACLESPATIAL ) {
                sb.append( "<spatialversion>" + con.getSpatialVersion() + "</spatialversion>" );
            }

            sb.append( "</Connection>" );
        }

        FeatureType[] ft = getFeatureTypes();

        for ( int i = 0; i < ft.length; i++ ) {
            sb.append( "<FeatureType name=\"" + ft[i].getName() + "\">" );

            // handle output format
            sb.append( "<OutputFormat>" );

            OutputFormat[] of = ft[i].getOutputFormat();

            for ( int j = 0; j < of.length; j++ ) {
                sb.append( "<" + of[j].getName() + " responsibleClass=\"" );
                sb.append( of[j].getResponsibleClass() + "\">" );

                ParameterList pl = of[j].getParameter();
                Parameter[] param = pl.getParameters();

                for ( int k = 0; k < param.length; k++ ) {
                    sb.append( "<Param name=\"" + param[k].getName() + "\" " );
                    sb.append( "value=\"" + param[k].getValue() + "\"/>" );
                }

                URL url = of[j].getSchemaLocation();

                if ( url != null ) {
                    sb.append( "<SchemaLocation>" );
                    sb.append( url.toString() + "</SchemaLocation>" );
                }

                sb.append( "</" + of[j].getName() + ">" );
            }

            sb.append( "</OutputFormat>" );

            // handle mappings
            HashMap mappings = ft[i].getMappings();
            Iterator iterator = mappings.keySet().iterator();

            while ( iterator.hasNext() ) {
                String prop = (String)iterator.next();
                int propType = ft[i].getPropertyType( prop );
                String dbField = ( (String[])mappings.get( prop ) )[0];
                int dbFieldType = ft[i].getDatastoreFieldType( dbField );
                sb.append( "<MappingField>" );
                sb.append( "<Property name=\"" + prop + "\" " );
                sb.append( "type=\"" + getDataType( propType ) + "\"/>" );
                sb.append( "<DatastoreField name=\"" + dbField + "\" " );
                sb.append( "type=\"" + getDataType( dbFieldType ) + "\"/>" );
                sb.append( "</MappingField>" );
            }

            //handle MasterTable
            MasterTable mt = ft[i].getMasterTable();
            sb.append( "<MasterTable name=\"" + mt.getName() + "\" " );
            sb.append( "targetName=\"" + mt.getTargetName() + "\">" );
            sb.append( "<IdField number=\"" + mt.isIdFieldNumber() + "\" " );
            sb.append( "auto=\"" + mt.isIdFieldAutoIncremented() + "\">" );
            sb.append( mt.getIdField() + "</IdField>" );

            Reference[] ref = mt.getReferences();

            for ( int j = 0; j < ref.length; j++ ) {
                sb.append( "<Reference tableField=\"" + ref[j].getTableField() + "\" " );
                sb.append( "replaceable=\"" + ref[j].isReplaceable() + "\" " );
                sb.append( "targetTable=\"" + ref[j].getTargetTable() + "\" " );
                sb.append( "targetField=\"" + ref[j].getTargetField() + "\"/>" );
            }

            GeoFieldIdentifier[] gfi = mt.getGeoFieldIdentifier();

            for ( int j = 0; j < gfi.length; j++ ) {
                if ( getType() == DatastoreConfiguration.POINTDB ) {
                    sb.append( "<GeoFieldIdentifier dimension=\"" + gfi[j].getDimension() + 
                               "\">" );
                } else {
                    sb.append( "<GeoFieldIdentifier>" );
                }

                sb.append( gfi[j].getDatastoreFieldBaseName() + "</GeoFieldIdentifier>" );
            }

            sb.append( "</MasterTable>" );

            // handle related tables
            RelatedTable[] td = ft[i].getRelatedTables();

            for ( int k = 0; k < td.length; k++ ) {
                sb.append( "<RelatedTable name=\"" + td[k].getName() + "\" " );
                sb.append( "targetName=\"" + td[k].getTargetName() + "\"" );
                sb.append( "joinTable=\"" + td[k].isJoinTable() + "\">" );
                sb.append( "<IdField number=\"" + td[k].isIdFieldNumber() + "\" " );
                sb.append( "auto=\"" + td[k].isIdFieldAutoIncremented() + "\">" );
                sb.append( td[k].getIdField() + "</IdField>" );
                ref = td[k].getReferences();

                for ( int j = 0; j < ref.length; j++ ) {
                    sb.append( "<Reference tableField=\"" + ref[j].getTableField() + "\" " );
                    sb.append( "replaceable=\"" + ref[j].isReplaceable() + "\" " );
                    sb.append( "targetTable=\"" + ref[j].getTargetTable() + "\" " );
                    sb.append( "targetField=\"" + ref[j].getTargetField() + "\"/>" );
                }

                gfi = mt.getGeoFieldIdentifier();

                for ( int j = 0; j < gfi.length; j++ ) {
                    if ( getType() == DatastoreConfiguration.POINTDB ) {
                        sb.append( "<GeoFieldIdentifier dimension=\"" + gfi[j].getDimension() + 
                                   "\">" );
                    } else {
                        sb.append( "<GeoFieldIdentifier>" );
                    }

                    sb.append( gfi[j].getDatastoreFieldBaseName() + "</GeoFieldIdentifier>" );
                }

                sb.append( "</RelatedTable>" );
            }

            if ( ft[i].getCRS() != null ) {
                sb.append( "<CRS>" + ft[i].getCRS() + "</CRS>" );
            }

            sb.append( "</FeatureType>" );
        }

        sb.append( "</DatastoreConfiguration>" );

        StringReader sr = new StringReader( sb.toString() );
        Document doc = null;

        try {
            doc = XMLTools.parse( sr );
        } catch ( Exception e ) {
            System.out.println( this.getClass().getName() + ":" );
            System.out.println( e );
        }

        return doc;
    }

    /**
     *
     *
     * @param t 
     *
     * @return 
     */
    private static String getDataType( int t ) {
        String type = null;

        switch ( t ) {
            case Types.ARRAY:
                type = "ARRAY";
                break;
            case Types.BIGINT:
                type = "BIGINT";
                break;
            case Types.BINARY:
                type = "BINARY";
                break;
            case Types.BIT:
                type = "BIT";
                break;
            case Types.BLOB:
                type = "BLOB";
                break;
            case Types.BOOLEAN:
                type = "BOOLEAN";
                break;
            case Types.CHAR:
                type = "CHAR";
                break;
            case Types.CLOB:
                type = "CLOB";
                break;
            case Types.DATALINK:
                type = "DATALINK";
                break;
            case Types.DATE:
                type = "DATE";
                break;
            case Types.DECIMAL:
                type = "DECIMAL";
                break;
            case Types.DISTINCT:
                type = "DISTINCT";
                break;
            case Types.DOUBLE:
                type = "DOUBLE";
                break;
            case Types.FLOAT:
                type = "FLOAT";
                break;
            case Types.INTEGER:
                type = "INTEGER";
                break;
            case Types.JAVA_OBJECT:
                type = "JAVA_OBJECT";
                break;
            case Types.LONGVARBINARY:
                type = "LONGVARBINARY";
                break;
            case Types.LONGVARCHAR:
                type = "LONGVARCHAR";
                break;
            case Types.NULL:
                type = "NULL";
                break;
            case Types.NUMERIC:
                type = "NUMERIC";
                break;
            case Types.OTHER:
                type = "OTHER";
                break;
            case Types.REAL:
                type = "REAL";
                break;
            case Types.SMALLINT:
                type = "SMALLINT";
                break;
            case Types.STRUCT:
                type = "STRUCT";
                break;
            case Types.TIME:
                type = "TIME";
                break;
            case Types.TIMESTAMP:
                type = "TIMESTAMP";
                break;
            case Types.TINYINT:
                type = "TINYINT";
                break;
            case Types.VARBINARY:
                type = "VARBINARY";
                break;
            case Types.VARCHAR:
                type = "VARCHAR";
                break;
            case Types.VARCHAR + 10000:
                type = "GEOMETRY";
                break;
        }

        return type;
    }
}