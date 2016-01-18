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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.deegree.services.wfs.configuration.GeoFieldIdentifier;
import org.deegree.services.wfs.configuration.Reference;
import org.deegree.services.wfs.configuration.TableDescription;


/**
 * the interface describes a table that contains data that are part
 * of a feature type
 *
 * <p>---------------------------------------------------------------------</p>
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:32 $
 */
public class TableDescription_Impl implements TableDescription {
    private HashMap geoFields = null;
    private HashMap references = null;
    private String idField = null;
    private String name = null;
    private String targetName = null;
    private boolean deleteAllowed = true;
    private boolean insertAllowed = true;
    private boolean isAutoIncremented = false;
    private boolean isNumber = false;
    private boolean updateAllowed = true;

    /**
     * Creates a new TableDescription_Impl object.
     *
     * @param name 
     * @param targetName 
     * @param idField 
     * @param isNumber 
     * @param isAutoIncremented 
     * @param insertAllowed 
     * @param updateAllowed 
     * @param deleteAllowed 
     * @param geoFields 
     * @param references 
     */
    TableDescription_Impl( String name, String targetName, String idField, boolean isNumber, 
                           boolean isAutoIncremented, boolean insertAllowed, boolean updateAllowed, 
                           boolean deleteAllowed, HashMap geoFields, HashMap references ) {

        this.geoFields = new HashMap();
        this.references = new HashMap();
        setName( name );
        setTargetName( targetName );
        setIdField( idField );
        setIsNumber( isNumber );
        setIdFieldIsAutoIncremented( isAutoIncremented );
        setGeoFieldIdentifiers( geoFields );
        setReferences( references );
        setInsertAllowed( insertAllowed );
        setUpdateAllowed( updateAllowed );
        setDeleteAllowed( deleteAllowed );
    }

    /**
     * returns the name of the table. in the case that a datastore
     * encapsulates an esri shapefile this is the name of the shape
     * without extension
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
     * returns the name the table shall be mapped to at a feature (type)
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * @see getTargetName
     */
    public void setTargetName( String targetName ) {
        this.targetName = targetName;
    }

    /**
     * returns the name of the table's field that shall be interpreted
     * as ID
     */
    public String getIdField() {
        return idField;
    }

    /**
     * @see getIdField
     */
    public void setIdField( String idField ) {
        this.idField = idField;
    }

    /**
     * returns true if the id field is a number data type
     */
    public boolean isIdFieldNumber() {
        return isNumber;
    }

    /**
     * @see isNumber
     */
    public void setIsNumber( boolean isNumber ) {
        this.isNumber = isNumber;
    }

    /**
     * returns true if the value of the id will be set automaticly by
     * a database
     */
    public boolean isIdFieldAutoIncremented() {
        return isAutoIncremented;
    }

    /**
     * @see isIdFieldAutoIncremented
     */
    public void setIdFieldIsAutoIncremented( boolean isAutoIncremented ) {
        this.isAutoIncremented = isAutoIncremented;
    }

    /**
     * returns true if the submitted field is a reference to another
     * table
     */
    public boolean isReference( String datastoreField ) {
        return references.get( datastoreField ) != null;
    }

    /**
     * returns an object that describes the reference to another
     * table
     */
    public Reference[] getReferences( String datastoreField ) {
        Reference[] ref = null;
        ArrayList list = (ArrayList)references.get( datastoreField );

        if ( list != null ) {
            ref = (Reference[])list.toArray( new Reference[list.size()] );
        }

        return ref;
    }

    /**
     * returns all references contained within the table. an implementation
     * have to ensure that if no references are contained a zero length
     * will be returned
     */
    public Reference[] getReferences() {
        ArrayList list = new ArrayList();
        Iterator it = references.values().iterator();

        while ( it.hasNext() ) {
            ArrayList l = (ArrayList)it.next();

            for ( int i = 0; i < l.size(); i++ ) {
                list.add( l.get( i ) );
            }
        }

        Reference[] ref = new Reference[list.size()];
        return (Reference[])list.toArray( ref );
    }

    /**
     * @see getReferences
     */
    public void setReferences( HashMap references ) {
        this.references = references;
    }

    /**
     * @see getReferences
     */
    public void addReference( String field, Reference reference ) {
        ArrayList list = (ArrayList)references.get( field );

        if ( list == null ) {
            list = new ArrayList();
        }

        list.add( reference );
        references.put( field, list );
    }

    /**
     * returns true if the submitted field shall be interpreted as field
     * that contains geo spatial data. only needed for GMLDB and POINTDB
     */
    public boolean isGeoFieldIdentifier( String datastoreField ) {
        return geoFields.get( datastoreField ) != null;
    }

    /**
     * return an object that describes a field that contains geo spatial
     * data.
     */
    public GeoFieldIdentifier getGeoFieldIdentifier( String datastoreField ) {
        return (GeoFieldIdentifier)geoFields.get( datastoreField );
    }

    /**
     * returns all describtions for all fields that contains geo spatial data.
     * an implementation have to ensure that if no references are contained a
     * zero length will be returned
     */
    public GeoFieldIdentifier[] getGeoFieldIdentifier() {
        GeoFieldIdentifier[] geo = new GeoFieldIdentifier[geoFields.size()];
        Iterator it = geoFields.values().iterator();
        int i = 0;

        while ( it.hasNext() ) {
            geo[i++] = (GeoFieldIdentifier)it.next();
        }

        return geo;
    }

    /**
     * @see getGeoFieldIdentifier
     */
    public void setGeoFieldIdentifiers( HashMap geoFields ) {
        this.geoFields = geoFields;
    }

    /**
     * @see getGeoFieldIdentifier
     */
    public void addGeoFieldIdentifier( String field, GeoFieldIdentifier geoFieldId ) {
        geoFields.put( field, geoFieldId );
    }

    /** returns true if deleting rows from the tables are allowed
     *
     */
    public boolean isDeleteAllowed() {
        return deleteAllowed;
    }

    /**
     *
     *
     * @param deleteAllowed 
     */
    public void setDeleteAllowed( boolean deleteAllowed ) {
        this.deleteAllowed = deleteAllowed;
    }

    /** returns true if inserts into the table are allowed
     *
     */
    public boolean isInsertAllowed() {
        return insertAllowed;
    }

    /**
     *
     *
     * @param insertAllowed 
     */
    public void setInsertAllowed( boolean insertAllowed ) {
        this.insertAllowed = insertAllowed;
    }

    /** returns true if updates of the tables rows are allowed
     *
     */
    public boolean isUpdateAllowed() {
        return updateAllowed;
    }

    /**
     *
     *
     * @param updateAllowed 
     */
    public void setUpdateAllowed( boolean updateAllowed ) {
        this.updateAllowed = updateAllowed;
    }
}