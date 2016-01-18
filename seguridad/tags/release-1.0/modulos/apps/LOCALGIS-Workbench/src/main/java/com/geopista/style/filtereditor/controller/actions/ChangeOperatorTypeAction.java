/*
 * Created on 17-sep-2004
 *
 */
package com.geopista.style.filtereditor.controller.actions;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import com.geopista.style.filtereditor.model.FilterFacade;
import com.geopista.style.filtereditor.model.FilterFacadeFactory;
import com.geopista.style.filtereditor.model.exceptions.IncorrectIdentifierException;

import es.enxenio.util.controller.Action;
import es.enxenio.util.controller.ActionForward;
import es.enxenio.util.controller.FrontController;
import es.enxenio.util.controller.Request;
import es.enxenio.util.controller.Session;
import es.enxenio.util.controller.impl.FrontControllerImpl;
import es.enxenio.util.controller.impl.SessionImpl;
import es.enxenio.util.exceptions.InternalErrorException;

/**
 * @author enxenio s.l.
 *
 */
public class ChangeOperatorTypeAction implements Action {

	/* (non-Javadoc)
	 * @see es.enxenio.util.controller.Action#doExecute(es.enxenio.util.controller.Request)
	 */
	public ActionForward doExecute(Request request) {

		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		MutableTreeNode node = (MutableTreeNode)request.getAttribute("Node");
		int newOperatorID = ((Integer)request.getAttribute("OperatorID")).intValue();
		DefaultTreeModel filterTree = (DefaultTreeModel)session.getAttribute("FilterTree");
		FilterFacade filterFacade = null;
		try {
			filterFacade = FilterFacadeFactory.getDelegate();
			filterFacade.changeOperatorType(filterTree,node,newOperatorID);
		} catch(InternalErrorException e) {
			System.err.println(e);
		} catch(IncorrectIdentifierException e) {
			System.err.println(e);
		}	
		ActionForward forward = frontController.getForward("InsertUpdateFilter");
		return forward;
	}
}
