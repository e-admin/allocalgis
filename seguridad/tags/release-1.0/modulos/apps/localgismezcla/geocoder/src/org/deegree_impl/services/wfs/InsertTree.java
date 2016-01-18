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

import org.deegree.gml.GMLComplexProperty;
import org.deegree.gml.GMLFeature;
import org.deegree.gml.GMLProperty;
import org.deegree.services.wfs.configuration.FeatureType;
import org.deegree.services.wfs.configuration.Reference;
import org.deegree.services.wfs.configuration.RelatedTable;
import org.deegree.services.wfs.configuration.TableDescription;
import org.deegree_impl.gml.GMLFeatureCollection_Impl;
import org.deegree_impl.gml.GMLProperty_Impl;
import org.deegree_impl.tools.Debug;
import org.deegree_impl.tools.StringExtend;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This recursive structure is used to hold all necessary data for an
 * INSERT-Transaction of one instance of an GMLFeature.
 *
 * @author  Markus Schneider
 * @author  Andreas Poth
 */
public class InsertTree {
    
    /** associated FeatureType */
    private FeatureType ft;
    
    /** associated DB-Table */
    private TableDescription table;
    
    /** true, if associated table is a jointable */
    private boolean isJoinTable = false;
    
    /** connected InsertTrees (Jointables and Non-Jointables) */
    private ArrayList sons = new ArrayList(100);
    
    /** lookup table for non-jointable-sons (values are InsertTrees) */
    private HashMap sonNames = new HashMap(100);
    
    /** lookup table for jointable-sons (values are RelatedTables) */
    private HashMap jSonNames = new HashMap(100);
    
    /** fieldNames/value pairs for this table (for flat fields) */
    private HashMap fields = new HashMap(100);
    
    /*
     * Builds an InsertTree-object from a GMLFeature and a FeatureType.
     * This structure is recursive (it has sons that are InsertTrees, too) and
     * every InsertTree corresponds to a TableDescription object (a table of
     * the database).
     * @param gmlFeature the concrete instance to use
     * @param ft the configuration-object that describes this FeatureType's
     *           structure and mappings
     * @return the constructed tree
     * @see GMLFeature
     * @see FeatureType
     */
    public static InsertTree buildFromGMLFeature(GMLFeature gmlFeature,
                                                 FeatureType ft) throws InsertException {
        InsertTree tree = new InsertTree(ft.getMasterTable(), ft);
        tree.buildFromGMLFeature(gmlFeature);
        return tree;
    }
    
    /**
     * Constructs a new InsertTree-object (only for class-internal use).
     * Iterates on the tables referenced by the given (constructed) table:
     * <ul>
     * <li>ref. table is a standard-table -> ref. table is added to the
     *     lookup-table & ref. table is added as a child</li>
     * <li>ref. table is a jointable -> ref. table is added to the
     *     lookup-table</li>
     * </ul>
     * @param table the table associated to the new InsertTree
     * @param ft the FeatureType this InsertTree corresponds to
     */
    private InsertTree(TableDescription table, FeatureType ft) throws InsertException {
        this.table = table;
        this.ft = ft;
        
        if (table.isIdFieldAutoIncremented()) {
            throw new InsertException("The use of autoincremented IDs is not "
            + "implemented yet (used in "
            + "configuration of table '"
            + table.getName() + "')");
        }
        if (table instanceof RelatedTable)
            isJoinTable = ((RelatedTable) table).isJoinTable();
        Reference [] refs = table.getReferences();
        
        for (int i = 0; i < refs.length; i++) {
            String ttName = refs [i].getTargetTable().toUpperCase();
            
            RelatedTable targetTable = (RelatedTable) ft.getTableByName(ttName);
            
            if (!targetTable.isJoinTable()) {
                addChild(targetTable, false);
            } else {
                jSonNames.put(ttName, targetTable);
            }
        }
    }
    
