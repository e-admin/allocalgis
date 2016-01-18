/*
 * Created on 08-jun-2004
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
public class DeleteUserStyleAction {
	
	private String _styleName;
	private ArrayList _userStyleList;
	
	public DeleteUserStyleAction(String styleName, ArrayList userStyleList) {
		
		_styleName = styleName;
		_userStyleList = userStyleList;
	}
	
	public void execute() {
		
		Iterator userStyleListIterator = _userStyleList.iterator();
		int pos = 0;
		boolean styleFound = false;
		while ((userStyleListIterator.hasNext())&&(!styleFound)) {
			UserStyle userStyle = (UserStyle)userStyleListIterator.next();
			if (!(userStyle.getName()).equals(_styleName)) {
				pos++;
			}
			else {
				styleFound = true;
			}
		}
		_userStyleList.remove(pos);
	}

}
