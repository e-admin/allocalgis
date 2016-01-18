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

import java.util.ArrayList;
import org.deegree.services.wfs.filterencoding.*;
import org.deegree.model.feature.Feature;

/**
 * Encapsulates the information of a <Filter> element that consists of a
 * number of FeatureId constraints (only) (as defined in the FeatureId DTD).
 * 
 * @author Markus Schneider
 * @version 06.08.2002
 */
public class FeatureFilter extends AbstractFilter {

    /** FeatureIds the FeatureFilter is based on */
    ArrayList featureIds = new ArrayList ();

    /** Adds a FeatureId constraint. */
    public void addFeatureId (FeatureId featureId) {
        featureIds.add (featureId);
    }
    
    /** Returns the contained FeatureIds. */
    public ArrayList getFeatureIds () {
        return featureIds;
    }    

    /**
     * Calculates the <tt>FeatureFilter</tt>'s logical value based on the ID
     * of the given <tt>Feature</tt>.
     * FIXME!!! Use a TreeSet (or something) to speed up comparison.
     * @param feature that determines the Id
     * @return true, if the <tt>FeatureFilter</tt> evaluates to true, else false
     * @throws FilterEvaluationException if the evaluation fails
     */
    public boolean evaluate (Feature feature) throws FilterEvaluationException {
        String id = feature.getId ();
        for (int i = 0; i < featureIds.size (); i++) {
            FeatureId featureId = (FeatureId) featureIds.get (i);
            if (id.equals (featureId.getValue ())) return true;
        }
        return false;
    }         
    
    /** Produces an indented XML representation of this object. */
    public StringBuffer toXML () {
        StringBuffer sb = new StringBuffer (500);
        sb.append ("<ogc:Filter xmlns:ogc='http://www.opengis.net/ogc'>");
        for (int i = 0; i < featureIds.size (); i++) {
            FeatureId fid = (FeatureId) featureIds.get (i);
            sb.append (fid.toXML ());
        }
        sb.append ("</ogc:Filter>");
        return sb;
    }
}