    /**
     * Adds a table as a child. (resulting in a new InsertTree connected to the
     * object)
     * If the table to add is a jointable, several children with the same name
     * are possible, non-jointable sons have to have unique names.
     * @param targetTable the table to add
     * @param childIsJT true, if child table is a jointable
     * @return the child's InsertTree-object
     */
    private InsertTree addChild(RelatedTable targetTable, boolean childIsJT)
    throws InsertException {
        
        String name = targetTable.getName().toUpperCase();
        InsertTree tree = new InsertTree(targetTable, ft);
        if (childIsJT) {
            // is jointable -> add it as a child (lookup-table already
            // has a corresponding entry from constructor)
            sons.add(tree);
            return tree;
        } else if (sonNames.get(name) == null) {
            // not a jointable & name is not known yet (this can only happen
            // when called by the constructor)
            sons.add(tree);
            sonNames.put(name, tree);
            return tree;
        }
        return (InsertTree) sonNames.get(name);
    }
    
    /**
     * Adds a field to the structure.
     * The field slips recursively through the tree until a InsertTree with a
     * matching name is found. The recursion does not descend to subtrees that
     * are connected via jointables, only "normally" connected subtrees are
     * searched.
     * @param value the data of the field
     * @param mappedTable the name of the db-table
     * @param mappedField the name of the db-field
     * @return true, if a matching table was found, else false
     */
    private boolean addField(String value, String mappedTable, String mappedField) {
        // recursion ends when current table has the same name as mappedTable
        if (table.getName().equalsIgnoreCase(mappedTable)) {
            //fields.put (mappedField, value);
            ArrayList li = (ArrayList)fields.get( mappedField );
            if ( li == null ) {
                li = new ArrayList();
            }
            li.add( value );
            fields.put(mappedField, li);
            return true;
        } else {
            Iterator it = sonNames.values().iterator();
            while (it.hasNext()) {
                InsertTree tree = (InsertTree) it.next();
                if (tree.addField(value, mappedTable, mappedField)) return true;
            }
        }
        return false;
    }
     
