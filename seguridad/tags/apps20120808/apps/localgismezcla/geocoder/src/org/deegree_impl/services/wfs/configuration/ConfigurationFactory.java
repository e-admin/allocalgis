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

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.deegree.services.wfs.configuration.Connection;
import org.deegree.services.wfs.configuration.DatastoreConfiguration;
import org.deegree.services.wfs.configuration.FeatureType;
import org.deegree.services.wfs.configuration.GeoFieldIdentifier;
import org.deegree.services.wfs.configuration.MasterTable;
import org.deegree.services.wfs.configuration.OutputFormat;
import org.deegree.services.wfs.configuration.Reference;
import org.deegree.services.wfs.configuration.RelatedTable;
import org.deegree.services.wfs.configuration.TableDescription;
import org.deegree.services.wfs.configuration.WFSConfigurationException;
import org.deegree.tools.ParameterList;
import org.deegree.xml.XMLTools;
import org.deegree_impl.io.DBAccess;
import org.deegree_impl.io.DBConnectionPool;
import org.deegree_impl.tools.Debug;
import org.deegree_impl.tools.ParameterList_Impl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 */
public final class ConfigurationFactory {
    private static String namespace = "http://www.deegree.org/wfs";

    /**
     * creates a <tt>DatastoreConfiguration</tt> object from a dom document
     * accessable by the submitted <tt>Reader</tt>
     */
    public static DatastoreConfiguration createDatastoreConfiguration( Reader reader )
                                                               throws WFSConfigurationException {
        Debug.debugMethodBegin( "ConfigurationFactory", "createDatastoreConfiguration(Reader)" );

        Document doc = null;

        try {
            doc = XMLTools.parse( reader );
        } catch ( Exception e ) {
            throw new WFSConfigurationException( e.toString() );
        }

        DatastoreConfiguration dc = createDatastoreConfiguration( doc );

        Debug.debugMethodEnd();
        return dc;
    }

    /**
     * creates a <tt>DatastoreConfiguration</tt> object from a dom document    
     */
    public static DatastoreConfiguration createDatastoreConfiguration( Document doc )
                                                               throws WFSConfigurationException {
        Debug.debugMethodBegin( "ConfigurationFactory", "createDatastoreConfiguration(Document)" );

        DatastoreConfiguration dc = null;

        try {
            Element element = doc.getDocumentElement();
            namespace = element.getNamespaceURI();

            // get datastore type
            String s = XMLTools.getAttrValue( element, "type" );
            int type = -1;

            if ( s.equals( "ORACLESPATIAL" ) ) {
                type = DatastoreConfiguration.ORACLESPATIAL;
            } else if ( s.equals( "GMLDB" ) ) {
                type = DatastoreConfiguration.GMLDB;
            } else if ( s.equals( "POINTDB" ) ) {
                type = DatastoreConfiguration.POINTDB;
            } else if ( s.equals( "SHAPEFILES" ) ) {
                type = DatastoreConfiguration.SHAPEFILES;
            } else if ( s.equals( "SDE" ) ) {
                type = DatastoreConfiguration.SDE;
            } else if ( s.equals( "BNA" ) ) {
                type = DatastoreConfiguration.BNA;
            } else if ( s.equals( "POSTGIS" ) ) {
                type = DatastoreConfiguration.POSTGIS;
            } else if ( s.equals( "MYSQL" ) ) {
                type = DatastoreConfiguration.MYSQL;
            } else if ( s.equals( "MAPINFO" ) ) {
                type = DatastoreConfiguration.MAPINFO;
            } else {
                throw new WFSConfigurationException( s + " is a unknown datastore type" );
            }

            // get datastore name
            String name = XMLTools.getAttrValue( element, "name" );

            // get connection description. this is only available if the datastore
            // encapsulates a database
            NodeList nl = element.getElementsByTagNameNS( namespace, "Connection" );
            Connection con = null;

            if ( ( nl != null ) && ( nl.getLength() > 0 ) ) {
                con = createConnection( (Element)nl.item( 0 ) );
            }

            // get the description of the feature types available through the datastore
            nl = element.getElementsByTagNameNS( namespace, "FeatureType" );

            FeatureType[] ft = createFeatureTypes( nl, con, type );

            dc = createDatastoreConfiguration( name, type, con, ft );
        } catch ( Exception e ) {
            throw new WFSConfigurationException( e.toString() );
        }

        Debug.debugMethodEnd();
        return dc;
    }

