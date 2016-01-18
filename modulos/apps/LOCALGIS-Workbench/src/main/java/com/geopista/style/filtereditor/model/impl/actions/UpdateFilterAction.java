/**
 * UpdateFilterAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 21-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl.actions;

import java.util.ArrayList;

import javax.swing.tree.MutableTreeNode;

import org.deegree.services.wfs.filterencoding.Expression;
import org.deegree.services.wfs.filterencoding.Filter;
import org.deegree.services.wfs.filterencoding.FilterConstructionException;
import org.deegree.services.wfs.filterencoding.Operation;
import org.deegree_impl.services.wfs.filterencoding.ComplexFilter;
import org.deegree_impl.services.wfs.filterencoding.LogicalOperation;
import org.deegree_impl.services.wfs.filterencoding.PropertyIsBetweenOperation;
import org.deegree_impl.services.wfs.filterencoding.PropertyIsCOMPOperation;
import org.deegree_impl.services.wfs.filterencoding.PropertyIsLikeOperation;
import org.deegree_impl.services.wfs.filterencoding.PropertyIsNullOperation;
import org.deegree_impl.services.wfs.filterencoding.SpatialOperation;

import com.geopista.style.filtereditor.model.Operator;
import com.geopista.style.filtereditor.model.OperatorIdentifiers;
import com.geopista.style.filtereditor.model.exceptions.MalformedFilterException;
import com.geopista.style.filtereditor.model.impl.BBOXOp;
import com.geopista.style.filtereditor.model.impl.BinaryLogicOp;
import com.geopista.style.filtereditor.model.impl.DistanceBufferOp;
import com.geopista.style.filtereditor.model.impl.Literal;
import com.geopista.style.filtereditor.model.impl.PropertyIsLikeOp;
import com.geopista.style.filtereditor.model.impl.PropertyName;
import com.geopista.style.filtereditor.model.impl.UnaryLogicOp;
import com.geopista.style.filtereditor.model.impl.UnknownOp;


/**
 * @author enxenio s.l.
 *
 */
public class UpdateFilterAction {
	
	private MutableTreeNode _rootNode;
	
	public UpdateFilterAction(MutableTreeNode rootNode) {
		
		_rootNode = rootNode;
	}
	
	public Object execute() throws MalformedFilterException,FilterConstructionException {
		
		if (!(_rootNode instanceof Operator)) {
			throw new MalformedFilterException("El nodo raiz no puede tener un elemento que no sea un operador"); 	
		}
		else if (_rootNode instanceof UnknownOp) {
			throw new MalformedFilterException("El nodo raiz no puede ser un operador desconocido");
		}
		else {
			Operator rootOperator = (Operator)_rootNode;
			Operation operation = createOperation(rootOperator);
			Filter filter = new ComplexFilter(operation);
			return filter;
		}
	}
	
