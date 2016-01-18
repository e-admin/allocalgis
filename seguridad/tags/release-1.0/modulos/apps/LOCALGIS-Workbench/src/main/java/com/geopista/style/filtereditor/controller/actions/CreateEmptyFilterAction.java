/*
 * Created on 16-sep-2004
 *
 */
package com.geopista.style.filtereditor.controller.actions;

import java.util.List;

import javax.swing.tree.DefaultTreeModel;

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
public class CreateEmptyFilterAction implements Action {

	/* (non-Javadoc)
	 * @see es.enxenio.util.controller.Action#doExecute(es.enxenio.util.controller.Request)
	 */
	public ActionForward doExecute(Request request) {

		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		/*Recogemos los parámetros de la Request*/
		String pageInvocator = (String) request.getAttribute("PageInvocator");
		String filterSessionName = (String) request.getAttribute("FilterSessionAttribute");
		String listPagesVisited = (String) request.getAttribute("ListPagesVisited");
		String layerSession = (String)request.getAttribute("LayerSessionAttribute");
		DefaultTreeModel filterTree = null;
		try {
			FilterFacade filterFacade = FilterFacadeFactory.getDelegate();
			filterTree = filterFacade.createEmptyFilter();
		} catch(InternalErrorException e) {
			System.err.println(e);
		} catch (IncorrectIdentifierException e) {
			e.printStackTrace();
		}
		/*Añadimos parámetros en la Session*/
		session.setAttribute("FilterName",filterSessionName);
		session.setAttribute("FilterTree",filterTree);
		session.setAttribute("PagesVisitedListName",listPagesVisited);
		session.setAttribute("LayerSessionAttribute",layerSession);
		/*Actualizamos el parámetro PagesVisited en la Session*/
		List pagesVisited = (List)session.getAttribute(listPagesVisited);
		pagesVisited.add(pageInvocator);
		session.setAttribute(listPagesVisited,pagesVisited);
		/*Redirigimos a otra acción*/
		ActionForward forward = frontController.getForward("InsertUpdateFilter");
		return forward;
	}
}
