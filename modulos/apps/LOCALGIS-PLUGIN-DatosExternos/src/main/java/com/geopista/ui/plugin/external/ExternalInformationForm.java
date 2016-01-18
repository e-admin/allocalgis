/**
 * ExternalInformationForm.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.external;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.geopista.app.AppContext;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.Attribute;
import com.geopista.feature.GeopistaSchema;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
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