    /**
     * creates a <tt>DatastoreConfiguration</tt> object 
     * @param name of the datastore configuration
     * @param type Type of the datastore to be described
     * @param connection Connection if the datastore if a database
     * @param featureTypes Feature type supported by the datastore
     * @return <tt>DatastoreConfiguration</tt>
     */
    public static DatastoreConfiguration createDatastoreConfiguration( String name, int type, 
                                                                       Connection connection, 
                                                                       FeatureType[] featureTypes ) {
        return new DatastoreConfiguration_Impl( name, type, connection, featureTypes );
    }

    /**
     * creates a <tt>Connection<tt> object that describes the JDBC connection 
     * paramter for accessing a database
     */
    private static Connection createConnection( Element element ) throws Exception {
        Debug.debugMethodBegin( "ConfigurationFactory", "createConnection" );

        Node node = element.getElementsByTagNameNS( namespace, "driver" ).item( 0 );
        String driver = node.getFirstChild().getNodeValue();

        if ( driver == null ) {
            driver = "";
        }

        node = element.getElementsByTagNameNS( namespace, "logon" ).item( 0 );

        String logon = node.getFirstChild().getNodeValue();

        node = element.getElementsByTagNameNS( namespace, "user" ).item( 0 );

        String user = "";

        if ( node.getFirstChild() != null ) {
            user = node.getFirstChild().getNodeValue();
        }

        node = element.getElementsByTagNameNS( namespace, "password" ).item( 0 );

        String password = "";

        if ( node.getFirstChild() != null ) {
            password = node.getFirstChild().getNodeValue();
        }

        node = element.getElementsByTagNameNS( namespace, "spatialversion" ).item( 0 );

        String spatialversion = null;

        if ( node != null ) {
            spatialversion = node.getFirstChild().getNodeValue();
        }

        node = element.getElementsByTagNameNS( namespace, "sdedatabase" ).item( 0 );

        String sdeDatabase = null;

        if ( node != null ) {
            if ( node.getFirstChild() != null ) {
                sdeDatabase = node.getFirstChild().getNodeValue();
            } else {
                sdeDatabase = "";
            }
        } else {
            sdeDatabase = "";
        }

        Connection con = null;

        if ( spatialversion != null ) {
            con = createConnectionOracle( driver, logon, user, password, spatialversion );
        } else if ( sdeDatabase != null ) {
            con = createConnectionSDE( driver, logon, user, password, sdeDatabase );
        } else {
            con = createConnection( driver, logon, user, password );
        }

        Debug.debugMethodEnd();
        return con;
    }

    /**
    * creates a <tt>Connection<tt> object that describes the JDBC connection 
    * paramter for accessing a database.
    * @param driver name of the JDBC driver to be used
    * @param logon connection description to a database
    * @param user user name
    * @param password users password
    */
    public static Connection createConnection( String driver, String logon, String user, 
                                               String password ) {
        return new Connection_Impl( driver, logon, user, password );
    }

    /**
     * creates a <tt>Connection<tt> object that describes the JDBC connection 
     * paramter for accessing a database.
     * @param driver name of the JDBC driver to be used
     * @param logon connection description to a database
     * @param user user name
     * @param password users password
     * @param spatialVersion version of the spatial extension if a Oracle database
     *                       shall be connected
     */
    public static Connection createConnectionOracle( String driver, String logon, String user, 
                                                     String password, String spatialVersion ) {
        Connection_Impl con = new Connection_Impl( driver, logon, user, password );
        con.setSpatialVersion( spatialVersion );
        return con;
    }

    /**
    * creates a <tt>Connection<tt> object that describes the JDBC connection 
    * paramter for accessing a database.
    * @param driver name of the JDBC driver to be used
    * @param logon connection description to a database
    * @param user user name
    * @param password users password
    * @param sdeDatabase 
    */
    public static Connection createConnectionSDE( String driver, String logon, String user, 
                                                  String password, String sdeDatabase ) {
        Connection_Impl con = new Connection_Impl( driver, logon, user, password );
        con.setSDEDatabase( sdeDatabase );
        return con;
    }

