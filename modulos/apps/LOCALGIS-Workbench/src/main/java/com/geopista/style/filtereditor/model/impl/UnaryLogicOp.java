/**
 * UnaryLogicOp.java
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