    /**
     * Builds a new InsertTree object recursively.
     * Recurses on both the GMLFeature-parameter and the InsertTree-instance.
     * Flat properties are simply added by calling addField ().
     * Complex properties require that a new child-InsertTree to be added,
     * recursion descends into the new child and the property.
     * @param gmlFeature the GMLFeature the InsertTree shall represent
     * @return the constructed and filled InsertTree
     */
    private InsertTree buildFromGMLFeature(GMLFeature gmlFeature) throws InsertException {
        
        Debug.debugMethodBegin(this, "buildFromGMLFeature");
        
        // these are all properties of the feature (flat and complex)
        GMLProperty [] gmlProperties = gmlFeature.getProperties();
        
        for (int i = 0; i < gmlProperties.length; i++) {
            GMLProperty gmlProperty = gmlProperties [i];
            String name             = gmlProperty.getName().replace('.', '/');
            Object value            = gmlProperty.getPropertyValue();

            // get the datastore-mapping of the property
            String mapping = null;
            String [] s_ = ft.getDatastoreField( name );

            if (s_ == null) {
                throw new InsertException(name + " is not known by the datastore");
            } else {
                mapping = s_[0];
            }

            String [] parts = splitString(mapping);
            String prefix = parts [0].toUpperCase();
            String suffix = parts [1].toUpperCase();
            
            // check if the property is flat or complex
            if (!(gmlProperty instanceof GMLComplexProperty)) {
                String fieldValue = "";
                if (value != null) {
                    fieldValue = value.toString();
                }

                addField(fieldValue, prefix, suffix);
                
            } else {

                // the child-element of the complex property MUST be a
                // featureCollection
                NodeList list = ((GMLProperty_Impl) gmlProperty).getAsElement().getChildNodes();
                Element child = null;
                
                for (int k = 0; k < list.getLength(); k++) {
                    
                    if (list.item(k).getNodeType() == Node.ELEMENT_NODE) {
                        child = (Element) list.item(k);
                        break;
                    }
                }
                
                // the properties' mapping must be of one of these two forms:
                // (a) currentTableName.targetTableName
                // (b) currentTableName.ForeignKeyToOtherTable
                //
                // (b) is only possibly if it has been defined explicitly
                // in the TableDeclaration (Reference-element with the name of
                // the mapping and attribute replaceable=true)
                //
                boolean found = false;
                
                if (child != null) {
                    
                    GMLFeatureCollection_Impl coll = new GMLFeatureCollection_Impl(child);
                    GMLFeature [] features = coll.getFeatures();
                    
                    if (features != null) {
                        
                        // check for connection of type a
                        InsertTree tree = (InsertTree) sonNames.get(suffix);
                        
                        if (tree != null) {
                            // found a connected table that matches
                            // the suffix of the mapping
                            found = true;
                            if (features.length == 1) {
                                tree.buildFromGMLFeature(features [0]);
                            } else {
                                for (int ii = 0; ii < features.length; ii++) {
                                    tree.buildFromGMLFeature(features [ii]);
                                }
                            }
                        } else {
                            RelatedTable jTable = (RelatedTable) jSonNames.get(suffix);
                            if (jTable != null) {
                                // found a connected jointable that matches
                                // the suffix of the mapping
                                found = true;
                                for (int k = 0; k < features.length; k++) {
                                    tree = addChild(jTable, true);
                                    tree.buildFromGMLFeature(features [k]);
                                }
                            }
                        }
                        
                        if (!found) {
                            
                           
                            // check for connection of type b
                            Reference [] refs = table.getReferences();
                            if (refs != null) {
                                for (int k = 0; k < refs.length; k++) {
                                    
                                    if (refs [k].getTableField().equalsIgnoreCase(suffix)) {
                                        String ttName = refs [k].getTargetTable();
                                        tree = (InsertTree) sonNames.get(ttName);
                                        if (tree != null) {
                                            // found a connected table with the name
                                            // matching the field-part of the mapping
//                                            if (!refs [k].isReplaceable()) {
//                                                throw new InsertException
//                                                ("Found a (replaced) reference "
//                                                + "to table '" + ttName + "' "
//                                                + "from table '"
//                                                + table.getName() + "', but "
//                                                + "attribute 'replaceable' is "
//                                                + "set to false.");
//                                            }
                                            for (int j = 0; j < features.length; j++) {
                                                found = true;
                                                tree.buildFromGMLFeature(features [j]);
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (!found)
                    throw new InsertException("No interpretation found for '"
                    + name + "' in context of table '"
                    + table.getName() + "'!");
            }
        }
        Debug.debugMethodEnd();
        return this;
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
    
    /** Returns a String representation of this object. */
    public String toString() { return toStringRecurse("").toString(); }
    
    /**
     * Builds a StringBuffer recursively that describes this object.
     * @param indent String to put in front of every line
     * @return StringBuffer describing the object (and it's sons)
     */
    private StringBuffer toStringRecurse(String indent) {
        StringBuffer sb = new StringBuffer(indent);
        sb.append("Table: ").append(table.getName().toUpperCase())
        .append(", ");
        Iterator it = fields.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            Object value = fields.get(key);
            sb.append("['").append(key).append("', '").append(value)
            .append("']");
            if (it.hasNext()) sb.append(", ");
        }
        sb.append("\n");
        for (int i = 0; i < sons.size(); i++) {
            InsertTree son = (InsertTree) sons.get(i);
            sb.append(son.toStringRecurse(indent + "  "));
        }
        return sb;
    }
    
    /**
     * returns the feature type that is mapped to the tree
     */
    public FeatureType getFeatureType() {
        return ft;
    }
    
    /**
     * returns the associated DB-Table
     */
    public TableDescription getTableDescription() {
        return table;
    }
    
    /**
     * returns true if the current table is a join table
     */
    public boolean isJoinTable() {
        return isJoinTable;
    }
    
    /**
     * returns a list of <tt>InsertTree</tt>s that are connected to this one
     */
    public ArrayList getSons() {
        return sons;
    }
    
    /**
     * returns fieldNames/value pair for this table (for flat fields)
     */
    public Object getFieldValue(String key) {
        return fields.get( key );
    }
    
    /**
     * set fieldNames/value pair for this table (for flat fields)
     */
    public Object putFieldValue(String key, Object value) {
        return fields.put( key, value );
    }
    
    public boolean isFieldsEmpty() {
        return fields.isEmpty();
    }
    
    public Iterator getFieldsKeys() {
        return fields.keySet().iterator();
    }
    
}
