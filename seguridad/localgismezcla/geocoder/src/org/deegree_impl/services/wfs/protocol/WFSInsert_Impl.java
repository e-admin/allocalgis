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

import java.util.*;

import org.deegree.gml.GMLFeature;

import org.deegree.services.wfs.protocol.*;


/**
 * Defines the insert operation for the WFS. 
 *
 * <p>-----------------------------------------------------</p>
 *
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */
class WFSInsert_Impl implements WFSInsert {
    private ArrayList features = null;
    private ArrayList featuretypes = null;
    private String handle = null;

    /**
     * constructor initializing the class with the <WFSInsert>
     */
    WFSInsert_Impl(GMLFeature[] features, String handle) {
        this.features = new ArrayList();
        this.featuretypes = new ArrayList();
        setFeatures(features);
        setHandle(handle);
    }

    /**
     * returns a list of the feature that belongs to the submitted 
     * feature type
     */
    public GMLFeature[] getFeatures(String featureType) {
        ArrayList list = new ArrayList();

        for (int i = 0; i < features.size(); i++) {
            GMLFeature feat = (GMLFeature) features.get(i);

            if (feat.getFeatureTypeName().equals(featureType)) {
                list.add(feat);
            }
        }

        return (GMLFeature[]) list.toArray(new GMLFeature[list.size()]);
    }

    /**
     * returns a list of the feature that should be inserted
     * into the data store
     */
    public GMLFeature[] getFeatures() {
        return (GMLFeature[]) features.toArray(new GMLFeature[features.size()]);
    }

    /**
     * adds the <Features>
     */
    public void addFeatures(GMLFeature features) {
        this.features.add(features);
    }

    /**
     * sets the <Features>
     */
    public void setFeatures(GMLFeature[] features) {
        this.features.clear();
        this.featuretypes.clear();

        if (features != null) {
            for (int i = 0; i < features.length; i++) {
                this.features.add(features[i]);

                if (!featuretypes.contains(features[i].getFeatureTypeName())) {
                    this.featuretypes.add(features[i].getFeatureTypeName());
                }
            }
        }
    }

    /**
     * the handle acts as an identifier for an operation
     */
    public String getHandle() {
        return handle;
    }

    /**
     * the handle acts as an identifier for an operation
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
    * returns the names of the feature types affected by the insertion.
    * Notice that the WFS spec allowes features of different types to be
    * inserted within one operation. This may cause problems if the features
    * are stored within different datastores and the insertion for one
    * of the datastores fails. So it seems to be more appropiate to use just
    * features of one type within one insertion.
    */
    public String[] getFeatureTypes() {
        return (String[]) featuretypes.toArray(new String[featuretypes.size()]);
    }
    
    public String toString() {
        return "WFSInsert:\n" + featuretypes + "\n" + features + "\n";
    }
}