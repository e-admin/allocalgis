/*
 * Created on 20-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl.actions;

import javax.swing.tree.MutableTreeNode;

import com.geopista.style.filtereditor.model.impl.PropertyName;

/**
 * @author enxenio s.l.
 *
 */
public class UpdatePropertyNameAction {
	
	private PropertyName _propertyName;
	private MutableTreeNode _parentNode;
	private Integer _insert;
	private String _value;
	
	public UpdatePropertyNameAction(PropertyName propertyName,MutableTreeNode parentNode,Integer insert,String value) {
		
		_propertyName  = propertyName;
		_parentNode = parentNode;
		_insert = insert;
		_value = value;
	}
	
	public Object execute() {
		
		_propertyName.setValue(_value);
		if (_insert.intValue() == 1) {
			int numChildren = _parentNode.getChildCount();
			_parentNode.insert(_propertyName,numChildren);	
		}
		return _propertyName;
	}
}
