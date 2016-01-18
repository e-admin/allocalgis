/**
 * PropertyIsLikeOp.java
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

import com.geopista.style.filtereditor.model.Operator;
import com.geopista.style.filtereditor.model.OperatorIdentifiers;
import com.geopista.style.filtereditor.model.exceptions.IncorrectIdentifierException;

/**
 * @author enxenio s.l.
 *
 */
public class PropertyIsLikeOp extends Operator {

	private String _stringRepresentation;
	private String _wildCard;
	private String _singleChar;
	private String _escape;
	private String _errorReport;
	
	public PropertyIsLikeOp(int operatorID) throws IncorrectIdentifierException {
		super(operatorID);
		_wildCard = "%";
		_singleChar = "_";
		_escape = "/";
		if (operatorID == OperatorIdentifiers.PROPERTYISLIKE) {
			_stringRepresentation = "LIKE";
			super.setStringRepresentation(_stringRepresentation);
		}
		else {
			throw new IncorrectIdentifierException("El identificador no corresponde al del operador ISLIKE");
		}
	}

	public String getWildCard() {
		return _wildCard;
	}
	
	public void setWildCard(String wildCard) {
		_wildCard = wildCard;
	}
	
	public String getSingleChar() {
		return _singleChar;
	}
	
	public void setSingleChar(String singleChar) {
		_singleChar = singleChar;
	}
	
	public String getEscape() {
		return _escape;
	}
	
	public void setEscape(String escape) {
		_escape = escape;
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
			if (i == 0) {	
				if (!(node instanceof PropertyName)) {
					correctStructure = false;
					_errorReport = ":El primer hijo debe ser un PropertyName";
					super.setErrorReport(_errorReport);
				}
			}
			else if (i == 1) {
				if (!(node instanceof Literal)) {
					correctStructure = false;
					_errorReport = ":El segundo hijo debe ser un Literal";
					super.setErrorReport(_errorReport);
				}
			}
			i++;
		}
		return correctStructure;
	}
}
