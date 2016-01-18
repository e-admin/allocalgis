/*
 * Created on 17-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl.actions;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import com.geopista.style.filtereditor.model.Operator;
import com.geopista.style.filtereditor.model.OperatorIdentifiers;
import com.geopista.style.filtereditor.model.exceptions.IncorrectIdentifierException;
import com.geopista.style.filtereditor.model.impl.BinaryComparationOp;
import com.geopista.style.filtereditor.model.impl.BinaryLogicOp;
import com.geopista.style.filtereditor.model.impl.PropertyIsBetweenOp;
import com.geopista.style.filtereditor.model.impl.PropertyIsLikeOp;
import com.geopista.style.filtereditor.model.impl.PropertyIsNullOp;
import com.geopista.style.filtereditor.model.impl.UnaryLogicOp;

/**
 * @author enxenio s.l.
 *
 */
public class ChangeOperatorTypeAction {

	private DefaultTreeModel _filterTree;
	private MutableTreeNode _oldNode;
	private int _newOperatorID;
	
	public ChangeOperatorTypeAction(DefaultTreeModel filterTree, MutableTreeNode oldNode, int newOperatorID) {
		
		_filterTree = filterTree;
		_oldNode = oldNode;
		_newOperatorID = newOperatorID;
	}
	
	public void execute() throws IncorrectIdentifierException {
		
		Operator aOperator = null;
		switch (_newOperatorID) {
			case OperatorIdentifiers.AND:
			case OperatorIdentifiers.OR: {
				aOperator = new BinaryLogicOp(_newOperatorID);
				break;
			}
			case OperatorIdentifiers.NOT: {
				aOperator = new UnaryLogicOp(_newOperatorID);
				break;
			}
			case OperatorIdentifiers.PROPERTYISEQUALTO:
			case OperatorIdentifiers.PROPERTYISGREATERTHAN:
			case OperatorIdentifiers.PROPERTYISGREATERTHANOREQUALTO:
			case OperatorIdentifiers.PROPERTYISLESSTHAN:
			case OperatorIdentifiers.PROPERTYISLESSTHANOREQUALTO: {
				aOperator = new BinaryComparationOp(_newOperatorID);
				break;
			}
			case OperatorIdentifiers.PROPERTYISBETWEEN: {
				aOperator = new PropertyIsBetweenOp(_newOperatorID);
				break;
			}
			case OperatorIdentifiers.PROPERTYISLIKE: {
				aOperator = new PropertyIsLikeOp(_newOperatorID);
				break;
			}
			case OperatorIdentifiers.PROPERTYISNULL: {
				aOperator = new PropertyIsNullOp(_newOperatorID);
				break;
			}
		}
		int numOldNodeChildren = _oldNode.getChildCount();
		for (int i=0;i<numOldNodeChildren;i++) {
			MutableTreeNode childNode = (MutableTreeNode)_oldNode.getChildAt(0);
			_oldNode.remove(0);
			aOperator.add(childNode);
		}
		MutableTreeNode parentNode = (MutableTreeNode)_oldNode.getParent();
		if (parentNode == null) {
			_filterTree.setRoot(aOperator);
		}
		else {
			int childPosition = parentNode.getIndex(_oldNode);
			parentNode.remove(childPosition);
			parentNode.insert(aOperator,childPosition);
		}
	}
}
