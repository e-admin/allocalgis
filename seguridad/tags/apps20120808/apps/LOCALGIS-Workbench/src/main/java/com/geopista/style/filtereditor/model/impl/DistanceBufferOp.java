/*
 * Created on 16-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl;

import javax.swing.tree.TreeNode;

import org.deegree.gml.GMLGeometry;

import com.geopista.style.filtereditor.model.Operator;
import com.geopista.style.filtereditor.model.OperatorIdentifiers;
import com.geopista.style.filtereditor.model.exceptions.IncorrectIdentifierException;
/**
 * @author enxenio s.l.
 *
 */
public class DistanceBufferOp extends Operator {

	private String _stringRepresentation;
	private GMLGeometry _geometry;
	private double _distance;
	private String _errorReport;
	
	public DistanceBufferOp(int operatorID,GMLGeometry geometry,double distance) throws IncorrectIdentifierException {
		super(operatorID);
		_geometry = geometry;
		_distance = distance;
		if (operatorID == OperatorIdentifiers.DWITHIN) {
			_stringRepresentation = "DWITHIN";
			super.setStringRepresentation(_stringRepresentation);
		}
		else if (operatorID == OperatorIdentifiers.BEYOND) {
			_stringRepresentation = "BEYOND";
			super.setStringRepresentation(_stringRepresentation);			
		}
		else {
			throw new IncorrectIdentifierException("El identificador no corresponde al del operador espacial de distancia");
		}
	}
	
	public void setGMLGeometry(GMLGeometry geometry) {
		_geometry = geometry;
	}
	
	public GMLGeometry getGMLGeometry() {
		return _geometry;
	}
	
	public void setDistance(double distance) {
		_distance = distance;
	}
	
	public double getDistance() {
		return _distance;
	}

	/* (non-Javadoc)
	 * @see com.geopista.styles.filtereditor.model.Operator#checkStructure()
	 */
	public boolean checkStructure() {
		boolean correctStructure = true;
		int numberChild = this.getChildCount();
		if (numberChild != 1) {
			correctStructure = false;
			_errorReport = ":El operador debe tener exactamente tener 1 hijo";
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