    /**
     * creates a description object for each feature type that's accessable through
     * a datastore.
     */
    private static FeatureType[] createFeatureTypes( NodeList nl, Connection con, int type )
                                             throws Exception {
        Debug.debugMethodBegin( "ConfigurationFactory", "createFeatureType" );

        FeatureType[] ft = new FeatureType[nl.getLength()];

        for ( int i = 0; i < nl.getLength(); i++ ) {
            Element element = (Element)nl.item( i );

            String name = XMLTools.getAttrValue( element, "name" );

            // get output format description
            Element elem = (Element)element.getElementsByTagNameNS( namespace, "OutputFormat" )
                                           .item( 0 );
            OutputFormat[] of = createOutputFormats( elem );

            // get description of each field belonging to a feature type
            NodeList nl_ = element.getElementsByTagNameNS( namespace, "MappingField" );
            HashMap[] mf = createMappingFields( nl_ );

            // get description of the feature types master table
            elem = (Element)element.getElementsByTagNameNS( namespace, "MasterTable" ).item( 0 );

            MasterTable mt = createMasterTable( elem, type );

            // get descriptions of all tables related to the master table for
            // a feature type
            nl_ = element.getElementsByTagNameNS( namespace, "RelatedTable" );

            RelatedTable[] rt = createRelatedTables( nl_ );

            // get the feature types coordinate reference system
            elem = (Element)element.getElementsByTagNameNS( namespace, "CRS" ).item( 0 );

            String crs = null;

            if ( elem != null ) {
                crs = elem.getFirstChild().getNodeValue();
            }
            
            // get the feature types coordinate reference system code as it is
            // stored within the data source. This code may differs from the
            // EPSG code stored in element <CRS>
            elem = (Element)element.getElementsByTagNameNS( namespace, "InternalCRS" ).item( 0 );

            String icrs = null;

            if ( elem != null ) {
                icrs = elem.getFirstChild().getNodeValue();
            }

            // query the field types from the db (for fallback if field is
            // not mapped but used in a Request)
            HashMap dbFieldTypes = new HashMap();

            if ( ( type == DatastoreConfiguration.ORACLESPATIAL ) || 
                 ( type == DatastoreConfiguration.GMLDB ) || 
                 ( type == DatastoreConfiguration.POINTDB ) ||
                 ( type == DatastoreConfiguration.MYSQL ) ||
                 ( type == DatastoreConfiguration.POSTGIS ) ) {
                if ( con != null ) {
                    // get jdbc connection and access object
                    DBConnectionPool pool = DBConnectionPool.getInstance();
                    java.sql.Connection jdbcCon = pool.acuireConnection( con.getDriver(), 
                                                                         con.getLogon(), 
                                                                         con.getUser(), 
                                                                         con.getPassword() );
                    DBAccess dbAccess = new DBAccess( jdbcCon );

                    // collect field types of master table
                    TableDescription table = mt;
                    HashMap columnTypes = dbAccess.getColumnTypesAsInt( table.getName(), null );
                    Iterator it = columnTypes.keySet().iterator();

                    while ( it.hasNext() ) {
                        String fieldName = (String)it.next();
                        Integer fieldType = (Integer)columnTypes.get( fieldName );
                        dbFieldTypes.put( table.getName() + "." + fieldName, fieldType );
                    }

                    // collect field types of related tables
                    for ( int j = 0; j < rt.length; j++ ) {
                        table = rt[j];
                        columnTypes = dbAccess.getColumnTypesAsInt( table.getName(), null );
                        it = columnTypes.keySet().iterator();

                        while ( it.hasNext() ) {
                            String fieldName = (String)it.next();
                            Integer fieldType = (Integer)columnTypes.get( fieldName );
                            dbFieldTypes.put( table.getName() + "." + fieldName, fieldType );
                        }
                    }

                    pool.releaseConnection( jdbcCon, con.getDriver(), con.getLogon(), con.getUser(), 
                                            con.getPassword() );
                }
            }

            ft[i] = createFeatureType( name, of, mf[0], mf[1], mf[2], mf[3], dbFieldTypes, mf[4], 
                                       mf[5], mt, rt, crs, icrs );
        }

        Debug.debugMethodEnd();
        return ft;
    }

    /**
     * creates a description object for each feature type that's accessable through
     * a datastore.
     * @param name Name of the feature type
     * @param outputFormats Output formats supported by the feature type
     * @param properties Mapping between properties and datastore fields
     * @param propertyTypes Mapping between properties and their data types
     * @param datastoreFields Mapping between datastore fields and properties
     * @param datastoreFieldTypes Mapping between datastore fields and their data types
     * @param dbFieldTypes Mapping between db fields and their data types
     * @param masterTable Description of the features types master table
     * @param relatedTables Description of the feature types related tables
     * @param crs Coordinate reference system of the feature type
     * @param icrs Coordinate reference system code of the feature type as it is
     *              used within the datasource
     */
    public static FeatureType createFeatureType( String name, OutputFormat[] outputFormats, 
                                                 HashMap properties, HashMap propertyTypes, 
                                                 HashMap datastoreFields, 
                                                 HashMap datastoreFieldTypes, HashMap dbFieldTypes, 
                                                 HashMap alias, HashMap aliasInv, 
                                                 MasterTable masterTable, 
                                                 RelatedTable[] relatedTables, 
                                                 String crs, String icrs ) {
        return new FeatureType_Impl( name, outputFormats, properties, propertyTypes, 
                                     datastoreFields, datastoreFieldTypes, dbFieldTypes, 
                                     alias, aliasInv, masterTable, relatedTables, 
                                     crs, icrs );
    }

