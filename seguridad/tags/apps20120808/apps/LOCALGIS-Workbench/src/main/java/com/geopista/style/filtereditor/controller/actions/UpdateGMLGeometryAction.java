/*
 * Created on 20-sep-2004
 *
 */
package com.geopista.style.filtereditor.controller.actions;

import java.util.List;

import org.deegree.gml.GMLGeometry;

import com.geopista.style.filtereditor.model.FilterFacade;
import com.geopista.style.filtereditor.model.FilterFacadeFactory;

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
public class UpdateGMLGeometryAction implements Action {

	/* (non-Javadoc)
	 * @see es.enxenio.util.controller.Action#doExecute(es.enxenio.util.controller.Request)
	 */
	public ActionForward doExecute(Request request) {

		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		String srs = (String)request.getAttribute("SRS");
		String coordx = (String)request.getAttribute("CoordX");
		String coordy = (String)request.getAttribute("CoordY");
		String coordz = (String)request.getAttribute("CoordZ");
		String coordinates = coordx+","+coordy;
		if (coordz != null) {
			coordinates+=","+coordz;
		}
		GMLGeometry geometry = (GMLGeometry)session.getAttribute("GMLGeometry");
		FilterFacade filterFacade = null;
		try {
			filterFacade = FilterFacadeFactory.getDelegate();
		} catch(InternalErrorException e) {
			System.err.println(e);
		}
		geometry = filterFacade.updateGMLGeometry(geometry,srs,coordinates);
		/*Añadimos la geometria creada en la Session*/
		session.setAttribute("GMLGeometry",geometry);
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
