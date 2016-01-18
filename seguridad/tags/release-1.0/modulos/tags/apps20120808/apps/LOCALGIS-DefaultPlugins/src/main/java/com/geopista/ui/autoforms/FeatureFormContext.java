/**
 * 
 */
package com.geopista.ui.autoforms;

import javax.swing.JComponent;

import com.geopista.feature.GeopistaFeature;
import com.vividsolutions.jump.feature.Feature;

/** 
 * @author juacas
 */
public class FeatureFormContext implements FormContext {

	private Feature feature;
	private FeatureFieldPanel panel;

	public FeatureFormContext(Feature feature, FeatureFieldPanel panel) {
		this.feature=feature;
		this.panel=panel;
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.autoforms.FormContext#getModel()
	 */
	public Object getModel() {
		
		return feature;
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.autoforms.FormContext#getForm()
	 */
	public AutoForm getForm() {
		
		return panel;
	}

	

	/**
	 * @param feature2
	 */
	public void setModel(Object feature2)
	{
	feature=(Feature) feature2;	
	}

	

	

}
