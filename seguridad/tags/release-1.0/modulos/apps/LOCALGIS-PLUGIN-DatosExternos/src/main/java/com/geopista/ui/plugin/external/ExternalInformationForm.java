package com.geopista.ui.plugin.external;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;

import com.geopista.app.AppContext;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.Attribute;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.ui.autoforms.FeatureFormContext;
import com.geopista.ui.cursortool.GeopistaFeatureInfoTool;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.Blackboard;

public class ExternalInformationForm implements FeatureExtendedForm{

	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
	public boolean checkPanels() {
		return true;
	}

	public void disableAll() {
		
	}

	public AbstractValidator getValidator() {
		// TODO Auto-generated method stub
		return null;
	}

	public void initialize(FeatureDialogHome home) {
		
		GeopistaSchema schema = (GeopistaSchema)home.getFeature().getSchema();
		List list = schema.getAttributes();
		Hashtable attributes = new Hashtable();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Attribute item = (Attribute) iterator.next();
			String attributeName = item.getName();
			String attributeColumnName = item.getColumn().getName();
			System.out.println(attributeColumnName);
			Object object = home.getFeature().getAttribute(attributeName);
			if (object!=null) {
				attributes.put(attributeColumnName, object);
			}
		}
		
		Blackboard blackboard = aplicacion.getBlackboard();
		Hashtable queries = (Hashtable) blackboard.get("ExternalQueries");		
		ExternalInformationPanel externalInformationPanel= new ExternalInformationPanel();
	    externalInformationPanel.setModels(queries);
	    externalInformationPanel.setAttributes(attributes);
	    home.addPanel(externalInformationPanel);     
		
	}

	public void setApplicationContext(ApplicationContext context) {
		
		
	}

}