    /**
     * creates a descrptions object for the output formats supported by the
     * datastore for a feature type 
     */
    private static OutputFormat[] createOutputFormats( Element element ) throws Exception {
        Debug.debugMethodBegin( "ConfigurationFactory", "createOutputFormats" );

        ArrayList list = new ArrayList();

        NodeList nl = element.getChildNodes();

        for ( int i = 0; i < nl.getLength(); i++ ) {
            if ( nl.item( i ) instanceof Element ) {
                Element elem = (Element)nl.item( i );
                // get format name
                String name = elem.getLocalName();
                String responsibleClass = XMLTools.getAttrValue( elem, "responsibleClass" );

                // get additional parameter that shall be submitted to the
                // responsible class       
                NodeList nl_ = elem.getElementsByTagNameNS( namespace, "Param" );
                ParameterList pl = createParameterList( nl_ );

                // get schema location if defined for the format
                URL url = null;
                NodeList nnl = elem.getElementsByTagNameNS( namespace, "SchemaLocation" );

                if ( ( nnl != null ) && ( nnl.getLength() > 0 ) ) {
                    String s = nnl.item( 0 ).getFirstChild().getNodeValue();
                    url = new URL( s );
                }

                list.add( createOutputFormat( name, responsibleClass, pl, url ) );
            }
        }

        OutputFormat[] of = new OutputFormat[list.size()];
        of = (OutputFormat[])list.toArray( of );

        Debug.debugMethodEnd();
        return of;
    }

    /**
     * creates a descrption object for a output format supported by a datastore 
     * for a feature type.
     * @param name Name of the output format
     * @param responsibleClass Class that's responsible for creating the output
     *                         format
     * @param parameter List of parameters that will be submitted to the 
     *                  responsible class
     * @param schemaLocation URL of the XML schema describing the output format
     * @return A <tt>OutputFormat</tt> object.
     */
    public static OutputFormat createOutputFormat( String name, String responsibleClass, 
                                                   ParameterList parameter, URL schemaLocation ) {
        return new OutputFormat_Impl( name, responsibleClass, parameter, schemaLocation );
    }

    /**
     * create additional parameters that shall be submitted to the resposible
     * for a defined output format
     */
    private static ParameterList createParameterList( NodeList nl ) {
        Debug.debugMethodBegin( "ConfigurationFactory", "createParameterList" );

        ParameterList pl = new ParameterList_Impl();

        if ( ( nl != null ) && ( nl.getLength() > 0 ) ) {
            for ( int i = 0; i < nl.getLength(); i++ ) {
                String name = XMLTools.getAttrValue( nl.item( i ), "name" );
                String value = XMLTools.getAttrValue( nl.item( i ), "value" );
                pl.addParameter( name, value );
            }
        }

        Debug.debugMethodEnd();
        return pl;
    }

