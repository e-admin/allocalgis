/**
 * SortedFeature.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
