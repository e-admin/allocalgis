/*
 * Created on 01-sep-2004
 *
 */
package com.geopista.style.sld.model.impl.actions;

import java.util.List;

import org.deegree.graphics.sld.Rule;

/**
 * @author enxenio s.l.
 *
 */
public class MoveRuleAction {

	private int _initialPosition;
	private int _finalPosition;
	private List _ruleList;
	
	public MoveRuleAction(int initialPosition,int finalPosition,List ruleList) {
		
		_initialPosition = initialPosition;
		_finalPosition = finalPosition;
		_ruleList = ruleList; 
	}
	
	public void execute() {
		
		Rule rule = (Rule)_ruleList.remove(_initialPosition);
		_ruleList.add(_finalPosition,rule);
	}
}
