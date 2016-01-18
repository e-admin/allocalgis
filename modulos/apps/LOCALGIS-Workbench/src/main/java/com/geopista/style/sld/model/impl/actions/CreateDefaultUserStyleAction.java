/**
 * CreateDefaultUserStyleAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
