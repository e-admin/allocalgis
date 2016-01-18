/*
 * Created on 17-sep-2004
 *
 */
package com.geopista.style.filtereditor.controller.actions;

import java.util.HashMap;
import java.util.List;

import com.geopista.style.filtereditor.model.FilterFacade;

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
public class CreateFeatureAttributesMapAction implements Action {

	/* (non-Javadoc)
	 * @see es.enxenio.util.controller.Action#doExecute(es.enxenio.util.controller.Request)
	 */
	public ActionForward doExecute(Request arg0) {

		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		FilterFacade filterFacade = null;
		HashMap featureAttributesMap = null;
/*		try {
			filterFacade = FilterFacadeFactory.getDelegate();
			featureAttributesMap = filterFacade.createFeatureAttributesMap();
		} catch(InternalErrorException e) {
			System.err.println(e);
		} catch(InstanceNotFoundException e) {
			System.err.println(e);
		}*/
		/*Insertamos en la Session el HashMap creado*/
		/*session.setAttribute("FeatureAttributesMap",featureAttributesMap);*/
		/*Redirigimos a otro panel, actualizando la lista de páginas visitadas*/
		String pagesVisitedName = (String)session.getAttribute("PagesVisitedListName");
		List pagesVisited = (List)session.getAttribute(pagesVisitedName);
		pagesVisited.add("InsertUpdateFilter");	
		session.setAttribute(pagesVisitedName,pagesVisited);
		ActionForward forward = frontController.getForward("InsertUpdatePropertyName");	
		return forward;
	}
}
