/*
 * Created on 08-jun-2004
 *
 */
package com.geopista.style.sld.model.impl.actions;

import org.deegree.graphics.sld.FeatureTypeStyle;
import org.deegree.graphics.sld.UserStyle;
import org.deegree_impl.graphics.sld.StyleFactory;
/**
 * @author enxenio s.l.
 *
 */
public class CreateDefaultUserStyleAction {
	
	public CreateDefaultUserStyleAction() {}
	
	public Object execute() {
		
		FeatureTypeStyle[] ftsArray = null;
		UserStyle userStyle = 
			(UserStyle) StyleFactory.createStyle("default","default","",ftsArray);
		return userStyle;
	}

}
