/*
 * Created on 20-sep-2004
 *
 */
package com.geopista.style.filtereditor.controller.actions;

import java.util.List;

import javax.swing.tree.MutableTreeNode;

import com.geopista.style.filtereditor.model.impl.Literal;

import es.enxenio.util.controller.Action;
import es.enxenio.util.controller.ActionForward;
import es.enxenio.util.controller.FrontController;
import es.enxenio.util.controller.Request;
import es.enxenio.util.controller.Session;
import es.enxenio.util.controller.impl.FrontControllerImpl;
import es.enxenio.util.controller.impl.SessionImpl;

/**
 * @author enxenio s.l.
 *
 */
public class ChangeLiteralAction implements Action {

	/* (non-Javadoc)
	 * @see es.enxenio.util.controller.Action#doExecute(es.enxenio.util.controller.Request)
	 */
	public ActionForward doExecute(Request request) {

		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		MutableTreeNode node = (MutableTreeNode)request.getAttribute("Node");
		Literal literal = (Literal)node;
		/*Insertamos en la Session el Literal recuperado de la Request*/
		session.setAttribute("Literal",literal);
		session.setAttribute("InsertExpression",new Integer(0));
		/*Redirigimos a otro panel*/
		String pagesVisitedName = (String)session.getAttribute("PagesVisitedListName");
		List pagesVisited = (List)session.getAttribute(pagesVisitedName);
		pagesVisited.add("InsertUpdateFilter");	
		session.setAttribute(pagesVisitedName,pagesVisited);
		ActionForward forward = frontController.getForward("InsertUpdateLiteral");	
		return forward;
	}
}
