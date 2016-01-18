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
 * Encapsulates the information of a logical_ops entity (as defined in the Filter DTD).
 * @author Markus Schneider
 * @version 10.08.2002
 */
public class LogicalOperation extends AbstractOperation {

    /** Arguments of the Operation. */
    ArrayList arguments = new ArrayList ();

    /**
     * Constructs a new LogicalOperation.
     * @see OperationDefines
     */
    public LogicalOperation (int operatorId, ArrayList arguments)
        throws FilterConstructionException {
        super( operatorId );
        this.arguments = arguments;
    }

    /**
     * Returns the arguments of the operation. These are <tt>Operations</tt>
     * as well.
     */
    public ArrayList getArguments () {        
        return arguments;
    }
    
    /**
     * Given a DOM-fragment, a corresponding Operation-object is built. This method
     * recursively calls other buildFromDOM () - methods to validate the structure
     * of the DOM-fragment.
     * @throws FilterConstructionException if the structure of the DOM-fragment is invalid
     */   
    public static Operation buildFromDOM (Element element)
        throws FilterConstructionException {

        // check if root element's name is a known operator
        String name = element.getLocalName();
        int operatorId = OperationDefines.getIdByName (name);
        ArrayList arguments = new ArrayList ();

        switch (operatorId) {
            case OperationDefines.AND:
            case OperationDefines.OR: {
                ElementList children = XMLTools.getChildElements (element);
                if (children.getLength () < 2)
                    throw new FilterConstructionException ("'" + name + "' requires at least 2 elements!");
                for (int i = 0; i < children.getLength (); i++) {
                    Element child = children.item (i);
                    Operation childOperation = AbstractOperation.buildFromDOM (child);
                    arguments.add (childOperation);
                }
                break;
            }
            case OperationDefines.NOT: {
                ElementList children = XMLTools.getChildElements (element);
                if (children.getLength () != 1)
                    throw new FilterConstructionException ("'" + name + "' requires exactly 1 element!");
                Element child = children.item (0);
                Operation childOperation = AbstractOperation.buildFromDOM (child);
                arguments.add (childOperation);
                break;
            }
            default: {
                throw new FilterConstructionException ("'" + name + "' is not a logical operator!");
            }
        }
        return new LogicalOperation (operatorId, arguments);
    }

    /** Produces an indented XML representation of this object. */
    public StringBuffer toXML () {
        StringBuffer sb = new StringBuffer (1000);
        sb.append ("<ogc:").append (getOperatorName ()).append(">");

        for (int i = 0; i < arguments.size (); i++) {
            Operation operation = (Operation) arguments.get (i);
            sb.append (operation.toXML ());
        }

        sb.append ("</ogc:").append (getOperatorName ()).append (">");
        return sb;
    }
    
    /**
     * Calculates the <tt>LogicalOperation</tt>'s logical value based on the
     * certain property values of the given <tt>Feature</tt>.
     * @param feature that determines the property values
     * @return true, if the <tt>LogicalOperation</tt> evaluates to true, else false
     * @throws FilterEvaluationException if the evaluation fails
     */
    public boolean evaluate (Feature feature) throws FilterEvaluationException {
        switch (getOperatorId ()) {
            case OperationDefines.AND: {
                for (int i = 0; i < arguments.size (); i++) {
                    Operation operation = (Operation) arguments.get (i);
                    if (!operation.evaluate (feature)) return false;
                }
                return true;
            }
            case OperationDefines.OR: {
                for (int i = 0; i < arguments.size (); i++) {
                    Operation operation = (Operation) arguments.get (i);
                    if (operation.evaluate (feature)) return true;
                }
                return false;
            }
            case OperationDefines.NOT: {
                Operation operation = (Operation) arguments.get (0);
                return !operation.evaluate (feature);
            }
            default: {
                throw new FilterEvaluationException (
                    "Unknown LogicalOperation encountered: '"
                  + getOperatorName () + "'");
            }
        }
    }       
}
