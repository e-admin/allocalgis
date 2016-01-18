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
package org.deegree_impl.services.wfs.db;

import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.deegree.gml.GMLCoord;
import org.deegree.gml.GMLCoordinates;
import org.deegree.gml.GMLFeature;
import org.deegree.gml.GMLPoint;
import org.deegree.gml.GMLProperty;
import org.deegree.model.table.Table;
import org.deegree.services.OGCWebServiceException;
import org.deegree.services.OGCWebServiceRequest;
import org.deegree.services.OGCWebServiceResponse;
import org.deegree.services.wfs.configuration.DatastoreConfiguration;
import org.deegree.services.wfs.configuration.FeatureType;
import org.deegree.services.wfs.configuration.Reference;
import org.deegree.services.wfs.configuration.TableDescription;
import org.deegree.services.wfs.filterencoding.Filter;
import org.deegree.services.wfs.protocol.WFSDelete;
import org.deegree.services.wfs.protocol.WFSInsert;
import org.deegree.services.wfs.protocol.WFSInsertResult;
import org.deegree.services.wfs.protocol.WFSOperation;
import org.deegree.services.wfs.protocol.WFSTransactionRequest;
import org.deegree.services.wfs.protocol.WFSUpdate;
import org.deegree.xml.XMLTools;
import org.deegree_impl.gml.GMLFactory;
import org.deegree_impl.io.DBAccess;
import org.deegree_impl.io.DBConnectionPool;
import org.deegree_impl.io.SQLTools;
import org.deegree_impl.services.OGCWebServiceException_Impl;
import org.deegree_impl.services.wfs.InsertException;
import org.deegree_impl.services.wfs.InsertTree;
import org.deegree_impl.services.wfs.WFSMainLoop;
import org.deegree_impl.services.wfs.filterencoding.AbstractSQLBuilder;
import org.deegree_impl.services.wfs.filterencoding.GMLDBSQLBuilder;
import org.deegree_impl.services.wfs.protocol.WFSProtocolFactory;
import org.deegree_impl.tools.Debug;
import org.deegree_impl.tools.StringExtend;
import org.w3c.dom.Document;


/**
 * class defining the processing of a transaction request
 */
class CTransaction extends WFSMainLoop {
    private DBConnectionPool pool = null;
    private DatastoreConfiguration config = null;

    /**
     * Creates a new CTransaction object.
     *
     * @param parent 
     * @param request 
     */
    public CTransaction( PointDBDataStore parent, OGCWebServiceRequest request ) {
        super( parent, request );
        config = parent.getConfiguration();
        pool = parent.getConnectionPool();
    }

    protected OGCWebServiceResponse [] performRequest (OGCWebServiceRequest request) {
        Debug.debugMethodBegin( this, "performRequest" );
        
        OGCWebServiceResponse[] response = new OGCWebServiceResponse [1];
        
        WFSTransactionRequest tr = (WFSTransactionRequest)request;
        String handle = tr.getHandle();
        String lockId = tr.getLockId();
        String releaseAction = tr.getReleaseAction();
        WFSOperation[] operations = tr.getOperations();
        String[] aft = getAffectedFeatureTypes( operations );
        ArrayList inR = new ArrayList();
        int[] oI = new int[operations.length];

        try {
            // handle operations defined by the request
            for (int i = 0; i < operations.length; i++) {                
                if (operations[i] instanceof WFSInsert) {
                    Object[] o = performInsert( (WFSInsert)operations[i] );
                    if ( o[0] != null ) inR.add( o[0] );
                    // if this value equals 0 no expection raised during
                    // insertion. if the value is positiv some insertions
                    // failed. if the value is < 0 all insertions failed
                    oI[i] = ((Integer)o[1]).intValue();
                } else if ( operations[i] instanceof WFSUpdate ) {
                    if (performUpdate ((WFSUpdate) operations [i])) {
                        oI[i] = 0;
                    } else {
                        oI[i] = -1;
                    }
                } else if ( operations[i] instanceof WFSDelete ) {
                    if (performDelete ((WFSDelete) operations [i])) {
                        oI[i] = 0;
                    } else {
                        oI[i] = -1;
                    }
                } else {
                    // native request
                }                
            }
            
            String status = null;
            boolean succ = true;
            boolean failed = true;
            for (int i = 0; i < oI.length; i++) {
                if ( oI[i] != 0 ) { succ = false; }
                if ( oI[i] > 0 ) { failed = false; }
            }
            if ( succ ) { status = "SUCCESS"; }
            else
                if ( failed ) { status = "FAILED"; }
                else { status = "PARTIAL"; }
            
            // create response object
            WFSProtocolFactory factory = new WFSProtocolFactory();
            WFSInsertResult[] ir = new WFSInsertResult[ inR.size() ];
            ir = (WFSInsertResult[])inR.toArray( ir );
            response[0] =
            	WFSProtocolFactory.createWFSTransactionResponse( request, aft, null,
                                                      ir, status, handle );
        } catch(Exception e) {
            Debug.debugException( e, null );
            OGCWebServiceException exce =
                new OGCWebServiceException_Impl( "CTransaction: performRequest", 
                                                 StringExtend.stackTraceToString( e.getStackTrace() ) );
            WFSProtocolFactory factory = new WFSProtocolFactory();
            response[0] = WFSProtocolFactory.createWFSGetFeatureResponse(request, aft, exce, null);
        }
        
        Debug.debugMethodEnd();
        return response;
    }

