/*
 * Created on 17-sep-2004
 *
 */
package com.geopista.style.filtereditor.controller.actions;

import java.util.ArrayList;

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
public class GetBackAction implements Action {

	/* (non-Javadoc)
	 * @see es.enxenio.util.controller.Action#doExecute(es.enxenio.util.controller.Request)
	 */
	public ActionForward doExecute(Request request) {

		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		/*Obtenemos el nombre de la lista de páginas visitadas en la Session*/
		String pagesVisitedList = (String)session.getAttribute("PagesVisitedListName");
		/*Actualizamos el parámetro PagesVisited en la Session*/
		ArrayList pagesVisited = (ArrayList)session.getAttribute(pagesVisitedList);
		int size = pagesVisited.size();
		if (size != 0) {
			String lastPageVisited = (String)pagesVisited.remove(size-1);
			session.setAttribute("PagesVisited",pagesVisited);
			/*Redirigimos a otra acción*/
			ActionForward forward = frontController.getForward(lastPageVisited);
			return forward;
		}
		else { 
		/* Si la lista está vacia, es que no hay a donde redirigir, por lo que devolvemos null */
			return null;
		}		
	}
}
