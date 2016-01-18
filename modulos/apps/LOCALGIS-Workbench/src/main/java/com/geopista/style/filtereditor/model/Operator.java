/**
 * Operator.java
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
