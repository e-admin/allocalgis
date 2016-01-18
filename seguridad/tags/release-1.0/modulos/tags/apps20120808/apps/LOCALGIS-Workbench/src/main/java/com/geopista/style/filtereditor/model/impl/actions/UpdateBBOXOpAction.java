/*
 * Created on 20-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl.actions;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import org.deegree.gml.GMLBox;

import com.geopista.style.filtereditor.model.impl.BBOXOp;

/**
 * @author enxenio s.l.
 *
 */
public class UpdateBBOXOpAction {
	
	private BBOXOp _bboxOp;
	private GMLBox _gmlBox;
	private MutableTreeNode _oldNode;
	private MutableTreeNode _parentNode;
	private DefaultTreeModel _filterTree;
	private Integer _insert;
	private Integer _index;
	
	public UpdateBBOXOpAction(BBOXOp bboxOp,GMLBox gmlBox,MutableTreeNode oldNode,MutableTreeNode parentNode,DefaultTreeModel filterTree,Integer insert,Integer index) {
		
		_bboxOp = bboxOp;
		_gmlBox = gmlBox;
		_oldNode = oldNode;
		_parentNode = parentNode;
		_filterTree = filterTree;
		_insert = insert;
		_index = index;
	}
	
	public Object execute() {
		
		_bboxOp.setBBOX(_gmlBox);
		if (_insert.intValue() == 1) {
			if (_oldNode != null) {
				int numOldNodeChildren = _oldNode.getChildCount();
				for (int i=0;i<numOldNodeChildren;i++) {
					MutableTreeNode childNode = (MutableTreeNode)_oldNode.getChildAt(0);
					_oldNode.remove(0);
					_bboxOp.add(childNode);
				}			
			}
			if (_parentNode == null) {
				_filterTree.setRoot(_bboxOp);
			}
			else {
				if (_index.intValue() != _parentNode.getChildCount()) {
					_parentNode.remove(_index.intValue());
				}
				_parentNode.insert(_bboxOp,_index.intValue());
			}
		}
		return _bboxOp;
	}
}
