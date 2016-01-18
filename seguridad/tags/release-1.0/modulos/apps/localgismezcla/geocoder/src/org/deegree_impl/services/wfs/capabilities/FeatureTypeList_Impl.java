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

package org.deegree_impl.services.wfs.capabilities;

import java.util.*;
import org.deegree.services.wfs.capabilities.*;



/**
 * This section defines the list of feature types (and operations on each feature type)
 * that are available from a web feature server. Additional information, such as
 * SRS, about each feature type is also provided.<p>
 * The main purpose of the <FeatureTypeList> section is to define the list of
 * feature types that a WFS can service and define the operations that are
 * supported on each feature type.
 *
 * <p>-----------------------------------------------------</p>
 *
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */

public class FeatureTypeList_Impl implements FeatureTypeList {
    
    private ArrayList operation = null;
    private HashMap featureType = null;
    
    /**
     * constructor initializing the class with the <FeatureTypeList>
     */
    FeatureTypeList_Impl(Operation[] operation,
                         FeatureType[] featureType) {
        this.operation = new ArrayList();
        this.featureType = new HashMap();
        setOperation(operation);
        setFeatureType(featureType);
    }
    
    /**
     * <ul>
     * <li>INSERT:  The <InsertFeature> element is used to indicate
     *				that the WFS is capable of creating new instances of
     *				a feature type.
     * <li>UPDATE:  The <UpdateFeature> element indicates that the WFS can change the
     *				existing state of a feature.
     * <li DELETE:  The <DeleteFeature> element indicates that the WFS can delete or
     * 				remove instances of a feature type from the datastore.
     * <li>QUERY:   The <QueryFeature> element indicates that the WFS is capable of
     * 				executing a query on a feature type.
     * <li>LOCK:    The <LockFeature> element indicates that the WFS is capable of
     *				locking instances of a feature type.
     * </ul><p>
     * Operations can be defined globally for all feature types or locally for
     * each specific feature type. Local <Operations> specifications take
     * precedence over global <Operations> specifications. If no operations are
     * defined anywhere, then the default operation list will include <Query> only.
     */
    public Operation[] getOperations() {
        return (Operation[])operation.toArray( new Operation[operation.size()] );
    }
    
    /**
     * adds the operation
     */
    public void addOperations(String operation) {
        this.operation.add( operation );
    }
    
    /**
     * sets the operation
     */
    public void setOperation(Operation[] operation) {
        this.operation.clear();
        for (int i = 0; i < operation.length; i++) {
            this.operation.add( operation[i] );
        }
    }
    /**
     * returns the list of the FeatureType.
     */
    public FeatureType[] getFeatureTypes() {
        FeatureType[] ft = new FeatureType[featureType.size()];
        return (FeatureType[])featureType.values().toArray( ft );
    }
    
    /** returns the <tt>FeatureType</tt> that matches the submitted name. If no
     * <tt>FeatureType</tt> matches, <tt>null</tt> will be returned
     *
     */
    public FeatureType getFeatureType(String name) {
        return (FeatureType)featureType.get( name );
    }
    
    /**
     * adds the featureType
     */
    public void addFeatureType(FeatureType featureType) {
        this.featureType.put( featureType.getName(), featureType );
    }
    
    /**
     * sets the featureType
     */
    public void setFeatureType(FeatureType[] featureType) {
        this.featureType.clear();
        for (int i = 0; i < featureType.length; i++) {
            addFeatureType( featureType[i] );
        }
    }
    
    
    public String toString() {
        String ret = null;
        ret = "operation = " + operation + "\n";
        ret += "featureType = " + featureType + "\n";
        return ret;
    }
    
}
/*
 * Changes to this class. What the people haven been up to:
 *
 * $Log: FeatureTypeList_Impl.java,v $
 * Revision 1.1  2009/07/09 07:25:33  miriamperez
 * Rama única LocalGISDOS
 *
 * Revision 1.1  2009/04/13 10:16:51  miriamperez
 * Meto en LocalGISPrivado el módulo del geocoder al completo
 *
 * Revision 1.1  2006/08/31 11:15:30  angeles
 * *** empty log message ***
 *
 * Revision 1.1.1.1  2006/08/17 11:34:01  angeles
 * no message
 *
 * Revision 1.3  2003/06/10 07:52:15  poth
 * no message
 *
 * Revision 1.2  2002/11/06 17:08:56  poth
 * no message
 *
 * Revision 1.1.1.1  2002/09/25 16:01:22  poth
 * no message
 *
 * Revision 1.8  2002/08/19 15:58:51  ap
 * no message
 *
 * Revision 1.7  2002/08/15 10:01:40  ap
 * no message
 *
 * Revision 1.6  2002/08/09 15:36:30  ap
 * no message
 *
 * Revision 1.5  2002/05/06 16:02:07  ap
 * no message
 *
 * Revision 1.4  2002/04/26 09:05:10  ap
 * no message
 *
 * Revision 1.2  2002/04/25 16:18:47  ap
 * no message
 *
 * Revision 1.1  2002/04/04 16:17:15  ap
 * no message
 *
 *
 */
