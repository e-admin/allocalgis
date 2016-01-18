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
package org.deegree_impl.services.wfs.filterencoding;

import org.w3c.dom.*;
import org.deegree.xml.*;
import org.deegree.model.feature.Feature;
import org.deegree.services.wfs.filterencoding.*;

/**
 * Encapsulates the information of a <PropertyIsNull>-element (as defined in Filter DTD).
 * The DTD defines the properties type to be tested as PropertyName or Literal.
 * @author Markus Schneider
 * @version 07.08.2002
 */
public class PropertyIsNullOperation extends ComparisonOperation {

    // PropertyName / Literal
    private Expression expression;
    
    public PropertyIsNullOperation (Expression expression) {
        super( OperationDefines.PROPERTYISNULL );
        this.expression = expression;
    }

    public Expression getExpression () {
        return expression;
    }
 
    /**
     * Given a DOM-fragment, a corresponding Operation-object is built. This method
     * recursively calls other buildFromDOM () - methods to validate the structure
     * of the DOM-fragment.
     * @throws FilterConstructionException if the structure of the DOM-fragment is invalid
     */       
    public static Operation buildFromDOM (Element element)
        throws FilterConstructionException {

        // check if root element's name equals 'PropertyIsNull'
        if (!element.getLocalName().equals ("PropertyIsNull"))
            throw new FilterConstructionException (
                "Name of element does not equal 'PropertyIsNull'!");

        ElementList children = XMLTools.getChildElements (element);
        if (children.getLength () != 1)
            throw new FilterConstructionException (
                "'PropertyIsNull' requires exactly 1 element!");

        Element child = children.item (0);
        Expression expr = null;
        
        switch (ExpressionDefines.getIdByName (child.getLocalName())) {
            case ExpressionDefines.PROPERTYNAME: {
                expr = PropertyName.buildFromDOM (child);
                break;
            }
            case ExpressionDefines.LITERAL: {
                expr = Literal.buildFromDOM (child);
                break;
            }
            default: {
                throw new FilterConstructionException (
                    "Name of element does not equal 'PropertyIsNull'!");
            }
        }
        
        return new PropertyIsNullOperation (expr);
    }
        
    /** Produces an indented XML representation of this object. */
    public StringBuffer toXML () {
        StringBuffer sb = new StringBuffer (500);
        sb.append ("<ogc:").append (getOperatorName ()).append (">");
        sb.append (expression.toXML ());
        sb.append ("</ogc:").append (getOperatorName ()).append (">");
        return sb;
    }

    /**
     * Calculates the <tt>PropertyIsNull</tt>-Operation's logical value based
     * on the certain property values of the given <tt>Feature</tt>.
     * @param feature that determines the property values
     * @return true, if the <tt>PropertyIsNull</tt>-Operation evaluates to
     *         true, else false
     * @throws FilterEvaluationException if the evaluation fails
     */
    public boolean evaluate (Feature feature) throws FilterEvaluationException {
        Object value = expression.evaluate (feature);
        if (value == null) return true;
        return false;
    }       
}
