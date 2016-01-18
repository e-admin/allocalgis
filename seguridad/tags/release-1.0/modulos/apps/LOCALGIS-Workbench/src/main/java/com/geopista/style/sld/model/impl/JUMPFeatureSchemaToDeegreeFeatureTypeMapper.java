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
