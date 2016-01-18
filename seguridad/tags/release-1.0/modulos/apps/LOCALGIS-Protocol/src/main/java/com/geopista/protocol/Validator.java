package com.geopista.protocol;

import com.geopista.server.administradorCartografia.ACFeature;
import com.geopista.server.administradorCartografia.ACFeatureUpload;
import com.geopista.server.administradorCartografia.ACLayer;

public interface Validator {

	public abstract void afterInsert(ACFeatureUpload uploadFeature, ACLayer acLayer);
	
	public abstract void beforeInsert(ACFeatureUpload uploadFeature, ACLayer acLayer);
	
	public abstract void afterDelete(ACFeatureUpload uploadFeature, ACLayer acLayer);
	
	public abstract void beforeDelete(ACFeatureUpload uploadFeature, ACLayer acLayer);
	
	public abstract void afterUpdate(ACFeatureUpload uploadFeature, ACLayer acLayer);
	
	public abstract void beforeUpdate(ACFeatureUpload uploadFeature, ACLayer acLayer);
	
	public abstract void afterRead(ACFeature uploadFeature, ACLayer acLayer);
	
	public abstract void beforeRead(ACLayer acLayer);
}