    /**
     * returns the list of feature types that are affected by a Transaction
     */
    private String[] getAffectedFeatureTypes( WFSOperation[] operations ) {
        ArrayList list = new ArrayList();

        for ( int i = 0; i < operations.length; i++ ) {
            if ( operations[i] instanceof WFSInsert ) {
                String[] ft = ( (WFSInsert)operations[i] ).getFeatureTypes();

                for ( int j = 0; j < ft.length; j++ ) {
                    if ( parent.isKnownFeatureType( ft[j] ) ) {
                        list.add( ft[j] );
                    }
                }
            } else if ( operations[i] instanceof WFSUpdate ) {
                //((WFSUpdate)operations[i]).
            } else if ( operations[i] instanceof WFSDelete ) {
            } else {
                // native request
            }
        }

        return (String[])list.toArray( new String[list.size()] );
    }

    // --------------- Handling of <wfs:Update>-Requests ---------------

    /**
     * Updates FeatureType-instances from the datastore that are specified
     * by the given <tt>WFSUpdate</tt>.
     * <p>
     * @param update the WFSUpdate request to perform
     * @return true, if the Request succeeded, else false
     */
     private boolean performUpdate (WFSUpdate update) throws Exception {
        boolean success = true;
        Debug.debugMethodBegin (this, "performUpdate (WFSUpdate)");
        DBAccess osa = null;
        java.sql.Connection con = null;
        org.deegree.services.wfs.configuration.Connection connect =
            config.getConnection();
       
        try {
            // get jdbc connection and access object for oracle spatial
            con = pool.acuireConnection (connect.getDriver (),
                                         connect.getLogon (),
                                         connect.getUser (),
                                         connect.getPassword ());
            osa = new DBAccess (con);
            osa.setAutoCommit (false);

            FeatureType ft = config.getFeatureType (update.getTypeName());
            Set fIDs = getFeatureIDs (ft, update.getFilter(), osa);
            if (!fIDs.isEmpty ()) {
                TableDescription masterTable = ft.getMasterTable ();
                PointRowSelector selector = new PointRowSelector (
                    masterTable, masterTable.getIdField (), fIDs, osa, ft);

                // create a new HashMap that contains the table names as
                // keys, and an ArrayList of changes that are to be performed
                // in that table (String-array with 2 values)
                HashMap changes = new HashMap ();                
                HashMap properties = update.getProperties();

                Iterator it = properties.keySet().iterator ();
                while (it.hasNext ()) {
                    String name  = (String) it.next ();
                    String value = (String) properties.get (name);

                    if (name.startsWith ("/")) {
                        name = name.substring (1);
                    }
                    name = name.replace ('.', '/');
                  
                    if (!ft.isPropertyKnown (name)) {
                        throw new Exception (
                            "Property '" + name.replace ('/', '.') + "' is not known to Feature '"
                           + ft.getName() + "' (not mapped?)");
                    }                                                            
                    
                    // get the datastore-mapping of the property
                    String mapping = null;
                    String [] s_ = ft.getDatastoreField (name);

                    if (s_ == null) {
                        throw new Exception (name +
                            " is not known by the datastore");
                    } else {
                        mapping = s_[0];
                    }

                    String [] parts = splitString (mapping);
                    String tableName = parts [0].toUpperCase ();
                    String fieldName = parts [1].toUpperCase ();
                    
                    ArrayList list = (ArrayList) changes.get (tableName);
                    if (list == null) {
                        list = new ArrayList ();
                    }
                    String [] change = new String [2];
                    change [0] = fieldName;
                    change [1] = value;
                    list.add (change);

                    changes.put (tableName, list);
                }
                
                success = performUpdate (selector, changes);
                osa.commit ();
            }
        } catch (Exception e) { 
            success = false;
            System.out.println ("Exception: " + e);
            if (osa != null) osa.rollback ();
            Debug.debugException (e, "");
        }
        if (con != null) {
            pool.releaseConnection (con, connect.getDriver (),
                                    connect.getLogon (),
                                    connect.getUser (),
                                    connect.getPassword ());
        }        

        Debug.debugMethodEnd ();
        return success;
     }

