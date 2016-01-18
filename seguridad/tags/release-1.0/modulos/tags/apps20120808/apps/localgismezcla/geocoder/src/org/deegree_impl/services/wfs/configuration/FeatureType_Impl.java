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

import org.deegree.services.wfs.configuration.FeatureType;
import org.deegree.services.wfs.configuration.MasterTable;
import org.deegree.services.wfs.configuration.OutputFormat;
import org.deegree.services.wfs.configuration.Reference;
import org.deegree.services.wfs.configuration.RelatedTable;
import org.deegree.services.wfs.configuration.TableDescription;
import org.deegree_impl.tools.StringExtend;


/**
 * describes the mapping of a feature type to one or more tables
 * of a datasource
 *
 * <p>---------------------------------------------------------------------</p>
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */
public class FeatureType_Impl implements FeatureType {
    private ArrayList relatedTables = null;
    private HashMap alias = null;
    private HashMap aliasInv = null;
    private HashMap datastoreFieldTypes = null;
    private HashMap datastoreFields = null;

    // used for fallback if field is not explicitly mapped (information in the
    // HashMap has been retrieved directly from the database system)
    // keys String, values: Integer
    private HashMap dbFieldTypes = null;
    private HashMap outputFormat = null;
    // keys: String, values: String []
    private HashMap properties = null;
    // keys: String, values: Integer
    private HashMap propertyTypes = null;
    private HashMap referencedBy = null;
    private HashMap tables = null;
    private MasterTable masterTable = null;
    private String crs = null;
    private String icrs = null;
    private String name = null;

    /**
     * Creates a new FeatureType_Impl object.
     *
     * @param name 
     * @param outputFormat 
     * @param properties 
     * @param propertyTypes 
     * @param datastoreFields 
     * @param datastoreFieldTypes 
     * @param dbFieldTypes
     * @param alias 
     * @param aliasInv 
     * @param masterTable 
     * @param relatedTables 
     * @param crs 
     */
    FeatureType_Impl( String name, OutputFormat[] outputFormat, HashMap properties, 
                      HashMap propertyTypes, HashMap datastoreFields, HashMap datastoreFieldTypes, 
                      HashMap dbFieldTypes, HashMap alias, HashMap aliasInv, MasterTable masterTable, 
                      RelatedTable[] relatedTables, String crs, String icrs ) {
        this.outputFormat = new HashMap();
        this.tables = new HashMap();
        this.referencedBy = new HashMap();
        this.relatedTables = new ArrayList();

        setName( name );
        setOutputFormat( outputFormat );
        setPropertyTypes( propertyTypes );
        setProperties( datastoreFields );
        setDatastoreFields( properties );
        setDatastoreFieldTypes( datastoreFieldTypes );
        setDBFieldTypes( dbFieldTypes );
        setAlias( alias );
        setPropertyFromAlias( aliasInv );
        setMasterTable( masterTable );
        setRelatedTables( relatedTables );
        setCRS( crs );       
        setInternalCRS( icrs );       
    }

    /**
     * returns the name of the feature type
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
     * returns an array of objects that describes the output formatting for the
     * feature type. at least one
     */
    public OutputFormat[] getOutputFormat() {
        OutputFormat[] tmp = new OutputFormat[outputFormat.size()];
        synchronized ( outputFormat ) {
            Iterator iterator = outputFormat.values().iterator();
            int i = 0;

            while ( iterator.hasNext() ) {
                tmp[i++] = (OutputFormat)iterator.next();
            }
        }

        return tmp;
    }

    /**
     * returns a named output format. if no output format with the submitted name
     * is known <tt>null</tt> will be returned
     */
    public OutputFormat getOutputFormat( String name ) {
        return (OutputFormat)outputFormat.get( name );
    }

    /**
     * @see getOutputFormat
     */
    public void setOutputFormat( OutputFormat[] outputFormat ) {
        this.outputFormat.clear();

        if ( outputFormat != null ) {
            for ( int i = 0; i < outputFormat.length; i++ ) {
                addOutputFormat( outputFormat[i] );
            }
        }
    }

    /**
     * @see getOutputFormat
     */
    public void addOutputFormat( OutputFormat outputFormat ) {
        this.outputFormat.put( outputFormat.getName(), outputFormat );
    }

    /** returns all mappings with property names as keys
     *
     */
    public HashMap getMappings() {
        return properties;
    }

    /**
     * returns the names of the datastore fields a property of the
     * feature type will be mapped to
     */
    public String[] getDatastoreField( String property ) {
        Object o = properties.get( property );
        if ( o != null ) {
            return (String[])o;
        } else {
            property = property.replace( '/', '.' );
            property = StringExtend.validateString( property, "." );
            return new String[] { property };
        }
    }