    /**
     * creates descrption objects for each field of the feature type to one or
     * more database fields.
     */
    private static HashMap[] createMappingFields( NodeList nl ) {
        Debug.debugMethodBegin( "ConfigurationFactory", "createMappingFields" );

        HashMap properties = new HashMap();
        HashMap propertyTypes = new HashMap();
        HashMap datastoreFields = new HashMap();
        HashMap datastoreFieldTypes = new HashMap();
        HashMap aliasMap = new HashMap();
        HashMap aliasMapInv = new HashMap();

        for ( int i = 0; i < nl.getLength(); i++ ) {
            // get property name and type
            Node prop = ( (Element)nl.item( i ) ).getElementsByTagNameNS( namespace, "Property" )
                                             .item( 0 );
            String pName = XMLTools.getAttrValue( prop, "name" );

            if ( pName.startsWith( "/" ) ) {
                pName = pName.substring( 1 );
            }

            String s = XMLTools.getAttrValue( prop, "type" );
            int pType = getDataType( s );
            // get name and type of the datastore field assigned to the property
            NodeList dsfnl = ( (Element)nl.item( i ) ).getElementsByTagNameNS( namespace, 
                                                                               "DatastoreField" );
            int[] dsfTypes = new int[dsfnl.getLength()];
            String[] dsfNames = new String[dsfnl.getLength()];
            String[] aliasNames = new String[dsfnl.getLength()];

            for ( int j = 0; j < dsfnl.getLength(); j++ ) {
                String s_ = XMLTools.getAttrValue( dsfnl.item( j ), "alias" );

                if ( ( s_ != null ) && ( s_.length() > 0 ) ) {
                    aliasNames[j] = s_.toUpperCase();
                } else {
                    aliasNames[j] = XMLTools.getAttrValue( dsfnl.item( j ), "name" ).toUpperCase();
                }

                dsfNames[j] = XMLTools.getAttrValue( dsfnl.item( j ), "name" ).toUpperCase();
                s = XMLTools.getAttrValue( dsfnl.item( j ), "type" );
                dsfTypes[j] = getDataType( s );
            }

            properties.put( pName, dsfNames );
            propertyTypes.put( pName, new Integer( pType ) );
            aliasMap.put( pName, aliasNames );

            for ( int j = 0; j < dsfNames.length; j++ ) {
                datastoreFields.put( dsfNames[j], pName );
                datastoreFieldTypes.put( dsfNames[j], new Integer( dsfTypes[j] ) );
                aliasMapInv.put( aliasNames[j], pName );
            }
        }

        HashMap[] h = new HashMap[6];
        h[0] = properties;
        h[1] = propertyTypes;
        h[2] = datastoreFields;
        h[3] = datastoreFieldTypes;
        h[4] = aliasMap;
        h[5] = aliasMapInv;

        Debug.debugMethodEnd();
        return h;
    }

    /**
     * creates descrption objects for each field of the feature type to map one or
     * more database fields. each property is assigned toone or more datastroe fields.
     * so all submitted arrays have the same size. the datastore fields and types
     * must have the same column number.
     * @param propertyNames array of property names
     * @param propertyTypes Array containing one type for each named property
     * @param dataStoreFields datastore fields assigned to the properties
     * @param dataStoreFieldTypes types of the datastore fields.
     * @return array <tt>HashMap</tt> containing the mappings
     */
    public static HashMap[] createMappingFields( String[] propertyNames, String[] propertyTypes, 
                                                 String[][] dataStoreFields, 
                                                 String[][] dataStoreFieldTypes )
                                         throws Exception {
        if ( ( propertyNames.length != propertyTypes.length ) || 
                 ( propertyTypes.length != dataStoreFields.length ) || 
                 ( dataStoreFields.length != dataStoreFieldTypes.length ) ) {
            throw new Exception( "submitted arrays must have the same length" );
        }

        HashMap properties_ = new HashMap();
        HashMap propertyTypes_ = new HashMap();
        HashMap datastoreFields_ = new HashMap();
        HashMap datastoreFieldTypes_ = new HashMap();

        for ( int i = 0; i < propertyTypes.length; i++ ) {
            properties_.put( propertyNames[i], dataStoreFields[i] );
            propertyTypes_.put( propertyNames[i], propertyTypes[i] );

            for ( int j = 0; j < dataStoreFields[i].length; j++ ) {
                datastoreFields_.put( dataStoreFields[i][j], propertyNames[i] );
                datastoreFields_.put( dataStoreFields[i][j], dataStoreFieldTypes[i][j] );
            }
        }

        HashMap[] h = new HashMap[4];
        h[0] = properties_;
        h[1] = propertyTypes_;
        h[2] = datastoreFields_;
        h[3] = datastoreFieldTypes_;

        return h;
    }

