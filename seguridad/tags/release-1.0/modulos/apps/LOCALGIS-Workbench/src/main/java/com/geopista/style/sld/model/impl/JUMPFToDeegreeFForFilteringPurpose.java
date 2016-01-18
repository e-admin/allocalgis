package com.geopista.style.sld.model.impl;

import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureProperty;
import org.deegree.model.feature.FeatureType;
import org.deegree.model.geometry.GM_Envelope;
import org.deegree.model.geometry.GM_Exception;
import org.deegree.model.geometry.GM_Object;

public class JUMPFToDeegreeFForFilteringPurpose implements Feature {

	public JUMPFToDeegreeFForFilteringPurpose(com.vividsolutions.jump.feature.Feature f) {
		_f = f.clone(true);
		try{
			_f.setAttribute(_f.getSchema().getGeometryIndex(),org.deegree_impl.model.geometry.JTSAdapter.wrap(_f.getGeometry()));
		} catch (GM_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	public Object[] getProperties() {return _f.getAttributes();};
	public Object getProperty(String name) {return _f.getAttribute(name);};
	public Object getProperty(int index) {return _f.getAttribute(index);};
	public GM_Object[] getGeometryProperties() {return null;};
	public GM_Object getDefaultGeometryProperty () {return (GM_Object)_f.getGeometry();};
	public void setProperty(FeatureProperty property) {_f.setAttribute(property.getName(), property.getValue());};
	public GM_Envelope getEnvelope() {return ((GM_Object)_f.getGeometry()).getEnvelope();}
	
	private com.vividsolutions.jump.feature.Feature _f;
}