    /**
     * Extracts the two parts of a concatenated String of the form
     * [a.]b (part a is optional). Method is null-save.
     * @return array of Strings, [0] = a, [1] = b
     */
    private String [] splitString(String name) {
        String [] parts = {"", ""};
        if (name != null) {
            String [] t = StringExtend.toArray(name, ".", false);
            if (t.length == 3) {
                parts [0] = t [0] + "." + t [1];
                parts [1] = t [2];
            }
            else if (t.length == 2) {
                parts [0] = t [0]; parts [1] = t [1];
            }
            else if (t.length == 1) {
                parts [1] = t [0];
            }
        }

        return parts;
    }     
     
     boolean performUpdate (PointRowSelector selector, HashMap changes)
        throws Exception {         
         PointRowSelector [] referencedRows = (PointRowSelector [])
            selector.getReferencedRows ();
         
         for (int i = 0; i < referencedRows.length; i++) {
             if (!performUpdate (referencedRows [i], changes)) {
                 return false;
             }             
         }
         String tableName = selector.getTableName ();

         ArrayList changeList = (ArrayList) changes.get (tableName);
         if (changeList != null) {
             selector.updateRows (changeList);
         }
         return true;
     }
    
    // --------------- Handling of <wfs:Delete>-Requests ---------------
    
    /**
     * Deletes FeatureType-instances from the datastore that are specified
     * by the given <tt>WFSDelete</tt>.
     * <p>
     * @param delete the WFS-Delete request to perform
     * @return true, if the Request succeeded, else false
     */
    private boolean performDelete (WFSDelete delete) throws Exception {
        Debug.debugMethodBegin( this, "performDelete (WFSDelete)" );

        boolean success = true;
        
        DBAccess osa = null;
        java.sql.Connection con = null;
        org.deegree.services.wfs.configuration.Connection connect =
            config.getConnection();
        
        try {
            // get jdbc connection and access object for oracle spatial
            con = pool.acuireConnection (connect.getDriver (),
                                         connect.getLogon (),
                                         connect.getUser (),
                                         connect.getPassword ());
            osa = new DBAccess (con);
            osa.setAutoCommit (false);

            FeatureType ft = config.getFeatureType (delete.getTypeName());            
            Set fIDs = getFeatureIDs (ft, delete.getFilter(), osa);
            if (!fIDs.isEmpty ()) {

                TableDescription masterTable = ft.getMasterTable ();
                PointRowSelector selector = new PointRowSelector (
                    masterTable, masterTable.getIdField(), fIDs, osa, ft);

                HashMap tables = new HashMap ();
                tables.put (masterTable.getName (), selector);
                performDelete (ft, tables);
                osa.commit ();
            }
        } catch (Exception e) { 
            success = false;
            if (osa != null) osa.rollback ();
            Debug.debugException (e, "");
        }
        if (con != null) {
            pool.releaseConnection (con, connect.getDriver (),
                                    connect.getLogon (),
                                    connect.getUser (),
                                    connect.getPassword ());
        }                
        Debug.debugMethodEnd();   
        return success;
    }    