    /**
     *
     *
     * @param s 
     *
     * @return 
     */
    private static int getDataType( String s ) {
        int type = FeatureType.UNKNOWN;

        if ( s.equals( "ARRAY" ) ) {
            type = Types.ARRAY;
        } else if ( s.equals( "BIGINT" ) ) {
            type = Types.BIGINT;
        } else if ( s.equals( "BINARY" ) ) {
            type = Types.BINARY;
        } else if ( s.equals( "BIT" ) ) {
            type = Types.BIT;
        } else if ( s.equals( "BLOB" ) ) {
            type = Types.BLOB;
        } else if ( s.equals( "BOOLEAN" ) ) {
            type = Types.BOOLEAN;
        } else if ( s.equals( "CHAR" ) ) {
            type = Types.CHAR;
        } else if ( s.equals( "CLOB" ) ) {
            type = Types.CLOB;
        } else if ( s.equals( "DATALINK" ) ) {
            type = Types.DATALINK;
        } else if ( s.equals( "DATE" ) ) {
            type = Types.DATE;
        } else if ( s.equals( "DECIMAL" ) ) {
            type = Types.DECIMAL;
        } else if ( s.equals( "DISTINCT" ) ) {
            type = Types.DISTINCT;
        } else if ( s.equals( "DOUBLE" ) ) {
            type = Types.DOUBLE;
        } else if ( s.equals( "FLOAT" ) ) {
            type = Types.FLOAT;
        } else if ( s.equals( "INTEGER" ) ) {
            type = Types.INTEGER;
        } else if ( s.equals( "JAVA_OBJECT" ) ) {
            type = Types.JAVA_OBJECT;
        } else if ( s.equals( "LONGVARBINARY" ) ) {
            type = Types.LONGVARBINARY;
        } else if ( s.equals( "LONGVARCHAR" ) ) {
            type = Types.LONGVARCHAR;
        } else if ( s.equals( "NULL" ) ) {
            type = Types.NULL;
        } else if ( s.equals( "NUMERIC" ) ) {
            type = Types.NUMERIC;
        } else if ( s.equals( "OTHER" ) ) {
            type = Types.OTHER;
        } else if ( s.equals( "REAL" ) ) {
            type = Types.REAL;
        } else if ( s.equals( "SMALLINT" ) ) {
            type = Types.SMALLINT;
        } else if ( s.equals( "STRUCT" ) ) {
            type = Types.STRUCT;
        } else if ( s.equals( "TIME" ) ) {
            type = Types.TIME;
        } else if ( s.equals( "TIMESTAMP" ) ) {
            type = Types.TIMESTAMP;
        } else if ( s.equals( "TINYINT" ) ) {
            type = Types.TINYINT;
        } else if ( s.equals( "VARBINARY" ) ) {
            type = Types.VARBINARY;
        } else if ( s.equals( "VARCHAR" ) ) {
            type = Types.VARCHAR;
        } else if ( s.equals( "GEOMETRY" ) ) {
            type = FeatureType.GEOMETRY;
        }

        return type;
    }

    /**
     * create a description object for the master table of the feature type
     */
    private static MasterTable createMasterTable( Element element, int type )
                                          throws IOException {
        Debug.debugMethodBegin( "ConfigurationFactory", "createMasterTable" );

        // get tables name. if the table isn't a file its name will be 
        // transformed to upper cases
        String name = XMLTools.getAttrValue( element, "name" );

        if ( ( type != DatastoreConfiguration.BNA ) && 
                 ( type != DatastoreConfiguration.SHAPEFILES ) ) {
            name = name.toUpperCase();
        } else {
            // test if referenced files really exist
            URL url = new URL( name );
            name = url.getFile();

            if ( !name.startsWith( "/" ) ) {
                name = "/" + name;
            }

            //its a shapefile; check if it exits
            File file = null;

            if ( type == DatastoreConfiguration.SHAPEFILES ) {
                // if the datasource is a shapefile the extension must be added
                // because shapes are just named with their base name without
                // extension                
                file = new File( name + ".shp" );
            } else {
                file = new File( name );
            }

            if ( !file.exists() ) {
                System.out.println( "file: " + file.getPath() + " doesn't exist" );
                throw new IOException( "file: " + file.getPath() + " doesn't exist" );
            }
        }

        String targetName = XMLTools.getAttrValue( element, "targetName" );

        String tmp = XMLTools.getAttrValue( element, "insertAllowed" );
        boolean insertAllowed = ( tmp == null ) || tmp.equalsIgnoreCase( "true" ) || 
                                tmp.equals( "1" );
        tmp = XMLTools.getAttrValue( element, "updateAllowed" );

        boolean updateAllowed = ( tmp == null ) || tmp.equalsIgnoreCase( "true" ) || 
                                tmp.equals( "1" );
        tmp = XMLTools.getAttrValue( element, "deleteAllowed" );

        boolean deleteAllowed = ( tmp == null ) || tmp.equalsIgnoreCase( "true" ) || 
                                tmp.equals( "1" );

        // get id field description
        Element elem = (Element)element.getElementsByTagNameNS( namespace, "IdField" ).item( 0 );
        String idFieldName = elem.getFirstChild().getNodeValue();
        String s = XMLTools.getAttrValue( elem, "number" );
        boolean number = s.equals( "1" ) || s.equals( "true" );
        s = XMLTools.getAttrValue( elem, "auto" );

        boolean auto = s.equals( "1" ) || s.equals( "true" );

        // get references to other tables
        NodeList nl = element.getElementsByTagNameNS( namespace, "Reference" );
        HashMap ref = new HashMap();

        if ( ( nl != null ) && ( nl.getLength() > 0 ) ) {
            ref = createReferences( nl );
        }

        // get geo-field identifiers if the table contains geodata
        nl = element.getElementsByTagNameNS( namespace, "GeoFieldIdentifier" );

        HashMap geoF = new HashMap();

        if ( ( nl != null ) && ( nl.getLength() > 0 ) ) {
            geoF = createGeoFieldIdentifiers( nl );
        }

        MasterTable mt = createMasterTable( name, targetName, idFieldName, number, auto, 
                                            insertAllowed, updateAllowed, deleteAllowed, geoF, ref );

        Debug.debugMethodEnd();
        return mt;
    }

