/*
 * Created on 17-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl.actions;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

/**
 * @author enxenio s.l.
 *
 */
public class DeleteNodeAction {

	private DefaultTreeModel _filterTree;
	private MutableTreeNode _node;
	
	public DeleteNodeAction(DefaultTreeModel filterTree,MutableTreeNode node) {
		
		_filterTree = filterTree;
		_node = node;
	}
	
	public void execute() {
		
		MutableTreeNode parentNode = (MutableTreeNode)_node.getParent();
		if (parentNode == null) {
			_filterTree.removeNodeFromParent(_node);
		}
		else {
			parentNode.remove(_node);
		}
	}
}
