/*
 * Created on 20-sep-2004
 *
 */
package com.geopista.style.filtereditor.controller.actions;

import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import org.deegree.gml.GMLGeometry;

import com.geopista.style.filtereditor.model.FilterFacade;
import com.geopista.style.filtereditor.model.FilterFacadeFactory;
import com.geopista.style.filtereditor.model.impl.DistanceBufferOp;

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
public class UpdateDistanceBufferOpAction implements Action {

	/* (non-Javadoc)
	 * @see es.enxenio.util.controller.Action#doExecute(es.enxenio.util.controller.Request)
	 */
	public ActionForward doExecute(Request request) {

		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		double distance = ((Double)request.getAttribute("Distance")).doubleValue();
		DistanceBufferOp distanceBufferOp = (DistanceBufferOp)session.getAttribute("DistanceBufferOperator");
		GMLGeometry geometry = (GMLGeometry)session.getAttribute("GMLGeometry");
		MutableTreeNode oldNode = (MutableTreeNode)session.getAttribute("OldNode");
		MutableTreeNode parentNode = (MutableTreeNode)session.getAttribute("ParentNode");
		DefaultTreeModel filterTree = (DefaultTreeModel)session.getAttribute("FilterTree");
		Integer insert = (Integer)session.getAttribute("InsertOperator");
		Integer index = (Integer)session.getAttribute("Index");
		FilterFacade filterFacade = null;
		try {
			filterFacade = FilterFacadeFactory.getDelegate();
		} catch(InternalErrorException e) {
			System.err.println(e);
		}
		distanceBufferOp = filterFacade.updateDistanceBufferOp(distanceBufferOp,distance,geometry,oldNode,parentNode,filterTree,insert,index);
		session.setAttribute("DistanceBufferOperator",distanceBufferOp);
		/*Redirigimos a otro panel*/
		String pagesVisitedName = (String)session.getAttribute("PagesVisitedListName");
		List pagesVisited = (List)session.getAttribute(pagesVisitedName);
		int size = pagesVisited.size();
		String lastPageVisited = (String) pagesVisited.remove(size-1);
		session.setAttribute(pagesVisitedName,pagesVisited);
		/*Redirección a una nueva interfaz*/
		ActionForward forward = frontController.getForward(lastPageVisited);
		return forward;
	}
}
