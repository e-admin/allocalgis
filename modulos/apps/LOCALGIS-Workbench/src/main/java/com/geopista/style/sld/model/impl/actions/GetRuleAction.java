/**
 * GetRuleAction.java
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

import org.deegree.graphics.sld.Rule;

/**
 * @author enxenio s.l.
 *
 */
public class GetRuleAction {

	private int _position;
	private ArrayList _ruleList;
	
	public GetRuleAction(int position, ArrayList ruleList) {
		
		_position = position;
		_ruleList = ruleList;
	}
	
	public Object execute() {
		
		int size = _ruleList.size();
		Rule rule = null;
		if ((_position>=0)&&(_position<size)) {
			 rule = (Rule) _ruleList.get(_position);
		}
		return rule;
	}
}