    private void performDelete (FeatureType ft, HashMap tables) throws Exception {

        while (!tables.isEmpty ()) {
            Set tableNames = tables.keySet ();

            
            Iterator it = tableNames.iterator();            

            // check for a non-referenced table (that can be deleted safely)
            while (it.hasNext ()) {
                String name = (String) it.next ();
                PointRowSelector selector = (PointRowSelector) tables.get (name);

                TableDescription table = ft.getTableByName (name);

                // check if it is o.k. to delete the rows from this table, i.e.
                // that there are no references left from other tables of this
                // feature              
                
                // FIXME: very dumb version cause of lacking functions
                // in FeatureType (every table can only be referenced by one
                // table at the moment)
                boolean isReferenced = false;
                Iterator it2 = tableNames.iterator ();
                while (it2.hasNext () && !isReferenced) {

                    String s = ((String) it2.next ()).toUpperCase ();
                    TableDescription table2 = ft.getTableByName (s);
                    Reference [] refs = table2.getReferences ();
                    for (int i= 0; i < refs.length; i++) {
                        TableDescription targetTable = ft.getTableByName (refs [i].getTargetTable());
                        if (targetTable.getName ().equalsIgnoreCase (name)) {
                            isReferenced = true;
                            break;
                        }
                    }
                }
                if (isReferenced) {
                    // try to delete the next table then
                    continue;
                }
                
                // it is cool to delete the table, so collect the references
                // from this table to other tables (for further descend)
                PointRowSelector [] referencedRows = (PointRowSelector []) 
                    selector.getReferencedRows ();
                
                for (int i = 0; i < referencedRows.length; i++) {
                    tables.put (referencedRows [i].getTableName (),
                                referencedRows [i]);
                }

                // try to delete all affected rows from the current table
                selector.deleteRows ();

                // remove current table
                tables.remove (name);
                
                // deleted a table, now end the inner loop
                break;
            }
        }
    }
          
    /**
     * Retrieves all IDs of the Feature's MasterTable that are affected by the
     * given <tt>Filter</tt>.
     * <p>
     * @param ft configuration information that is used for the mapping of the
     *        XML-Filter to the SQL-table structure
     * @param filter <tt>Filter</tt> that is transformed to a WHERE-clause
     * @throws Exception if SQL-generation from the <tt>Filter</tt> fails or
     *         an SQL-error occurs
     */
    private Set getFeatureIDs (FeatureType ft, Filter filter,
                               DBAccess osa) throws Exception {
        
        StringBuffer query = new StringBuffer (
            "SELECT " + ft.getMasterTable().getIdField());
        AbstractSQLBuilder sqlBuilder = new GMLDBSQLBuilder (ft);
       
        if (filter != null) {
            query.append (sqlBuilder.filter2SQL (filter));
        } else {
            query.append (" FROM " + ft.getMasterTable().getTargetName());
        }
        
        java.sql.Connection con = osa.getConnection ();
        Statement stmt = con.createStatement ();
        ResultSet rs = stmt.executeQuery (query.toString ());              

        TreeSet fIDs = new TreeSet ();

        // extract all affected Feature-IDs (as Strings)
        while (rs.next()) {
            String fID = rs.getString (1);
            fIDs.add (fID);
        }

        rs.close ();
        stmt.close ();
        return fIDs;
    }

    // --------------- Handling of <wfs:Insert>-Requests ---------------    

    /**
     * performs an insertion of one or more features into the database
     */
    private Object[] performInsert( WFSInsert insert ) throws Exception {
        Debug.debugMethodBegin( this, "performInsert (WFSInsert)" );

        WFSInsertResult result = null;
        java.sql.Connection con = null;
        org.deegree.services.wfs.configuration.Connection connect = config.getConnection();

        DBAccess osa = null;
        // database insert attempts
        int k1 = 0;
        // successful inserts
        int k2 = 0;

        try {
            // get jdbc connection and access object for oracle spatial
            con = pool.acuireConnection( connect.getDriver(), connect.getLogon(), connect.getUser(), 
                                         connect.getPassword() );
            osa = new DBAccess( con );
            osa.setAutoCommit( false );

            String[] ftNames = insert.getFeatureTypes();
            ArrayList idlist = new ArrayList();
            String handle = "";

            for ( int i = 0; i < ftNames.length; i++ ) {
                String ftName = ftNames[i];

                if ( parent.isKnownFeatureType( ftName ) ) {
                    org.deegree.services.wfs.configuration.FeatureType featureType = 
                            config.getFeatureType( ftName );
                    GMLFeature[] gmlFeatures = insert.getFeatures( ftName );

                    for ( int j = 0; j < gmlFeatures.length; j++ ) {
                        GMLFeature gmlFeature = gmlFeatures[j];

                        try {
                            k1++;
                            InsertTree tree = InsertTree.buildFromGMLFeature( gmlFeature, 
                                                                              featureType );
                            performInsert( tree, osa );
                            k2++;
                        } catch ( Exception e ) {
                            Debug.debugException( e, "" );

                            // rollback if insertion failed
                            osa.rollback();

                            // store/collect error message(s) as handle that will be
                            // submitted to the WFSInsertResult
                            handle += ( e.getMessage() + "\n" );
                        }

                        osa.commit();
                    }
                }
            }

            String[] ids = (String[])idlist.toArray( new String[idlist.size()] );
            result = WFSProtocolFactory.createWFSInsertResult( handle, ids );
        } catch ( SQLException e ) {
            if ( osa != null ) {
                osa.rollback();
            }

            Debug.debugException( e, "" );
        }

        if ( pool != null ) {
            pool.releaseConnection( con, connect.getDriver(), connect.getLogon(), connect.getUser(), 
                                    connect.getPassword() );
        }

        // calculate success status
        // 0 = success
        // > 0 = failed partially
        // < 0 = failed completely
        int k = k1 - k2;

        if ( k2 == 0 ) {
            k *= -1;
        }

        Debug.debugMethodEnd();
        return new Object[] { result, new Integer( k ) };
    }    
    
