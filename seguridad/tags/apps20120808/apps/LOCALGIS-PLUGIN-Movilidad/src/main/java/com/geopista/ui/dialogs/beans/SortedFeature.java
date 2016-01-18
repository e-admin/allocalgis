package com.geopista.ui.dialogs.beans;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.feature.Feature;

/**
 * Clase para ordenar las features de una cuadrícula de izq a dcha y de abajo arriba
 * @author irodriguez
 *
 */
public class SortedFeature implements Comparable<SortedFeature>{
	
	private Feature feature;

	public SortedFeature(Feature feature){
		this.feature = feature;
	}

	@Override
	public int compareTo(SortedFeature src) {
		Envelope srcEnv = src.getFeature().getGeometry().getEnvelopeInternal();
		Envelope featEnv = feature.getGeometry().getEnvelopeInternal();
		
		double srcMinX = srcEnv.getMinX();
		double srcMinY = srcEnv.getMinY();
		
		double featMinX = featEnv.getMinX();
		double featMinY = featEnv.getMinY();
		
		if(featMinY < srcMinY) {return -1;}
		if(featMinY == srcMinY && featMinX < srcMinX) {return -1;}
		if(featMinY == srcMinY && featMinX == srcMinX) {return 0;}
		
		return 1;
	}
	
	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}
	
}
