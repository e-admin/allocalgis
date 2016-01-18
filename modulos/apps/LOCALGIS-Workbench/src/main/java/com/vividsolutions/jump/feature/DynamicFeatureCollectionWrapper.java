
/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */

package com.vividsolutions.jump.feature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.geopista.io.datasource.IGeopistaConnection;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;


/**
 * FeatureCollectionWrapper for DynamicLayerFeatures.
 */
public class DynamicFeatureCollectionWrapper extends FeatureCollectionWrapper {
    protected FeatureCollection fc;
    private DataSourceQuery dataSourceQuery;

    /**
     * Constructs a FeatureCollectionWrapper that delegates to the given FeatureCollection.
     */
    public DynamicFeatureCollectionWrapper(FeatureCollection fc, DataSourceQuery dataSourceQuery) {
        super(fc);
        this.dataSourceQuery = dataSourceQuery;
    }

    /**
     * Returns the non-wrapper FeatureCollection wrapped by this wrapper and
     * possibly by other wrappers in-between. Intended to get at the "real"
     * FeatureCollection underneath several layers of FeatureCollectionWrappers.
     * 
     * @see #getWrappee()
     */
    public FeatureCollection getUltimateWrappee() {
        return super.getUltimateWrappee();
    }

    /**
	 * Throws an AssertionFailedException if this FeatureCollectionWrapper
	 * wraps (directly or indirectly) another FeatureCollectionWrapper having
	 * the same class (or descendant class thereof). A consistency check that
	 * is useful for some FeatureCollectionWrapper implementations.
	 */
    public void checkNotWrappingSameClass() {
        super.checkNotWrappingSameClass();
    }

    public Collection remove(Envelope env) {
        return super.remove(env);
    }

    /**
	 * Returns whether this FeatureCollectionWrapper (or a
	 * FeatureCollectionWrapper that it wraps, directly or indirectly) is an
	 * instance of the given class (or one of its descendants).
	 */
    public boolean hasWrapper(Class c) {
        return super.hasWrapper(c);
    }

    /**
     * Returns the FeatureCollection that this wrapper delegates to (possibly
     * another FeatureCollectionWrapper).
     * @see #getUltimateWrappee()
     */
    public FeatureCollection getWrappee() {
        return super.getWrappee();
    }

  public FeatureSchema getFeatureSchema() {
        return super.getFeatureSchema();
    }

    public Envelope getEnvelope() {
        return super.getEnvelope();
    }

    public int size() {
        return super.size();
    }

    public boolean isEmpty() {
        return super.isEmpty();
    }

    public List getFeatures() {
        return this.fc.getFeatures();
    }

    public Iterator iterator() {
        return super.iterator();
    }

    public List query(Envelope envelope) {
    	try{
    		IGeopistaConnection connection = (IGeopistaConnection)dataSourceQuery.getDataSource().getConnection();
	    	connection.setDinamica(true);
	    	this.fc = connection.executeQuery(dataSourceQuery.getQuery(), null);
	    	return this.fc.getFeatures();
    	}catch (Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }

    public void add(Feature feature) {
    	super.add(feature);
    }

    public Iterator queryIterator(Envelope envelope){
    	return super.queryIterator(envelope);
    }
    public void add(Feature feature, boolean quietly) {
    	super.add(feature,quietly);
    }
    

    public void remove(Feature feature) {
    	super.remove(feature);
    }

    public void remove(Feature feature, boolean quietly) {
    	super.remove(feature);
    }

    public void addAll(Collection features) {
    	super.addAll(features);
    }

    public void removeAll(Collection features) {
    	//System.out.println("ObservableFeaureCollection.removeAll");
    	super.removeAll(features);
    }

    public void clear() {
    	//System.out.println("FeatureColletionWrapper.clear");
        //Create a new ArrayList to avoid a ConcurrentModificationException. [Jon Aquino]
    	super.removeAll(new ArrayList(getFeatures()));
    }
}
