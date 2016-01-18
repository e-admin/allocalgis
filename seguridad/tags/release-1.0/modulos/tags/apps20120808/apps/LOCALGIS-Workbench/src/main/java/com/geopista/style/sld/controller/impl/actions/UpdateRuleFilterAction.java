/*
 * Created on 23-sep-2004
 *
 */
package com.geopista.style.sld.controller.impl.actions;

import es.enxenio.util.controller.Action;
import es.enxenio.util.controller.ActionForward;
import es.enxenio.util.controller.FrontController;
import es.enxenio.util.controller.FrontControllerFactory;
import es.enxenio.util.controller.Request;
import es.enxenio.util.controller.Session;

/**
 * @author enxenio s.l.
 *
 */
public class UpdateRuleFilterAction implements Action {

	/* (non-Javadoc)
	 * @see es.enxenio.util.controller.Action#doExecute(es.enxenio.util.controller.Request)
	 */
	public ActionForward doExecute(Request request) {

		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerFactory.getFrontController();
		Session session = FrontControllerFactory.getSession();
		Request theRequest = FrontControllerFactory.createRequest();
		String pageInvocator = (String)request.getAttribute("PageInvocator");
		theRequest.setAttribute("PageInvocator",pageInvocator);
		theRequest.setAttribute("FilterSessionAttribute","RuleFilter");
		theRequest.setAttribute("ListPagesVisited","PagesVisited");
		theRequest.setAttribute("LayerSessionAttribute","Layer");
		/*Redirigimos a otra acción*/
		Action action = frontController.getAction("ChangeFilter");
		ActionForward forward = action.doExecute(theRequest);
		return forward;
	}

}
