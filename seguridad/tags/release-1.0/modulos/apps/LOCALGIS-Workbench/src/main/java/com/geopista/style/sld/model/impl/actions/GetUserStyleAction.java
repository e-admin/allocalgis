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
