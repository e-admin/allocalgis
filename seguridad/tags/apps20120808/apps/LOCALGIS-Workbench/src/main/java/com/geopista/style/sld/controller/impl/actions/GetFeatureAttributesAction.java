/*
 * Created on 29-jul-2004
 *
 */
package com.geopista.style.sld.controller.impl.actions;

import java.util.HashMap;

import com.geopista.style.sld.model.SLDFacade;
import com.geopista.style.sld.model.SLDFactory;
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
		Layer layer = (Layer)session.getAttribute("Layer");	
		SLDFacade sldFacade = null;
		try {
			sldFacade = SLDFactory.getDelegate();
			featureAttributes = sldFacade.getFeatureAttributes(layer);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		session.setAttribute("FeatureAttributes", featureAttributes);
		/*Redirigimos a otra acción*/
		ActionForward forward = null;
		return forward;
	}
}
