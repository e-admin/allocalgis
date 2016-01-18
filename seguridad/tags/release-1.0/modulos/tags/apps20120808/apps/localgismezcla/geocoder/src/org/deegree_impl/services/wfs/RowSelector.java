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

import java.util.*;
import java.sql.*;

import org.deegree_impl.io.*;
import org.deegree_impl.tools.TimeTools;
import org.deegree.services.wfs.configuration.*;

/**
 * Encapsulates selected rows of a table that are identified by a table, a
 * <tt>Set</tt> of key values, and a keyfield. Used in the processing of
 * WFSDelete-requests.
 * <p>
 * Due to the use of <tt>PreparedStatements</tt> this class should be generic,
 * but for of Geometry-fields, the <tt>updateRows()</tt>-method has to be
 * overwritten in a specific class.
 * <p>
 * @author <a href="mailto:mschneider@lat-lon>Markus Schneider</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:32 $
 */
public class RowSelector {
    
    protected TableDescription table;    
    protected Set keys;
    protected String keyfield;
    protected int type;
    protected DBAccess osa;
    protected FeatureType ft;   
    protected java.sql.Connection con;
        
    /** Creates a new instance of RowSelector */
    public RowSelector (TableDescription table, String keyfield, Set keys,
                        DBAccess osa, FeatureType ft) throws SQLException {

        keyfield = keyfield.toUpperCase ();
        this.table = table;
        this.keys = keys;
        this.keyfield = keyfield;
        this.ft = ft;
        this.osa = osa;
        con = osa.getConnection ();

        String fullName = table.getName () + "." + keyfield;
        type = ft.getDatastoreFieldType (fullName);
    }

    /**
     * Returns the associated table name.
     * <p>
     * @return the name of the table
     */
    public String getTableName () {
        return table.getName ();
    }
    
    /**
     * Creates new <tt>RowSelector</tt>-objects that represent the rows
     * in other tables that are referenced from the current table's rows.
     * <p>
     * @return the referenced rows represented by new <tt>RowSelector</tt>s
     */
    public RowSelector [] getReferencedRows () throws SQLException {
        Reference [] refs = table.getReferences ();
        RowSelector [] rowSelectors = new RowSelector [refs.length];
System.out.println(777);        
        for (int i = 0; i < refs.length; i++) {

            // the referenced table
            TableDescription targetTable =  ft.getTableByName (
                refs [i].getTargetTable ());

            // the field that is referenced
            String targetField = refs [i].getTargetField ();            
            StringBuffer sql = new StringBuffer ("SELECT ")
                .append (refs [i].getTableField ())
                .append (" FROM ")
                .append (table.getName ())
                .append (" WHERE ")
                .append (keyfield)
                .append (" IN (");
            
            Iterator it = keys.iterator ();
            while (it.hasNext ()) {
                it.next ();
                sql.append ("?");
                if (it.hasNext ()) {
                    sql.append (",");
                }
            }            
            sql.append (")");           
            
            PreparedStatement stmt = con.prepareStatement (sql.toString ());
            it = keys.iterator ();
            int j = 1;
            while (it.hasNext ()) {
                String value = it.next ().toString ();
                setFieldValue (stmt, j, type, value, keyfield);
                j++;
            }

            ResultSet rs = stmt.executeQuery ();
            TreeSet fKeys = new TreeSet ();
            while (rs.next ()) {
                String fk = rs.getString (1);
                if (fk != null) fKeys.add (fk);
            }
            rs.close ();
            stmt.close ();

            // create a new RowSelector that holds the referenced rows in the
            // referenced table
            rowSelectors [i] = new RowSelector (targetTable, targetField, fKeys,
                                                osa, ft);
        }        
        return rowSelectors;
    }

    /**
     * Deletes the selected rows from the table.
     */
    public void deleteRows () throws SQLException {

        // if table has deleteAllowed="false" set, skip delete
        if (!table.isDeleteAllowed ()) return;
        
        StringBuffer sql = new StringBuffer ("DELETE FROM ")
            .append (table.getName ())
            .append (" WHERE ")
            .append (keyfield)
            .append (" IN (");

        Iterator it = keys.iterator ();
        while (it.hasNext ()) {
            it.next ();
            sql.append ("?");
            if (it.hasNext ()) {
                sql.append (",");
            }
        }            
        sql.append (")");

        PreparedStatement stmt = con.prepareStatement (sql.toString ());
        it = keys.iterator ();
        int i = 1;
        while (it.hasNext ()) {
            String value = it.next ().toString ();
            setFieldValue (stmt, i, type, value, keyfield);
            i++;
        }
        stmt.execute ();
        stmt.close ();
    }

