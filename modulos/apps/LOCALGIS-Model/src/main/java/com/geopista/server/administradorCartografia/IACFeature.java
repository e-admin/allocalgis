/**
 * IACFeature.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.io.Serializable;
import java.util.HashMap;

import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;

public interface IACFeature {

	public abstract String getGeometry();

	public abstract void setGeometry(String geometry);

	public abstract HashMap getAttributes();

	public abstract void setAttributes(HashMap attributes);

	public abstract void setAttribute(String sKey, Serializable oValue);

	public abstract Object getAttribute(String sKey);


	/** Obtiene un nuevo GeopistaFeature a partir de estos datos */
	public abstract GeopistaFeature convert(GeopistaSchema schema)
			throws NoIDException;

	public abstract String getError();

}