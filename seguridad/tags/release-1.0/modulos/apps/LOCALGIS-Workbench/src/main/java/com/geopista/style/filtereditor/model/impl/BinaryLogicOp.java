/*
 * Created on 16-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl;

import javax.swing.tree.TreeNode;

import com.geopista.style.filtereditor.model.Expression;
import com.geopista.style.filtereditor.model.Operator;
import com.geopista.style.filtereditor.model.OperatorIdentifiers;
import com.geopista.style.filtereditor.model.exceptions.IncorrectIdentifierException;
/**
 * @author enxenio s.l.
 *
 */
public class BinaryLogicOp extends Operator {

	private String _stringRepresentation;
	private String _errorReport;
	
	public BinaryLogicOp(int operatorID) throws IncorrectIdentifierException {
		super(operatorID);
		if (operatorID == OperatorIdentifiers.AND) {
			_stringRepresentation = "AND";
			super.setStringRepresentation(_stringRepresentation);
		}
		else if (operatorID == OperatorIdentifiers.OR) {
			_stringRepresentation = "OR";
			super.setStringRepresentation(_stringRepresentation);
		}
		else {
			throw new IncorrectIdentifierException("El identificador no corresponde al de un operador lógico binario");
		}
	}
	/* (non-Javadoc)
	 * @see com.geopista.styles.filtereditor.model.Operator#checkStructure()
	 */
	public boolean checkStructure() {
		boolean correctStructure = true;
		int numberChild = this.getChildCount();
		if ((super.getOperatorID() == OperatorIdentifiers.AND)||(super.getOperatorID() == OperatorIdentifiers.OR)) {
			if (numberChild < 2) {
				correctStructure = false;
				_errorReport = ":El operador debe tener, como mínimo, 2 hijos";
				super.setErrorReport(_errorReport);
			}			
		}
		int i = 0;
		while ((i<numberChild)&&(correctStructure)) {
			TreeNode node = this.getChildAt(i);	
			if (node instanceof Expression) {
				correctStructure = false;
				_errorReport = ":El operador sólo puede tener como hijos a otros operadores";
				super.setErrorReport(_errorReport);
			}
			i++;
		}
		return correctStructure;
	}
}
