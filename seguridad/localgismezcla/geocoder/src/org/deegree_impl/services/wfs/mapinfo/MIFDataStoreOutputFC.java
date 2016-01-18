/*----------------    FILE HEADER  ------------------------------------------


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
package org.deegree_impl.services.wfs.mapinfo;

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
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:35 $
 * <p>
 */
public class MIFDataStoreOutputFC implements DataStoreOutputFormat {
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