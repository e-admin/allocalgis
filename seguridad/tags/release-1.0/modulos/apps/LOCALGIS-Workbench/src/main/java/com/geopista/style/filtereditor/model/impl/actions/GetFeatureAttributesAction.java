/*
 * Created on 29-jul-2004
 *
 */
package com.geopista.style.filtereditor.model.impl.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
			attributes.add(_layer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeName(i));
		}		
		featureAttributes.put(_layer.getName(),attributes);
		return featureAttributes;
	}

}
