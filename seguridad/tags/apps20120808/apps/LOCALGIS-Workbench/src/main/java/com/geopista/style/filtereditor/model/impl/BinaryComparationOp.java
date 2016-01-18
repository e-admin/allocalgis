/*
 * Created on 16-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl;

import javax.swing.tree.TreeNode;

import com.geopista.style.filtereditor.model.Operator;
import com.geopista.style.filtereditor.model.OperatorIdentifiers;
import com.geopista.style.filtereditor.model.exceptions.IncorrectIdentifierException;

/**
 * @author enxenio s.l.
 *
 */
public class BinaryComparationOp extends Operator {

	private String _stringRepresentation;
	private String _errorReport;
	
	public BinaryComparationOp(int operatorID) throws IncorrectIdentifierException {
		super(operatorID);
		if (operatorID == OperatorIdentifiers.PROPERTYISEQUALTO) {
			_stringRepresentation = "=";
			super.setStringRepresentation(_stringRepresentation);
		}
		else if (operatorID == OperatorIdentifiers.PROPERTYISGREATERTHAN) {
			_stringRepresentation = ">";
			super.setStringRepresentation(_stringRepresentation);
		}
		else if (operatorID == OperatorIdentifiers.PROPERTYISLESSTHAN) {
			_stringRepresentation = "<";
			super.setStringRepresentation(_stringRepresentation);
		}
		else if (operatorID == OperatorIdentifiers.PROPERTYISGREATERTHANOREQUALTO) {
			_stringRepresentation = ">=";
			super.setStringRepresentation(_stringRepresentation);
		}
		else if (operatorID == OperatorIdentifiers.PROPERTYISLESSTHANOREQUALTO) {
			_stringRepresentation = "<=";
			super.setStringRepresentation(_stringRepresentation);
		}
		else {
			throw new IncorrectIdentifierException("El identificador no corresponde al de un operador de comparación binario");
		}
	}

	/* (non-Javadoc)
	 * @see com.geopista.styles.filtereditor.model.Operator#checkStructure()
	 */
	public boolean checkStructure() {
		boolean correctStructure = true;
		int numberChild = this.getChildCount();
		if (numberChild != 2) {
			correctStructure = false;
			_errorReport = ":El operador debe tener exactamente 2 hijos";
			super.setErrorReport(_errorReport);
		}			
		int i = 0;
		while ((i<numberChild)&&(correctStructure)) {
			TreeNode node = this.getChildAt(i);	
			if (node instanceof Operator) {
				correctStructure = false;
				_errorReport = ":El operador sólo puede tener Expresiones";
				super.setErrorReport(_errorReport);
			}
			i++;
		}
		return correctStructure;
	}
}
