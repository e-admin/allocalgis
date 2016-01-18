package com.geopista.protocol;

import com.geopista.protocol.control.ISesion;
import com.geopista.server.administradorCartografia.ACFeature;
import com.geopista.server.administradorCartografia.ACFeatureUpload;
import com.geopista.server.administradorCartografia.ACLayer;

public abstract class AbstractValidator implements Validator{

	public AbstractValidator(){
		
	}
	
	public void afterInsert(ACFeatureUpload uploadFeature, ACLayer acLayer){
		
	}
	
	public void beforeInsert(ACFeatureUpload uploadFeature, ACLayer acLayer){
		System.out.println("Antes de la inserción");
	}
	
	public void beforeInsertTemporal(ACFeatureUpload uploadFeature, ACLayer acLayer,int estadoValidacion){
		System.out.println("Antes de la inserción temoral");
	}
	
	public void afterDelete(ACFeatureUpload uploadFeature, ACLayer acLayer){
		
	}
	
	public void beforeDelete(ACFeatureUpload uploadFeature, ACLayer acLayer){
		
	}
	
	public void beforeDeleteTemporal(ACFeatureUpload uploadFeature, ACLayer acLayer){
		
	}
	
	public void afterUpdate(ACFeatureUpload uploadFeature, ACLayer acLayer){
		
	}
	
	public void beforeUpdate(ACFeatureUpload uploadFeature, ACLayer acLayer){
		
	}
	
	public void beforeUpdateTemporal(ACFeatureUpload uploadFeature, ACLayer acLayer,int estadoValidacion){
		
	}
	public void beforeUpdatePublicable(ACFeatureUpload uploadFeature, ACLayer acLayer,int estadoValidacion){
		
	}
	
	public void beforeUpdateAutoPublicable(ACFeatureUpload uploadFeature, ACLayer acLayer,int estadoValidacion){
		
	}
			
	public void afterRead(ACFeature feature, ACLayer acLayer){
		
	}
	
	public void beforeRead(ACLayer acLayer){
		
	}
	
	public void setVersion (Version version){
		
	}
}
