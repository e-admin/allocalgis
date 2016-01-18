/**
 * JUMPFToDeegreeFForFilteringPurpose.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
