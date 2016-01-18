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
public class UnaryLogicOp extends Operator {

	private String _stringRepresentation;
	private String _errorReport;
	
	public UnaryLogicOp(int operatorID) throws IncorrectIdentifierException {
		super(operatorID);
		if (operatorID == OperatorIdentifiers.NOT) {
			_stringRepresentation = "NOT";
			super.setStringRepresentation(_stringRepresentation);
		}
		else {
			throw new IncorrectIdentifierException("El identificador no corresponde al de un operador lógico unario");
		}
	}
	/* (non-Javadoc)
	 * @see com.geopista.styles.filtereditor.model.Operator#checkStructure()
	 */
	public boolean checkStructure() {
		boolean correctStructure = true;
		int numberChild = this.getChildCount();
		if ((super.getOperatorID() == OperatorIdentifiers.NOT)) {
			if (numberChild != 1) {
				correctStructure = false;
				_errorReport = ":El operador debe tener exactamente 1 hijo";
				super.setErrorReport(_errorReport);
			}			
		}
		int i = 0;
		while ((i<numberChild)&&(correctStructure)) {
			TreeNode node = this.getChildAt(i);	
			if (node instanceof Expression) {
				correctStructure = false;
				_errorReport = ":El hijo sólo puede ser un operador";
				super.setErrorReport(_errorReport);
			}
			i++;
		}
		return correctStructure;
	}
}
