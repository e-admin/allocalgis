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
package org.deegree_impl.services.wfs.postgis;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.deegree.model.geometry.GM_Object;
import org.deegree.services.wfs.configuration.FeatureType;
import org.deegree.services.wfs.configuration.Reference;
import org.deegree.services.wfs.configuration.TableDescription;
import org.deegree_impl.io.DBAccess;
import org.deegree_impl.model.geometry.GMLAdapter;
import org.deegree_impl.model.geometry.PostGISAdapter;
import org.deegree_impl.services.wfs.RowSelector;
import org.postgis.Geometry;

/**
 * Encapsulates selected rows of a table that are identified by a table, a
 * <tt>Set</tt> of key values, and a keyfield. Used in the processing of
 * WFSDelete-requests.
 * <p>
 * Adds Point-DB specific handling of Update-Requests to the base class.
 * <p>
 * @author <a href="mailto:mschneider@lat-lon>Markus Schneider</a>
  * @author <a href="mailto:poth@lat-lon>Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:32 $
 */
public class PostgisRowSelector extends RowSelector {
    
    /** Creates a new instance of RowSelector */
    public PostgisRowSelector(TableDescription table, String keyfield, Set keys, 
                              DBAccess osa, FeatureType ft) throws SQLException {

        super (table, keyfield, keys, osa,  ft);
    }

    /**
     * Creates new <tt>RowSelector</tt>-objects that represent the rows
     * in other tables that are referenced from the current table's rows.
     * <p>
     * @return the referenced rows represented by new <tt>RowSelector</tt>s
     */
    public RowSelector [] getReferencedRows () throws SQLException {
        Reference [] refs = table.getReferences ();
        PostgisRowSelector [] rowSelectors = new PostgisRowSelector [refs.length];

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
            rowSelectors [i] = new PostgisRowSelector (targetTable, targetField, fKeys,
                                                     osa, ft);
        }        
        return rowSelectors;
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

            String fieldName = change [0];
            String fullName = table.getName () + "." + fieldName;            
            sql.append (fieldName).append ("=?");

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
        PreparedStatement stmt = con.prepareStatement( sql.toString () );

        for (int i = 0; i < changeList.size (); i++) {
            String [] change = (String []) changeList.get (i);
            String name  = change [0];
            String value = change [1];
            String fullName = table.getName () + "." + name;
            
            int type = ft.getDatastoreFieldType (fullName);
            if (type == FeatureType.GEOMETRY) {
                try {
                    // transform GML string to a deegree geometry
                    GM_Object gm = GMLAdapter.wrap( value );
                    // transform deegree geometry to a postgis geometry
                    Geometry geom = PostGISAdapter.export( gm );
                    stmt.setObject(index++, geom );
                } catch (Exception e) {
                    new SQLException( e.toString() );
                }
            }
            else {
                setFieldValue (
                    stmt, index++, ft.getDatastoreFieldType (fullName), value, name);
            }
        }        
        
        it = keys.iterator ();
        while (it.hasNext ()) {
            String value = it.next ().toString ();
            setFieldValue (stmt, index++, type, value, keyfield);
        }        
        stmt.execute ();
        stmt.close ();
    }
        
}
