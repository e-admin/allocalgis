/*----------------    FILE HEADER  ------------------------------------------

 Copyright (C) 2001-2005 by:
 lat/lon GmbH
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
 lat/lon GmbH
 Aennchenstraße 19
 53177 Bonn
 Germany


 ---------------------------------------------------------------------------*/

package de.latlon.deejump.wfs.jump;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.FeatureEventType;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerListener;

/**
 * This class keeps track of changes to its layer. It is intended to be used with a WFSLayer only.
 * Getters allow access to list and maps with the changed features. These are used by the
 * UpdataWFSLayerPlugIn (and TransactionFactory) to generated the WFS Transaction requests.
 * 
 * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
 * 
 */
public class WFSLayerListener implements LayerListener {

    private ArrayList<Feature> changedGeomFeatures = new ArrayList<Feature>();

    private ArrayList<Feature> changedAttrFeatures = new ArrayList<Feature>();

    private ArrayList<Feature> changedDelFeatures = new ArrayList<Feature>();

    private ArrayList<Feature> changedAddFeatures = new ArrayList<Feature>();

    /** map containing reference to original feature (to use as Filter) */
    private HashMap<Feature, Feature> oldGeomFeatures = new HashMap<Feature, Feature>();

    /** map containing reference to original feature (to use as Filter) */
    private HashMap<Feature, Feature> oldAttrFeatures = new HashMap<Feature, Feature>();

    /** the JUMP layer name */
    private String layerName;

    /**
     * Creates a WfsLayerListener using the layer name. This layer name is the JUMP layer name and
     * <b>not<b/> the original WFS layer name.
     * 
     * @param layerName
     *            the JUMP layer name
     */
    public WFSLayerListener( String layerName ) {
        this.layerName = layerName;
    }

