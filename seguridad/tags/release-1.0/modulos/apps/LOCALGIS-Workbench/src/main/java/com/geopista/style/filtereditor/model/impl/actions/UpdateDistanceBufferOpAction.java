/*
 * Created on 20-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl.actions;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import org.deegree.gml.GMLGeometry;

import com.geopista.style.filtereditor.model.impl.DistanceBufferOp;

/**
 * @author enxenio s.l.
 *
 */
public class UpdateDistanceBufferOpAction {
	
	private DistanceBufferOp _distanceBufferOp;
	private double _distance;
	private GMLGeometry _geometry;
	private MutableTreeNode _oldNode;
	private MutableTreeNode _parentNode;
	private DefaultTreeModel _filterTree;
	private Integer _insert;
	private Integer _index;
	
	public UpdateDistanceBufferOpAction(DistanceBufferOp distanceBufferOp,double distance,GMLGeometry geometry,MutableTreeNode oldNode,MutableTreeNode parentNode,DefaultTreeModel filterTree,Integer insert,Integer index) {
		
		_distanceBufferOp = distanceBufferOp;
		_distance = distance;
		_geometry = geometry;
		_oldNode = oldNode;
		_parentNode = parentNode;
		_filterTree = filterTree;
		_insert = insert;
		_index = index;
	}
	
	public Object execute() {
		
		_distanceBufferOp.setDistance(_distance);
		_distanceBufferOp.setGMLGeometry(_geometry);
		if (_insert.intValue() == 1) {
			if (_oldNode != null) {
				int numOldNodeChildren = _oldNode.getChildCount();
				for (int i=0;i<numOldNodeChildren;i++) {
					MutableTreeNode childNode = (MutableTreeNode)_oldNode.getChildAt(0);
					_oldNode.remove(0);
					_distanceBufferOp.add(childNode);
				}
			}
			if (_parentNode == null) {
				_filterTree.setRoot(_distanceBufferOp);
			}
			else {
				if (_index.intValue() != _parentNode.getChildCount()) {
					_parentNode.remove(_index.intValue());
				}
				_parentNode.insert(_distanceBufferOp,_index.intValue());
			}
		}
		return _distanceBufferOp;	
	}
}
