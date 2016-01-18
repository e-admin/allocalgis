/**
 * SelectScaleRangeAction.java
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

import java.util.List;

import com.geopista.style.sld.model.SLDFacade;
import com.geopista.style.sld.model.SLDFactory;
import com.geopista.style.sld.model.ScaleRange;

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
public class SelectScaleRangeAction implements Action {

	/* (non-Javadoc)
	 * @see com.geopista.style.sld.controller.Action#doExecute(com.geopista.style.sld.controller.Request)
	 */
	public ActionForward doExecute(Request request) {
		
		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		/*Recuperamos los parámetros de la Request*/
		String styleName = (String)request.getAttribute("StyleName");
		session.setAttribute("StyleName",styleName);
		int position = ((Integer)request.getAttribute("Position")).intValue();
		List scaleRangeList = (List) session.getAttribute("ScaleRangeList");
		SLDFacade sldFacade = null;
		try {
			sldFacade = SLDFactory.getDelegate();
		} catch(InternalErrorException e) {
			System.err.println(e);
		}
		session.setAttribute("ScaleRangePosition",new Integer(position));
		/*Llamamos al caso de uso del modelo correspondiente*/
		List inserts = (List)session.getAttribute("Insert");
		inserts.add(1,new Integer(0));
		session.setAttribute("Insert",inserts);				
		ScaleRange scaleRange = sldFacade.getScaleRange(position,scaleRangeList);
		/*Asociamos a la Session el ScaleRange recuperado*/
		session.setAttribute("ScaleRange",scaleRange);
		/*Redirigimos a otra acción*/
		ActionForward forward = frontController.getForward("InsertUpdateCustomStyle");
		return forward;
	}
}
