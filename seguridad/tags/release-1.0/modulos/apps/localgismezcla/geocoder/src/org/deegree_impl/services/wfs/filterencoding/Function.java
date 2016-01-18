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
import java.util.ArrayList;
import org.deegree.xml.*;
import org.deegree.model.feature.Feature;
import org.deegree.services.wfs.filterencoding.*;

/**
 * Encapsulates the information of a <Function> element as defined in the
 * Expression DTD.
 *
 * @author Markus Schneider
 * @version 07.08.2002
 */
public class Function extends Expression_Impl {

    /** The Function's name (as specified in it's name attribute). */
    String name;

    /** The Function's arguments. */
    ArrayList args = new ArrayList ();

    /** Constructs a new Function. */
    public Function (String name, ArrayList args) {
        id = ExpressionDefines.FUNCTION;        
        this.name = name;
        this.args = args;
    }

    /**
     * Given a DOM-fragment, a corresponding Expression-object is built. This method
     * recursively calls other buildFromDOM () - methods to validate the structure
     * of the DOM-fragment.
     * @throws FilterConstructionException if the structure of the DOM-fragment is invalid
     */  
    public static Expression buildFromDOM (Element element) throws FilterConstructionException {

        // check if root element's name equals 'Function'
        if (!element.getLocalName().toLowerCase ().equals ("function"))
            throw new FilterConstructionException ("Name of element does not equal 'Function'!");

        // determine the name of the Function
        String name = element.getAttribute ("name");
        if (name == null) throw new FilterConstructionException ("Function's name (-attribute) is unspecified!");

        // determine the arguments of the Function
        ElementList children = XMLTools.getChildElements (element);
        if (children.getLength () < 1)
            throw new FilterConstructionException ("'" + name + "' requires at least 1 element!");

        ArrayList args = new ArrayList (children.getLength ());
        for (int i = 0; i < children.getLength (); i++) {            
            args.add (Expression_Impl.buildFromDOM (children.item (i)));
        }

        return new Function (name, args);
    }

   /**
    * Returns the Function's name.
    */
    public String getName () {
            return name;
    }

    /** 
     * @see #getName
     */
    public void setName (String name) {
            this.name = name;
    }
    
    /**
     * returns the arguments of the function
     */
    public ArrayList getArguments() {
        return args;
    }

    /** Produces an indented XML representation of this object. */
    public StringBuffer toXML () {
        StringBuffer sb = new StringBuffer (1000);
        sb.append ("<ogc:Function name=\"").append (name).append ("\">");
        for (int i = 0; i < args.size (); i++) {
            Expression expr = (Expression) args.get (i);
            sb.append (expr.toXML ());
        }
        sb.append ("</ogc:Function>");
        return sb;
    }

    /**
     * Returns the <tt>Function</tt>'s value (to be used in the evaluation of
     * a complexer <tt>Expression</tt>).
     * @param feature that determines the concrete values of 
     *                <tt>PropertyNames</tt> found in the expression
     * @return the resulting value
     */
    public Object evaluate (Feature feature) throws FilterEvaluationException {
        throw new FilterEvaluationException ("Function evaluation is not implemented yet!");
    }       
}
