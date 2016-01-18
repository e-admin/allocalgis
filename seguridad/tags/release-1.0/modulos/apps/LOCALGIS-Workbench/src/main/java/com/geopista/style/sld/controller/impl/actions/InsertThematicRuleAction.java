/*
 * Created on 27-jul-2004
 *
 */
package com.geopista.style.sld.controller.impl.actions;

import java.util.List;

import es.enxenio.util.controller.Action;
import es.enxenio.util.controller.ActionForward;
import es.enxenio.util.controller.FrontController;
import es.enxenio.util.controller.FrontControllerFactory;
import es.enxenio.util.controller.Request;
import es.enxenio.util.controller.Session;
import es.enxenio.util.controller.impl.FrontControllerImpl;
import es.enxenio.util.controller.impl.SessionImpl;

/**
 * @author enxenio s.l.
 *
 */
public class InsertThematicRuleAction implements Action {

	/* (non-Javadoc)
	 * @see com.geopista.style.sld.controller.Action#doExecute(com.geopista.style.sld.controller.Request)
	 */
	public ActionForward doExecute(Request request) {

		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		String styleName = (String)request.getAttribute("StyleName");
		session.setAttribute("StyleName",styleName);
		String symbolizerType = (String)request.getAttribute("SymbolizerType");
		List inserts = (List)session.getAttribute("Insert");
		inserts.add(2,new Integer(1));
		session.setAttribute("Insert",inserts);
		session.setAttribute("SymbolizerType",symbolizerType);
		/*Actualizamos el parámetro PagesVisited en la Session*/
		List pagesVisited = (List)session.getAttribute("PagesVisited");
		pagesVisited.add("InsertUpdateCustomStyle");
		session.setAttribute("PagesVisited",pagesVisited);
		/*Redirigimos a otra acción del controlador*/
		Action action = frontController.getAction("GetFeatureAttributes");
		Request newRequest = FrontControllerFactory.createRequest();
		String layerName = (String)session.getAttribute("LayerName");
		newRequest.setAttribute("LayerName",layerName);
		action.doExecute(newRequest);
		ActionForward forward = null;
		if ((symbolizerType.toLowerCase()).equals("point")) {
			forward = frontController.getForward("InsertUpdateThematicPointRule");
		}
		else if ((symbolizerType.toLowerCase()).equals("line")) {
			forward = frontController.getForward("InsertUpdateThematicLineRule");
		}
		else if ((symbolizerType.toLowerCase()).equals("polygon")) {
			forward = frontController.getForward("InsertUpdateThematicPolygonRule");
		}
		else if ((symbolizerType.toLowerCase()).equals("text")) {
			forward = frontController.getForward("InsertUpdateThematicTextRule");
		}
		return forward;
	}
}
