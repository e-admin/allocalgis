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
import org.deegree.services.wfs.filterencoding.FilterConstructionException;
import org.deegree.services.wfs.filterencoding.Expression;

/**
 * Abstract superclass representing expr-entities (as defined in the Expression DTD).
 * @author Markus Schneider
 * @version 06.08.2002
 */
abstract public class Expression_Impl implements Expression {


    /**
     * The underlying expression's id.
     * @see ExpressionDefines
     */
    int id;


    /**
     * Given a DOM-fragment, a corresponding Expression-object is built. This method
     * recursively calls other buildFromDOM () - methods to validate the structure
     * of the DOM-fragment.
     * @throws FilterConstructionException if the structure of the DOM-fragment is invalid
     */
    public static Expression buildFromDOM (Element element)
        throws FilterConstructionException {

        // check if root element's name is a known expression
        String name = element.getLocalName(); 
        int id = ExpressionDefines.getIdByName (name);
        Expression expression = null;

        switch (id) {
            case ExpressionDefines.EXPRESSION: {
                break;
            }
            case ExpressionDefines.PROPERTYNAME: {
                expression = PropertyName.buildFromDOM (element);
                break;
            }
            case ExpressionDefines.LITERAL: {
                expression = Literal.buildFromDOM (element);
                break;
            }
            case ExpressionDefines.FUNCTION: {
                expression = Function.buildFromDOM (element);
                break;
            }
            case ExpressionDefines.ADD:
            case ExpressionDefines.SUB:
            case ExpressionDefines.MUL:
            case ExpressionDefines.DIV: {
                expression = ArithmeticExpression.buildFromDOM (element);
                break;
            }
            default: {
                throw new FilterConstructionException ("Unknown expression '" + name + "'!");
            }
        }
        return expression;
    }

    /** Returns the name of the expression. */
    public String getExpressionName () {
        return ExpressionDefines.getNameById (id);
    }

    /**
     * Returns the expression's id.
     * @see ExpressionDefines
     */
    public int getExpressionId () {
        return id;
    }

    /** Produces an indented XML representation of this object. */
    public abstract StringBuffer toXML ();
}