/**
 * CreateFilterTreeAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 16-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl.actions;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.tree.DefaultTreeModel;

import org.deegree.gml.GMLBox;
import org.deegree.gml.GMLGeometry;
import org.deegree.services.wfs.filterencoding.Filter;
import org.deegree_impl.services.wfs.filterencoding.AbstractOperation;
import org.deegree_impl.services.wfs.filterencoding.ComplexFilter;
import org.deegree_impl.services.wfs.filterencoding.Expression_Impl;
import org.deegree_impl.services.wfs.filterencoding.LogicalOperation;
import org.deegree_impl.services.wfs.filterencoding.PropertyIsBetweenOperation;
import org.deegree_impl.services.wfs.filterencoding.PropertyIsCOMPOperation;
import org.deegree_impl.services.wfs.filterencoding.PropertyIsLikeOperation;
import org.deegree_impl.services.wfs.filterencoding.PropertyIsNullOperation;
import org.deegree_impl.services.wfs.filterencoding.SpatialOperation;

import com.geopista.style.filtereditor.model.Expression;
import com.geopista.style.filtereditor.model.Operator;
import com.geopista.style.filtereditor.model.OperatorIdentifiers;
import com.geopista.style.filtereditor.model.exceptions.IncorrectIdentifierException;
import com.geopista.style.filtereditor.model.exceptions.MalformedFilterException;
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
/**
 * @author enxenio s.l.
 *
 */
public class CreateFilterTreeAction {
	
	private Filter _filter;
	
	public CreateFilterTreeAction(Filter filter) {
		_filter = filter;
	}

	public Object execute() throws IncorrectIdentifierException,MalformedFilterException {
		
		DefaultTreeModel filterTree = null;
		if (_filter instanceof ComplexFilter) {
			ComplexFilter complexFilter = (ComplexFilter)_filter;
			AbstractOperation operation = (AbstractOperation)complexFilter.getOperation();
			Operator aOperator = createOperator(operation);
			filterTree = new DefaultTreeModel(aOperator);
			return filterTree;
		}
		else {
			throw new MalformedFilterException("Filtro no está bien formado");
		}
	}