    /**
     *
     *
     * @param tree 
     * @param osa 
     *
     * @return 
     *
     * @throws Exception 
     * @throws InsertException 
     */
    public ArrayList performInsert( InsertTree tree, DBAccess osa ) throws Exception {
        Debug.debugMethodBegin( this, "performInsert (InsertTree, DBAccess)" );

        String dbVendor = osa.getDataBaseVendor().toUpperCase();
        Iterator it = tree.getSons().iterator();
        Reference[] refs = tree.getTableDescription().getReferences();

        // descend into the connected subtrees
        while ( it.hasNext() ) {

            InsertTree subTree = (InsertTree)it.next();
            ArrayList foreignKey = performInsert( subTree, osa );

            if ( ( foreignKey == null ) || ( foreignKey.size() == 0 ) ) {
                break;
            }

            // inherited foreignKey is only used if subTree is not a 
            // jointable AND the current table does not have an explicit
            // value for the foreignKey-field
            if ( ( refs != null ) && !subTree.isJoinTable() ) {
                for ( int i = 0; i < refs.length; i++ ) {
                    if ( refs[i].getTargetTable()
                                .equalsIgnoreCase( subTree.getTableDescription().getName() ) ) {
                        if ( tree.getFieldValue( refs[i].getTableField() ) == null ) {
                            tree.putFieldValue( refs[i].getTableField(), foreignKey );
                        }

                        break;
                    }
                }
            }
        }

        // now insert the data contained in the fields of the current
        // InsertTree-object (as these should be fields of the table)
        String table = tree.getTableDescription().getName().toUpperCase();
        String idField = tree.getTableDescription().getIdField().toUpperCase();
        ArrayList idValues = (ArrayList)tree.getFieldValue( idField );
        HashMap columnTypes = osa.getColumnTypesAsInt( table, null );

        if ( columnTypes == null ) {
            throw new InsertException( "Table '" + table + "' has no columnTypes?!" );
        }

        // if the current table has no values for fields at all, do nothing
        if ( tree.isFieldsEmpty() ) {
            return null;
        }

        for ( int ii = 0; ii < idValues.size(); ii++ ) {
            //
            // STEP a: SELECT the data (check for an identical row)
            //
            it = tree.getFieldsKeys();
            StringBuffer select = new StringBuffer ("SELECT * FROM ").append(table).append (" WHERE ");
            if ( tree.getTableDescription().isIdFieldNumber() ) {
                select.append( idField ).append( "= " ).append( idValues.get(ii) );
            } else {
                select.append( idField ).append( "= '" ).append( idValues.get(ii) ).append( "'" );
            }
                        
            Table result = osa.performTableQuery (select.toString (), 0, 1);
            if (result != null && result.getRowCount () > 0) {
                //Found identical row and leave the current loop turn
                continue;
            }
            
            //
            // STEP B: INSERT the data
            //
            StringBuffer fieldList = new StringBuffer( "(" );
            StringBuffer valueList = new StringBuffer( "VALUES(" );
            it = tree.getFieldsKeys();

            while ( it.hasNext() ) {
                String key = (String)it.next();
                ArrayList vals = (ArrayList)tree.getFieldValue( key );

                int type = tree.getFeatureType().getDatastoreFieldType( table + "." + key );

                if ( type == org.deegree.services.wfs.configuration.FeatureType.UNKNOWN ) {
                    // not defined in configuration-file, so ask from db
                    Integer typeFromDB = (Integer)columnTypes.get( key );

                    if ( typeFromDB == null ) {
                        throw new InsertException( "Table '" + table + "' has no field named '" + 
                                                   key + "'" );
                    }

                    type = typeFromDB.intValue();
                }

                // because the field will be split into it x- and y-value if it's
                // a geometry
                if ( type != org.deegree.services.wfs.configuration.FeatureType.GEOMETRY ) {
                    fieldList.append( key );
                } else {
                    fieldList.append( key + "_x," ).append( key + "_y" );
                }
                
                if ( vals.get( ii ) == null ) {
                    valueList.append( "NULL" );
                } else { 
                    switch ( type ) {
                        case Types.BIGINT:
                        case Types.NUMERIC:
                        case Types.REAL:
                        case Types.SMALLINT:
                        case Types.TINYINT:
                        case Types.DOUBLE:
                        case Types.FLOAT:
                        case Types.INTEGER:
                        case Types.DECIMAL:
                        {
                            valueList.append( vals.get( ii ) );
                            break;
                        }
                        case Types.CHAR:
                        case Types.VARCHAR:
                        case Types.LONGVARCHAR:
                        {
                            if ( ( (String)vals.get( ii ) ).length() == 0 ) {
                                valueList.append( "NULL" );
                            } else {
                                valueList.append( "'" )
                                         .append( (String)vals.get( ii ) ) 
                                         .append( "'" );
                            }

                            break;
                        }
                        case Types.DATE:
                        case Types.TIME:
                        case Types.TIMESTAMP:
                        {
                            valueList.append( SQLTools.formatDate( dbVendor, (String)vals.get( ii ) ) );
                            break;
                        }
                        case org.deegree.services.wfs.configuration.FeatureType.GEOMETRY:
                        {
                            if ( ( (String)vals.get( ii ) ).length() == 0 ) {
                                valueList.append( "NULL,NULL" );
                            } else {
                                valueList.append( getGeomInsert( (String)vals.get( ii ) ) );
                            }

                            break;
                        }
                        default:
                            throw new InsertException( table + "." + key + " has invalid type: " + 
                                                       type );
                    }
                }

                if ( it.hasNext() ) {
                    fieldList.append( "," );
                    valueList.append( "," );
                }
            }

            fieldList.append( ")" );
            valueList.append( ")" );

            StringBuffer stmt = new StringBuffer( valueList.length() + 1000 ).append( 
                                        "INSERT INTO " ).append( table ).append( " " )
                                                                             .append( fieldList )
                                                                             .append( " " )
                                                                             .append( valueList );
            Debug.debugSimpleMessage( stmt.toString() );

            osa.performInsert( stmt.toString() );
        }

        Debug.debugMethodEnd();
        return idValues;
    }

    /**
     * returns the insert fragment for the submitted GML geometry in PointDB
     * conform expression (x- and y-column)
     */
    private String getGeomInsert( String gml ) throws Exception {
        Debug.debugMethodBegin( this, "getGeomInsert" );

        Document doc = XMLTools.parse( new StringReader( gml ) );

        GMLPoint point = (GMLPoint)GMLFactory.createGMLGeometry( doc.getDocumentElement() );

        StringBuffer geom = new StringBuffer();

        GMLCoord coord = point.getCoord();

        if ( coord != null ) {
            geom.append( coord.getX() ).append( "," ).append( coord.getY() );
        } else {
            GMLCoordinates coordinates = point.getCoordinates();
            String s = coordinates.getCoordinates();
            String sep = "" + coordinates.getCoordinateSeperator();
            String[] vals = StringExtend.toArray( s, sep, false );
            geom.append( vals[0] ).append( "," ).append( vals[1] );
        }

        Debug.debugMethodEnd();
        return geom.toString();
    }

    /**
     * returns the name of the database/table column extracted from
     * the name of a gml property
     */
    private String getColumnName( String propertyName ) {
        String[] s = StringExtend.toArray( propertyName, ".", false );
        return s[s.length - 1];
    }
}