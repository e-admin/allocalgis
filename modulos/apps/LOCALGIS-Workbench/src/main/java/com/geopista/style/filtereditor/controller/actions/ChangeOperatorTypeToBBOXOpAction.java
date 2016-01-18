/**
 * ChangeOperatorTypeToBBOXOpAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
public class ChangeOperatorTypeToBBOXOpAction implements Action {

	/* (non-Javadoc)
	 * @see es.enxenio.util.controller.Action#doExecute(es.enxenio.util.controller.Request)
	 */
	public ActionForward doExecute(Request request) {

		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		MutableTreeNode oldNode = (MutableTreeNode)request.getAttribute("OldNode");
		session.setAttribute("OldNode",oldNode);
		MutableTreeNode parentNode = (MutableTreeNode)oldNode.getParent();
		session.setAttribute("ParentNode",parentNode);
		if (parentNode != null) {
			int index = parentNode.getIndex(oldNode);
			session.setAttribute("Index",new Integer(index));
		}
		int operatorID = ((Integer)request.getAttribute("OperatorID")).intValue();
		FilterFacade filterFacade = null;
		session.removeAttribute("GMLBox");
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
