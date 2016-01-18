/*
 * Created on 16-sep-2004
 *
 */
package com.geopista.style.filtereditor.model;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author enxenio s.l.
 *
 */
public abstract class Operator extends DefaultMutableTreeNode{
	
	private int _operatorID;
	private String _stringRepresentation;
	private String _errorReport;
	
	public Operator(int operatorID) {
		_operatorID = operatorID;	
	}
	
	public int getOperatorID() {
		return _operatorID;
	}
	
	public void setStringRepresentation(String stringRepresentation) {
		_stringRepresentation = stringRepresentation;
	}
	
	public void setErrorReport(String errorReport) {
		_errorReport = errorReport;
	}
	
	public String getErrorReport() {
		return _errorReport;
	}
	
	public String toString() {
		return _stringRepresentation;
	}
	
	public abstract boolean checkStructure();
}
