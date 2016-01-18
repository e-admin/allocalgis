/*
 * Created on 18-jun-2004
 */
package com.geopista.style.sld.model.impl;

import org.deegree.model.feature.FeatureProperty;
import org.deegree.model.feature.FeatureType;
import org.deegree.model.geometry.GM_Envelope;
import org.deegree.model.geometry.GM_Object;

import com.vividsolutions.jump.feature.Feature;

/**
 * @author Enxenio S.L.
 */
public class JUMPFeatureToDeegreeFeatureMapper implements org.deegree.model.feature.Feature {

	public JUMPFeatureToDeegreeFeatureMapper(Feature f) {
		_f = f;
	}
	 
	public String getId() {return Integer.toString(_f.getID());}
	public FeatureType getFeatureType() {
		if (_f != null) {
			return new JUMPFeatureSchemaToDeegreeFeatureTypeMapper(_f.getSchema());
		}
		else {
			return new JUMPFeatureSchemaToDeegreeFeatureTypeMapper(null);
		}		
	}
	public Object[] getProperties() {return null;};
	public Object getProperty(String name) {return _f.getAttribute(name);};
	public Object getProperty(int index) {return null;};
	public GM_Object[] getGeometryProperties() {return null;};
	public GM_Object getDefaultGeometryProperty () {return null;};
	public void setProperty(FeatureProperty property) {};
	public GM_Envelope getEnvelope() {return null;}
	
	private Feature _f;
}
