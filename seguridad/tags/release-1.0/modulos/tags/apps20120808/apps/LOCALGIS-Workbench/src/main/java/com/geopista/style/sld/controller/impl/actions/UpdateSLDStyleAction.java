/*
 * Created on 16-jun-2004
 *
 */
package com.geopista.style.sld.controller.impl.actions;

import java.util.ArrayList;
import java.util.List;

import com.geopista.style.sld.model.SLDFacade;
import com.geopista.style.sld.model.SLDFactory;
import com.geopista.style.sld.model.SLDStyle;

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
public class UpdateSLDStyleAction implements Action {

	/* (non-Javadoc)
	 * @see com.geopista.style.sld.controller.Action#doExecute(com.geopista.style.sld.controller.Request)
	 */
	public ActionForward doExecute(Request request) {
		
		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		List pagesVisited = (List)session.getAttribute("PagesVisited");
		/*Recibo en la request el nombre de un estilo*/
		String currentStyleName = (String)request.getAttribute("CurrentStyleName");		
		SLDStyle sldStyle = (SLDStyle)session.getAttribute("SLDStyle");
		ArrayList userStyleList = (ArrayList)session.getAttribute("UserStyleList");
		SLDFacade sldFacade = null;
		try {
			sldFacade = SLDFactory.getDelegate();
		} catch(InternalErrorException e) {
			System.err.println(e);
		}
		
		/*Llamamos al caso de uso del modelo*/
		sldStyle = sldFacade.updateSLDStyle(sldStyle,userStyleList);
		sldStyle.setCurrentStyleName(currentStyleName);
		session.setAttribute("SLDStyle",sldStyle);
		/*Borramos el parámetro PagesVisited de la Session*/
		session.removeAttribute("PagesVisited");
		return null;
	}

}
