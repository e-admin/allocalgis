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
package org.deegree_impl.services.wfs.shape;

import java.util.HashMap;
import java.util.Iterator;

import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureCollection;
import org.deegree.services.wfs.DataStoreOutputFormat;
import org.deegree.services.wfs.WFSConstants;
import org.deegree.tools.Parameter;
import org.deegree.tools.ParameterList;

import org.deegree_impl.model.feature.FeatureFactory;
import org.deegree_impl.tools.Debug;


/**
 * Implements the DataStoreOutputFormat interface to format the result of a
 * data accessing class returned within the values of a HashMap as deegree
 * feature collection
 *
 * <p>-----------------------------------------------------------------------</p>
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 * <p>
 */
public class SHPDataStoreOutputFC implements DataStoreOutputFormat {
    /**
    * formats the data store at the values of the HashMap into
    * one single data structure.
    */
    public Object format( HashMap map, ParameterList paramList ) throws Exception {
        Debug.debugMethodBegin( this, "format" );

        Iterator iterator = map.values().iterator();
        int initcap = 0;

        while ( iterator.hasNext() ) {
            // get submitted parameters
            ParameterList pl = (ParameterList)iterator.next();
            Parameter p = pl.getParameter( WFSConstants.FEATURES );
            Feature[] feature = (Feature[])p.getValue();
            initcap += feature.length;
        }

        FeatureFactory ff = new FeatureFactory();
        FeatureCollection fc = ff.createFeatureCollection( "" + map.hashCode(), initcap );

        // get iterator to iterate each feature collection contained within
        // the HashMap
        iterator = map.values().iterator();

        while ( iterator.hasNext() ) {
            // get submitted parameters
            ParameterList pl = (ParameterList)iterator.next();
            Parameter p = pl.getParameter( WFSConstants.FEATURES );
            Feature[] feature = (Feature[])p.getValue();

            for ( int i = 0; i < feature.length; i++ ) {
                // adds the feature to the feature collection			    
                fc.appendFeature( feature[i] );
            }
        }

        Debug.debugMethodEnd();
        return fc;
    }
}