    /**
     * create a description object for the master table of a feature type
     * @param name Name of the table
     * @param targetName Name that shall be used for this table within FaetureType
     *                   objects
     * @param idField Name of the field that shall be used as id for the tables
     *                content
     * @param isNumber True if the ID-field if of a number type
     * @param isAutoIncremented True if the values of the ID-field will be incremented
     *                          automaticly by the datastore (database)
     * @param geoFields names and dimensions of the tables geo-spatial fields
     * @param references names and descriptions of references to other tables
     * @return <tt>MasterTable</tt>
     */
    public static MasterTable createMasterTable( String name, String targetName, String idField, 
                                                 boolean isNumber, boolean isAutoIncremented, 
                                                 boolean insertAllowed, boolean updateAllowed, 
                                                 boolean deleteAllowed, HashMap geoFields, 
                                                 HashMap references ) {
        return new MasterTable_Impl( name, targetName, idField, isNumber, isAutoIncremented, 
                                     insertAllowed, updateAllowed, deleteAllowed, geoFields, 
                                     references );
    }

    /**
     * creates a description object for reference to a related table for the 
     * master table or an higher level related table
     */
    private static HashMap createReferences( NodeList nl ) {
        Debug.debugMethodBegin( "ConfigurationFactory", "createReferences" );

        HashMap ref = new HashMap();

        for ( int i = 0; i < nl.getLength(); i++ ) {
            String tableField = XMLTools.getAttrValue( nl.item( i ), "tableField" ).toUpperCase();
            String s = XMLTools.getAttrValue( nl.item( i ), "replaceable" );
            boolean replaceable = s.equals( "1" ) || s.equals( "true" );
            String targetTable = XMLTools.getAttrValue( nl.item( i ), "targetTable" ).toUpperCase();
            String targetField = XMLTools.getAttrValue( nl.item( i ), "targetField" ).toUpperCase();
            ArrayList list = (ArrayList)ref.get( tableField );

            if ( list == null ) {
                list = new ArrayList();
            }

            list.add( createReferences( tableField, replaceable, targetTable, targetField ) );
            ref.put( tableField, list );
        }

        Debug.debugMethodEnd();
        return ref;
    }

    /**
     * creates a description object for reference to a related table for the 
     * master table or an higher level related table
     * @param tableField Name of the field that shall be interpreted as reference
     * @param replaceable True if the field shall be replaced by the refrenced content
     * @param targetTable Name of the table that is referenced
     * @param targetField Name of the target tables field that must match the 
     *                    tableField
     * @return <tt>Reference</tt>
     */
    public static Reference createReferences( String tableField, boolean replaceable, 
                                              String targetTable, String targetField ) {
        return new Reference_Impl( tableField, replaceable, targetTable, targetField );
    }

    /**
     * creates a description object for each gea column of a table
     */
    private static HashMap createGeoFieldIdentifiers( NodeList nl ) {
        Debug.debugMethodBegin( );

        HashMap geoF = new HashMap();

        for ( int i = 0; i < nl.getLength(); i++ ) {
            String name = nl.item( i ).getFirstChild().getNodeValue().toUpperCase();
            String s = XMLTools.getAttrValue( nl.item( i ), "dimension" );
            int dim = -1;

            if ( s != null ) {
                dim = Integer.parseInt( s );
            }

            String id = name;

            if ( dim > 0 ) {
                geoF.put( id + "_X", createGeoFieldIdentifiers( name, dim ) );
                geoF.put( id + "_Y", createGeoFieldIdentifiers( name, dim ) );

                if ( dim == 3 ) {
                    geoF.put( id + "_Z", createGeoFieldIdentifiers( name, dim ) );
                }
            } else {
                geoF.put( id, createGeoFieldIdentifiers( name, dim ) );
            }
        }

        Debug.debugMethodEnd();
        return geoF;
    }

