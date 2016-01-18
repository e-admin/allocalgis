/**
 * ChangeFilterAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 17-sep-2004
 *
 */
package com.geopista.style.filtereditor.controller.actions;

import java.util.List;

import javax.swing.tree.DefaultTreeModel;

import org.deegree.services.wfs.filterencoding.Filter;

import com.geopista.style.filtereditor.model.FilterFacade;
import com.geopista.style.filtereditor.model.FilterFacadeFactory;
import com.geopista.style.filtereditor.model.exceptions.IncorrectIdentifierException;
import com.geopista.style.filtereditor.model.exceptions.MalformedFilterException;

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
public class ChangeFilterAction implements Action {

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
		/*Recogemos el filtro de la Session*/
		Filter filter = (Filter)session.getAttribute(filterSessionName);
		DefaultTreeModel filterTree = null;
		try {
			FilterFacade filterFacade = FilterFacadeFactory.getDelegate();
			filterTree = filterFacade.createFilterTree(filter);
		} catch(InternalErrorException e) {
			System.err.println(e);
		} catch (IncorrectIdentifierException e) {
			System.err.println(e);
		} catch (MalformedFilterException e) {
			System.err.println(e);
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