    /**
     * @see getDatastoreField
     */
    public void setDatastoreFields( HashMap properties ) {
        this.properties = properties;
    }

    /**
     * @see getDatastoreField
     */
    public void addDatastoreField( String property, String[] datastoreFields ) {
        properties.put( property, datastoreFields );
    }

    /**
     * Returns the type of the datastore field. If has not been mapped
     * explicitly, the metadata of the underlying database is queried (i.e.
     * the table description).
     * <p>
     * @param datastoreField fieldname in the form 'TABLE.FIELD' (uppercased)
     * @return type as defined in <tt>java.sql.Types</tt>/<tt>FeatureType</tt>,
     *         <tt>FeatureType.UNKNOWN</tt> if the field is neither defined in
     *         configuration nor in the database system
     */
    public int getDatastoreFieldType (String datastoreField) {
        Integer type = (Integer) datastoreFieldTypes.get (datastoreField);
        if (type == null) {
            type = (Integer) dbFieldTypes.get (datastoreField);
        }
        if (type != null) {
            return type.intValue ();
        }
        return FeatureType.UNKNOWN;
    }

    /**
     * Checks if a Property (mapping or TABLE.FIELD) is known to the datastore.
     * TABLE.FIELD syntax is only valid if no mappings are defined at all.
     * Properties may be used if they are explicitly defined in the datastore
     * configuration.
     * <p>
     * @param property Property to be looked up
     * @return true, if it is known, else false
     */
    public boolean isPropertyKnown (String property) {
        return (properties.get (property) != null ||
                (properties.isEmpty () && dbFieldTypes.get (property) != null));
    }
    
    /**
     * @see getDatastoreFieldType
     */
    public void setDatastoreFieldTypes( HashMap datastoreFieldTypes ) {
        this.datastoreFieldTypes = datastoreFieldTypes;
    }

    /**
     * @see getDatastoreField
     */
    public void addDatastoreFieldType( String datastoreField, int datastoreFieldType ) {
        propertyTypes.put( datastoreField, new Integer( datastoreFieldType ) );
    }

    public void setDBFieldTypes( HashMap dbFieldTypes ) {
        this.dbFieldTypes = dbFieldTypes;
    }    
    
    /**
    * returns the names of the property a datastore field will be mapped to
    */
    public String getProperty( String datastoreField ) {
        return (String)datastoreFields.get( datastoreField );
    }

    /**
     * sets datafield - property combinations to the feature type
     */
    public void setProperties( HashMap datastoreFields ) {
        this.datastoreFields = datastoreFields;
    }

    /**
     * adds a datafield - property combination to the feature type
     */
    public void addProperty( String datastoreField, String property ) {
        datastoreFields.put( datastoreField, property );
    }

    /**
     * returns the type of the property a datastore field will be mapped to
     */
    public int getPropertyType( String property ) {
        Integer in_ = (Integer)propertyTypes.get( property );

        if ( in_ != null ) {
            return in_.intValue();
        } else {
            return FeatureType.UNKNOWN;
        }
    }

    /**
     * @see getProperty
     */
    public void setPropertyTypes( HashMap propertyTypes ) {
        this.propertyTypes = propertyTypes;
    }

    /**
     * @see getProperty
     */
    public void addPropertyType( String property, int propertyType ) {
        propertyTypes.put( property, new Integer( propertyType ) );
    }

    /**
     * returns the description of the datastores master table
     */
    public MasterTable getMasterTable() {
        return masterTable;
    }

    /**
     * @see getMasterTable
     */
    public void setMasterTable( MasterTable masterTable ) {
        this.masterTable = masterTable;

        Reference[] ref = masterTable.getReferences();

        for ( int i = 0; i < ref.length; i++ ) {
            referencedBy.put( ref[i].getTargetTable().toUpperCase(), 
                              masterTable.getName().toUpperCase() );
        }

        tables.put( masterTable.getName().toUpperCase(), masterTable );
    }

    /**
     * returns a description of all tables that are related direct or
     * indirect to the master table. if no related tables are defined
     * the method returns a zero length array
     */
    public RelatedTable[] getRelatedTables() {
        RelatedTable[] tmp = new RelatedTable[relatedTables.size()];
        return (RelatedTable[])relatedTables.toArray( tmp );
    }

    /**
     * @see getRelatedTables
     */
    public void setRelatedTables( RelatedTable[] relatedTables ) {
        this.relatedTables.clear();

        if ( relatedTables != null ) {
            for ( int i = 0; i < relatedTables.length; i++ ) {
                addRelatedTable( relatedTables[i] );
            }
        }
    }

