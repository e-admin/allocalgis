/**
 * DefineFileExportUserStyleAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 26-jul-2004
 *
 */
package com.geopista.style.sld.controller.impl.actions;

import com.geopista.style.sld.model.SLDStyle;

import es.enxenio.util.controller.Action;
import es.enxenio.util.controller.ActionForward;
import es.enxenio.util.controller.FrontController;
import es.enxenio.util.controller.FrontControllerFactory;
import es.enxenio.util.controller.Request;
import es.enxenio.util.controller.impl.FrontControllerImpl;

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
