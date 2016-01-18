/*
 * Created on 16-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl;

import com.geopista.style.filtereditor.model.Operator;
import com.geopista.style.filtereditor.model.OperatorIdentifiers;
import com.geopista.style.filtereditor.model.exceptions.IncorrectIdentifierException;

/**
 * @author enxenio s.l.
 *
 */
public class UnknownOp extends Operator {

	private String _stringRepresentation;
	
	public UnknownOp(int operatorID) throws IncorrectIdentifierException {
		super(operatorID);
		if (operatorID == OperatorIdentifiers.UNKNOWN) {
			_stringRepresentation = "No Definido. (Pulse con botón derecho del ratón)";
			super.setStringRepresentation(_stringRepresentation);
		}
		else {
			throw new IncorrectIdentifierException("El identificador no corresponde al del operador DESCONOCIDO");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.geopista.styles.filtereditor.model.Operator#checkStructure()
	 */
	public boolean checkStructure() {
		return false;
	}

}