    /**
     * Updates the selected rows of the table. The changes to be performed are
     * given as an <tt>ArrayList</tt> of String-Arrays s [] of the following
     * form:
     * <ul>
     * <li>s[0]: fieldName (name of the field of the table)
     * <li>s[1]: new value for the field
     * </ul>
     * <p>
     * @param changeList list of changes to be performed
     */
    public void updateRows (ArrayList changeList) throws SQLException {

        StringBuffer sql = new StringBuffer ("UPDATE ").
            append (table.getName ()).append (" SET ");

        for (int i = 0; i < changeList.size (); i++) {
            String [] change = (String []) changeList.get (i);
            sql.append (change [0]).append ("=?");
            if (i != changeList.size () - 1) {
                sql.append (",");
            }
        }
        
        sql.append (" WHERE ").append (keyfield).append (" IN (");

        Iterator it = keys.iterator ();
        while (it.hasNext ()) {
            it.next ();
            sql.append ("?");
            if (it.hasNext ()) {
                sql.append (",");
            }
        }            
        sql.append (")");

        // now set the values for both the SET-part as well as the WHERE-
        // condition of the PreparedStatement
        int index = 1;
        PreparedStatement stmt = con.prepareStatement (sql.toString ());

        for (int i = 0; i < changeList.size (); i++) {
            String [] change = (String []) changeList.get (i);
            String name  = change [0];
            String value = change [1];
            setFieldValue (stmt, index++, ft.getDatastoreFieldType (name),
                           value, name);
        }        
        
        it = keys.iterator ();
        while (it.hasNext ()) {
            String value = it.next ().toString ();
            setFieldValue (stmt, index++, type, value, keyfield);
        }        
        stmt.execute ();
        stmt.close ();
    }    

    /**
     * Produces a <tt>String</tt> representation of this object.
     * <p>
     * @return the textual representation
     */
    public String toString () {
        StringBuffer sb = new StringBuffer ();
        sb.append ("Table: '").append (table.getName ())
            .append ("', Field: '").append (keyfield)
            .append ("', Values: [");

        Iterator it = keys.iterator ();
        while (it.hasNext ()) {
            sb.append (it.next ());
            if (it.hasNext ()) {
                sb.append (", ");
            }
        }
        sb.append ("]");

        return sb.toString ();
    }    
    
    /**
     * Convenience method that calls the right setXXX ()-method for the
     * given <tt>PreparedStatment</tt>.
     * <p>
     * @param stmt the <tt>PreparedStatement</tt> to be "filled"
     * @param i index of the field to be set
     * @param type <tt>java.sql.Types</tt>-constant for the field type
     * @param value new String value for the field
     * @throws Exception if the conversion of the String failed (for example
     *         in the case of <tt>Integer</tt>s or <tt>Date</tt>s)
     */
    protected void setFieldValue (PreparedStatement stmt, int i, int type,
                                  String value, String field)
        throws SQLException {
           
        try {
            switch (type) {
                case Types.BIGINT:                        
                case Types.SMALLINT:
                case Types.TINYINT:
                case Types.INTEGER:{
                    stmt.setInt (i, (new Double (value)).intValue());
                    break;
                }
                case Types.DOUBLE:
                case Types.FLOAT:
                case Types.REAL:
                case Types.NUMERIC:                                                
                case Types.DECIMAL:{
                    stmt.setDouble (i, Double.parseDouble (value));
                    break;
                }
                case Types.CHAR:
                case Types.VARCHAR:
                case Types.LONGVARCHAR: {
                    stmt.setString (i, value);
                    break;
                }                                        
                case Types.DATE:
                case Types.TIME:
                case Types.TIMESTAMP: {
                    stmt.setDate (i, new java.sql.Date (
                        TimeTools.createCalendar (value).getTime ().getTime()));
                    break;
                }
                default: {
                    throw new SQLException (table.getName () + "." + field
                            + " has invalid type: " + type);
                }
            }
        } catch (NumberFormatException e) {
            throw new SQLException ("Value '" + value + "' is not suitable for "
              + "field '" + field + "' in table '" + table.getName () + "': "
              + e.getMessage ());
        }
    }  
}
