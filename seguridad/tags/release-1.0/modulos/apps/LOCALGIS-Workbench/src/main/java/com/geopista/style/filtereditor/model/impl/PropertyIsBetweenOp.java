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
public class PropertyIsBetweenOp extends Operator {

	private String _stringRepresentation;
	private String _errorReport;
	
	public PropertyIsBetweenOp(int operatorID) throws IncorrectIdentifierException {
		super(operatorID);
		if (operatorID == OperatorIdentifiers.PROPERTYISBETWEEN) {
			_stringRepresentation = "BETWEEN";
			super.setStringRepresentation(_stringRepresentation);
		}
		else {
			throw new IncorrectIdentifierException("El identificador no corresponde al del operador ISBETWEEN");
		}
	}

	/* (non-Javadoc)
	 * @see com.geopista.styles.filtereditor.model.Operator#checkStructure()
	 */
	public boolean checkStructure() {
		boolean correctStructure = true;
		int numberChild = this.getChildCount();
		if (numberChild != 3) {
			correctStructure = false;
			_errorReport = ":El operador debe tener exactamente 3 hijos";
			super.setErrorReport(_errorReport);
		}			
		int i = 0;
		while ((i<numberChild)&&(correctStructure)) {
			TreeNode node = this.getChildAt(i);
			if (node instanceof Operator) {
				correctStructure = false;
				_errorReport = ":El operador sólo puede tener como hijos a Expresiones";
				super.setErrorReport(_errorReport);
			}
			i++;
		}
		return correctStructure;
	}
}
