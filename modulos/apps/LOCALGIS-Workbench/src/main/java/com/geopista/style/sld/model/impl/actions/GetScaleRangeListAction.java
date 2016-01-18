/**
 * GetScaleRangeListAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 26-jul-2004
 *
 */
package com.geopista.style.sld.model.impl.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.deegree.graphics.sld.FeatureTypeStyle;
import org.deegree.graphics.sld.Rule;

import com.geopista.style.sld.model.ScaleRange;
import com.geopista.style.sld.model.impl.ScaleRangeImpl;

/**
 * @author enxenio s.l.
 *
 */
public class GetScaleRangeListAction {
	
	private FeatureTypeStyle _featureTypeStyle;
	
	public GetScaleRangeListAction(FeatureTypeStyle featureTypeStyle) {
		
		_featureTypeStyle = featureTypeStyle;
	}
	
	public Object doExecute() {
		
		List scaleRangeList = new ArrayList();
		Rule[] rules = _featureTypeStyle.getRules();
		int rulesSize = rules.length;
		for (int i=0; i<rulesSize; i++) {
			Rule rule = rules[i];
			if (existScaleRange(rule,scaleRangeList)) {
				addToScaleRange(rule,scaleRangeList);
			}
			else {
				insertNewScaleRange(rule,scaleRangeList);
			}
		}
		Collections.sort(scaleRangeList);
		return scaleRangeList;
	}
	
	private boolean existScaleRange(Rule rule,List scaleRangeList) {
		
		boolean existScaleRange = false;
		if (scaleRangeList.isEmpty()) {
			return existScaleRange;
		}
		else {
			Double ruleMinScale = new Double(rule.getMinScaleDenominator());
			Double ruleMaxScale = new Double(rule.getMaxScaleDenominator());
			Iterator scaleRangeIterator = scaleRangeList.iterator();
			while ((scaleRangeIterator.hasNext())&&!(existScaleRange)) {
				ScaleRange scaleRange = (ScaleRange)scaleRangeIterator.next();
				if ((scaleRange.getMinScale().equals(ruleMinScale))&&(scaleRange.getMaxScale().equals(ruleMaxScale))) {
					existScaleRange = true;
				}
			}
			return existScaleRange;
		}
	}
	
	private void addToScaleRange(Rule rule, List scaleRangeList) {
		
		boolean foundScaleRange = false;
		ScaleRange scaleRange = null;
		Double ruleMinScale = new Double(rule.getMinScaleDenominator());
		Double ruleMaxScale = new Double(rule.getMaxScaleDenominator());
		Iterator scaleRangeIterator = scaleRangeList.iterator();
		while ((scaleRangeIterator.hasNext())&&!(foundScaleRange)) {
			ScaleRange scaleRangeAux = (ScaleRange)scaleRangeIterator.next();
			if ((scaleRangeAux.getMinScale().equals(ruleMinScale))&&(scaleRangeAux.getMaxScale().equals(ruleMaxScale))) {
				foundScaleRange = true;
				scaleRange = scaleRangeAux;
			}
		}
		scaleRange.addRule(rule);
	}
	
	private void insertNewScaleRange(Rule rule,List scaleRangeList) {
		
		Double ruleMinScale = new Double(rule.getMinScaleDenominator());
		Double ruleMaxScale = new Double(rule.getMaxScaleDenominator());
		ScaleRange scaleRange = new ScaleRangeImpl(ruleMinScale,ruleMaxScale);
		scaleRange.addRule(rule);
		scaleRangeList.add(scaleRange);
	}

}
