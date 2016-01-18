/*
 * Created on 29-jul-2004
 *
 */
package com.geopista.style.filtereditor.controller.actions;

import java.util.HashMap;
import java.util.List;

import com.geopista.style.filtereditor.model.FilterFacade;
import com.geopista.style.filtereditor.model.FilterFacadeFactory;
import com.vividsolutions.jump.workbench.model.Layer;

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
public class GetFeatureAttributesAction implements Action {

	/* (non-Javadoc)
	 * @see com.geopista.style.sld.controller.Action#doExecute(com.geopista.style.sld.controller.Request)
	 */
	public ActionForward doExecute(Request request) {

		HashMap featureAttributes = new HashMap();
		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();	
		Session session = SessionImpl.getInstance();
		String layerSession = (String)session.getAttribute("LayerSessionAttribute");
		Layer layer = (Layer)session.getAttribute(layerSession);	
		FilterFacade filterFacade = null;
		try {
			filterFacade = FilterFacadeFactory.getDelegate();
			featureAttributes = filterFacade.getFeatureAttributes(layer);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		session.setAttribute("FeatureAttributesMap", featureAttributes);
		/*Redirigimos a otro panel, actualizando la lista de páginas visitadas*/
		String pagesVisitedName = (String)session.getAttribute("PagesVisitedListName");
		List pagesVisited = (List)session.getAttribute(pagesVisitedName);
		pagesVisited.add("InsertUpdateFilter");	
		session.setAttribute(pagesVisitedName,pagesVisited);
		ActionForward forward = frontController.getForward("InsertUpdatePropertyName");	
		return forward;
	}
}