	public Operator createOperator(AbstractOperation operation) throws IncorrectIdentifierException {
		
		Operator aOperator = null;
		switch (operation.getOperatorId()){
			case OperatorIdentifiers.AND:
			case OperatorIdentifiers.OR: {
				aOperator = new BinaryLogicOp(operation.getOperatorId());
				ArrayList childOperators = ((LogicalOperation)operation).getArguments();
				Iterator operatorIterator = childOperators.iterator();
				while (operatorIterator.hasNext()) {
					AbstractOperation childOperation = (AbstractOperation)operatorIterator.next();
					Operator childOperator = createOperator(childOperation);
					aOperator.add(childOperator);
				}
				break;
			}
			case OperatorIdentifiers.NOT: {
				aOperator = new UnaryLogicOp(operation.getOperatorId());
				ArrayList childOperators = ((LogicalOperation)operation).getArguments();
				Iterator operatorIterator = childOperators.iterator();
				while (operatorIterator.hasNext()) {
					AbstractOperation childOperation = (AbstractOperation)operatorIterator.next();
					Operator childOperator = createOperator(childOperation);
					aOperator.add(childOperator);
				}
			
				break;			
			}
			case OperatorIdentifiers.PROPERTYISEQUALTO:
			case OperatorIdentifiers.PROPERTYISGREATERTHAN:
			case OperatorIdentifiers.PROPERTYISGREATERTHANOREQUALTO:
			case OperatorIdentifiers.PROPERTYISLESSTHAN:
			case OperatorIdentifiers.PROPERTYISLESSTHANOREQUALTO: {
				aOperator = new BinaryComparationOp(operation.getOperatorId());
				Expression_Impl expression1 = (Expression_Impl)((PropertyIsCOMPOperation)operation).getFirstExpression();
				Expression expr1 = createExpression(expression1);
				aOperator.add(expr1);
				Expression_Impl expression2 = (Expression_Impl)((PropertyIsCOMPOperation)operation).getSecondExpression();
				Expression expr2 = createExpression(expression2);
				aOperator.add(expr2);
				break;
			}
			case OperatorIdentifiers.PROPERTYISBETWEEN: {
				aOperator = new PropertyIsBetweenOp(operation.getOperatorId());
				Expression_Impl expression1 = (Expression_Impl)((PropertyIsBetweenOperation)operation).getPropertyName();
				Expression expr1 = createExpression(expression1);
				aOperator.add(expr1);
				Expression_Impl expression2 = (Expression_Impl)((PropertyIsBetweenOperation)operation).getLowerBoundary();
				Expression expr2 = createExpression(expression2);
				aOperator.add(expr2);
				Expression_Impl expression3 = (Expression_Impl)((PropertyIsBetweenOperation)operation).getUpperBoundary();
				Expression expr3 = createExpression(expression3);
				aOperator.add(expr3);
				break;
			}
			case OperatorIdentifiers.PROPERTYISLIKE: {
				aOperator = new PropertyIsLikeOp(operation.getOperatorId());
				Expression_Impl expression1 = (Expression_Impl)((PropertyIsLikeOperation)operation).getPropertyName();
				Expression expr1 = createExpression(expression1);
				aOperator.add(expr1);				
				Expression_Impl expression2 = (Expression_Impl)((PropertyIsLikeOperation)operation).getLiteral();
				Expression expr2 = createExpression(expression2);
				aOperator.add(expr2);
				PropertyIsLikeOp propertyIsLikeOp = (PropertyIsLikeOp)aOperator;
				propertyIsLikeOp.setWildCard(""+((PropertyIsLikeOperation)operation).getWildCard());
				propertyIsLikeOp.setSingleChar(""+((PropertyIsLikeOperation)operation).getSingleChar());
				propertyIsLikeOp.setEscape(""+((PropertyIsLikeOperation)operation).getEscapeChar());
				aOperator = propertyIsLikeOp;
				break;
			}
			case OperatorIdentifiers.PROPERTYISNULL: {
				aOperator = new PropertyIsNullOp(operation.getOperatorId());
				Expression_Impl expression = (Expression_Impl)((PropertyIsNullOperation)operation).getExpression();
				Expression expr = createExpression(expression);
				aOperator.add(expr);								
				break;
			}
			case OperatorIdentifiers.BBOX: {
				aOperator = new BBOXOp(operation.getOperatorId(),(GMLBox)((SpatialOperation)operation).getGeometry());
				Expression_Impl expression = (Expression_Impl)((SpatialOperation)operation).getPropertyName();
				Expression expr = createExpression(expression);
				aOperator.add(expr);								
				break;				
			}
			case OperatorIdentifiers.BEYOND:
			case OperatorIdentifiers.DWITHIN: {
				aOperator = new DistanceBufferOp(operation.getOperatorId(),(GMLGeometry)((SpatialOperation)operation).getGeometry(),((SpatialOperation)operation).getDistance());
				Expression_Impl expression = (Expression_Impl)((SpatialOperation)operation).getPropertyName();
				Expression expr = createExpression(expression);
				aOperator.add(expr);												
				break;
			}
		}
		return aOperator;
	}
	
	public Expression createExpression(Expression_Impl expression) {
		
		Expression newExpression = null;
		if (expression.getExpressionId() == OperatorIdentifiers.EXPRESSION_LITERAL) {
			org.deegree_impl.services.wfs.filterencoding.Literal deegreeLiteral = 
				(org.deegree_impl.services.wfs.filterencoding.Literal)expression;
			newExpression = new Literal(deegreeLiteral.getValue());
		}
		else if (expression.getExpressionId() == OperatorIdentifiers.EXPRESSION_PROPERTYNAME) {
			org.deegree_impl.services.wfs.filterencoding.PropertyName deegreePropertyName =
				(org.deegree_impl.services.wfs.filterencoding.PropertyName)expression;
			newExpression = new PropertyName(deegreePropertyName.getValue());
		}
		return newExpression;
	}

}

