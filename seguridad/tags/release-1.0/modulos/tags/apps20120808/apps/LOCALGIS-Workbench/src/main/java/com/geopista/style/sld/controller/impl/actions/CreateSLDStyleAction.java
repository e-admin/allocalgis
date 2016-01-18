/*
 * Created on 22-jul-2004
 *
 */
package com.geopista.style.sld.controller.impl.actions;

import java.util.ArrayList;

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
public class CreateSLDStyleAction implements Action {

	/* (non-Javadoc)
	 * @see com.geopista.style.sld.controller.Action#doExecute(com.geopista.style.sld.controller.Request)
	 */
	public ActionForward doExecute(Request request) {
		
		Session session = SessionImpl.getInstance();
		FrontController frontController = FrontControllerImpl.getInstance();
		/*Recibo en la request el nombre de un estilo*/
		String currentStyleName = (String)request.getAttribute("CurrentStyleName");		
		/*Recuperamos instancias del FrontController y de la Session*/
		String layerName = (String) session.getAttribute("LayerName");
		SLDStyle sldStyle = (SLDStyle) session.getAttribute("SLDStyle");
		ArrayList userStyleList = (ArrayList)session.getAttribute("UserStyleList");
		sldStyle.putStyles(SLDStyle.REPLACEALL,userStyleList);
		sldStyle.setCurrentStyleName(currentStyleName);
		if (sldStyle.getType() == SLDStyle.DATABASE) {
			SLDFacade sldFacade = null;
			try {
				sldFacade = SLDFactory.getDelegate();
				sldFacade.insertUserStyles(layerName,sldStyle.getStyles());
			} catch(InternalErrorException e) {
				System.err.println(e);
			}
		}
		else if (sldStyle.getType() == SLDStyle.FILE) {
			sldStyle.createSLDFile();
		}
		/*Limpio la session por completo*/
		session.clean();
		return null;
	}
}
