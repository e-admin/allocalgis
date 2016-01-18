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