    /**
     * @see getRelatedTables
     */
    public void addRelatedTable( RelatedTable relatedTable ) {
        relatedTables.add( relatedTable );

        Reference[] ref = relatedTable.getReferences();

        for ( int i = 0; i < ref.length; i++ ) {
            referencedBy.put( ref[i].getTargetTable().toUpperCase(), 
                              relatedTable.getName().toUpperCase() );
        }

        tables.put( relatedTable.getName().toUpperCase(), relatedTable );
    }

    /**
     * returns the description of the table where the submitted table (name) is
     * referenced by. If the name of the master table is submitted, <tt>null</tt>
     * will be returned because it isn't referenced by a related table.
     */
    public String getReferencedBy( String name ) {
        return (String)referencedBy.get( name );
    }

    /**
     * returns a description object for the table identified by the submitted name.
     * if no table with the submitted can be found <tt>null</tt> will be returned.
     */
    public TableDescription getTableByName( String name ) {
        return (TableDescription)tables.get( name );
    }

    /**
     * returns the name of the coordinated reference system the geo spatial
     * data are stored. this is mandatory for ORACLESPATIAL, POINTDB and
     * SHAPEFILES.
     */
    public String getCRS() {
        return crs;
    }

    /**
     * @see getCRS
     */
    public void setCRS( String crs ) {
        this.crs = crs;
    }
    
    /**
     * returns the name of the coordinated reference system as it is used within
     * the data source. This code may differs from the EPSG code stored in 
     * element <CRS> 
     */
    public String getInternalCRS() {
        return icrs;
    }

    /**
     * @see getCRS
     */
    public void setInternalCRS( String icrs ) {
        this.icrs = icrs;
    }

    /** returns the alieas names of the datastore fields asigned to a property
     *
     */
    public String[] getAlias( String datastoreField ) {
        return (String[])alias.get( datastoreField );
    }
 
    /**
     *
     *
     * @param alias 
     */
    public void setAlias( HashMap alias ) {
        this.alias = alias;
    }

    /** returns the property name asigned to datastore fields alias
     *
     */
    public String getPropertyFromAlias( String alias ) {
        return (String)aliasInv.get( alias );
    }    

    /**
     *
     *
     * @param aliasInv 
     */
    public void setPropertyFromAlias( HashMap aliasInv ) {
        this.aliasInv = aliasInv;
    }
    
    /** returns the list (in correct order) of tables that are used to reach
     * the submitted property
     *
     */
    public TableDescription[] getPath(String property) {
        
        String df = getDatastoreField( property )[0];
        String tab = extractTableName( df );
        TableDescription td = getTableByName( tab );
        if ( tab.equals( getMasterTable().getName() ) ) {
            return new TableDescription[] { td };
        }
        
        return null;
    }
    
    /**
     * extracts the table name from a property. it is assumed that the property
     * name is constructed like this: table.propertyname or schema.table.propertyname.
     * if no table is specified with a property name, the name of the feature
     * types master table will be returned.
     */
    private String extractTableName(String prop)
    {
        String tab = getMasterTable().getName();
        
        int pos = prop.lastIndexOf('.');
        if ( pos > 0 ) {
            tab = prop.substring(0,pos);
        } 
        
        return tab;
    }
    
    public String toString () {
        StringBuffer sb = new StringBuffer ();
        sb.append ("FeatureType '").append (name).append ("':\n");
        sb.append ("- Properties:\n");        
        Iterator it = properties.keySet ().iterator ();
        while (it.hasNext ()) {
            String key = (String) it.next ();
            String [] value = (String []) properties.get (key);
            sb.append ("-- key: '" + key + "', value: [");
            for (int i = 0; i < value.length; i++) {
                sb.append ("'" + value [i] + "'");
                if (i != value.length - 1) {
                    sb.append (", ");
                }
            }
            sb.append ("]\n");
        }

        sb.append ("- PropertyTypes:\n");        
        it = propertyTypes.keySet ().iterator ();
        while (it.hasNext ()) {
            String key = (String) it.next ();
            Integer value = (Integer) propertyTypes.get (key);
            sb.append ("-- key: '" + key + "', value: " + value + "\n");
        }

        sb.append ("- DatastoreFieldTypes:");        
        it = datastoreFieldTypes.keySet ().iterator ();
        while (it.hasNext ()) {
            String key = (String) it.next ();
            Integer value = (Integer) datastoreFieldTypes.get (key);
            sb.append ("-- key: '" + key + "', value: " + value + "\n");
        }        

        sb.append ("- DBFieldTypes:");        
        it = dbFieldTypes.keySet ().iterator ();
        while (it.hasNext ()) {
            String key = (String) it.next ();
            Integer value = (Integer) dbFieldTypes.get (key);
            sb.append ("-- key: '" + key + "', value: " + value + "\n");
        }
        return sb.toString ();
    }
}