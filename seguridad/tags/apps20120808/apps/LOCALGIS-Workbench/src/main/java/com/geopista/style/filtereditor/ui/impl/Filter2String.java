/*
 * Created on 24-sep-2004
 *
 */
package com.geopista.style.filtereditor.ui.impl;

import com.geopista.style.filtereditor.model.Expression;
import com.geopista.style.filtereditor.model.Operator;
import com.geopista.style.filtereditor.model.OperatorIdentifiers;
import com.geopista.style.filtereditor.model.impl.BBOXOp;
import com.geopista.style.filtereditor.model.impl.BinaryComparationOp;
import com.geopista.style.filtereditor.model.impl.BinaryLogicOp;
import com.geopista.style.filtereditor.model.impl.DistanceBufferOp;
import com.geopista.style.filtereditor.model.impl.Literal;
import com.geopista.style.filtereditor.model.impl.PropertyIsBetweenOp;
import com.geopista.style.filtereditor.model.impl.PropertyIsLikeOp;
import com.geopista.style.filtereditor.model.impl.PropertyIsNullOp;
import com.geopista.style.filtereditor.model.impl.PropertyName;
import com.geopista.style.filtereditor.model.impl.UnaryLogicOp;
import com.geopista.style.filtereditor.model.impl.UnknownOp;

/**
 * @author enxenio s.l.
 *
 */
public class Filter2String {
	
	public static String createStringFromExpression(Expression expression) {
		String expressionAsText = null;
		
		if (expression instanceof PropertyName) {
			expressionAsText = ((PropertyName)expression).getValue(); 
		}
		else if (expression instanceof Literal) {
			expressionAsText = ((Literal)expression).getValue(); 
		}
		return expressionAsText;
	}

