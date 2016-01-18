/**
 * IACLayerFamily.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.util.Hashtable;

import com.vividsolutions.jump.workbench.model.ILayerManager;

public interface IACLayerFamily {
	

	public abstract int getId();

	public abstract void setId(int id);

	public abstract String getName();

	public abstract void setName(String name);

	public abstract Hashtable getLayers();

	public abstract void setLayers(Hashtable layers);

	public abstract String getDescription();

	public abstract void setDescription(String description);

	public abstract void convert(ILayerManager layerManager, int iPosition,
			Hashtable htLayers, Hashtable htStyleXMLs);

}