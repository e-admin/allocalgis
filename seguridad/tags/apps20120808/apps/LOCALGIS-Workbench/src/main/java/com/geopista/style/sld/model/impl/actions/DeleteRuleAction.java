/*
 * Created on 09-jun-2004
 *
 */
package com.geopista.style.sld.model.impl.actions;

import java.util.ArrayList;

import org.deegree.graphics.sld.Rule;
/**
 * @author enxenio s.l.
 *
 */
public class DeleteRuleAction {

	private int _position;
	private ArrayList _ruleList;
	
	public DeleteRuleAction(int position, ArrayList ruleList) {
		
		_position = position;
		_ruleList = ruleList;
	}
	
	public void execute() {
		
		int size = _ruleList.size();
		Rule rule = null;
		if ((_position>=0)&&(_position<size)) {
			rule = (Rule) _ruleList.get(_position);
			_ruleList.remove(_position);
		}
	}

}
