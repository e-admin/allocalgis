/**
 * IACDynamicLayer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.util.HashMap;
import java.util.List;

import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.workbench.model.ILayerManager;

public interface IACDynamicLayer {

	/*public abstract void convertWMS(ILayerManager layerManager,
			FeatureDataset features);*/

	//public abstract DynamicLayer convertDynamic(ILayerManager layerManager);

	public abstract HashMap getSelectedStyles();

	public abstract/*static */void setSelectedStyles(HashMap ss);

	public abstract String getTime();

	public abstract void setTime(String time);
	
	public abstract int getPositionOnMap();
	
	public abstract List getParams();
	


}