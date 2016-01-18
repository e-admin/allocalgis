/*
 * Created on 10-jun-2004
 *
 */
package com.geopista.style.sld.controller.impl.actions;

import java.util.ArrayList;

import com.geopista.style.sld.model.SLDFacade;
import com.geopista.style.sld.model.SLDFactory;

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
public class DeleteUserStyleAction implements Action {

	/* (non-Javadoc)
	 * @see com.geopista.style.sld.controller.Action#doExecute(com.geopista.style.sld.controller.Request)
	 */
	public ActionForward doExecute(Request request) {
		
		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		/*Recuperamos los parámetros de la Request*/
		String styleName = (String)request.getAttribute("StyleName");
		SLDFacade sldFacade = null;
		try {
			sldFacade = SLDFactory.getDelegate();
		} catch(InternalErrorException e) {
			System.err.println(e);
		}
		/*Llamamos al caso de uso del modelo*/
		ArrayList userStyleList = (ArrayList) session.getAttribute("UserStyleList");
		sldFacade.deleteUserStyle(styleName,userStyleList);
		/*Redirigimos a otra acción*/
		ActionForward forward = frontController.getForward("DisplayStyles");
		return forward;
	}

}
