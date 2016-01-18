/*
 * Created on 16-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl;

import javax.swing.tree.TreeNode;

import org.deegree.gml.GMLBox;

import com.geopista.style.filtereditor.model.Operator;
import com.geopista.style.filtereditor.model.OperatorIdentifiers;
import com.geopista.style.filtereditor.model.exceptions.IncorrectIdentifierException;
/**
 * @author enxenio s.l.
 *
 */
public class BBOXOp extends Operator {

	private String _stringRepresentation;
	private GMLBox _bbox;
	private String _errorReport;
	
	public BBOXOp(int operatorID,GMLBox bbox) throws IncorrectIdentifierException {
		super(operatorID);
		_bbox = bbox;
		if (operatorID == OperatorIdentifiers.BBOX) {
			_stringRepresentation = "BBOX";
			super.setStringRepresentation(_stringRepresentation);
		}
		else {
			throw new IncorrectIdentifierException("El identificador no corresponde al del operador espacial BBOX");
		}
	}

	public void setBBOX(GMLBox bbox) {
		_bbox = bbox;
	}
	
	public GMLBox getBBOX() {
		return _bbox;
	}
	
	/* (non-Javadoc)
	 * @see com.geopista.styles.filtereditor.model.Operator#checkStructure()
	 */
	public boolean checkStructure() {
		boolean correctStructure = true;
		int numberChild = this.getChildCount();
		if (numberChild != 1) {
			correctStructure = false;
			_errorReport = ":El operador debe tener exactamente 1 hijo";
			super.setErrorReport(_errorReport);
		}			
		int i = 0;
		while ((i<numberChild)&&(correctStructure)) {
			TreeNode node = this.getChildAt(i);
			if (!(node instanceof PropertyName)) {
				correctStructure = false;
				_errorReport = ":El hijo debe ser un PropertyName";
				super.setErrorReport(_errorReport);
			}
			i++;
		}
		return correctStructure;
	}
}