    /**
     * Keeps tracks of changes to the associated layer (the layer is associated using its display
     * name). The four <code>FeatureEventType</code>s are treated separatedly.
     * <code>GEOMETRY_MODIFIED</code> <code>ATTRIBUTE_MODIFIED</code> are combined by the
     * <code>TransactionFactory</code> into one UPDATE statement.<br/> Changed features (and the
     * reference to their riginal features) can be retrieved with the methods
     * getChangedFeaturesMap(), getOldGeomFeaturesMap() and getAttrGeomFeaturesMap(). <br/>
     * 
     * @see com.vividsolutions.jump.workbench.model.LayerListener#featuresChanged(com.vividsolutions.jump.workbench.model.FeatureEvent)
     */
    public void featuresChanged( FeatureEvent e ) {

        if ( !( e.getLayer() instanceof WFSLayer ) ) {
            return;
        }
        if ( !( (WFSLayer) e.getLayer() ).getName().equals( layerName ) ) {
            return;
        }

        FeatureEventType fet = e.getType();

        Collection<?> collec = e.getFeatures();

        if ( fet == FeatureEventType.GEOMETRY_MODIFIED ) {

            Iterator<?> oldFeatIter = null;
            Collection<?> oldFeatures = null;

            for ( Iterator<?> iter = collec.iterator(); iter.hasNext(); ) {
                Feature modifiedFeature = (Feature) iter.next();

                if ( changedGeomFeatures.contains( modifiedFeature ) ) {
                    changedGeomFeatures.remove( modifiedFeature );
                }
                changedGeomFeatures.add( modifiedFeature );

                // old features
                if ( oldFeatures == null ) {
                    oldFeatures = e.getOldFeatureClones();
                    oldFeatIter = oldFeatures.iterator();
                }

                Feature f = (Feature) oldFeatIter.next();

                if ( !oldGeomFeatures.containsKey( modifiedFeature ) ) {
                    oldGeomFeatures.put( modifiedFeature, f );
                }

                // this check is for poly with holes...

                for ( Iterator<Feature> iterAd = changedAddFeatures.iterator(); iterAd.hasNext(); ) {
                    Feature fAd = iterAd.next();
                    if ( changedGeomFeatures.contains( fAd ) ) {
                        // must remove frm above *and* from hash map
                        changedGeomFeatures.remove( fAd );
                        oldGeomFeatures.remove( fAd );
                    }
                }
            }

        } else if ( fet == FeatureEventType.ATTRIBUTES_MODIFIED ) {

            Iterator<?> oldFeatIter = null;
            Collection<?> oldFeatures = null;

            for ( Iterator<?> iter = collec.iterator(); iter.hasNext(); ) {
                Feature modifiedFeature = (Feature) iter.next();
                if ( changedAttrFeatures.contains( modifiedFeature ) ) {
                    changedAttrFeatures.remove( modifiedFeature );
                }
                changedAttrFeatures.add( modifiedFeature );

                if ( oldFeatures == null ) {
                	//TODO SATEC
                    //oldFeatures = e.getOldFeatureAttClones();
                    oldFeatures = e.getOldFeatureClones();
                }

                if ( oldFeatIter == null ) {
                    oldFeatIter = oldFeatures.iterator();
                }

                Feature f = (Feature) oldFeatIter.next();

                if ( !oldAttrFeatures.containsKey( modifiedFeature ) ) {
                    oldAttrFeatures.put( modifiedFeature, f );
                }

                if ( changedAddFeatures.contains( modifiedFeature ) ) {
                    changedAttrFeatures.remove( modifiedFeature );
                    oldAttrFeatures.remove( modifiedFeature );
                }

            }

        } else if ( fet == FeatureEventType.DELETED ) {
            for ( Iterator<?> iter = collec.iterator(); iter.hasNext(); ) {
                // changedDelFeatures.put( iter.next(), e.getType() );
                Feature o = (Feature) iter.next();
                if ( changedDelFeatures.contains( o ) ) {
                    changedDelFeatures.remove( o );
                }
                if ( changedAddFeatures.contains( o ) ) {
                    changedAddFeatures.remove( o );
                } else {
                    changedDelFeatures.add( o );
                }
            }

        } else {

            for ( Iterator<?> iter = collec.iterator(); iter.hasNext(); ) {
                Feature o = (Feature) iter.next();
                if ( changedAddFeatures.contains( o ) ) {
                    changedAddFeatures.remove( o );
                }
                changedAddFeatures.add( o );
            }
        }
    }

    /**
     * @return a map containing lists of changed, deleted or added features. The keys to access
     *         those lists are the <code>FeatureEventType</code>s. For example, the key
     *         <code>FeatureEventType.ATTRIBUTES_MODIFIED</code> can be used to retireve the list
     *         of features with changed attributes.
     */
    public HashMap<FeatureEventType, ArrayList<Feature>> getChangedFeaturesMap() {
        HashMap<FeatureEventType, ArrayList<Feature>> map = new HashMap<FeatureEventType, ArrayList<Feature>>( 4 );
        map.put( FeatureEventType.GEOMETRY_MODIFIED, changedGeomFeatures );
        map.put( FeatureEventType.ATTRIBUTES_MODIFIED, changedAttrFeatures );
        map.put( FeatureEventType.DELETED, changedDelFeatures );
        map.put( FeatureEventType.ADDED, changedAddFeatures );

        return map;
    }

    /**
     * @return the original map of geometry attributes
     */
    public HashMap<Feature, Feature> getOldGeomFeaturesMap() {
        return oldGeomFeatures;
    }

    /**
     * @return the original map of normal attributes
     */
    public HashMap<Feature, Feature> getOldAttrFeaturesMap() {
        return oldAttrFeatures;
    }

    public void layerChanged( LayerEvent e ) {
        // unused
    }

    public void categoryChanged( CategoryEvent e ) {
        // unused
    }

    /**
     * Clears up lists and maps with changed, added or deleted features.
     * 
     */
    public void reset() {
        changedAddFeatures.clear();
        changedDelFeatures.clear();
        changedGeomFeatures.clear();
        changedAttrFeatures.clear();
        oldGeomFeatures.clear();
        oldAttrFeatures.clear();
    }

}
