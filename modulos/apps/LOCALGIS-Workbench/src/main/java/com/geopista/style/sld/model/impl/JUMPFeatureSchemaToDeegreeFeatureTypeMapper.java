/**
 * JUMPFeatureSchemaToDeegreeFeatureTypeMapper.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 18-jun-2004
 */
package com.geopista.style.sld.model.impl;

import org.deegree.model.feature.FeatureType;

import com.vividsolutions.jump.feature.FeatureSchema;


/**
 * @author Enxenio S.L.
 */
public class JUMPFeatureSchemaToDeegreeFeatureTypeMapper implements org.deegree.model.feature.FeatureType {

		public JUMPFeatureSchemaToDeegreeFeatureTypeMapper(FeatureSchema f) {
				_f = f;
		}
	
		public FeatureType[] getParents() {
			return null; 
		}
	
		public FeatureType[] getChildren() {
			return null;
		}
	
		public String getName() {
			return null;
		}

		public org.deegree.model.feature.FeatureTypeProperty[] getProperties() {
			return new org.deegree.model.feature.FeatureTypeProperty[]{};
		}
	
		public org.deegree.model.feature.FeatureTypeProperty getProperty(String name) {
			if (_f != null) {
				return new JUMPAttributeTypeToDeegreeFeatureTypePropertyMapper(_f.getAttributeType(name));
			}
			else {
				return null;
			}
		}

	private FeatureSchema _f;    
}
