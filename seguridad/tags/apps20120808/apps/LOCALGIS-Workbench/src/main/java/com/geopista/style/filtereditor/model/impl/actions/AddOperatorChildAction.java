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
public class AddOperatorChildAction {

	private DefaultTreeModel _filterTree;
	private MutableTreeNode _parentNode;
	private int _operatorID;
	
	public AddOperatorChildAction(DefaultTreeModel filterTree, MutableTreeNode parentNode, int operatorID) {
		
		_filterTree = filterTree;
		_parentNode = parentNode;
		_operatorID = operatorID;
	}
		
	public void execute() throws IncorrectIdentifierException {
		
		Operator aOperator = null;
		switch (_operatorID) {
			case OperatorIdentifiers.AND:
			case OperatorIdentifiers.OR: {
				aOperator = new BinaryLogicOp(_operatorID);
				break;
			}
			case OperatorIdentifiers.NOT: {
				aOperator = new UnaryLogicOp(_operatorID);
				break;
			}
			case OperatorIdentifiers.PROPERTYISEQUALTO:
			case OperatorIdentifiers.PROPERTYISGREATERTHAN:
			case OperatorIdentifiers.PROPERTYISGREATERTHANOREQUALTO:
			case OperatorIdentifiers.PROPERTYISLESSTHAN:
			case OperatorIdentifiers.PROPERTYISLESSTHANOREQUALTO: {
				aOperator = new BinaryComparationOp(_operatorID);
				break;
			}
			case OperatorIdentifiers.PROPERTYISBETWEEN: {
				aOperator = new PropertyIsBetweenOp(_operatorID);
				break;
			}
			case OperatorIdentifiers.PROPERTYISLIKE: {
				aOperator = new PropertyIsLikeOp(_operatorID);
				break;
			}
			case OperatorIdentifiers.PROPERTYISNULL: {
				aOperator = new PropertyIsNullOp(_operatorID);
				break;
			}
		}
		if (_parentNode == null) {
			_filterTree.setRoot(aOperator);
		}
		else {
			int numChildren = _parentNode.getChildCount();
			_parentNode.insert(aOperator,numChildren);
		}
	}
}