	public Operation createOperation(Operator operator) throws MalformedFilterException,FilterConstructionException {
		
		Operation operation = null;
		switch (operator.getOperatorID()){
			case OperatorIdentifiers.AND:
			case OperatorIdentifiers.OR: 
			case OperatorIdentifiers.NOT: {
				ArrayList arguments = new ArrayList();
				if (!operator.checkStructure()) {
					if (operator instanceof BinaryLogicOp) {
						throw new MalformedFilterException("Operador lógico binario mal construido"+operator.getErrorReport());
					}
					else if (operator instanceof UnaryLogicOp) {
						throw new MalformedFilterException("Operador lógico unario mal construido"+operator.getErrorReport());
					}
				}
				else {
					int numChildren = operator.getChildCount();
					for (int i=0; i<numChildren; i++) {
						Operator childOperator = (Operator)operator.getChildAt(i);
						Operation childOperation = createOperation(childOperator);
						arguments.add(childOperation);
					}
				}
				LogicalOperation logicalOperation = new LogicalOperation(operator.getOperatorID(),arguments);
				operation = logicalOperation;
				break;
			}
			case OperatorIdentifiers.PROPERTYISEQUALTO:
			case OperatorIdentifiers.PROPERTYISGREATERTHAN:
			case OperatorIdentifiers.PROPERTYISGREATERTHANOREQUALTO:
			case OperatorIdentifiers.PROPERTYISLESSTHAN:
			case OperatorIdentifiers.PROPERTYISLESSTHANOREQUALTO: {
				PropertyIsCOMPOperation propertyIsCompOperation = null;
				if (!operator.checkStructure()) {
					throw new MalformedFilterException("Operador escalar de comparación binario mal construido"+operator.getErrorReport());
				}
				else {
					com.geopista.style.filtereditor.model.Expression childExpression1 = 
						(com.geopista.style.filtereditor.model.Expression)operator.getChildAt(0);
					Expression expression1 = createExpression(childExpression1);
					com.geopista.style.filtereditor.model.Expression childExpression2 = 
						(com.geopista.style.filtereditor.model.Expression)operator.getChildAt(1);
					Expression expression2 = createExpression(childExpression2);
					propertyIsCompOperation = new PropertyIsCOMPOperation(operator.getOperatorID(),expression1,expression2);	
				}					
				operation = propertyIsCompOperation;
				break;
			}
			case OperatorIdentifiers.PROPERTYISBETWEEN: {
				PropertyIsBetweenOperation propertyIsBetweenOperation = null;
				if (!operator.checkStructure()) {
					throw new MalformedFilterException("Operador escalar Between mal construido"+operator.getErrorReport());
				}
				else {
					com.geopista.style.filtereditor.model.Expression childExpression1 = 
						(com.geopista.style.filtereditor.model.Expression)operator.getChildAt(0);
					Expression expression1 = createExpression(childExpression1);
					com.geopista.style.filtereditor.model.Expression childExpression2 = 
						(com.geopista.style.filtereditor.model.Expression)operator.getChildAt(1);
					Expression expression2 = createExpression(childExpression2);
					com.geopista.style.filtereditor.model.Expression childExpression3 = 
						(com.geopista.style.filtereditor.model.Expression)operator.getChildAt(2);
					Expression expression3 = createExpression(childExpression3);
					propertyIsBetweenOperation = 
						new PropertyIsBetweenOperation((org.deegree_impl.services.wfs.filterencoding.PropertyName)expression1,expression2,expression3);					
				}
				operation = propertyIsBetweenOperation;
				break;
			}
			case OperatorIdentifiers.PROPERTYISLIKE: {
				PropertyIsLikeOperation propertyIsLikeOperation = null;
				if (!operator.checkStructure()) {
					throw new MalformedFilterException("Operador escalar IsLike mal construido"+operator.getErrorReport());
				}
				else {
					com.geopista.style.filtereditor.model.Expression childExpression1 = 
						(com.geopista.style.filtereditor.model.Expression)operator.getChildAt(0);
					Expression expression1 = createExpression(childExpression1);
					com.geopista.style.filtereditor.model.Expression childExpression2 = 
						(com.geopista.style.filtereditor.model.Expression)operator.getChildAt(1);
					Expression expression2 = createExpression(childExpression2);
					PropertyIsLikeOp propertyIsLikeOp = (PropertyIsLikeOp)operator;
					propertyIsLikeOperation = 
						new PropertyIsLikeOperation((org.deegree_impl.services.wfs.filterencoding.PropertyName)expression1,(org.deegree_impl.services.wfs.filterencoding.Literal)expression2,
						propertyIsLikeOp.getWildCard().charAt(0),propertyIsLikeOp.getSingleChar().charAt(0),propertyIsLikeOp.getEscape().charAt(0));					
				}
				operation = propertyIsLikeOperation;
				break;
			}
			case OperatorIdentifiers.PROPERTYISNULL: {
				PropertyIsNullOperation propertyIsNullOperation = null;
				if (!operator.checkStructure()) {
					throw new MalformedFilterException("Operador escalar IsNull mal construido"+operator.getErrorReport());
				}
				else {
					com.geopista.style.filtereditor.model.Expression childExpression1 = 
						(com.geopista.style.filtereditor.model.Expression)operator.getChildAt(0);
					Expression expression1 = createExpression(childExpression1);
					propertyIsNullOperation = 
						new PropertyIsNullOperation(expression1);					
				}
				operation = propertyIsNullOperation;
				break;
			}
			case OperatorIdentifiers.BBOX: {
				SpatialOperation spatialOperation = null;
				if (!operator.checkStructure()) {
					throw new MalformedFilterException("Operador espacial BBOX mal construido"+operator.getErrorReport());
				}
				else {
					com.geopista.style.filtereditor.model.Expression childExpression1 = 
						(com.geopista.style.filtereditor.model.Expression)operator.getChildAt(0);
					Expression expression1 = createExpression(childExpression1);
					BBOXOp bboxOp = (BBOXOp)operator;
					spatialOperation = new SpatialOperation(operator.getOperatorID(),(org.deegree_impl.services.wfs.filterencoding.PropertyName)expression1,bboxOp.getBBOX());
				}
				operation = spatialOperation;
				break;				
			}
			case OperatorIdentifiers.BEYOND:
			case OperatorIdentifiers.DWITHIN: {
				SpatialOperation spatialOperation = null;
				if (!operator.checkStructure()) {
					throw new MalformedFilterException("Operador espacial BEYOND/WITHIN mal construido"+operator.getErrorReport());
				}
				else {
					com.geopista.style.filtereditor.model.Expression childExpression1 = 
						(com.geopista.style.filtereditor.model.Expression)operator.getChildAt(0);
					Expression expression1 = createExpression(childExpression1);
					DistanceBufferOp distanceBufferOp = (DistanceBufferOp)operator;
					spatialOperation = new SpatialOperation(operator.getOperatorID(),(org.deegree_impl.services.wfs.filterencoding.PropertyName)expression1,distanceBufferOp.getGMLGeometry(),distanceBufferOp.getDistance());
				}
				operation = spatialOperation;
				break;				
			}
		}
		return operation;
	}
	
	private Expression createExpression(com.geopista.style.filtereditor.model.Expression expression) {
		
		Expression newExpression = null;
		if (expression instanceof Literal) {
			Literal literal = (Literal)expression;
			newExpression = new org.deegree_impl.services.wfs.filterencoding.Literal(literal.getValue());
		}
		else if (expression instanceof PropertyName) {
			PropertyName propertyName = (PropertyName)expression;
			newExpression = new org.deegree_impl.services.wfs.filterencoding.PropertyName(propertyName.getValue());
		}
		return newExpression;
	}

}
