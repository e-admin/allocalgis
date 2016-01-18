/*
 * Created on 16-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl.actions;

import javax.swing.tree.DefaultTreeModel;

import com.geopista.style.filtereditor.model.OperatorIdentifiers;
import com.geopista.style.filtereditor.model.exceptions.IncorrectIdentifierException;
import com.geopista.style.filtereditor.model.impl.UnknownOp;

/**
 * @author enxenio s.l.
 *
 */
public class CreateEmptyFilterAction {
	
	public CreateEmptyFilterAction() {}
	
	public Object execute() throws IncorrectIdentifierException{
	
		UnknownOp unknownOp = new UnknownOp(OperatorIdentifiers.UNKNOWN);
		DefaultTreeModel filterTree = new DefaultTreeModel(unknownOp);
		return filterTree;
	}
}