    /**
     * creates a description object for a column containing geo-spatial data
     * @param name Name of the column containing geo-spatial data
     * @param dim Dimension of the geo-spatial data; just used for POINTDB
     * @return <tt>GeoFieldIdentifier</tt>
     */
    public static GeoFieldIdentifier createGeoFieldIdentifiers( String name, int dimension ) {
        return new GeoFieldIdentifier_Impl( name, dimension );
    }

    /**
     * create a description object for each table of the feature type related 
     * direct or indirect to the master table
     */
    private static RelatedTable[] createRelatedTables( NodeList nl ) {
        Debug.debugMethodBegin( "ConfigurationFactory", "createRelatedTable" );

        RelatedTable[] rt = new RelatedTable[nl.getLength()];

        for ( int i = 0; i < nl.getLength(); i++ ) {
            Element element = (Element)nl.item( i );
            // get tables name
            String name = XMLTools.getAttrValue( element, "name" );

            String targetName = XMLTools.getAttrValue( element, "targetName" );

            String tmp = XMLTools.getAttrValue( element, "insertAllowed" );
            boolean insertAllowed = ( tmp == null ) || tmp.equalsIgnoreCase( "true" ) || 
                                    tmp.equals( "1" );
            tmp = XMLTools.getAttrValue( element, "updateAllowed" );

            boolean updateAllowed = ( tmp == null ) || tmp.equalsIgnoreCase( "true" ) || 
                                    tmp.equals( "1" );
            tmp = XMLTools.getAttrValue( element, "deleteAllowed" );

            boolean deleteAllowed = ( tmp == null ) || tmp.equalsIgnoreCase( "true" ) || 
                                    tmp.equals( "1" );

            // get id field description
            Element elem = (Element)element.getElementsByTagNameNS( namespace, "IdField" )
                                           .item( 0 );
            String idFieldName = elem.getFirstChild().getNodeValue();
            String s = XMLTools.getAttrValue( elem, "number" );
            boolean number = s.equals( "1" ) || s.equals( "true" );
            s = XMLTools.getAttrValue( elem, "auto" );

            boolean auto = s.equals( "1" ) || s.equals( "true" );

            // get references to other tables
            NodeList nl_ = element.getElementsByTagNameNS( namespace, "Reference" );
            HashMap ref = new HashMap();

            if ( ( nl_ != null ) && ( nl_.getLength() > 0 ) ) {
                ref = createReferences( nl_ );
            }

            // get geo-field identifiers if the table contains geodata
            nl_ = element.getElementsByTagNameNS( namespace, "GeoFieldIdentifier" );

            HashMap geoF = new HashMap();

            if ( ( nl_ != null ) && ( nl_.getLength() > 0 ) ) {
                geoF = createGeoFieldIdentifiers( nl_ );
            }

            s = XMLTools.getAttrValue( element, "joinTable" );

            boolean joinTable = ( s != null ) && ( s.equals( "1" ) || s.equals( "true" ) );

            rt[i] = createRelatedTable( name, targetName, idFieldName, number, auto, insertAllowed, 
                                        updateAllowed, deleteAllowed, geoF, ref, joinTable );
        }

        Debug.debugMethodEnd();
        return rt;
    }

    /**
     * create a description object for a table of a feature type related 
     * direct or indirect to the master table
     * @param name Name of the table
     * @param targetName Name that shall be used for this table within FaetureType
     *                   objects
     * @param idField Name of the field that shall be used as id for the tables
     *                content
     * @param isNumber True if the ID-field if of a number type
     * @param isAutoIncremented True if the values of the ID-field will be incremented
     *                          automaticly by the datastore (database)
     * @param geoFields names and dimensions of the tables geo-spatial fields
     * @param references names and descriptions of references to other tables
     * @param joinTable True if the table is a join table
     * @return <tt>RelatedTable</tt>
     */
    public static RelatedTable createRelatedTable( String name, String targetName, String idField, 
                                                   boolean isNumber, boolean isAutoIncremented, 
                                                   boolean insertAllowed, boolean updateAllowed, 
                                                   boolean deleteAllowed, HashMap geoFields, 
                                                   HashMap references, boolean joinTable ) {
        return new RelatedTable_Impl( name, targetName, idField, isNumber, isAutoIncremented, 
                                      insertAllowed, updateAllowed, deleteAllowed, geoFields, 
                                      references, joinTable );
    }
}