/*
 * Created on 26-jul-2004
 *
 */
package com.geopista.style.sld.controller.impl.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.deegree.graphics.sld.UserStyle;
import org.deegree.xml.XMLParsingException;

import com.geopista.style.sld.model.SLDFacade;
import com.geopista.style.sld.model.SLDFactory;
import com.geopista.style.sld.model.SLDStyle;
import com.geopista.style.sld.model.impl.SLDStyleImpl;
import com.vividsolutions.jump.workbench.model.Layer;

import es.enxenio.util.controller.Action;
import es.enxenio.util.controller.ActionForward;
import es.enxenio.util.controller.FrontController;
import es.enxenio.util.controller.FrontControllerFactory;
import es.enxenio.util.controller.Request;
import es.enxenio.util.controller.Session;
import es.enxenio.util.controller.impl.FrontControllerImpl;
import es.enxenio.util.controller.impl.SessionImpl;
import es.enxenio.util.exceptions.InternalErrorException;

/**
 * @author enxenio s.l.
 *
 */
public class DefineFileExportUserStyleAction implements Action {

	/* (non-Javadoc)
	 * @see com.geopista.style.sld.controller.Action#doExecute(com.geopista.style.sld.controller.Request)
	 */
	public ActionForward doExecute(Request request) {
		SLDStyle theStyle = (SLDStyle)request.getAttribute("SLDStyle");
		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Request theRequest = FrontControllerFactory.createRequest();
		/*Metemos los parámetros en la Request*/
		theRequest.setAttribute("SLDStyle", theStyle);
		theRequest.setAttribute("Layer", request.getAttribute("Layer"));
		theRequest.setAttribute("LayerName", request.getAttribute("LayerName"));
		theRequest.setAttribute("importar", request.getAttribute("importar"));
		/*Redirigimos a otra acción del controlador*/
		ActionForward forward = frontController.getForward("DefineFileExport");
		return forward;
	}

}
