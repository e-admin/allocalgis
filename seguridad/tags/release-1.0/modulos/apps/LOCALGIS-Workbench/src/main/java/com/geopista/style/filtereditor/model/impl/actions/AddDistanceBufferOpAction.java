/*
 * Created on 20-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl.actions;

import com.geopista.style.filtereditor.model.exceptions.IncorrectIdentifierException;
import com.geopista.style.filtereditor.model.impl.DistanceBufferOp;

/**
 * @author enxenio s.l.
 *
 */
public class AddDistanceBufferOpAction {
	
	private int _operatorID;
	
	public AddDistanceBufferOpAction(int operatorID) {
		
		_operatorID = operatorID;
	}
	
	public Object execute() throws IncorrectIdentifierException {
		
		DistanceBufferOp distanceBufferOp = new DistanceBufferOp(_operatorID,null,0);
		return distanceBufferOp;
	}
}
