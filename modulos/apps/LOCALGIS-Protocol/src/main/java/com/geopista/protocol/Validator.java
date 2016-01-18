/**
 * Validator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol;

import com.geopista.server.administradorCartografia.ACFeature;
import com.geopista.server.administradorCartografia.ACFeatureUpload;
import com.geopista.server.administradorCartografia.IACLayer;

public interface Validator {

	public abstract void afterInsert(ACFeatureUpload uploadFeature, IACLayer acLayer);
	
	public abstract void beforeInsert(ACFeatureUpload uploadFeature, IACLayer acLayer);
	
	public abstract void afterDelete(ACFeatureUpload uploadFeature, IACLayer acLayer);
	
	public abstract void beforeDelete(ACFeatureUpload uploadFeature, IACLayer acLayer, int estadoValidacion);
	
	public abstract void afterUpdate(ACFeatureUpload uploadFeature, IACLayer acLayer);
	
	public abstract void beforeUpdate(ACFeatureUpload uploadFeature, IACLayer acLayer);
	
	public abstract void afterRead(ACFeature uploadFeature, IACLayer acLayer);
	
	public abstract void beforeRead(IACLayer acLayer);
}
