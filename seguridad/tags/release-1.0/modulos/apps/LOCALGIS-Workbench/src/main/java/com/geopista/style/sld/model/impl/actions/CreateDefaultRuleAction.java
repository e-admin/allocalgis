/*
 * Created on 08-jun-2004
 *
 */
package com.geopista.style.sld.model.impl.actions;

import org.deegree.graphics.sld.Rule;
import org.deegree.graphics.sld.Symbolizer;
import org.deegree_impl.graphics.sld.StyleFactory;
/**
 * @author enxenio s.l.
 *
 */
public class CreateDefaultRuleAction {

	public CreateDefaultRuleAction() {}
	
	public Object execute() {
		
		Symbolizer[] symbolizerArray = null;
		Rule newRule = StyleFactory.createRule(symbolizerArray);
		return newRule;
	}
}
