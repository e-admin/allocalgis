/**
 * GetFeatureAttributesAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 29-jul-2004
 *
 */
package com.geopista.style.sld.model.impl.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.geopista.style.sld.model.impl.FeatureAttribute;
import com.vividsolutions.jump.workbench.model.Layer;

/**
 * @author enxenio s.l.
 *
 */
public class GetFeatureAttributesAction {
	private Layer _layer;
	
	public GetFeatureAttributesAction(Layer layer) {
		
		_layer = layer;
	}
	/* (non-Javadoc)
	 * @see es.enxenio.util.sql.PlainAction#execute(java.sql.Connection)
	 */
	public Object doExecute() {

		HashMap featureAttributes = new HashMap();
		List attributes = new ArrayList();
		for (int i = 0; i<_layer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeCount(); i++) {
			FeatureAttribute featureAttribute = 
				new FeatureAttribute(_layer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeName(i),
			_layer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeType(i).toString());
			attributes.add(featureAttribute);
		}		
		featureAttributes.put(_layer.getName(),attributes);
		return featureAttributes;
	}

}
