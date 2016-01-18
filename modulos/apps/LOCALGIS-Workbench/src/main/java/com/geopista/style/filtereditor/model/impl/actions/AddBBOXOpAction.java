/**
 * AddBBOXOpAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 20-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl.actions;

import com.geopista.style.filtereditor.model.exceptions.IncorrectIdentifierException;
import com.geopista.style.filtereditor.model.impl.BBOXOp;

/**
 * @author enxenio s.l.
 *
 */
public class AddBBOXOpAction {
	
	private int _operatorID;
	
	public AddBBOXOpAction(int operatorID) {
	
		_operatorID = operatorID;	
	}
	 
	public Object execute() throws IncorrectIdentifierException {
		
		BBOXOp bboxOp = new BBOXOp(_operatorID,null);
		return bboxOp;
	}
}
