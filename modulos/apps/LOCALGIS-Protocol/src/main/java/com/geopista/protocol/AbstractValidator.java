/**
 * AbstractValidator.java
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

public abstract class AbstractValidator implements Validator{

	public AbstractValidator(){
		
	}
	
	public void afterInsert(ACFeatureUpload uploadFeature, IACLayer acLayer){
		
	}
	
	public void beforeInsert(ACFeatureUpload uploadFeature, IACLayer acLayer){
		System.out.println("Antes de la inserciï¿½n");
	}
	
	public void beforeInsertTemporal(ACFeatureUpload uploadFeature, IACLayer acLayer,int estadoValidacion){
		System.out.println("Antes de la inserciï¿½n temoral");
	}
	
	public void afterDelete(ACFeatureUpload uploadFeature, IACLayer acLayer){
		
	}
	
	public void beforeDelete(ACFeatureUpload uploadFeature, IACLayer acLayer, int estadoValidacion){
		
	}
	
	public void afterUpdate(ACFeatureUpload uploadFeature, IACLayer acLayer){
		
	}
	
	public void beforeUpdate(ACFeatureUpload uploadFeature, IACLayer acLayer){
		
	}
	
	public void beforeUpdateTemporal(ACFeatureUpload uploadFeature, IACLayer acLayer,int estadoValidacion){
		
	}
	public void beforeUpdatePublicable(ACFeatureUpload uploadFeature, IACLayer acLayer,int estadoValidacion){
		
	}
	
	public void beforeUpdateAutoPublicable(ACFeatureUpload uploadFeature, IACLayer acLayer,int estadoValidacion){
		
	}
			
	public void afterRead(ACFeature feature, IACLayer acLayer){
		
	}
	
	public void beforeRead(IACLayer acLayer){
		
	}
	
	public void setVersion (Version version){
		
	}
}
