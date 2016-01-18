/*
 * Created on 20-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl.actions;

import javax.swing.tree.MutableTreeNode;

import com.geopista.style.filtereditor.model.impl.Literal;

/**
 * @author enxenio s.l.
 *
 */
public class UpdateLiteralAction {
	
	private Literal _literal;
	private MutableTreeNode _parentNode;
	private Integer _insert;
	private String _value;

	public UpdateLiteralAction(Literal literal,MutableTreeNode parentNode,Integer insert,String value) {
		
		_literal  = literal;
		_parentNode = parentNode;
		_insert = insert;
		_value = value;
	}
	
	public Object execute() {
		
		_literal.setValue(_value);
		if (_insert.intValue() == 1) {
			int numChildren = _parentNode.getChildCount();
			_parentNode.insert(_literal,numChildren);	
		}
		return _literal;
	}
}