	public static String createStringFromOperation(Operator operator){

		StringBuffer filterAsText = new StringBuffer();
		int numChildren = operator.getChildCount();
		if (operator instanceof BinaryComparationOp) {
			if (numChildren >= 1) {
				filterAsText.append(createStringFromExpression((Expression)operator.getChildAt(0))+ " ");
			}
			filterAsText.append(createStringFromOperationID(operator.getOperatorID())+ " ");
			if (numChildren  > 1) {
				filterAsText.append(createStringFromExpression((Expression)operator.getChildAt(1))+ " ");
			}
		}
		else if (operator instanceof BinaryLogicOp) {
			if (numChildren == 0) {
				if (operator.getOperatorID() == OperatorIdentifiers.AND) {
					filterAsText.append("AND");
				}
				else if (operator.getOperatorID() == OperatorIdentifiers.OR) {
					filterAsText.append("OR");
				}				 
			}
			else {
				filterAsText.append("(");
				filterAsText.append(createStringFromOperation((Operator)operator.getChildAt(0)));				
				if (operator.getOperatorID() == OperatorIdentifiers.AND) {
					filterAsText.append(") AND ");
				}
				else if (operator.getOperatorID() == OperatorIdentifiers.OR) {
					filterAsText.append(") OR ");
				}	
				if (numChildren >= 2) {
					filterAsText.append("(");
					filterAsText.append(createStringFromOperation((Operator)operator.getChildAt(1)));				
					if (operator.getOperatorID() == OperatorIdentifiers.AND) {
						filterAsText.append(")");
					}
					else if (operator.getOperatorID() == OperatorIdentifiers.OR) {
						filterAsText.append(")");
					}				
								
				}
				for (int i = 2; i < numChildren ; i++) {
					if (operator.getOperatorID() == OperatorIdentifiers.AND) {
						filterAsText.append(" AND (");
					}
					else if (operator.getOperatorID() == OperatorIdentifiers.OR) {
						filterAsText.append(" OR (");
					}				
					filterAsText.append(createStringFromOperation((Operator)operator.getChildAt(i)));					
					filterAsText.append(")");
				}
			}
		}
		else if (operator instanceof UnaryLogicOp) {
			if (numChildren == 0) {
				filterAsText.append("NOT");
			}
			else {
				filterAsText.append("NOT (");
				filterAsText.append(createStringFromOperation((Operator)operator.getChildAt(0)));
				filterAsText.append(")");
			}
		}	
		else if (operator instanceof PropertyIsLikeOp) {
			if (numChildren >= 1) {
				filterAsText.append(createStringFromExpression((Expression)operator.getChildAt(0))+ " ");
			}
			filterAsText.append(createStringFromOperationID(operator.getOperatorID())+ " ");
			if (numChildren  > 1) {
				filterAsText.append(createStringFromExpression((Expression)operator.getChildAt(1))+ " ");
			}
		}
		else if (operator instanceof PropertyIsBetweenOp) {
			if (numChildren >= 1) {
				filterAsText.append(createStringFromExpression((Expression)operator.getChildAt(0))+ " ");
			}
			filterAsText.append(createStringFromOperationID(operator.getOperatorID())+ " ");
			if (numChildren  >= 2) {
				filterAsText.append(createStringFromExpression((Expression)operator.getChildAt(1))+ " ");
			}
			if (numChildren  > 3) {
				filterAsText.append(createStringFromExpression((Expression)operator.getChildAt(2))+ " ");
			}
		}
		else if (operator instanceof PropertyIsNullOp) {
			filterAsText.append(createStringFromOperationID(operator.getOperatorID())+ " ");
			if (numChildren  == 1) {
				filterAsText.append(createStringFromExpression((Expression)operator.getChildAt(0))+ " ");
			}
		}
		else if (operator instanceof BBOXOp) {
			if (numChildren == 1) {
				filterAsText.append(createStringFromExpression((Expression)operator.getChildAt(0))+ " ");
			}
			BBOXOp bboxOp = (BBOXOp)operator;
			if (bboxOp.getBBOX() != null) {
				filterAsText.append(createStringFromOperationID(operator.getOperatorID())+ " ");
				filterAsText.append("("+bboxOp.getBBOX().toString()+")");
			}
		}
		else if (operator instanceof DistanceBufferOp) {
			if (numChildren == 1) {
				filterAsText.append(createStringFromExpression((Expression)operator.getChildAt(0))+ " ");
			}
			DistanceBufferOp distanceBufferOp = (DistanceBufferOp)operator;
			if (distanceBufferOp.getGMLGeometry() != null) {
				filterAsText.append(createStringFromOperationID(operator.getOperatorID())+ " ");
				filterAsText.append("distance of"+distanceBufferOp.getDistance()+" ("+distanceBufferOp.getGMLGeometry().toString()+")");
			}
		}
		else if (operator instanceof UnknownOp) {
			filterAsText.append(createStringFromOperationID(operator.getOperatorID()));
		}
		return filterAsText.toString();
	}

	public static String createStringFromOperationID(int operationID){
		String operatorAsText = null;
		
		switch(operationID) {
			case OperatorIdentifiers.PROPERTYISEQUALTO:
				operatorAsText = "=";
				break;
			case OperatorIdentifiers.PROPERTYISGREATERTHAN:
				operatorAsText = ">";
				break;
			case OperatorIdentifiers.PROPERTYISGREATERTHANOREQUALTO:
				operatorAsText = ">=";
				break;
			case OperatorIdentifiers.PROPERTYISLESSTHAN:
				operatorAsText = "<";
				break;
			case OperatorIdentifiers.PROPERTYISLESSTHANOREQUALTO:
				operatorAsText = "<=";
				break;
			case OperatorIdentifiers.PROPERTYISBETWEEN:
				operatorAsText = "between";
				break;
			case OperatorIdentifiers.AND:
				operatorAsText = "and";
				break;				
			case OperatorIdentifiers.OR:
				operatorAsText = "or";
				break;				
			case OperatorIdentifiers.NOT:
				operatorAsText = "not";
				break;
			case OperatorIdentifiers.PROPERTYISLIKE:
				operatorAsText = "IsLike";
				break;
			case OperatorIdentifiers.PROPERTYISNULL:
				operatorAsText = "IsNull";
				break;
			case OperatorIdentifiers.BBOX:
				operatorAsText = "BBOX";
				break;
			case OperatorIdentifiers.DWITHIN:
				operatorAsText = "DWithin";
				break;
			case OperatorIdentifiers.BEYOND:
				operatorAsText = "Beyond";
				break;
			default:
				operatorAsText = "unknown";
				break;
		}
		return operatorAsText;
	}


}
