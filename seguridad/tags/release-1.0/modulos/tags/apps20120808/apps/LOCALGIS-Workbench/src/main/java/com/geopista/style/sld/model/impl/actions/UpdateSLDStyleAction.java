/*
 * Created on 16-jun-2004
 *
 */
package com.geopista.style.sld.model.impl.actions;

import java.util.ArrayList;

import com.geopista.style.sld.model.SLDStyle;

/**
 * @author enxenio s.l.
 *
 */
public class UpdateSLDStyleAction {

	private SLDStyle _sldStyle;
	private ArrayList _userStyleList;
	
	public UpdateSLDStyleAction(SLDStyle sldStyle, ArrayList userStyleList) {
		
		_sldStyle = sldStyle;
		_userStyleList = userStyleList;
	}
	
	public Object execute() {
		
		_sldStyle.putStyles(SLDStyle.REPLACEALL,_userStyleList);
		return _sldStyle;
	}
		
}
