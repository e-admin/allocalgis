/*
 * Created on 18-jun-2004
 */
package com.geopista.style.sld.model.impl;

import com.vividsolutions.jump.feature.AttributeType;

/**
 * @author Enxenio S.L.
 */
public class JUMPAttributeTypeToDeegreeFeatureTypePropertyMapper implements org.deegree.model.feature.FeatureTypeProperty {

	public JUMPAttributeTypeToDeegreeFeatureTypePropertyMapper(AttributeType a) {
			_a = a;
	}

	public String getName() {return null;}			 
	public String getType() {return _a.toString();}
	public boolean isNullable() {return true;}

	private AttributeType _a; 	
}
