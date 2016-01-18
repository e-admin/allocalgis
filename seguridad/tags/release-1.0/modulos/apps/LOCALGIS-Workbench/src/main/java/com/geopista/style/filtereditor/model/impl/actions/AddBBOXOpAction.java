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
