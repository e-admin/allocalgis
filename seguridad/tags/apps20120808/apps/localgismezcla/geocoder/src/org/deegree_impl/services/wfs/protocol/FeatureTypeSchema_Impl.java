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
package org.deegree_impl.services.wfs.protocol;

import org.deegree.services.wfs.protocol.FeatureTypeSchema;

import org.deegree.xml.*;

import org.w3c.dom.*;


/**
*
*
* <p>-----------------------------------------------------</p>
*
* @author Andreas Poth
* @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
* <p>
*/
public class FeatureTypeSchema_Impl implements FeatureTypeSchema {
    private org.w3c.dom.Document schema = null;

    /**
     * Creates a new FeatureTypeSchema_Impl object.
     */
    public FeatureTypeSchema_Impl() {
        schema = XMLTools.create();
    }

    /**
     * Creates a new FeatureTypeSchema_Impl object.
     *
     * @param schema 
     *
     * @throws Exception 
     */
    public FeatureTypeSchema_Impl(org.w3c.dom.Document schema) throws Exception {
        setSchema(schema);
    }

    /**
     * Creates a new FeatureTypeSchema_Impl object.
     *
     * @param schema 
     *
     * @throws Exception 
     */
    public FeatureTypeSchema_Impl(org.w3c.dom.Element schema) throws Exception {
        this();
        setSchema(schema);
    }

    /**
     * return the feature type schema as w3c dom node
     */
    public org.w3c.dom.Document getSchema() {
        return schema;
    }

    /**
     * @see #getSchema
     */
    public void setSchema(org.w3c.dom.Document schema) throws Exception {
        if (schema.getElementsByTagName("xsd:schema") == null) {
            throw new Exception("submitted document doesn't contain a " + "XML-schema");
        }

        this.schema = schema;
    }

    /**
     * @see #getSchema
     */
    public void setSchema(org.w3c.dom.Element schema) throws Exception {
        if (!schema.getNodeName().equals("xsd:schema")) {
            throw new Exception("submitted element isn't a XML-schema");
        }

        Element elem = this.schema.createElement(schema.getNodeName());
        this.schema.appendChild(elem);
        XMLTools.copyNode(schema, elem);
    }
}