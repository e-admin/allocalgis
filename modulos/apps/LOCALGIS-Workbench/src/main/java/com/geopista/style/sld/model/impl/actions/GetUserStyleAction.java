/**
 * GetUserStyleAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 07-jun-2004
 *
 */
package com.geopista.style.sld.model.impl.actions;

import java.util.ArrayList;
import java.util.Iterator;

import org.deegree.graphics.sld.UserStyle;
/**
 * @author enxenio s.l.
 *
 */
public class GetUserStyleAction {
	
	private String _styleName;
	private ArrayList _userStyleList;
	
	public GetUserStyleAction(String styleName,ArrayList userStyleList) {
		
		_styleName = styleName;
		_userStyleList = userStyleList;
	}
	
	public Object execute() {
		
		UserStyle style = null;
		int index = 0;
		Iterator stylesIterator = _userStyleList.iterator();
		boolean styleFound = false;
		while ((stylesIterator.hasNext())&& !(styleFound)) {
			UserStyle userStyle = (UserStyle) stylesIterator.next();
			String styleName = userStyle.getName();
			if (styleName.equals(_styleName)) {
				styleFound = true;
			}
			else {
				index++;
			}
		}
		style = (UserStyle)_userStyleList.get(index);
		return style;
	}

}
