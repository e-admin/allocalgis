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

import org.deegree.services.wfs.configuration.Reference;


/**
 * the interface describes a reference from one table to another
 *
 * <p>---------------------------------------------------------------------</p>
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:32 $
 */
public class Reference_Impl implements Reference {
    private String tableField = null;
    private String targetField = null;
    private String targetTable = null;
    private boolean replaceable = false;

    /**
     * Creates a new Reference_Impl object.
     *
     * @param tableField 
     * @param replaceable 
     * @param targetTable 
     * @param targetField 
     */
    Reference_Impl( String tableField, boolean replaceable, String targetTable, String targetField ) {
        setTableField( tableField );
        setReplaceable( replaceable );
        setTargetTable( targetTable );
        setTargetField( targetField );
    }

    /**
     * returns the name of the field of the source table that is used
     * as reference to another table
     */
    public String getTableField() {
        return tableField;
    }

    /**
     * @see getTableField
     */
    public void setTableField( String tableField ) {
        this.tableField = tableField.toUpperCase();
    }

    /**
     * returns true if the reference field of the source table shall
     * be replaced by the referenced data
     */
    public boolean isReplaceable() {
        return replaceable;
    }

    /**
     * @see isReplaceable
     */
    public void setReplaceable( boolean replaceable ) {
        this.replaceable = replaceable;
    }

    /**
     * returns the name of the table targeted by the reference. in the case
     * of a shapefile datastore this is the name of another shapefile without
     * extension
     */
    public String getTargetTable() {
        return targetTable;
    }

    /**
     * @see getTargetTable
     */
    public void setTargetTable( String targetTable ) {
        this.targetTable = targetTable.toUpperCase();
    }

    /**
     * returns the name of the field of the target table that matches the
     * source tables reference field (@see getTableField)
     */
    public String getTargetField() {
        return targetField;
    }

    /**
     * @getTargetField
     */
    public void setTargetField( String targetField ) {
        this.targetField = targetField.toUpperCase();
    }
}