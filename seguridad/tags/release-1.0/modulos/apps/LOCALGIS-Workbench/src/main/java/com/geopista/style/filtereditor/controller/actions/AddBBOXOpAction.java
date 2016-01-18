/*
 * Created on 20-sep-2004
 *
 */
package com.geopista.style.filtereditor.controller.actions;

import java.util.List;

import javax.swing.tree.MutableTreeNode;

import com.geopista.style.filtereditor.model.FilterFacade;
import com.geopista.style.filtereditor.model.FilterFacadeFactory;
import com.geopista.style.filtereditor.model.exceptions.IncorrectIdentifierException;
import com.geopista.style.filtereditor.model.impl.BBOXOp;

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
public class AddBBOXOpAction implements Action {

	/* (non-Javadoc)
	 * @see es.enxenio.util.controller.Action#doExecute(es.enxenio.util.controller.Request)
	 */
	public ActionForward doExecute(Request request) {

		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		MutableTreeNode parentNode = (MutableTreeNode)request.getAttribute("ParentNode");
		int operatorID = ((Integer)request.getAttribute("OperatorID")).intValue();
		session.removeAttribute("GMLBox");
		FilterFacade filterFacade = null;
		BBOXOp bboxOp = null;
		try {
			filterFacade = FilterFacadeFactory.getDelegate();
			bboxOp = filterFacade.addBBOXOp(operatorID);
		} catch(InternalErrorException e) {
			System.err.println(e);
		} catch (IncorrectIdentifierException e) {
			System.err.println(e);
		}
		/*Añadimos el operador creado en la Session*/
		session.setAttribute("BBOXOperator",bboxOp);
		session.setAttribute("ParentNode",parentNode);
		int numChildren = parentNode.getChildCount();
		session.setAttribute("Index",new Integer(numChildren));
		session.setAttribute("InsertOperator",new Integer(1));
		/*Redirigimos a otro panel*/
		String pagesVisitedName = (String)session.getAttribute("PagesVisitedListName");
		List pagesVisited = (List)session.getAttribute(pagesVisitedName);
		pagesVisited.add("InsertUpdateFilter");
		session.setAttribute(pagesVisitedName,pagesVisited);
		/*Redirección a una nueva interfaz*/
		ActionForward forward = frontController.getForward("InsertUpdateBBOXOp");
		return forward;
	}
}
